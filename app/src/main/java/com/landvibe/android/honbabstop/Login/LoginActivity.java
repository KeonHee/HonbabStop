package com.landvibe.android.honbabstop.Login;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.kakao.auth.Session;
import com.landvibe.android.honbabstop.Login.presenter.LoginPresenter;
import com.landvibe.android.honbabstop.Login.presenter.LoginPresenterImpl;
import com.landvibe.android.honbabstop.Main.MainActivity;
import com.landvibe.android.honbabstop.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class LoginActivity extends AppCompatActivity
    implements LoginPresenter.View{

    private final static String TAG = "LoginActivity";

    private LoginPresenterImpl loginPresenter;

    @BindView(R.id.pb_loading_indicator)
    ProgressBar mLoadingIndicator;

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

    }

    private void changeStatusBarColor(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.deep_orange));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            Log.d(TAG, "handleActivityResult()");
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
