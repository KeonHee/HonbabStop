package com.landvibe.android.honbabstop.UpdateProfile;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.landvibe.android.honbabstop.R;
import com.landvibe.android.honbabstop.UpdateProfile.presenter.UpdateProfilePresenter;
import com.landvibe.android.honbabstop.UpdateProfile.presenter.UpdateProfilePresenterImpl;
import com.landvibe.android.honbabstop.base.domain.User;
import com.landvibe.android.honbabstop.base.domain.UserStore;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by user on 2017-02-14.
 */

public class UpdateProfileActivity extends AppCompatActivity implements UpdateProfilePresenter.View{


    private final static String TAG = "UpdateProfileActivity";

    @BindView(R.id.et_name) MaterialEditText mNameEditText;
    @BindView(R.id.et_email) MaterialEditText mEmailEditText;
    @BindView(R.id.et_status) MaterialEditText mStatusEditText;
    @BindView(R.id.et_address) MaterialEditText mAddressEditText;

    private UpdateProfilePresenterImpl updateProfilePresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_update);
        ButterKnife.bind(this);

        init();

    }

    private void init(){
        setActionBar();

        updateProfilePresenter = new UpdateProfilePresenterImpl();
        updateProfilePresenter.attachView(this,this);
        updateProfilePresenter.loadUserInfo();
    }

    private void setActionBar(){
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        updateProfilePresenter.detachView();
        updateProfilePresenter=null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profie_update,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                moveToMainActivity();
                return true;
            case R.id.action_save:
                updateProfilePresenter.saveUserInfo(getCurrentUser());
                moveToMainActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private User getCurrentUser(){
        User user = UserStore.getUser();
        if(user==null){
            return null;
        }
        user.setName(mNameEditText.getText().toString());
        user.setEmail(mEmailEditText.getText().toString());
        user.setStatus(mStatusEditText.getText().toString());
        user.setAddress(mAddressEditText.getText().toString());
        return user;
    }

    @Override
    public void moveToMainActivity() {
        NavUtils.navigateUpFromSameTask(this);
    }

    @Override
    public void updateUserInfo(User user) {
        runOnUiThread(()->{
            if(user==null ||
                    mNameEditText==null || mEmailEditText==null ||
                    mStatusEditText==null || mAddressEditText==null){
                return;
            }

            if(user.getName()!=null&&user.getName().length()>0){
                mNameEditText.setText(user.getName());
            }
            if(user.getEmail()!=null&&user.getEmail().length()>0){
                mEmailEditText.setText(user.getEmail());
            }
            if(user.getStatus()!=null&&user.getStatus().length()>0){
                mStatusEditText.setText(user.getStatus());
            }
            if(user.getAddress()!=null&&user.getAddress().length()>0){
                mAddressEditText.setText(user.getAddress());
            }

        });
    }
}
