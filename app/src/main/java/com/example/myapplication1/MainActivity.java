package com.example.myapplication1;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;


public class MainActivity extends AppCompatActivity {
    boolean stopBGM=false;
    ImageView bgm;
    Button btnEditPersonalInfo,btnRecord,btnStart,btnRegister;
    SQLiteDatabase db;
    Intent intentSV;
    File SharedPreferences = new File(
            "/data/data/com.example.myapplication1/shared_prefs/pref.xml");//is registered?

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnRecord=(findViewById(R.id.btnRecord));
        btnEditPersonalInfo=findViewById(R.id.btnEditPersonalInfo);
        btnStart=findViewById(R.id.btnStart);
        btnRegister=findViewById(R.id.btnRegister);
        bgm=findViewById(R.id.imageView);
        if(!SharedPreferences.exists()) {//show button
            btnStart.setVisibility(View.GONE);
            btnEditPersonalInfo.setVisibility(View.GONE);
            btnRecord.setVisibility(View.GONE);
        }else{
            btnRegister.setVisibility(View.GONE);
        }

        intentSV=new Intent(this,MyService.class);//bgm Service
        intentSV.putExtra("run",true);
       startService(intentSV);
    }

    protected void onPause(){
        super.onPause();
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);//check power of mon
        if(!pm.isScreenOn()){
            intentSV=new Intent(this,MyService.class);
            intentSV.putExtra("run",false);
            startService(intentSV);
        }
    }
    protected  void onUserLeaveHint(){
        Log.d("onUserLeaveHint", "onUserLeaveHint: onUserLeaveHint");
        intentSV=new Intent(this,MyService.class);
        intentSV.putExtra("run",false);
        startService(intentSV);
        super.onUserLeaveHint();
    }
     protected void onResume(){ super.onResume();
        Log.d("state", "onResume: onResume");
        intentSV=new Intent(this,MyService.class);
        intentSV.putExtra("run",true);
        startService(intentSV);
    }
    protected void onDestroy(){
        super.onDestroy();
        intentSV=new Intent(this,MyService.class);
        stopService(intentSV);
    }


    public void click(View v){
        //moveTaskToBack(true);
        Intent intent;

        if(v.getId()==R.id.btnRegister){
             intent = new Intent(this, registration.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
            startActivityForResult(intent,4513);
        }else if(v.getId()==R.id.btnEditPersonalInfo){
             intent = new Intent(this, UpdateInfo.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
            startActivityForResult(intent,4503);
        }else if(v.getId()==R.id.btndelete){
            SharedPreferences.delete();
            deleteDB();
            this.recreate();
            startActivity(getIntent());
        }else if(v.getId()==R.id.btnStart){
             intent = new Intent(this, FindPlayer.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
            startActivityForResult(intent,4501);
        }else  if (v.getId()==R.id.btnRecord){
               intent = new Intent(this, ShowRecords.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
             startActivityForResult(intent,4502);
        }else if(v.getId()==R.id.imageView){
            Intent intentSV=new Intent(this,MyService.class);
            if(stopBGM){
            bgm.setImageResource(R.drawable.ic_volume_up_black_24dp);
                intentSV.putExtra("BGM",true);
                intentSV.putExtra("stopBGM",false);
        }else{
            bgm.setImageResource(R.drawable.ic_volume_off_black_24dp);
            intentSV.putExtra("BGM",true);
            intentSV.putExtra("stopBGM",true);
        }
            startService(intentSV);
            stopBGM=!stopBGM;
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode==4513){//Reload UI
            this.recreate();
            startActivity(getIntent());
        }
    }
    public void deleteDB(){
        db = SQLiteDatabase.openDatabase("/data/data/com.example.myapplication1/MemberDB", null, SQLiteDatabase.CREATE_IF_NECESSARY);
        String sql = "DROP TABLE IF EXISTS gameRecord;";
        db.execSQL(sql);
        db.close();

    }


}



