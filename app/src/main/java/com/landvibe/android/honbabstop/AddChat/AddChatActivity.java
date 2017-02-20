package com.landvibe.android.honbabstop.AddChat;


import android.content.Intent;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.landvibe.android.honbabstop.AddChat.adapter.SearchAdapter;
import com.landvibe.android.honbabstop.AddChat.presenter.AddChatPresenter;
import com.landvibe.android.honbabstop.AddChat.presenter.AddChatPresenterImpl;
import com.landvibe.android.honbabstop.ChatDetail.ChatDetailActivity;
import com.landvibe.android.honbabstop.R;
import com.landvibe.android.honbabstop.base.domain.ChatRoom;
import com.landvibe.android.honbabstop.base.domain.FoodRestaurant;
import com.landvibe.android.honbabstop.base.domain.User;
import com.landvibe.android.honbabstop.base.domain.UserStore;
import com.landvibe.android.honbabstop.base.listener.OnShowMarkerListener;
import com.landvibe.android.honbabstop.base.utils.TimeFormatUtils;
import com.landvibe.android.honbabstop.nmaps.NMapFragment;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
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
        implements AddChatPresenter.View, TimePickerDialog.OnTimeSetListener,
        MaterialSearchView.OnQueryTextListener, AdapterView.OnItemClickListener,
        View.OnTouchListener{

    private final static String TAG = "AddChatActivity";

    @BindView(R.id.et_title)
    MaterialEditText mTitleEditText;

    @BindView(R.id.et_food_name)
    MaterialEditText mFoodNameEditText;

    @BindView(R.id.np_max_people)
    NumberPicker mMaxPeoplePicker;

    @BindView(R.id.btn_contact_time_label)
    FButton mSelectTimeBtn;

    @BindView(R.id.search_view)
    MaterialSearchView mSearchView;

    @BindView(R.id.activity_add_chat)
    LinearLayout mActivityContainer;

    @BindView(R.id.maps_fragment_space)
    LinearLayout mMapContainer;

    private AddChatPresenter.Presenter mAddChatPresenter;

    private NMapFragment mMapFragment;

    private SearchAdapter mSearchAdapter;

    private int selectedHour=1;
    private int selectedMinute=0;

    private FoodRestaurant mSelectedRestaurant;

    private OnShowMarkerListener mMarkerListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_chat);
        ButterKnife.bind(this);


        init();
    }

    private void init(){
        setActionBar();
        initMapFragment();

        mAddChatPresenter = new AddChatPresenterImpl();
        mAddChatPresenter.attachView(this,this);

        mSelectTimeBtn.setOnClickListener(v-> showTimePicker());

        mSearchView.post(()->mSearchView.showSearch());

        mSearchView.setVoiceSearch(false);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setOnItemClickListener(this);
        mSearchView.getRootView().setOnTouchListener(this);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAddChatPresenter.detachView();
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
        //TODO 사진 업로드
        chatRoom.setFoodImageUrl("");
        chatRoom.setFoodName(mFoodNameEditText.getText().toString());

        chatRoom.setFoodTitle(mSelectedRestaurant.getTitle().replace("<b>","").replace("</b>",""));
        chatRoom.setFoodCategory(mSelectedRestaurant.getCategory());
        chatRoom.setFoodDescription(mSelectedRestaurant.getDescription());
        chatRoom.setFoodTelephone(mSelectedRestaurant.getTelephone());
        chatRoom.setAddress(mSelectedRestaurant.getAddress());
        chatRoom.setRoadAddress(mSelectedRestaurant.getRoadAddress());
        chatRoom.setLocationX(mSelectedRestaurant.getMapx());
        chatRoom.setLocationY(mSelectedRestaurant.getMapy());

        /* 방장 정보 */
        User user = UserStore.getInstance().getUser();
        chatRoom.setHeader(user);

        /* 참여자 정보 (UID)*/
        List<String> members = Arrays.asList(user.getUid());
        chatRoom.setMembers(members);

        chatRoom.setStatus(ChatRoom.STATUS_REMAIN);

        mAddChatPresenter.addChat(chatRoom);
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
    }

    @Override
    public void showSuggestions(String[] suggestions) {
        for(String s : suggestions){
            Log.d(TAG, "suggestions : " + s);
        }
        mSearchView.post(()->{
            mSearchAdapter = new SearchAdapter(this, suggestions);
            mSearchView.setAdapter(mSearchAdapter);
        });
    }

    @Override
    public void showMapMarker(FoodRestaurant foodRestaurant) {
        if(foodRestaurant!=null){
            mSelectedRestaurant=foodRestaurant;
            Log.d(TAG, foodRestaurant.getTitle());
            Log.d(TAG, foodRestaurant.getMapx() + " : " + foodRestaurant.getMapy());

            if(mMarkerListener!=null){
                mMarkerListener.onMarkPin(foodRestaurant);
            }
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


    /* MaterialSearchView.OnQueryTextListener */

    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.d(TAG, "query : "+query);
        mAddChatPresenter.searchLocation(query);
        //TODO 키보드 숨기기
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        //Log.d(TAG, "newText : " + newText);
        return false;
    }

    /* AdapterView.OnItemClickListener */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mSearchView.dismissSuggestions();

        runOnUiThread(() -> {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.weight = 0;
            mSearchView.setLayoutParams(params);

            if(mMapContainer.getVisibility()==View.GONE){
                mMapContainer.setVisibility(View.VISIBLE);
            }
        });

        String selectedName = (String) mSearchAdapter.getItem(position);
        Log.d(TAG,"position : "+position);
        Log.d(TAG,"selectedName : "+selectedName);
        mAddChatPresenter.loadPOImark(position);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_DOWN){
            Log.d(TAG, v.getClass().toString());
            if(mSearchView.getClass()==v.getClass()){
                mSearchView.post(()->{
                    ViewGroup.LayoutParams params = mSearchView.getLayoutParams();
                    params.height = 200;
                    mSearchView.setLayoutParams(params);

                    if(mMapContainer.getVisibility()==View.VISIBLE){
                        mMapContainer.setVisibility(View.GONE);
                    }
                });
            }
        }
        return true;
    }
}
