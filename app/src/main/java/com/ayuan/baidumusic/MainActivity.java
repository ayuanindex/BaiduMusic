package com.ayuan.baidumusic;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static String TAG = "MainActivity";

    private IService iService;
    private MyConnection myConnection;
    private static SeekBar schedule;
    public static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            //歌曲总时长
            Object musicDuration = data.get("musicDuration");
            //当前已经播放时长
            Object currentTimeOfMusic = data.get("currentTimeOfMusic");
            if (schedule != null) {
                schedule.setMax((Integer) musicDuration);
                schedule.setProgress((Integer) currentTimeOfMusic);
                /*if (((Integer) musicDuration - (Integer) currentTimeOfMusic) <= 2) {
                    schedule.setProgress(0);
                }*/
            }
            Log.i(TAG, "总时长:" + musicDuration);
            Log.i(TAG, "当前时长:" + currentTimeOfMusic);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button player = (Button) findViewById(R.id.btn_player);
        Button stop = (Button) findViewById(R.id.btn_stop);
        Button continueplayin = (Button) findViewById(R.id.btn_continueplayin);
        schedule = (SeekBar) findViewById(R.id.sb_schedule);

        player.setOnClickListener(new MyPlayer());
        stop.setOnClickListener(new MyStop());
        continueplayin.setOnClickListener(new MyContinuePlaying());
        schedule.setOnSeekBarChangeListener(new MySeekTo());

        Intent intent = new Intent(this, MusicService.class);
        myConnection = new MyConnection();
        //混合方式开启服务
        //先调用startService   目的：保证服务能够再后台长期运行
        startService(intent);
        //调用bindService 目的：为了获取自己定义的IBinder的实现类
        boolean b = bindService(intent, myConnection, BIND_AUTO_CREATE);
        if (b) {
            Toast.makeText(this, "服务开启成功", Toast.LENGTH_SHORT).show();
        }
    }

    //点击按钮开始播放
    private class MyPlayer implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            iService.callPlayMusic();
        }
    }

    //点击按钮暂停播放
    private class MyStop implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            iService.callPauseMusic();
        }
    }

    //点击按钮继续播放
    private class MyContinuePlaying implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            iService.callRePlayMusic();
        }
    }

    //指定播放进度
    private class MySeekTo implements SeekBar.OnSeekBarChangeListener {
        //当进度改变的时候调用
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        }

        //当开始拖动的时候调用
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        //当拖动停止的时候调用
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            iService.callSeekTo(seekBar.getProgress());
        }
    }


    //监听服务的状态
    public class MyConnection implements ServiceConnection {
        //当服务连接成功是调用
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //获取定义的IBinder的实现类
            iService = (IService) service;
        }

        //当服务服务失去连接时调用
        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "正在解绑服务", Toast.LENGTH_SHORT).show();
        //当Activity销毁的时候解绑服务 目的：是为了不报红警告的日志
        if (myConnection != null) {
            unbindService(myConnection);
        }
    }

}
