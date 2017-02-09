package com.landvibe.android.honbabstop.Login.presenter;

import android.app.Activity;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;
import com.landvibe.android.honbabstop.Login.model.LoginModel;

/**
 * Created by user on 2017-02-09.
 */

public class LoginPresenterImpl implements LoginPresenter.Presenter {


    private final static String TAG = "LoginPresenterImpl";

    public static final String KAKAODOMAIN = "@kakao.com";
    public static final String KAKAOPROVIDER = "kakao";

    private LoginPresenter.View view;

    private LoginModel loginModel;

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

    }

    @Override
    public void detachView() {
        view = null;
        loginModel.setActivity(null);
        loginModel.setOnLoginLisenter(null);
        loginModel.setOnSignupLisenter(null);
        loginModel.setOnAuthLisenter(null);
        loginModel=null;

        closeKakaoSession();
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
            view.moveToMainActivity();
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
