package com.landvibe.android.honbabstop.login.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;
import com.landvibe.android.honbabstop.login.model.LoginModel;
import com.landvibe.android.honbabstop.base.auth.google.GoogleApiClientStore;

import java.util.Arrays;

/**
 * Created by user on 2017-02-09.
 */

public class LoginPresenterImpl implements LoginPresenter.Presenter {


    private final static String TAG = "LoginPresenterImpl";

    public static final String KAKAODOMAIN = "@kakao.com";
    public static final String KAKAOPROVIDER = "kakao";

    private LoginPresenter.View view;

    private LoginModel loginModel;
    private Activity mActivity;

    private CallbackManager mFacebookCallbackManager;
    private LoginButton mFacebookLoginBtn;

    private GoogleApiClient mGoogleApiClient;

    @Override
    public void attachView(LoginPresenter.View view, Activity activity) {
        this.view = view;

        mActivity=activity;

        loginModel=new LoginModel();
        loginModel.setOnLoginLisenter(firebaseLoginCallback);
        loginModel.setOnSignupLisenter(firebaseSignUpCallback);
        loginModel.setOnAuthLisenter(firebaseAuthCallback);
        loginModel.setActivity(activity);
        loginModel.loadAuth();
        loginModel.loadDB();

        connectKakaoSession();

        mFacebookCallbackManager=CallbackManager.Factory.create();
    }

    @Override
    public void detachView() {
        view = null;

        mActivity=null;

        loginModel.setActivity(null);
        loginModel.setOnLoginLisenter(null);
        loginModel.setOnSignupLisenter(null);
        loginModel.setOnAuthLisenter(null);
        loginModel=null;

        closeKakaoSession();

        mFacebookCallbackManager=null;
        firebaseAuthCallback=null;
        iSessionCallback=null;
        firebaseLoginCallback=null;
        firebaseSignUpCallback =null;
        facebookCallback=null;
        onConnectionFailedListener=null;
        mFacebookLoginBtn=null;
    }

    /**
     * 현재 Firebase 인증 여부 확인 콜백
     */
    private LoginModel.FirebaseAuthCallback firebaseAuthCallback = new LoginModel.FirebaseAuthCallback() {
        @Override
        public void onExist() {
            view.moveToMainActivity();
            iSessionCallback=null;
        }

        @Override
        public void onNotExist() {

        }
    };

    /************************************** Kakao **************************************/

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
     * 카카오톡 콜백 메소드 실행
     * 네이티브 앱으로 가입시 수행
     * Webview 로그인시 바로 콜백 메소드 호출
     */
    @Override
    public boolean onKakaoActivityResult(int requestCode, int resultCode, Intent data) {
        return Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data);
    }
    /**
     * 카카오톡 로그인 결과 수신 콜백
     * Webview 로그인시 바로 콜백 메소드 호출
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
     * 카카오톡 프로필 정보 요청
     */
    private void requestMe() {
        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onSuccess(UserProfile userProfile) {
                Logger.d("UserProfile : " + userProfile);
                long userId = userProfile.getId();
                String nickName = userProfile.getNickname();
                String profileUrl = userProfile.getProfileImagePath();
                Log.d(TAG, "userId = " + userId);
                Log.d(TAG, "nickName= " + nickName);


                // firebase Auth
                firebaseAuthWithEmailForKakao(userId,nickName,profileUrl);
            }

            @Override
            public void onFailure(ErrorResult errorResult) {
                String message = "failed to get user info. msg=" + errorResult;
                Logger.d(message);

                //view.moveToLoginActivity();
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                //view.moveToLoginActivity();
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
    public void firebaseAuthWithEmailForKakao(long userId, String name, String profileUrl){

        StringBuffer emailBuffer = new StringBuffer();
        emailBuffer.append(String.valueOf(userId));
        emailBuffer.append(KAKAODOMAIN);
        String email = emailBuffer.toString();

        String password = String.valueOf(userId);

        loginModel.firebaseLogin(email,password, name, profileUrl);
    }
    /**
     * Firebase 로그인 콜백
     */
    private LoginModel.FirebaseLoginCallback firebaseLoginCallback = new LoginModel.FirebaseLoginCallback() {
        @Override
        public void onSuccess() {
            view.moveToMainActivity();
        }

        @Override
        public void onSignUp(String email, String password, String name, String profileUrl) {
            loginModel.firebaseSignup(email, password, name, profileUrl);
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
        public void onSuccess(String email, String password, String name, String profileUrl) {
            loginModel.firebaseLogin(email,password,name,profileUrl);
        }

        @Override
        public void onFailure() {

        }
    };

    /************************************** Faceboock **************************************/
    /**
     * Faceboock 로그인 콜백 등록
     */
    @Override
    public void setFacebookLoginCallback(LoginButton facebookLoginBtn) {
        mFacebookLoginBtn=facebookLoginBtn;
        mFacebookLoginBtn.setReadPermissions(Arrays.asList("public_profile","email"));
        mFacebookLoginBtn.registerCallback(mFacebookCallbackManager,facebookCallback);
    }

    /**
     * Facebook 로그인 요청 콜백 실행
     */
    @Override
    public boolean onFacebookActivityResult(int requestCode, int resultCode, Intent data) {
        return mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Facebook 로그인 요청 콜백
     */
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

    /************************************** Google **************************************/

    /**
     *  Google 로그인 클라이언트 생성
     */
    @Override
    public void setGoogleApiClient(Context context, AppCompatActivity activity, String token) {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(token)
                .requestEmail()
                .build();
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .enableAutoManage(activity /* AppCompatActivity */, onConnectionFailedListener /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        GoogleApiClientStore.storeGoogleApiClient(mGoogleApiClient);
    }

    /**
     * Google 로그인 클라이언트 생성 에러 리스너
     */
    private GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener = connectionResult
            -> Log.d(TAG, connectionResult.getErrorMessage());

    /**
     * Google 로그인 요청
     */
    @Override
    public void onGoogleLogin(Activity activity, int requestCode) {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        activity.startActivityForResult(signInIntent, requestCode);
    }

    /**
     *  Google 로그인 요청 콜백
     */
    @Override
    public void onGoogleActivityResult(Intent data) {
        view.showLoading();

        GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
        if (result.isSuccess()) {
            // Google Sign In was successful, authenticate with Firebase
            GoogleSignInAccount account = result.getSignInAccount();
            loginModel.firebaseAuthWithGoogle(account);
        } else {

        }
    }
}
