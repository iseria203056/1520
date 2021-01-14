package com.example.myapplication1;

import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

public class SelectHand extends AppCompatActivity {
        Button btn00,btn05,btn50,btn55;
    Intent intentSV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_hand);
        btn00=findViewById(R.id.btn00);
        btn05=findViewById(R.id.btn05);
        btn50=findViewById(R.id.btn50);
        btn55=findViewById(R.id.btn55);
    }
    public void selected(View v){

        Intent Info=getIntent();
        Intent goGuess;
        boolean turn=Info.getBooleanExtra("turn",true);

        if(turn){
         goGuess = new Intent(this, Guess.class);}
        else{
          goGuess = new Intent(this, GameResult.class);
        }
        goGuess.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
        goGuess.putExtra("turn",turn);
        goGuess.putExtra("ID", Info.getStringExtra("ID"));
        goGuess.putExtra("name", Info.getStringExtra("name"));
        if(v.getId()==R.id.btn00)goGuess.putExtra("hands","00");
        else if(v.getId()==R.id.btn05)goGuess.putExtra("hands","05");
        else if(v.getId()==R.id.btn50)goGuess.putExtra("hands","50");
        else if(v.getId()==R.id.btn55)goGuess.putExtra("hands","55");

        startActivity(goGuess);
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
    protected void onStart(){ super.onStart();
        Log.d("state", "onResume: onResume");
        intentSV=new Intent(this,MyService.class);
        intentSV.putExtra("run",true);
        startService(intentSV);
    }
}
