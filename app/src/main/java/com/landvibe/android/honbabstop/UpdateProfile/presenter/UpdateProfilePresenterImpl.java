package com.landvibe.android.honbabstop.updateprofile.presenter;

import android.app.Activity;

import com.landvibe.android.honbabstop.GlobalApp;
import com.landvibe.android.honbabstop.profile.model.ProfileModel;
import com.landvibe.android.honbabstop.base.domain.User;
import com.landvibe.android.honbabstop.base.domain.UserStore;


/**
 * Created by user on 2017-02-14.
 */

public class UpdateProfilePresenterImpl implements UpdateProfilePresenter.Presenter{


    private UpdateProfilePresenter.View view;
    private Activity mActivity;

    private ProfileModel mProfileModel;

    @Override
    public void attachView(UpdateProfilePresenter.View view, Activity activity) {
        this.view=view;
        mActivity=activity;

        mProfileModel = new ProfileModel();
    }

    @Override
    public void detachView() {
        this.view=null;
        mActivity=null;

        mProfileModel=null;
    }

    @Override
    public void loadUserInfo() {
        User user = UserStore.getInstance().getUser();
        if(user==null){
            view.moveToMainActivity();
            return;
        }
        view.updateUserInfo(user);
    }

    @Override
    public void saveUserInfo(User user) {
        if(user==null){
            return;
        }
        mProfileModel.saveUser(user);
        view.moveToMainActivity();

        GlobalApp.getGlobalApplicationContext().changeModel(user);
    }

}

