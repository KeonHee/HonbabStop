package com.landvibe.android.honbabstop.main.presenter;

/**
 * Created by user on 2017-02-09.
 */

public interface MainPresenter {
    interface View {

        //void moveToMainActivity();

        //void showLoading();

        //void saveUserInfo(User user);

    }

    interface Presenter {

        void attachView(MainPresenter.View view);

        void detachView();

    }


}
