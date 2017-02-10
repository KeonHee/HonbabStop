package com.landvibe.android.honbabstop.Login.presenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;
import com.landvibe.android.honbabstop.Login.model.LoginModel;

import java.util.Arrays;
import java.util.List;

/**
 * Created by user on 2017-02-09.
 */

public class LoginPresenterImpl implements LoginPresenter.Presenter {


    private final static String TAG = "LoginPresenterImpl";

    public static final String KAKAODOMAIN = "@kakao.com";
    public static final String KAKAOPROVIDER = "kakao";

    private LoginPresenter.View view;

    private LoginModel loginModel;

    private CallbackManager mFacebookCallbackManager;
    private LoginButton mFacebookLoginBtn;

    @Override
    public void attachView(LoginPresenter.View view, Activity activity) {
        this.view = view;

        loginModel=new LoginModel();
        loginModel.setOnLoginLisenter(firebaseLoginCallback);
        loginModel.setOnSignupLisenter(firebaseSignUpCallback);
        loginModel.setOnAuthLisenter(firebaseAuthCallback);
        loginModel.setActivity(activity);
        loginModel.loadAuth();
        loginModel.loadDB();

        mFacebookCallbackManager=CallbackManager.Factory.create();
    }

    @Override
    public void detachView() {
        view = null;

        loginModel.setActivity(null);
        loginModel.setOnLoginLisenter(null);
        loginModel.setOnSignupLisenter(null);
        loginModel.setOnAuthLisenter(null);
        loginModel.removeAuthListener();
        loginModel=null;

        closeKakaoSession();

        mFacebookCallbackManager=null;
    }

    @Override
    public void setFacebookLoginCallback(LoginButton facebookLoginBtn) {
        mFacebookLoginBtn=facebookLoginBtn;
        mFacebookLoginBtn.setReadPermissions(Arrays.asList("public_profile","email"));
        mFacebookLoginBtn.registerCallback(mFacebookCallbackManager,facebookCallback);
    }

    private FacebookCallback<LoginResult> facebookCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {

            view.showLoading();

            GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                    (jsonObject, response) -> {
                        if(response.getError()!=null){
                            Log.d(TAG, "GraphResponse Error" );
                        }else {
                            Log.d(TAG, "user: " + jsonObject.toString());
                            Log.d(TAG, "AccessToken: " + loginResult.getAccessToken().getToken());

                            loginModel.onFacebookFirebaseLogin(
                                    loginResult.getAccessToken(),
                                    jsonObject
                            );

                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,gender"); // 필요한 정보
            request.setParameters(parameters);
            request.executeAsync();
        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException error) {

        }
    };

    @Override
    public boolean onFacebookActivityResult(int requestCode, int resultCode, Intent data) {
        return mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onKakaoActivityResult(int requestCode, int resultCode, Intent data) {
        return Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data);
    }

    /**
     * 카카오톡 로그인 버튼 콜백 등록
     */
    private void connectKakaoSession(){
        Session.getCurrentSession().addCallback(iSessionCallback);
        Session.getCurrentSession().checkAndImplicitOpen();
    }

    /**
     * 카카오톡 로그인 버튼 콜백 해제
     */
    private void closeKakaoSession(){
        Session.getCurrentSession().removeCallback(iSessionCallback);
    }


    /**
     * 카카오톡 프로필 정보 요청
     */
    private void requestMe() {
        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                String message = "failed to get user info. msg=" + errorResult;
                Logger.d(message);

                view.moveToLoginActivity();
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                view.moveToLoginActivity();
            }

            @Override
            public void onSuccess(UserProfile userProfile) {
                Logger.d("UserProfile : " + userProfile);
                long userId = userProfile.getId();
                String nickName = userProfile.getNickname();
                Log.d(TAG, "userId = " + userId);
                Log.d(TAG, "nickName= " + nickName);

                // firebase 로그인 또는 회원가입
                createIdAndLoginOnFirebase(userId);
            }

            @Override
            public void onNotSignedUp() {
                // 로그인 실패
            }
        });
    }
    /**
     * Firebase 로그인 or 회원가입
     *
     * @param userId
     */
    public void createIdAndLoginOnFirebase(long userId){

        StringBuffer emailBuffer = new StringBuffer();
        emailBuffer.append(String.valueOf(userId));
        emailBuffer.append(KAKAODOMAIN);
        String email = emailBuffer.toString();

        String password = String.valueOf(userId);

        loginModel.firebaseLogin(email,password);
    }

    /**
     * 카카오톡 로그인 결과 수신 콜백
     * */
    private ISessionCallback iSessionCallback = new ISessionCallback() {
        @Override
        public void onSessionOpened() {
            Log.d(TAG, "onSessionOpened()");
            view.showLoading();
            requestMe();
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            Log.d(TAG, "onSessionOpenFailed()");
            if(exception != null) {
                Logger.e(exception);
            }
        }
    };

    /**
     * Firebase 로그인 콜백
     */
    private LoginModel.FirebaseLoginCallback firebaseLoginCallback = new LoginModel.FirebaseLoginCallback() {
        @Override
        public void onSuccess() {
            //view.moveToMainActivity();
        }

        @Override
        public void onSignUp(String email, String password) {
            loginModel.firebaseSignup(email, password);
        }

        @Override
        public void onFailure() {

        }
    };

    /**
     * Firebase 회원 가입 콜백
     */
    private LoginModel.FirebaseSignUpCallback firebaseSignUpCallback = new LoginModel.FirebaseSignUpCallback(){

        @Override
        public void onSuccess(String email, String password) {
            loginModel.firebaseLogin(email,password);
        }

        @Override
        public void onFailure() {

        }
    };


    /**
     * 현재 Firebase 인증 여부 확인 콜백
     */
    private LoginModel.FirebaseAuthCallback firebaseAuthCallback = new LoginModel.FirebaseAuthCallback() {
        @Override
        public void onExist() {
            view.moveToMainActivity();
        }

        @Override
        public void onNotExist() {
            /* kakao login*/
            connectKakaoSession();
        }

    };

}
