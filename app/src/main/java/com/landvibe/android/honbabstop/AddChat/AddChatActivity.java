package com.landvibe.android.honbabstop.addchat;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.github.javiersantos.bottomdialogs.BottomDialog;
import com.landvibe.android.honbabstop.R;
import com.landvibe.android.honbabstop.addchat.presenter.AddChatPresenter;
import com.landvibe.android.honbabstop.addchat.presenter.AddChatPresenterImpl;
import com.landvibe.android.honbabstop.base.domain.ChatRoom;
import com.landvibe.android.honbabstop.base.domain.FoodRestaurant;
import com.landvibe.android.honbabstop.base.domain.User;
import com.landvibe.android.honbabstop.base.domain.UserStore;
import com.landvibe.android.honbabstop.base.listener.OnShowMarkerListener;
import com.landvibe.android.honbabstop.base.utils.TimeFormatUtils;
import com.landvibe.android.honbabstop.chatdetail.ChatDetailActivity;
import com.landvibe.android.honbabstop.search.SearchActivity;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.shawnlin.numberpicker.NumberPicker;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import info.hoang8f.widget.FButton;

public class AddChatActivity extends AppCompatActivity
        implements AddChatPresenter.View, TimePickerDialog.OnTimeSetListener{

    private final static String TAG = "AddChatActivity";

    private final int REQ_CODE_SELECT_IMAGE = 1001;

    @BindView(R.id.et_title)
    MaterialEditText mTitleEditText;

    @BindView(R.id.np_max_people)
    NumberPicker mMaxPeoplePicker;

    @BindView(R.id.btn_contact_time_label)
    FButton mSelectTimeBtn;

    @BindView(R.id.et_food_name)
    MaterialEditText mFoodNameEditText;

    @BindView(R.id.btn_food_image_upload)
    FButton mImageUploadButton;

    @BindView(R.id.btn_search_restaurant)
    FButton mSearchButton;

    @BindView(R.id.activity_add_chat)
    LinearLayout mActivityContainer;

    @BindView(R.id.maps_fragment_space)
    LinearLayout mMapContainer;

    private AddChatPresenter.Presenter mAddChatPresenter;

    private NMapFragment mMapFragment;

    private int selectedHour=1;
    private int selectedMinute=0;

    private FoodRestaurant mSelectedRestaurant;

    private OnShowMarkerListener mMarkerListener;

    private Uri tmpUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_chat);
        Log.d(TAG, "onCreate");
        ButterKnife.bind(this);

        init();
    }

    private void init(){
        setActionBar();
        initMapFragment();

        mAddChatPresenter = new AddChatPresenterImpl();
        mAddChatPresenter.attachView(this,this);

        mSelectTimeBtn.setOnClickListener(v-> showTimePicker());

        mImageUploadButton.setOnClickListener(v->showGalleryDialog());

        mSearchButton.setOnClickListener(v->moveToSearchActivity());
    }

    private void setActionBar(){
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initMapFragment(){
        mMapFragment = new NMapFragment();
        mMapFragment.setArguments(new Bundle());
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.maps_fragment_space, mMapFragment);
        fragmentTransaction.commit();

        mMarkerListener=mMapFragment;
    }

    private void showTimePicker(){
        Calendar now = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                false
        );
        tpd.show(getFragmentManager(), "Timepickerdialog");
    }

    private void showGalleryDialog(){
        new BottomDialog.Builder(this)
                .setTitle(this.getString(R.string.dialog_title))
                .setContent(this.getString(R.string.dialog_content))
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
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        mAddChatPresenter.detachView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "resultCode : " + resultCode);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQ_CODE_SELECT_IMAGE) {
                tmpUri = data.getData();
                Log.d(TAG, "selectedImageUri : " + tmpUri);

                //TODO 사진 보여주기
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.chat_add,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                moveToMainActivity();
                return true;
            case R.id.action_add_chat:
                createChatRoom();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void createChatRoom(){
        if(mSelectedRestaurant==null){
            return;
        }

        ChatRoom chatRoom = new ChatRoom();

        chatRoom.setTitle(mTitleEditText.getText().toString());

        /* 방 생성 시간*/
        Calendar now = Calendar.getInstance();
        chatRoom.setStartTimeStamp(now.getTimeInMillis());

        /* 만남 시간 */
        now.set(Calendar.HOUR_OF_DAY,selectedHour);
        now.set(Calendar.MINUTE,selectedMinute);
        chatRoom.setContactTime(now.getTimeInMillis());

        /* 만남 하루 뒤 삭제*/
        now.set(Calendar.DAY_OF_MONTH,now.get(Calendar.DAY_OF_MONTH)+1);
        chatRoom.setEndTimeStamp(now.getTimeInMillis());

        /* 참가 인원 */
        chatRoom.setCurrentPeople(1);
        chatRoom.setMaxPeople(mMaxPeoplePicker.getValue());

        /* 음식 정보 */
        chatRoom.setFoodName(mFoodNameEditText.getText().toString());

        chatRoom.setFoodTitle(mSelectedRestaurant.getTitle().replace("<b>","").replace("</b>",""));
        chatRoom.setFoodCategory(mSelectedRestaurant.getCategory());
        chatRoom.setFoodDescription(mSelectedRestaurant.getDescription());
        chatRoom.setFoodTelephone(mSelectedRestaurant.getTelephone());
        chatRoom.setAddress(mSelectedRestaurant.getAddress());
        chatRoom.setRoadAddress(mSelectedRestaurant.getRoadAddress());
        chatRoom.setLat(mSelectedRestaurant.getLat());
        chatRoom.setLon(mSelectedRestaurant.getLon());

        /* 방장 정보 */
        User user = UserStore.getInstance().getUser();
        chatRoom.setHeader(user);

        /* 참여자 정보 (UID)*/
        List<User> members = Arrays.asList(user);
        chatRoom.setMembers(members);

        mAddChatPresenter.addChat(chatRoom, tmpUri);
    }

    @Override
    public void moveToMainActivity() {
        NavUtils.navigateUpFromSameTask(this);
    }

    @Override
    public void moveToChatDetailActivity(String roomId) {
        final Intent intent = new Intent(this, ChatDetailActivity.class);
        intent.putExtra("roomId",roomId);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    @Override
    public void moveToSearchActivity() {
        final Intent intent = new Intent(this, SearchActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    @Override
    public void showMapMarker(FoodRestaurant foodRestaurant) {
        mSelectedRestaurant=foodRestaurant;
        Log.d(TAG, foodRestaurant.getTitle());
        Log.d(TAG, foodRestaurant.getMapx() + " : " + foodRestaurant.getMapy());

        if(mMarkerListener!=null){
            mMarkerListener.onMarkPin(foodRestaurant);
        }
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {

        Calendar contactTime = Calendar.getInstance();
        contactTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
        contactTime.set(Calendar.MINUTE, minute);
        String timeStringFormat = TimeFormatUtils.converTimeStamp(contactTime.getTimeInMillis());

        mSelectTimeBtn.post(() -> mSelectTimeBtn.setText(timeStringFormat));

        selectedHour = hourOfDay;
        selectedMinute = minute;
      }


}
