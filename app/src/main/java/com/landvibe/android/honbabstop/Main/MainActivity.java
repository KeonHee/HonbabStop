package com.landvibe.android.honbabstop.Main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
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
import com.landvibe.android.honbabstop.Main.page.SwipeViewPager;
import com.landvibe.android.honbabstop.Main.presenter.MainPresenter;
import com.landvibe.android.honbabstop.Main.presenter.MainPresenterImpl;
import com.landvibe.android.honbabstop.R;
import com.landvibe.android.honbabstop.auth.google.GoogleApiClientStore;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainPresenter.View{

    private final static String TAG = "MainActivity";

    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;

    @BindView(R.id.viewpager_container)
    SwipeViewPager viewPager;

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
        viewPager.setPagingEnabled(false);
        viewPager.setOffscreenPageLimit(3);


        BottomNavigationViewHelper.removeShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.action_chat_list:
                    viewPager.setCurrentItem(0,false);
                    break;
                case R.id.action_my_chat:
                    viewPager.setCurrentItem(1,false);
                    break;
                case R.id.action_friends:
                    viewPager.setCurrentItem(2,false);
                    break;
                case R.id.action_profile:
                    viewPager.setCurrentItem(3,false);
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
        }

        return super.onOptionsItemSelected(item);
    }

    private void signOut(){
        //TODO 현재 회원가입 되어있는 provider만 로그아웃 시키기 (SharedPreference사용)
        /* firebase sign out */
        if(mAuth!=null) mAuth.signOut();

        /* kakao sign out */
        UserManagement.requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
                Log.d(TAG, "kakao logout comlete");
            }
        });

        /* facebook sign out */
        LoginManager.getInstance().logOut();

        /* google sign out */
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

}
