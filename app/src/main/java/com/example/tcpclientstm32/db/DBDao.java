package com.example.tcpclientstm32.db;

import android.content.Context;

import com.example.tcpclientstm32.Myapplication;

public class DBDao {
    // 表名
    public static String TABLE_NAME = "person";

    private static final String ID = "id";
    private static final String Temp = "person_temp";
    private static final String Press = "person_press";
    private static final String Heart = "person_heart";

    // 创建数据库对象
    private DBHelper dbHelper;

    public static final String SQL_CREATE_TABLE = "create table " + TABLE_NAME +
            "(" + ID + " integer primary key autoincrement," +
            Temp + " text," +
            Press + " text," +
            Heart + " text" +
            ")";

    private DBDao() {
        dbHelper = new DBHelper(Myapplication.getContext());
    }
    
}
