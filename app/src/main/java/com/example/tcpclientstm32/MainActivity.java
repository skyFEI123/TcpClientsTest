package com.example.tcpclientstm32;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.view.View.OnClickListener;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MyApplication";
    Button ConnectButton; // 定义连接按钮

    boolean Connected = false;
    Socket socket = null; // 定义socket
    private OutputStream outputStream = null; // 定义输出流（发送）
    private InputStream inputStream = null; // 定义输入流（接收）

    Connect_Thread connect_Thread = null;
    Receive_Thread receive_Thread = null;

    ExecutorService SendService = null;
    private BufferedReader in = null;
    private PrintWriter out = null;
    private String msg;

    //UI控件绑定变量
    TextView textViewTemperature, textViewHeart, textViewPress, textViewTitle, textViewResult, textViewTmpResult, textViewHeartResult, textViewPressResult;
    EditText editTextHeart, editTextTemp, editTextPress, editTextResult;
    Button buttonHeart, buttonTemp, buttonPress, buttonResult, buttonBack;
    String heartThreshold;
    String tempThreshold;
    String pressThreshold;

    final String[] tempTemp = new String[1];
    final String[] pressTemp = new String[1];
    final String[] heartTemp = new String[1];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConnectButton = (Button) findViewById(R.id.buttonConnect); // 获得按钮对象

        textViewTemperature = (TextView) findViewById(R.id.textViewTemperature);
        textViewHeart = (TextView) findViewById(R.id.textViewHeart);
        textViewPress = (TextView) findViewById(R.id.textViewPress);
        textViewTitle = (TextView) findViewById(R.id.textView2);
        textViewResult = (TextView) findViewById(R.id.textView7);
        textViewTmpResult = (TextView) findViewById(R.id.textView3);
        textViewHeartResult = (TextView) findViewById(R.id.textView5);
        textViewPressResult = (TextView) findViewById(R.id.textView6);

        editTextHeart = (EditText) findViewById(R.id.editTextHeart);
        editTextTemp = (EditText) findViewById(R.id.editTextTemp);
        editTextPress = (EditText) findViewById(R.id.editTextPress);
        editTextResult = (EditText) findViewById(R.id.editTextResult);

        buttonHeart = (Button) findViewById(R.id.buttonHeart);
        buttonTemp = (Button) findViewById(R.id.buttonTemp);
        buttonPress = (Button) findViewById(R.id.buttonPress);
        buttonResult = (Button) findViewById(R.id.buttonShow);
        buttonBack = (Button) findViewById(R.id.buttonBack);

        buttonHeart.setEnabled(false);
        buttonTemp.setEnabled(false);
        buttonPress.setEnabled(false);
        editTextHeart.setEnabled(false);
        editTextTemp.setEnabled(false);
        editTextPress.setEnabled(false);

        SendService = Executors.newSingleThreadExecutor(); //创建单线程池，用于发送数据

        /* 点击连接按钮 */
        ConnectButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (Connected == false) // 标志位 = false表示未连接
                {
                    connect_Thread = new Connect_Thread();
                    connect_Thread.start();
                } else {
                    ConnectButton.setText("连接"); // 按钮上显示连接
                    Connected = false; // 置为true
                    buttonHeart.setEnabled(false);
                    buttonTemp.setEnabled(false);
                    buttonPress.setEnabled(false);
                    editTextHeart.setEnabled(false);
                    editTextTemp.setEnabled(false);
                    editTextPress.setEnabled(false);

                    try {
                        socket.close(); // 关闭连接
                        socket = null;
                        buttonResult.setVisibility(View.VISIBLE);
                        Toast.makeText(MainActivity.this, "STM32断开", Toast.LENGTH_SHORT).show();

                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        });

        // 结果界面设置
        buttonResult.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectButton.setVisibility(View.GONE);
                textViewTemperature.setVisibility(View.GONE);
                textViewHeart.setVisibility(View.GONE);
                textViewPress.setVisibility(View.GONE);
                editTextResult.setVisibility(View.VISIBLE);
                textViewResult.setVisibility(View.VISIBLE);

                buttonHeart.setVisibility(View.GONE);
                buttonTemp.setVisibility(View.GONE);
                buttonPress.setVisibility(View.GONE);
                buttonResult.setVisibility(View.GONE);
                buttonBack.setVisibility(View.VISIBLE);

                textViewTmpResult.setText("温度结果");
                textViewHeartResult.setText("心率结果");
                textViewPressResult.setText("血压结果");

                boolean judgeTemp = Integer.valueOf(tempTemp[0]) > Integer.valueOf(tempThreshold) ? true : false;
                boolean judgeHeart = Integer.valueOf(heartTemp[0]) > Integer.valueOf(heartThreshold) ? true : false;
                boolean judgePress = Integer.valueOf(pressTemp[0]) > Integer.valueOf(pressThreshold) ? true : false;

                if (judgeTemp) {
                    textViewTemperature.setText("温度过高");
                } else {
                    textViewTemperature.setText("温度正常");
                }

                if (judgeHeart) {
                    textViewHeart.setText("心率过高");
                } else {
                    textViewHeart.setText("心率正常");
                }

                if (judgePress) {
                    textViewPress.setText("血压过高");
                } else {
                    textViewPress.setText("血压正常");
                }

                if (judgeHeart && judgePress && judgeTemp) {
                    editTextResult.setText("身体优秀！");
                }else if (!judgeHeart && !judgePress && !judgeTemp){
                    editTextResult.setText("身体较差！");
                }else {
                    editTextResult.setText("身体良好！");
                }
            }
        });

        // 返回测量界面设置
        buttonBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectButton.setVisibility(View.VISIBLE);
                textViewTemperature.setVisibility(View.VISIBLE);
                textViewHeart.setVisibility(View.VISIBLE);
                textViewPress.setVisibility(View.VISIBLE);
                editTextResult.setVisibility(View.GONE);
                textViewResult.setVisibility(View.GONE);

                buttonHeart.setVisibility(View.VISIBLE);
                buttonTemp.setVisibility(View.VISIBLE);
                buttonPress.setVisibility(View.VISIBLE);
                buttonResult.setVisibility(View.VISIBLE);
                buttonBack.setVisibility(View.GONE);

                textViewTmpResult.setText("温度阈值");
                textViewHeartResult.setText("心率阈值");
                textViewPressResult.setText("血压阈值");
            }
        });
        //心率阈值设置
        buttonHeart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editTextHeart.getText().toString().isEmpty()) {
                    try {
                        if (null == socket) {
                        }
                        heartThreshold = editTextHeart.getText().toString();
                        msg = "[H" + heartThreshold + "]";
                        Log.d(TAG, "Heart" + msg);//定义发送数据
                        if (socket.isConnected()) {
                            if (out != null) {
                                SendService.execute(sendService);       //开启发送数据线程
                            }
                        }
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, "数据发送失败", Toast.LENGTH_SHORT).show();
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        });


        //温度阈值设置
        buttonTemp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editTextTemp.getText().toString().isEmpty()) {
                    try {
                        if (null == socket) {
                        }
                        tempThreshold = editTextHeart.getText().toString();
                        msg = "[T" + tempThreshold + "]";
                        Log.d(TAG, "Temp" + msg);
                        //定义发送数据
                        if (socket.isConnected()) {
                            if (out != null) {
                                SendService.execute(sendService);       //开启发送数据线程
                            }
                        }
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, "数据发送失败", Toast.LENGTH_SHORT).show();
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        });


        //血压阈值设置
        buttonPress.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editTextPress.getText().toString().isEmpty()) {
                    try {
                        if (null == socket) {
                        }
                        pressThreshold = editTextHeart.getText().toString();
                        msg = "[P" + pressThreshold + "]";
                        Log.d(TAG, "press" + msg);
                        //定义发送数据
                        if (socket.isConnected()) {
                            if (out != null) {
                                SendService.execute(sendService);       //开启发送数据线程
                            }
                        }
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, "数据发送失败", Toast.LENGTH_SHORT).show();
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    //定义发送消息接口
    Runnable sendService = new Runnable() {
        @Override
        public void run() {
            out.println(msg);   //通过输出流发送数据
        }
    };

    // 继承Thread
    class Connect_Thread extends Thread {
        // 重写run方法
        public void run() {
            try {
                // 如果已经连接上了，就不再执行连接程序
                if (socket == null) {
                    // 用InetAddress方法获取ip地址
//                    InetAddress ipAddress = InetAddress.getByName(IPEditText.getText().toString());
//                    int port = Integer.valueOf(PortText.getText().toString()); // 获取端口号
                    //System.out.println(111);
                    //socket = new Socket(ipAddress, port);

                    socket = new Socket("192.168.6.6", 6666);
                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

                    Connected = true; // 置为true

                    runOnUiThread(new Runnable()// 不允许其他线程直接操作组件，用提供的此方法可以
                    {
                        public void run() {
                            ConnectButton.setText("断开"); // 按钮上显示--断开

                            buttonResult.setVisibility(View.VISIBLE);

                            buttonHeart.setEnabled(true);
                            buttonTemp.setEnabled(true);
                            buttonPress.setEnabled(true);
                            editTextHeart.setEnabled(true);
                            editTextTemp.setEnabled(true);
                            editTextPress.setEnabled(true);

                            // 吐司一下，告诉用户发送成功
                            Toast.makeText(MainActivity.this, "STM32连接成功", Toast.LENGTH_SHORT).show();
                        }
                    });
                    // 在创建完连接后启动接收线程
                    receive_Thread = new Receive_Thread();
                    receive_Thread.start();
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    // 接收线程
    class Receive_Thread extends Thread {
        public void run()// 重写run方法
        {
            if (null != socket) {// 不会奔溃
                try {
                    while (true) {
                        final byte[] buffer = new byte[1024];// 创建接收缓冲区
                        inputStream = socket.getInputStream();
                        final int len = inputStream.read(buffer);// 数据读出来，并且返回数据的长度
                        if (len == -1) {
                            socket.close(); // 关闭连接
                            socket = null;
                            Connected = false;
                            runOnUiThread(new Runnable()// 不允许其他线程直接操作组件，用提供的此方法可以
                            {
                                public void run() {
                                    ConnectButton.setText("连接"); // 按钮上显示--断开
                                }
                            });
                            return;
                        }
                        runOnUiThread(new Runnable()// 不允许其他线程直接操作组件，用提供的此方法可以
                        {
                            public void run() {
                                // TODO Auto-generated method stub
                                //RrceiveEditText.setText(new String(buffer, 0, len));

                                byte result[] = new byte[1024];
                                int start = 0;

                                for (int i = 0; i < buffer.length; i++) {
                                    if (start == 0 && buffer[i] == '[') {
                                        start++;
                                    } else if (start != 0 && buffer[i] != ']') {
                                        result[start - 1] = buffer[i];
                                        start++;
                                    } else if (start != 0 && buffer[i] == ']') {
                                        start = 0;
                                        String transferResult = null;
                                        switch (result[0]) {
                                            case 'T'://温度
                                                try {
                                                    transferResult = new String(result, "utf-8");
                                                } catch (UnsupportedEncodingException e) {
                                                    e.printStackTrace();
                                                }
                                                tempTemp[0] = transferResult.substring(1, 6);
                                                textViewTemperature.setText("温度: " + tempTemp[0] + "℃");
                                                break;

                                            case 'H'://心率
                                                try {
                                                    transferResult = new String(result, "utf-8");
                                                } catch (UnsupportedEncodingException e) {
                                                    e.printStackTrace();
                                                }
                                                heartTemp[0] = transferResult.substring(1, 6);
                                                textViewHeart.setText("心率: " + heartTemp[0] + "次/min");
                                                break;


                                            case 'P'://血压
                                                try {
                                                    transferResult = new String(result, "utf-8");
                                                } catch (UnsupportedEncodingException e) {
                                                    e.printStackTrace();
                                                }
                                                pressTemp[0] = transferResult.substring(1, 6);
                                                // 换算人体血压值需要*14
                                                textViewPress.setText("血压: " + pressTemp[0] + "Kpa");
//                                                textViewPress.setText("血压: " + transferResult.substring(1, 6) + "Kpa");
                                                break;

                                            default:
                                                break;
                                        }
                                        java.util.Arrays.fill(result, (byte) 0);//buffer数组清零
                                    }
                                }
                            }
                        });
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
//
//                if ()
            }
        }
    }
}
