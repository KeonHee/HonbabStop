package com.landvibe.android.honbabstop.profile;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.javiersantos.bottomdialogs.BottomDialog;
import com.landvibe.android.honbabstop.profile.presenter.ProfilePresenter;
import com.landvibe.android.honbabstop.profile.presenter.ProfilePresenterImpl;
import com.landvibe.android.honbabstop.R;
import com.landvibe.android.honbabstop.updateprofile.UpdateProfileActivity;
import com.landvibe.android.honbabstop.base.domain.User;
import com.landvibe.android.honbabstop.base.domain.UserStore;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by user on 2017-02-13.
 */

public class ProfileFragment extends Fragment implements ProfilePresenter.View {

    private final static String TAG ="ProfileFragment";

    private final int REQ_CODE_SELECT_IMAGE = 1001;

    private int mPage;

    @BindView(R.id.iv_avatar) ImageView mAvatarImageView;

    @BindView(R.id.tv_name) TextView mNameTextView;

    @BindView(R.id.iv_provider) ImageView mProviderImageView;

    @BindView(R.id.tv_email) TextView mEmailTextView;

    @BindView(R.id.tv_status) TextView mStatusTextView;

    @BindView(R.id.tv_age) TextView mAgeTextView;

    @BindView(R.id.tv_gender) TextView mGenderTextView;

    @BindView(R.id.tv_address) TextView mAddressTextView;

    @BindView(R.id.btn_settings) Button mSettingsBtn;

    private ProfilePresenter.Presenter profilePresenter;

    public static Fragment getInstance(int page){
        ProfileFragment profileFragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putInt("page", page);
        profileFragment.setArguments(args);
        return profileFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
        mPage=getArguments().getInt("page");
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container,false);
        ButterKnife.bind(this,view);
        Log.d(TAG, "onCreateView()");

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated()");

        init();
    }

    private void init(){
        profilePresenter = new ProfilePresenterImpl();
        profilePresenter.attachView(this,getActivity());
        profilePresenter.loadUser();

        mSettingsBtn.setOnClickListener(v->{
            if(UserStore.getInstance().getUser()!=null) {
                moveToUpdateProfileActivity();
            }
        });

        mAvatarImageView.setOnClickListener(v -> showGalleryDialog());
    }

    private void showGalleryDialog(){
        new BottomDialog.Builder(getActivity())
                .setTitle(getActivity().getString(R.string.dialog_title))
                .setContent(getActivity().getString(R.string.dialog_content))
                .setPositiveText("OK")
                .setPositiveBackgroundColorResource(R.color.fbutton_color_pomegranate)
                .setPositiveTextColorResource(android.R.color.white)
                .onPositive(dialog -> getPhotoFromGallery())
                .show();
    }

    public void getPhotoFromGallery() {
        startActivityForResult(
                Intent.createChooser(
                        new Intent(Intent.ACTION_GET_CONTENT)
                                .setType("image/*"), "사진 가져오기"),
                REQ_CODE_SELECT_IMAGE);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart()");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop()");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView()");
        profilePresenter.detachView();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "resultCode : " + resultCode);
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == REQ_CODE_SELECT_IMAGE) {
                Uri imageUrl = data.getData();
                Log.d(TAG, "selectedImageUri : " + imageUrl);
                profilePresenter.uploadImageToStorage(imageUrl);
            }
        }
    }

    @Override
    public void updateUserProfile(String url) {
        getActivity().runOnUiThread(() -> {
            if(mAvatarImageView==null){
                return;
            }
            //TODO 프로그레스바
            Glide.with(this)
                    .load(url)
                    .override(200,200)
                    //.placeholder(R.drawable.default_profile)
                    .bitmapTransform(new CropCircleTransformation(getActivity()))
                    .thumbnail(
                            Glide.with(this)
                            .load(R.drawable.default_profile)
                            .override(200,200)
                            .bitmapTransform(new CropCircleTransformation(getActivity()))
                    )
                    .crossFade()
                    .into(mAvatarImageView);

        });
    }

    @Override
    public void updateUserName(String name) {
        getActivity().runOnUiThread(() -> {
            if(mNameTextView==null){
                return;
            }
            mNameTextView.setText(name);
        });
    }

    @Override
    public void updateUserProvider(String provider) {
        getActivity().runOnUiThread(()->{
            if(mProviderImageView==null){
                return;
            }
            int drawableId;
            if(provider.equals(User.KAKAO)){
                drawableId=R.drawable.kakaolink_btn_medium;
            }else if(provider.equals(User.FACEBOOK)){
                drawableId=R.drawable.facebook_logo_100;
            }else {
                drawableId=R.drawable.google_logo;
            }
            Glide.with(this).load(drawableId)
                    .override(75,75)
                    .into(mProviderImageView);
        });
    }

    @Override
    public void updateUserEmail(String email) {
        getActivity().runOnUiThread(()->{
            if(mEmailTextView==null){
                return;
            }
            mEmailTextView.setText(email);
        });

    }

    @Override
    public void updateUserStatus(String status) {
        getActivity().runOnUiThread(()->{
            if(status==null){
                return;
            }
            mStatusTextView.setText(status);
        });
    }

    @Override
    public void updateUserAge(String ageStr) {
        getActivity().runOnUiThread(()->{
            if(mAgeTextView==null){
                return;
            }
            mAgeTextView.setText(ageStr);
        });
    }

    @Override
    public void updateUserGender(String gender) {
        getActivity().runOnUiThread(()->{
            if(mGenderTextView==null){
                return;
            }
            mGenderTextView.setText(gender);
        });
    }

    @Override
    public void updateAddress(String address) {
        getActivity().runOnUiThread(()->{
            if(mAddressTextView==null){
                return;
            }
            mAddressTextView.setText(address);
        });
    }

    @Override
    public void moveToUpdateProfileActivity() {
        final Activity activity = getActivity();
        final Intent intent = new Intent(activity, UpdateProfileActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }
}
