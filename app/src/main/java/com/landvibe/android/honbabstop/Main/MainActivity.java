package com.landvibe.android.honbabstop.Main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.landvibe.android.honbabstop.Login.LoginActivity;
import com.landvibe.android.honbabstop.Main.page.BottomNavigationViewHelper;
import com.landvibe.android.honbabstop.Main.page.MainPageAdapter;
import com.landvibe.android.honbabstop.Main.presenter.MainPresenter;
import com.landvibe.android.honbabstop.Main.presenter.MainPresenterImpl;
import com.landvibe.android.honbabstop.R;
import com.landvibe.android.honbabstop.base.auth.google.GoogleApiClientStore;
import com.landvibe.android.honbabstop.base.domain.User;
import com.landvibe.android.honbabstop.base.domain.UserStore;
import com.landvibe.android.honbabstop.base.utils.SharedPreferenceUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainPresenter.View, ViewPager.OnPageChangeListener{

    private final static String TAG = "MainActivity";

    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;

    @BindView(R.id.viewpager_container)
    ViewPager viewPager;
    // SwipeViewPager viewPager;

    private MenuItem prevBottomNavigation;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private MainPresenter.Presenter mainPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Log.d(TAG,"onCreate()");

        init();

    }

    private void init(){

        /* Firebase */
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                // User is signed in
                Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
            } else {
                // User is signed out
                Log.d(TAG, "onAuthStateChanged:signed_out");
                redirectLoginActivity();
            }
        };


        mainPresenter = new MainPresenterImpl();
        mainPresenter.attachView(this);

        viewPager.setAdapter(new MainPageAdapter(getSupportFragmentManager()));
        viewPager.setCurrentItem(0,false);
        viewPager.addOnPageChangeListener(this);
        prevBottomNavigation=bottomNavigationView.getMenu().getItem(0);
        //viewPager.setPagingEnabled(false);
        viewPager.setOffscreenPageLimit(3);


        //BottomNavigationViewHelper.removeShiftMode(bottomNavigationView);  /* bottom nav item 4개 이상*/
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.action_chat_list:
                    viewPager.setCurrentItem(0,false);
                    break;
                case R.id.action_my_chat:
                    viewPager.setCurrentItem(1,false);
                    break;
                case R.id.action_profile:
                    viewPager.setCurrentItem(2,false);
                    break;
            }
            return true;
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
        viewPager.removeOnPageChangeListener(this);
        mainPresenter.detachView();
        mainPresenter=null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_sign_out:
                signOut();
                return true;
            //TODO 리스트 새로고침 기능
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void signOut(){

        /* firebase sign out */
        if(mAuth!=null) mAuth.signOut();

        UserStore.getInstance().saveUser(null);

        SharedPreferenceUtils.setBooleanPreference(
                this,SharedPreferenceUtils.SESSION_BOOLEAN_KEY, false);

        switch (SharedPreferenceUtils.getStringPreference(this, SharedPreferenceUtils.PROVIDER_STRING_KEY)){
            case User.KAKAO:
                kakaoSignOut();
                return;
            case User.FACEBOOK:
                facebookSignOut();
                return;
            case User.GOOGLE:
                googleSignOut();

        }

    }

    private void kakaoSignOut(){
        UserManagement.requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
                Log.d(TAG, "kakao logout comlete");
            }
        });
    }
    private void facebookSignOut(){
        LoginManager.getInstance().logOut();

    }
    private void googleSignOut(){
        GoogleApiClient mGoogleApiClient = GoogleApiClientStore.getGoogleApiClient();
        if(mGoogleApiClient!=null){
            mGoogleApiClient.connect();
            if(mGoogleApiClient.isConnected()){
                Auth.GoogleSignInApi
                        .signOut(mGoogleApiClient)
                        .setResultCallback(status -> Log.d(TAG,"google sign out suecces"));
            }
        }
    }

    protected void redirectLoginActivity() {
        final Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (prevBottomNavigation != null) {
            prevBottomNavigation.setChecked(false);
        }
        prevBottomNavigation = bottomNavigationView.getMenu().getItem(position);
        prevBottomNavigation.setChecked(true);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
