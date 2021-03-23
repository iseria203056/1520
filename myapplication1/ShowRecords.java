package com.example.myapplication1;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ShowRecords extends AppCompatActivity {
    ListView lwRecord;
    SQLiteDatabase db;
     Cursor cursor = null;
     TextView tv;
    Intent intentSV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_records);
        lwRecord=findViewById(R.id.lvRecord);
        tv=findViewById(R.id.tvSR_Title);
        db = SQLiteDatabase.openDatabase("/data/data/com.example.myapplication1/MemberDB", null, SQLiteDatabase.OPEN_READONLY);
        cursor=db.rawQuery("select * from gameRecord Order by gameID Desc",null);
        if (cursor.getCount()>0){


            cursor.moveToFirst();
            ArrayList<String> myList = new ArrayList<String>();
            do{
                String str="";
                str+="Game ID:"+cursor.getString(0)+"\n";
                str+="Game Time:"+cursor.getString(1)+"\n";
                str+="Opponent Name:"+cursor.getString(2)+"\n";
                if(cursor.getInt(3)==1){
                str+="Result: Win";
                }else{
                    str+="Result: Lose";}
                myList.add(str);

            } while(cursor.moveToNext());
            ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,myList);
            lwRecord.setAdapter(adapter);

        }else{

        }

        db.close();
    }
    public void onclick(View v){
        if(v.getId()==R.id.btnMainmeun){
            finish();
        }else if(v.getId()==R.id.btnShowStat){
            Intent goStat=new Intent(this,ShowStat.class);
            goStat.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
            startActivity(goStat);
        }

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
    public void onUserInteraction(){
        Log.d("onUserInteraction", "onUserInteraction: ");
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
