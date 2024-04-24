package com.example.tcpclientstm32;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.tcpclientstm32.util.PersonInfo;

public class ResultShowActivity extends AppCompatActivity {

    private EditText etHeart, etPress, etTemp, etPersonResult;
    private Button back;
    private Context mContext = Myapplication.getContext();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_show);

        etHeart = (EditText) findViewById(R.id.editResultHeart);
        etPress = (EditText) findViewById(R.id.editResultPress);
        etTemp = (EditText) findViewById(R.id.editResultTemp);
        etPersonResult = (EditText) findViewById(R.id.editPresonResult);

        back = findViewById(R.id.ButtonBack);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Boolean judgeTemp = false;
        Boolean judgePress = false;
        Boolean judgeHeart = false;

        String getTemp = PersonInfo.getInstance(mContext).getTemp();
        String getHeart = PersonInfo.getInstance(mContext).getHeart();
        String getPress = PersonInfo.getInstance(mContext).getPress();
        String getTempThreshold = PersonInfo.getInstance(mContext).getTempThreshold();
        String getHeartThreshold = PersonInfo.getInstance(mContext).getHeartThreshold();
        String getPressThreshold = PersonInfo.getInstance(mContext).getPressThreshold();


        if (getTemp != null && getTempThreshold != null){
            judgeTemp = Float.valueOf(getTemp) > Float.valueOf(getTempThreshold) ? true : false;
            if (judgeTemp) {
                etTemp.setText("体温过高");
            }else{
                etTemp.setText("体温正常");
            }
        }

        if (getHeart != null && getHeartThreshold != null){
            judgeHeart = Float.valueOf(getHeart) > Float.valueOf(getHeartThreshold) ? true : false;
            if (judgeHeart) {
                etHeart.setText("心率过高");
            }else{
                etHeart.setText("心率正常");
            }
        }

        if (getPress != null && getPressThreshold != null){
            judgePress = Float.valueOf(getPress) > Float.valueOf(getPressThreshold) ? true : false;
            if (judgePress) {
                etPress.setText("压力过高");
            }else{
                etPress.setText("压力正常");
            }
        }

        if (!judgeHeart && !judgeTemp && !judgePress) {
            etPersonResult.setText("身体正常！");
        } else if ((!judgeHeart && judgePress && judgeTemp) || (judgeHeart && !judgePress && judgeTemp)
                || (judgeHeart && judgePress && !judgeTemp)) {
            etPersonResult.setText("身体较差！");
        } else if (judgeHeart && judgePress && judgePress) {
            etPersonResult.setText("需要就医！");
        } else {
            etPersonResult.setText("身体一般！");
        }

//        judgeHeartPerson();
//        judgeTempPerson();
//        judgePressPerson();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void judgeTempPerson() {
        Uri uriTemp = Uri.parse("content://com.example.tcpclientstm32.provider/string/person_info/temp");
        Cursor cursorTemp = getContentResolver().query(uriTemp, null, null, null, null);
        String tempValue = null;
        if (cursorTemp != null && cursorTemp.getCount() > 0) {
            cursorTemp.moveToFirst();
            tempValue = cursorTemp.getString(cursorTemp.getColumnIndex("temp"));
            cursorTemp.close();
        }

        Uri uriTempThreshold = Uri.parse("content://com.example.tcpclientstm32.provider/string/person_info/tempThreshold");
        Cursor cursorTempThreshold = getContentResolver().query(uriTempThreshold, null, null, null, null);
        String tempThresholdValue = null;
        if (cursorTempThreshold != null && cursorTempThreshold.getCount() > 0) {
            cursorTempThreshold.moveToFirst();
            tempThresholdValue = cursorTempThreshold.getString(cursorTempThreshold.getColumnIndex("tempThreshold"));
            cursorTempThreshold.close();
        }

        if (tempValue != null && tempThresholdValue != null){
            if (Integer.valueOf(tempValue) > Integer.valueOf(tempThresholdValue)){
                etTemp.setText("温度过高");
            } else {
                etTemp.setText("温度正常");
            }
        }
    }

    private void judgeHeartPerson() {
        Uri uriHeart = Uri.parse("content://com.example.tcpclientstm32.provider/string/person_info/heart");
        Cursor cursorHeart = getContentResolver().query(uriHeart, null, null, null, null);
        String heartValue = null;
        if (cursorHeart != null && cursorHeart.getCount() > 0) {
            cursorHeart.moveToFirst();
            heartValue = cursorHeart.getString(cursorHeart.getColumnIndex("heart"));
            cursorHeart.close();
        }

        Uri heartThreshold = Uri.parse("content://com.example.tcpclientstm32.provider/string/person_info/heartThreshold");
        Cursor cursorHeartThreshold = getContentResolver().query(heartThreshold, null, null, null, null);
        String heartThresholdValue = null;
        if (cursorHeartThreshold != null && cursorHeartThreshold.getCount() > 0) {
            cursorHeartThreshold.moveToFirst();
            heartThresholdValue = cursorHeartThreshold.getString(cursorHeartThreshold.getColumnIndex("heartThreshold"));
            cursorHeartThreshold.close();
        }

        if (heartValue != null && heartThresholdValue != null){
            if (Integer.valueOf(heartValue) > Integer.valueOf(heartThresholdValue)){
                etHeart.setText("心率过高");
            } else {
                etHeart.setText("心率正常");
            }
        }
    }

    private void judgePressPerson() {
        Uri uriPress = Uri.parse("content://com.example.tcpclientstm32.provider/string/person_info/press");
        Cursor cursorPress = getContentResolver().query(uriPress, null, null, null, null);
        String pressValue = null;
        if (cursorPress != null && cursorPress.getCount() > 0) {
            cursorPress.moveToFirst();
            pressValue = cursorPress.getString(cursorPress.getColumnIndex("press"));
            cursorPress.close();
        }

        Uri uriPressThreshold = Uri.parse("content://com.example.tcpclientstm32.provider/string/person_info/pressThreshold");
        Cursor cursorPressThreshold = getContentResolver().query(uriPressThreshold, null, null, null, null);
        String pressThresholdValue = null;
        if (cursorPressThreshold != null && cursorPressThreshold.getCount() > 0) {
            cursorPressThreshold.moveToFirst();
            pressThresholdValue = cursorPressThreshold.getString(cursorPressThreshold.getColumnIndex("pressThreshold"));
            cursorPressThreshold.close();
        }

        if (pressValue != null && pressThresholdValue != null){
            if (Integer.valueOf(pressValue) > Integer.valueOf(pressThresholdValue)){
                etHeart.setText("温度过高");
            } else {
                etHeart.setText("温度正常");
            }
        }
    }
}