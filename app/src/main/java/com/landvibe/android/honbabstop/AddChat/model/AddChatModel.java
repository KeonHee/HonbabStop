package com.landvibe.android.honbabstop.AddChat.model;

import android.net.Uri;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.landvibe.android.honbabstop.base.domain.ChatRoom;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 2017-02-15.
 */

public class AddChatModel {

    private final static String TAG ="AddChatModel";

    public final static String BASE_URL="gs://honbabstop.appspot.com";

    public final static String CHAT_PATH="chat";

    private DatabaseReference mDatabase;

    private StorageReference mStorage;

    private ImageUploadCallback mImageUploadCallback;

    public interface ImageUploadCallback{
        void onComplete(Uri saveUri, String roomId);
        void onFailure();
        void onProgress(double progress);
        void onPause();
    }

    public void setOnImageUploadCallback(ImageUploadCallback callback){
        mImageUploadCallback=callback;
    }


    public AddChatModel(){
        loadDB();
        loadStorage();
    }

    /**
     * DB instance 로드
     */
    private void loadDB(){
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    /**
     * Chat 추가
     */
    public void createChat(ChatRoom chatRoom){
        String key = mDatabase.child("ChatList").push().getKey();
        chatRoom.setId(key);

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(key, chatRoom);
        mDatabase.child("ChatList").updateChildren(childUpdates);
    }

    /**
     * Storage instance 로드
     */
    private void loadStorage(){
        mStorage = FirebaseStorage.getInstance().getReferenceFromUrl(BASE_URL).child(CHAT_PATH);
    }


    /**
     * 이미지 업로드
     */
    public void saveImageToStorage(String roomId, Uri imageUrl){
        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType("image/jpeg")
                .build();

        //gs://honbabstop.appspot.com/chat/{roomId}/imagename.jpg
        UploadTask uploadTask = mStorage
                .child(roomId)
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
                mImageUploadCallback.onComplete(downloadUrl, roomId);
            }
        });
    }

    public void saveChatImageUrl(Uri imageUrl, String roomId){
        mDatabase.child("ChatList")
                .child(roomId)
                .child("foodImageUrl")
                .setValue(imageUrl.toString());
    }
}
