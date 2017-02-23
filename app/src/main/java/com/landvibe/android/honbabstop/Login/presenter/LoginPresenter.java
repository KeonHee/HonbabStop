package com.landvibe.android.honbabstop.login.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.facebook.login.widget.LoginButton;

/**
 * Created by user on 2017-02-09.
 */

public interface LoginPresenter {

    interface View {

        void moveToMainActivity();

        void showLoading();

        void hideLoading();
    }

    interface Presenter {

        void attachView(View view, Activity activity);

        void detachView();

        void setFacebookLoginCallback(LoginButton facebookLoginBtn);

        void setGoogleApiClient(Context context, AppCompatActivity activity, String token);

        void onGoogleLogin(Activity activity, int requestCode);

        boolean onFacebookActivityResult(int requestCode, int resultCode, Intent data);

        boolean onKakaoActivityResult(int requestCode, int resultCode, Intent data);

        void onGoogleActivityResult(Intent data);


    }
}
