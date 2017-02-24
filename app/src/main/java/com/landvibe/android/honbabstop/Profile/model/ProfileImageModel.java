package com.landvibe.android.honbabstop.profile.model;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.landvibe.android.honbabstop.base.domain.User;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by user on 2017-02-14.
 */

public class ProfileImageModel {

    private final static String TAG ="ProfileImageModel";

    public final static String BASE_URL="gs://honbabstop.appspot.com";

    public final static String PROFILE_PATH="profile";

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mDatabase;
    private StorageReference mStorage;

    private ImageUploadCallback mImageUploadCallback;
    private ChangeProfileImage mChangeProfileImage;

    public interface ImageUploadCallback{
        void onComplete(Uri saveUri);
        void onFailure();
        void onProgress(double progress);
        void onPause();
    }

    public interface ChangeProfileImage{
        void update(String url);
    }

    public void setOnImageUploadCallback(ImageUploadCallback callback){
        mImageUploadCallback=callback;
    }
    public void setOnImageChangeCallback(ChangeProfileImage callback){
        mChangeProfileImage=callback;
    }

    public ProfileImageModel(){
        loadAuth();
        loadDB();
        loadStorage();
    }

    /**
     * 인증 정보 로드
     */
    private void loadAuth(){
        mAuth=FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        if (mUser != null) {
            Log.d(TAG, "onAuthStateChanged:signed_in:" + mUser.getUid());
        } else {
            Log.d(TAG, "onAuthStateChanged:signed_out");
        }
    }

    /**
     * DB instance 로드
     */
    private void loadDB(){
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    /**
     * Storage instance 로드
     */
    private void loadStorage(){
        if(mUser==null){
            return;
        }
        String uid = mUser.getUid();
        mStorage = FirebaseStorage.getInstance().getReferenceFromUrl(BASE_URL).child(PROFILE_PATH).child(uid);
    }

    public void saveImageToStorage(Activity activity, Uri imageUrl){

        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType("image/jpeg")
                .build();

        //gs://honbabstop.appspot.com/profile/{uid}/imagename.jpg
        UploadTask uploadTask = mStorage
                .child(imageUrl.getLastPathSegment()+".jpeg")
                .putFile(imageUrl,metadata);

        uploadTask.addOnProgressListener(taskSnapshot -> {
            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
            Log.d(TAG, "Upload is " + progress + " done");
            if(mImageUploadCallback!=null){
                mImageUploadCallback.onProgress(progress);
            }
        }).addOnPausedListener(taskSnapshot -> {
            System.out.println("Upload is paused");
            if(mImageUploadCallback!=null){
                mImageUploadCallback.onPause();
            }
        }).addOnFailureListener(e -> {
            if(mImageUploadCallback!=null){
                mImageUploadCallback.onFailure();
            }
        }).addOnSuccessListener(taskSnapshot -> {
            Uri downloadUrl = taskSnapshot.getMetadata().getDownloadUrl();
            Log.d(TAG, "Upload is Success");
            Log.d(TAG, "downloadUrl :" + downloadUrl);
            Log.d(TAG, "bucket :" + taskSnapshot.getMetadata().getBucket());
            Log.d(TAG, "getPath :" + taskSnapshot.getMetadata().getPath());
            if(mImageUploadCallback!=null){
                mImageUploadCallback.onComplete(downloadUrl);
            }
        });
    }

    public void changeProfileUrl(Uri profileUrl){
        if(mDatabase==null || mUser==null){
            return;
        }

        String uid = mUser.getUid();
        mDatabase.child("users").child(uid).runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                User user = mutableData.getValue(User.class);
                if(user==null){
                    return Transaction.success(mutableData);
                }
                Log.d(TAG, "db url change in transaction");
                user.setProfileUrl(profileUrl.toString());
                mutableData.setValue(user);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                if(mChangeProfileImage!=null){
                    Log.d(TAG, "DB Change Complete");
                    mChangeProfileImage.update(profileUrl.toString());
                }
            }
        });

    }



}
