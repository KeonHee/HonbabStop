package com.landvibe.android.honbabstop.Login.presenter;

import android.app.Activity;
import android.content.Intent;

import com.facebook.login.widget.LoginButton;

import java.util.List;

/**
 * Created by user on 2017-02-09.
 */

public interface LoginPresenter {

    interface View {

        void moveToMainActivity();

        void moveToLoginActivity();

        void showLoading();

    }

    interface Presenter {

        void attachView(View view, Activity activity);

        void detachView();

        void setFacebookLoginCallback(LoginButton facebookLoginBtn);

        boolean onFacebookActivityResult(int requestCode, int resultCode, Intent data);

        boolean onKakaoActivityResult(int requestCode, int resultCode, Intent data);

    }
}
