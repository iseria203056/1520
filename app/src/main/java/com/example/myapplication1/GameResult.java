package com.example.myapplication1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class GameResult extends AppCompatActivity {
    FetchPageTask task = null;
    TextView tvOName,tvSName,tvshowGuessValue,tvResult;
    ImageView imr1,imr2,imS1,imS2;
    Button btnNext,btnQuit,btnContinue;
    LinearLayout l1,l2,llLoading;
    SQLiteDatabase db;
    boolean turn;
    Intent intentSV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_result);
        btnNext=findViewById(R.id.btnNext);
        btnQuit=findViewById(R.id.btnQuit);
        btnContinue=findViewById(R.id.btnContinue);
        tvOName=findViewById(R.id.tvOname);
        tvSName=findViewById(R.id.tvSelfName);
        tvResult=findViewById(R.id.tvResult);
        tvshowGuessValue=findViewById(R.id.tvshowGuessValue);
        llLoading=findViewById(R.id.llLoading);
        l1=findViewById(R.id.ll1);
        l2=findViewById(R.id.ll2);
        l1.setVisibility(View.GONE);//hide l1
        l2.setVisibility(View.GONE);//hide l2
        imr1=findViewById(R.id.imr1);
        imr2=findViewById(R.id.imr2);
        imS1=findViewById(R.id.imS1);
        imS2=findViewById(R.id.imS2);

        Intent intent=getIntent();
        String name=intent.getStringExtra("name");
        turn =intent.getBooleanExtra("turn",true);
        String id=intent.getStringExtra("ID");
        String hands=intent.getStringExtra("hands");
        int guessValue=intent.getIntExtra("guessValue",0);
        if(turn){
            tvshowGuessValue.setText("You guess"+guessValue);
        }
        if(hands.compareTo("00")==0){
            imS1.setImageResource(R.drawable.stone139);
            imS2.setImageResource(R.drawable.stone139);
        }else if(hands.compareTo("05")==0){
            imS1.setImageResource(R.drawable.stone139);
            imS2.setImageResource(R.drawable.paper139);
        }else if(hands.compareTo("50")==0){
            imS1.setImageResource(R.drawable.paper139);
            imS2.setImageResource(R.drawable.stone139);
        }
        else if(hands.compareTo("55")==0){
            imS1.setImageResource(R.drawable.paper139);
            imS2.setImageResource(R.drawable.paper139);
        }
        tvOName.setText("Opponent Name:"+name);
        SharedPreferences sharedPreferences = getSharedPreferences("pref", MODE_PRIVATE);
        tvSName.setText("Your Name:"+sharedPreferences.getString("Name","") );
        if (task == null || task.getStatus().equals(AsyncTask.Status.FINISHED)) {
            task = new FetchPageTask();
            task.execute("https://4qm49vppc3.execute-api.us-east-1.amazonaws.com/Prod/itp4501_api/opponent/"+id);


        }
    }
    private class FetchPageTask extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... values) {
            InputStream inputStream = null;
            String result = "";
            URL url = null;
            try {

                url = new URL(values[0]);
                HttpsURLConnection con = (HttpsURLConnection)
                        url.openConnection();        // Make GET request
                con.setRequestMethod("GET");
                con.connect();
                Log.d("doInBackground", "connect suss");
                inputStream = con.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = "";
                while ((line = bufferedReader.readLine()) != null)
                    result += line;
                inputStream.close();
            } catch (Exception e) {
                result = e.getMessage();
            }
            return result;
        }

        protected void onPostExecute(String result) {
            try {
                JSONObject jObj = new JSONObject(result);
                int left=jObj.getInt("left");
                int right=jObj.getInt("right");

                if(left==0){
                    imr2.setImageResource(R.drawable.stone139r);
                }else imr2.setImageResource(R.drawable.paper139r);

                if(right==0){
                      imr1.setImageResource(R.drawable.stone139r);
                }else imr1.setImageResource(R.drawable.paper139r);
                if(!turn){
                    tvshowGuessValue.setText(jObj.getString("name")+" guess "+jObj.getString("guess"));
                }
                showResult(left,right,jObj.getInt("guess"));

            } catch (Exception e) {
                String error = e.getMessage();
                Log.d("JSONObject", error);
            }

        }
    }
    public void showResult(int oLeft,int oRight,int oGuessvalue){
        Intent intent=getIntent();
        String hands=intent.getStringExtra("hands");
        int total=oLeft+oRight+Character.getNumericValue(hands.charAt(0))+Character.getNumericValue(hands.charAt(1));
        llLoading.setVisibility(View.GONE);//hide loading
        l1.setVisibility(View.VISIBLE);//show l1
        l2.setVisibility(View.VISIBLE);//show l2

        if(turn){
        if(total==intent.getIntExtra("guessValue",0)){
            tvResult.setText("You Win");
            btnQuit.setVisibility(View.VISIBLE);
            btnContinue.setVisibility(View.VISIBLE);
            writeRecord(intent.getStringExtra("name"),1);
        }else{
            tvResult.setText("you guess wrong");
            btnNext.setVisibility(View.VISIBLE);
        }
        }else{if(total==oGuessvalue){
            tvResult.setText("You LOSE");
            btnQuit.setVisibility(View.VISIBLE);
            btnContinue.setVisibility(View.VISIBLE);
            writeRecord(intent.getStringExtra("name"),0);
        }else{
            tvResult.setText("your opponent guess wrong");
            btnNext.setVisibility(View.VISIBLE);
        }
        }
    }
    public void turn(View v) {
        Intent temp=getIntent();
        String name=temp.getStringExtra("name");
        String id=temp.getStringExtra("ID");
        Intent intentNextTurn=new Intent(this,SelectHand.class);
        intentNextTurn.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
        intentNextTurn.putExtra("turn",!turn);
        intentNextTurn.putExtra("name",name);
        intentNextTurn.putExtra("ID",id);
        startActivity(intentNextTurn);
        finish();
    }//chang the guess turn
    public void quit(View v){
        finishActivity(4501);
        finish();
    }//quit the game
    public void Continue(View v){
        Intent intent=new Intent(this,FindPlayer.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
        startActivity(intent);
        finish();
    }//find next player

    public  void writeRecord(String name,int i){
        db = SQLiteDatabase.openDatabase("/data/data/com.example.myapplication1/MemberDB", null, SQLiteDatabase.OPEN_READWRITE);
        Cursor c=db.rawQuery("SELECT MAX(GameID) from gameRecord",null);//find newest id
        c.moveToFirst();
        int id=c.getInt(0)+1;
        name.replace("Opponent Name:","");//get the Opponent name
        db.execSQL("INSERT INTO gameRecord VALUES("+id+",datetime(),'"+name+"',"+i+");");
        db.close();
        c.close();
    }//update the game record
    protected void onPause(){
        super.onPause();
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);//check power of mon
        if(!pm.isScreenOn()){
            intentSV=new Intent(this,MyService.class);
            intentSV.putExtra("run",false);
            startService(intentSV);
        }
    }//+bgm event
    protected  void onUserLeaveHint(){
        Log.d("onUserLeaveHint", "onUserLeaveHint: onUserLeaveHint");
        intentSV=new Intent(this,MyService.class);
        intentSV.putExtra("run",false);
        startService(intentSV);
        super.onUserLeaveHint();
    }//+bgm event
    protected void onResume(){
         super.onResume();
        Log.d("state", "onResume: onResume");
        intentSV=new Intent(this,MyService.class);
        intentSV.putExtra("run",true);
        startService(intentSV);
    }//+bgm event
}
