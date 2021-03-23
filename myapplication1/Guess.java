package com.example.myapplication1;

import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;

public class Guess extends AppCompatActivity {
    RadioButton rb0,rb5,rb10,rb15,rb20;
    ImageView im1,im2;
    String value="";
    Intent intentSV;
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess);
        Intent intent=getIntent();
        im1=findViewById(R.id.im1);
        im2=findViewById(R.id.im2);
        rb0=findViewById(R.id.rb0);
        rb5=findViewById(R.id.rb5);
        rb10=findViewById(R.id.rb10);
        rb15=findViewById(R.id.rb15);
        rb20=findViewById(R.id.rb20);
        value=intent.getStringExtra("hands");
        if(value.compareTo("00")==0){
        im1.setImageResource(R.drawable.stone86);
        im2.setImageResource(R.drawable.stone86);
        }else if(value.compareTo("05")==0){
            im1.setImageResource(R.drawable.stone86);
            im2.setImageResource(R.drawable.paper86);
        }else if(value.compareTo("50")==0){
            im1.setImageResource(R.drawable.paper86);
            im2.setImageResource(R.drawable.stone86);
        }
        else if(value.compareTo("55")==0){
            im1.setImageResource(R.drawable.paper86);
            im2.setImageResource(R.drawable.paper86);
        }

    }
    public void onClickGuess(View v){
        Intent intent=getIntent();
        Intent goResult=new Intent(this,GameResult.class);
        goResult.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
        goResult.putExtra("ID",intent.getStringExtra("ID"));
        goResult.putExtra("name",intent.getStringExtra("name"));
        goResult.putExtra("hands",intent.getStringExtra("hands"));
        if(rb0.isChecked()){
            goResult.putExtra("guessValue",0);
            Log.d("guessValue", "onCreate: 0");
        }else if(rb5.isChecked()){
            goResult.putExtra("guessValue",5);
            Log.d("guessValue", "onCreate: 5");
         }else if(rb10.isChecked()){
            goResult.putExtra("guessValue",10);
            Log.d("guessValue", "onCreate: 10");
        }else if(rb15.isChecked()){
            goResult.putExtra("guessValue",15);
            Log.d("guessValue", "onCreate: 15");
        }else if(rb20.isChecked()){
            goResult.putExtra("guessValue",20);
            Log.d("guessValue", "onCreate: 20");
        }

        startActivity(goResult);
        finish();
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
}
