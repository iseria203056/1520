package com.example.myapplication1;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class MyService extends Service {
    MediaPlayer mPlayer;
    boolean stopBgm;
    public MyService() {
    }
    public void onCreate(){
        super.onCreate();
        try{
            mPlayer= new MediaPlayer();//create player
            mPlayer=MediaPlayer.create(MyService.this,R.raw.flymetothestar);//identify what song
            mPlayer.setLooping(true);//loop the player
            stopBgm=false;
        }catch(IllegalStateException e){
            e.printStackTrace();
        }
    }
    @Override
    public IBinder onBind(Intent intent){
        return null;
    }//not use

    public int onStartCommand(Intent intent,int flags,int startId){
        if(intent.getBooleanExtra("BGM",false))//check is that related to stop bgm
        {
            stopBgm=intent.getBooleanExtra("stopBGM",false);
        }


        if(stopBgm){
            if(mPlayer.isPlaying()){mPlayer.pause();}//stop to play bgm when playing
            return super.onStartCommand(intent, flags, startId);
        }else{
        if(intent.getBooleanExtra("run",true)&&!mPlayer.isPlaying()){
            mPlayer.start();
        }else if(!intent.getBooleanExtra("run",true)&&mPlayer.isPlaying())
            mPlayer.pause();
        mPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            public boolean onError(MediaPlayer mp, int what, int extra) {//check error
                try {
                    mPlayer.release();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
        return super.onStartCommand(intent, flags, startId);}
        }


    @Override
    public void onDestroy(){
        mPlayer.release();
        super.onDestroy();
    }
}
