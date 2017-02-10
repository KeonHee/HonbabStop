package com.landvibe.android.honbabstop.Main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.landvibe.android.honbabstop.Login.LoginActivity;
import com.landvibe.android.honbabstop.Login.presenter.LoginPresenterImpl;
import com.landvibe.android.honbabstop.R;
import com.landvibe.android.honbabstop.base.auth.google.GoogleApiClientStore;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity{

    private final static String TAG = "MainActivity";
    @BindView(R.id.btn_logout)
    Button mLogoutBtn;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private LoginManager mFacebookLoginManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        Log.d(TAG,"onCreate()");
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

        mFacebookLoginManager=LoginManager.getInstance();

        mLogoutBtn.setOnClickListener(v -> {

            /* firebase sign out */
            if(mAuth!=null) mAuth.signOut();
            /* kakao sign out */
            onClickLogout();
            /* facebook sign out */
            if(mFacebookLoginManager!=null) {
                mFacebookLoginManager.logOut();
            }
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

    private void onClickLogout() {
        UserManagement.requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
                Log.d(TAG, "kakao logout comlete");
            }
        });
    }
    protected void redirectLoginActivity() {
        final Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

}
