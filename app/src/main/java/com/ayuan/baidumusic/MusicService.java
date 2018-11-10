package com.ayuan.baidumusic;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MusicService extends Service {

    private String TAG = "MusicService";
    private MediaPlayer mediaPlayer;

    @Override
    public IBinder onBind(Intent intent) {
        return new MyBind();
    }

    //服务已开启就执行这个方法
    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();
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
        if (mediaPlayer != null) {
            try {
                mediaPlayer.reset();
                mediaPlayer.setDataSource("/mnt/sdcard/Music/Jake Miller - Parties.mp3");//播放本地音乐
                mediaPlayer.prepare();
                /*mediaPlayer.setDataSource("http://172.50.223.22:8080/a.mp3");//播放网络音乐
                mediaPlayer.prepareAsync();*/
                mediaPlayer.start();
                //更新SeekBar
                updateSeekBa();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Toast.makeText(this, "正在播放", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateSeekBa() {
        //获取音乐的总时长 和当前已经播放时长
        if (mediaPlayer != null) {
            //总时长
            final int musicDuration = mediaPlayer.getDuration();
            //使用Timer定时器来定期取得当前播放时长------时间间隔1s
            final Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    int currentTimeOfMusic = mediaPlayer.getCurrentPosition();
                    Message message = Message.obtain();
                    Bundle bundle = new Bundle();
                    bundle.putInt("musicDuration", musicDuration);
                    bundle.putInt("currentTimeOfMusic", currentTimeOfMusic);
                    message.setData(bundle);
                    MainActivity.handler.sendMessage(message);
                    Log.i(TAG, "time:" + currentTimeOfMusic);
                }
            }, 1, 1000);
            //当歌曲执行完毕后把Timer和TimerTask取消
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    Log.i(TAG, "歌曲播放完成了");
                    timer.cancel();
                }
            });
        }
    }

    //暂停音乐的方法
    public void pauseMusic() {
        mediaPlayer.pause();
        Toast.makeText(this, "暂停播放", Toast.LENGTH_SHORT).show();
    }

    //继续播放
    public void rePlayMusic() {
        mediaPlayer.start();
        Toast.makeText(this, "继续播放", Toast.LENGTH_SHORT).show();
    }

    //实现指定播放的位置
    public void seekTo(int position) {
        mediaPlayer.seekTo(position);
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

        @Override
        public void callSeekTo(int position) {
            seekTo(position);
        }


    }

}
