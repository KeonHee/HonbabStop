package com.landvibe.android.honbabstop.Login;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.SignInButton;
import com.landvibe.android.honbabstop.Login.presenter.LoginPresenter;
import com.landvibe.android.honbabstop.Login.presenter.LoginPresenterImpl;
import com.landvibe.android.honbabstop.Main.MainActivity;
import com.landvibe.android.honbabstop.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class LoginActivity extends AppCompatActivity
    implements LoginPresenter.View{

    private final static String TAG = "LoginActivity";

    public static final int KAKAO_SIGN_IN_REQEUST_CODE = 1;
    public static final int FACEBOOK_SIGN_IN_REQEUST_CODE = 64206;
    public static final int GOOGLE_SIGN_IN_REQEUST_CODE = 1024;

    private LoginPresenterImpl loginPresenter;

    @BindView(R.id.pb_loading_indicator)
    ProgressBar mLoadingIndicator;

    @BindView(R.id.btn_facebook_login)
    LoginButton mFacebookLoginBtn;

    @BindView(R.id.btn_google_login)
    SignInButton mGoogleLoginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        init();

    }

    private void init(){

        changeStatusBarColor();

        loginPresenter = new LoginPresenterImpl();
        loginPresenter.attachView(this,this);

        loginPresenter.setFacebookLoginCallback(mFacebookLoginBtn);

        loginPresenter.setGoogleApiClient(this,this,getString(R.string.google_web_client_id));
        mGoogleLoginBtn.setSize(SignInButton.SIZE_WIDE);
        mGoogleLoginBtn.setOnClickListener(v->loginPresenter.onGoogleLogin(this, GOOGLE_SIGN_IN_REQEUST_CODE));
    }

    private void changeStatusBarColor(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.deep_orange));
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        loginPresenter.addAuthListener();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==KAKAO_SIGN_IN_REQEUST_CODE) {
            if (loginPresenter.onKakaoActivityResult(requestCode,resultCode,data)) {
                Log.d(TAG, "kakao requestCode : " + requestCode);
                Log.d(TAG, "kakao resultCode : " + resultCode);
                return;
            }
        }else if(requestCode==FACEBOOK_SIGN_IN_REQEUST_CODE){
            if(loginPresenter.onFacebookActivityResult(requestCode,resultCode,data)){
                Log.d(TAG, "facebook requestCode : " + requestCode);
                Log.d(TAG, "facebook resultCode : " + resultCode);
                return;
            }
        }else if(requestCode==GOOGLE_SIGN_IN_REQEUST_CODE){
            loginPresenter.onGoogleActivityResult(data);
            Log.d(TAG, "google requestCode : " + requestCode);
            Log.d(TAG, "google resultCode : " + resultCode);
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        loginPresenter.detachView();
        loginPresenter=null;
    }


    @Override
    public void moveToMainActivity() {
        final Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    @Override
    public void moveToLoginActivity() {
        final Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    @Override
    public void showLoading(){
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

}
