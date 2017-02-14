package com.landvibe.android.honbabstop.Main.presenter;


import com.landvibe.android.honbabstop.Main.model.MainModel;

/**
 * Created by user on 2017-02-09.
 */

public class MainPresenterImpl implements MainPresenter.Presenter{


    private MainPresenter.View view;

    @Override
    public void attachView(MainPresenter.View view) {
        this.view=view;
    }

    @Override
    public void detachView() {
        this.view=null;
    }


}
