package com.ayuan.baidumusic;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.widget.Toast;

public class MusicService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBind();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    //播放音乐的方法
    public void playeMusic() {
        //TODO 等多媒体学完就开始完善这个播放音乐的功能
        Toast.makeText(this, "正在播放", Toast.LENGTH_SHORT).show();
    }

    //暂停音乐的方法
    public void pauseMusic() {
        Toast.makeText(this, "暂停播放", Toast.LENGTH_SHORT).show();
    }

    //继续播放
    public void rePlayMusic() {
        Toast.makeText(this, "继续播放", Toast.LENGTH_SHORT).show();
    }

    //再服务内部定义一个IBinder的实现类(Binder是IBinder的直接实现子类）
    private class MyBind extends Binder implements IService {

        //调用播放音乐的方法
        @Override
        public void callPlayMusic() {
            playeMusic();
        }

        //调用暂停音乐的方法
        @Override
        public void callPauseMusic() {
            pauseMusic();
        }

        //调用继续播放的方法
        @Override
        public void callRePlayMusic() {
            rePlayMusic();
        }
    }

}
