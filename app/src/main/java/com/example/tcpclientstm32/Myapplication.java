package com.example.tcpclientstm32;

import android.app.Application;
import android.content.Context;

public class Myapplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext(){
        return context;
    }
}
