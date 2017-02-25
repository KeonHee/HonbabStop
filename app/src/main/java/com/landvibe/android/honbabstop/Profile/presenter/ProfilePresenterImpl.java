package com.landvibe.android.honbabstop.profile.presenter;

import android.app.Activity;
import android.net.Uri;

import com.landvibe.android.honbabstop.GlobalApp;
import com.landvibe.android.honbabstop.profile.model.ProfileImageModel;
import com.landvibe.android.honbabstop.profile.model.ProfileModel;
import com.landvibe.android.honbabstop.R;
import com.landvibe.android.honbabstop.base.domain.User;
import com.landvibe.android.honbabstop.base.observer.CustomObserver;


public class ProfilePresenterImpl implements ProfilePresenter.Presenter, ProfileModel.UserDataChange,
        ProfileImageModel.ImageUploadCallback, ProfileImageModel.ChangeProfileImage, CustomObserver{

    private ProfilePresenter.View view;

    private ProfileModel mProfileModel;
    private ProfileImageModel mProfileImageModel;

    private Activity mActivity;


    /**
     * Presenter
     */
    @Override
    public void attachView(ProfilePresenter.View view, Activity activity) {
        this.view=view;
        mActivity=activity;

        mProfileModel = new ProfileModel();
        mProfileModel.setOnChangeListener(this);

        mProfileImageModel = new ProfileImageModel();
        mProfileImageModel.setOnImageUploadCallback(this);
        mProfileImageModel.setOnImageChangeCallback(this);

        GlobalApp.getGlobalApplicationContext().addObserver(this);
    }

    @Override
    public void detachView() {
        this.view=null;
        mActivity=null;

        mProfileModel.setOnChangeListener(null);
        mProfileModel=null;

        mProfileImageModel.setOnImageUploadCallback(null);
        mProfileImageModel.setOnImageChangeCallback(null);
        mProfileImageModel=null;

        GlobalApp.getGlobalApplicationContext().removeObserver(this);
    }

    @Override
    public void loadUser() {
        mProfileModel.loadUser();
    }

    @Override
    public void renewUserInfo() {
        mProfileModel.notifyUserInfoChange();
    }

    @Override
    public void uploadImageToStorage(Uri url) {
        view.showLoading();
        mProfileImageModel.saveImageToStorage(mActivity, url);
    }

    private void updateUserView(User user){
        if (user==null || view==null){
            return;
        }

        if (user.getProfileUrl()!=null && user.getProfileUrl().length()>0){
            view.updateUserProfile(user.getProfileUrl());
        }

        if (user.getName()!=null && user.getName().length()>0){
            view.updateUserName(user.getName());
        }else {
            view.updateUserName(mActivity.getString(R.string.empty_name));
        }

        if(user.getProviderId()!=null){
            view.updateUserProvider(user.getProviderId());
        }

        if(user.getEmail()!=null && user.getEmail().length()>0){
            if(user.getEmail().contains("@"+User.KAKAO)){
                view.updateUserEmail(mActivity.getString(R.string.empty_email));
            }else {
                view.updateUserEmail(user.getEmail());
            }
        }else {
            view.updateUserEmail(mActivity.getString(R.string.empty_email));
        }

        if (user.getStatus()!=null && user.getStatus().length()>0){
            view.updateUserStatus(user.getStatus());
        }else {
            view.updateUserStatus(mActivity.getString(R.string.empty_status));
        }

        if(user.getAge()!=0){
            view.updateUserAge(String.valueOf(user.getAge())+" 살");
        }else {
            view.updateUserAge(mActivity.getString(R.string.empty_age));
        }


        if(user.getGender()!=null){
            view.updateUserGender(user.getGender());
        }else {
            view.updateUserGender(mActivity.getString(R.string.empty_gender));
        }

        if(user.getAddress()!=null && user.getAddress().length()>0){
            view.updateAddress(user.getAddress());
        }else {
            view.updateAddress(mActivity.getString(R.string.empty_address));
        }
    }

    /**
     * UserDataChange
     */
    @Override
    public void update(User user) {
        updateUserView(user);
    }


    /**
     * ChangeProfileImage
     */
    @Override
    public void update(String url) {
        view.updateUserProfile(url);
    }

    /**
     * ImageUploadCallback
     */
    @Override
    public void onComplete(Uri saveUri) {
        mProfileImageModel.changeProfileUrl(saveUri);
        view.hideLoading();
    }

    @Override
    public void onFailure() {

    }

    @Override
    public void onProgress(double progress) {

    }

    @Override
    public void onPause() {

    }


    /**
     *  CustomObserver
     */
    @Override
    public void update(Object object) {
        if(object instanceof User){
            updateUserView((User)object);
        }
    }
}
