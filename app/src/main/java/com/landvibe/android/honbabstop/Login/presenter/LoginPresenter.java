package com.landvibe.android.honbabstop.Login.presenter;

import android.app.Activity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kakao.auth.ISessionCallback;

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

        void attachView(View view);

        void detachView();

        void setActivity(Activity activity);

        void removeSessionCallback();

    }
}
