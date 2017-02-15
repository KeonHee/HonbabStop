package com.landvibe.android.honbabstop.AddChat;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;

import com.landvibe.android.honbabstop.AddChat.presenter.AddChatPresenter;
import com.landvibe.android.honbabstop.AddChat.presenter.AddChatPresenterImpl;
import com.landvibe.android.honbabstop.R;
import com.landvibe.android.honbabstop.base.domain.ChatRoom;
import com.landvibe.android.honbabstop.base.domain.User;
import com.landvibe.android.honbabstop.base.domain.UserStore;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.shawnlin.numberpicker.NumberPicker;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddChatActivity extends AppCompatActivity implements AddChatPresenter.View, TimePickerDialog.OnTimeSetListener {

    @BindView(R.id.et_title)
    MaterialEditText mTitleEditText;

    @BindView(R.id.et_location)
    MaterialEditText mLocationEditText;

    @BindView(R.id.np_max_people)
    NumberPicker mMaxPeoplePicker;

    @BindView(R.id.btn_contact_time_label)
    Button mSelectTimeBtn;

    private AddChatPresenter.Presenter mAddChatPresenter;

    private int selectedHour=1;
    private int selectedMinute=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_chat);
        ButterKnife.bind(this);


        init();
    }

    private void init(){
        setActionBar();

        mAddChatPresenter = new AddChatPresenterImpl();
        mAddChatPresenter.attachView(this,this);

        mSelectTimeBtn.setOnClickListener(v-> showTimePicker());
    }

    private void setActionBar(){
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
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

        /* 만남 장소 */
        chatRoom.setLocationStr(mLocationEditText.getText().toString());
        //TODO 위치정보 추가, 네이버 maps 연동
        chatRoom.setLocationLat(0.0);
        chatRoom.setLocationLon(0.0);

        /* 음식 정보 */
        chatRoom.setFoodName("");
        //TODO 사진 업로드
        chatRoom.setFoodImageUrl("");
        chatRoom.setFoodRestaurant("");

        /* 방장 정보 */
        User user = UserStore.getUser();
        chatRoom.setHeader(user);

        /* 참여자 정보 (UID)*/
        List<String> members = Arrays.asList(user.getUid());
        chatRoom.setMembers(members);

        mAddChatPresenter.addChat(chatRoom);
    }

    @Override
    public void moveToMainActivity() {
        NavUtils.navigateUpFromSameTask(this);
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        selectedHour = hourOfDay;
        selectedMinute = minute;
      }
}
