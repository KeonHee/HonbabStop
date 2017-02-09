package com.landvibe.android.honbabstop.Main.presenter;

import android.app.Activity;

import com.landvibe.android.honbabstop.Login.presenter.LoginPresenter;

/**
 * Created by user on 2017-02-09.
 */

public interface MainPresenter {

    interface View {


    }

    interface Presenter {

        void attachView(MainPresenter.View view, Activity activity);

        void detachView();

    }
}
