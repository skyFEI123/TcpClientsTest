package com.example.tcpclientstm32.util;

import android.content.Context;
import android.preference.PreferenceManager;

public class PersonInfo {
    private static final String TEMP = "temp";
    private static final String HEART = "heart";
    private static final String PRESS = "press";
    private static final String HEART_THRESHOLD = "heartThreshold";
    private static final String TEMP_THRESHOLD = "tempThreshold";
    private static final String PRESS_THRESHOLD = "pressThreshold";

    private static PersonInfo mInstance;
    protected final Context mContext;

    public static PersonInfo getInstance(Context mContext){
        if (mInstance == null) {
            mInstance = new PersonInfo(mContext);
        }
        return mInstance;
    }
    public PersonInfo(Context mContext) {
        this.mContext = mContext;
    }
    
    public final void setTemp(String temp) {
        PreferenceManager.getDefaultSharedPreferences(mContext)
                .edit()
                .putString(TEMP, temp)
                .apply();
    }
    public final void setHeart(String heart) {
        PreferenceManager.getDefaultSharedPreferences(mContext)
                .edit()
                .putString(HEART, heart)
                .apply();
    }

    public final void setPress(String press) {
        PreferenceManager.getDefaultSharedPreferences(mContext)
                .edit()
                .putString(PRESS, press)
                .apply();
    }

    public final void setHeartThreshold(String heartThreshold) {
        PreferenceManager.getDefaultSharedPreferences(mContext)
                .edit()
                .putString(HEART_THRESHOLD, heartThreshold)
                .apply();
    }
    public final void setTempThreshold(String tempThreshold) {
        PreferenceManager.getDefaultSharedPreferences(mContext)
                .edit()
                .putString(TEMP_THRESHOLD, tempThreshold)
                .apply();
    }
    public final void setPressThreshold(String pressThreshold) {
        PreferenceManager.getDefaultSharedPreferences(mContext)
                .edit()
                .putString(PRESS_THRESHOLD, pressThreshold)
                .apply();
    }

    public String getTemp(){
        return PreferenceManager.getDefaultSharedPreferences(mContext)
                .getString(TEMP, null);
    }
    public String getHeart(){
        return PreferenceManager.getDefaultSharedPreferences(mContext)
                .getString(HEART, null);
    }
    public String getPress(){
        return PreferenceManager.getDefaultSharedPreferences(mContext)
                .getString(PRESS, null);
    }
    public String getTempThreshold(){
        return PreferenceManager.getDefaultSharedPreferences(mContext)
                .getString(TEMP_THRESHOLD, null);
    }
    public String getPressThreshold(){
        return PreferenceManager.getDefaultSharedPreferences(mContext)
                .getString(PRESS_THRESHOLD, null);
    }
    public String getHeartThreshold(){
        return PreferenceManager.getDefaultSharedPreferences(mContext)
                .getString(HEART_THRESHOLD, null);
    }


}
