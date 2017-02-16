package com.landvibe.android.honbabstop.ChatDetail.presenter;

import android.app.Activity;


/**
 * Created by user on 2017-02-16.
 */

public interface ChatDetailPresenter {

    interface View{

        void moveToMainActivity();

    }

    interface Presenter{
        void attachView(ChatDetailPresenter.View view, Activity activity);

        void detachView();
    }
}
