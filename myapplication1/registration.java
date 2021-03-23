package com.example.myapplication1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class registration extends AppCompatActivity {
    EditText etName,etDOB,etEmail,etPhone;
    Button btnSubmit;
    SQLiteDatabase db;
    Intent intentSV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        btnSubmit=findViewById(R.id.btnRegister);
        etName=findViewById(R.id.etName);
        etDOB=findViewById(R.id.etDOB);
        etEmail=findViewById(R.id.etEmail);
        etPhone=findViewById(R.id.etPhone);
        

    }
    public void onclickSubmit(View v){
        if(TextUtils.isEmpty(etName.getText())){
            etName.setError(" All the information are required");
        }else {
        SharedPreferences pref=getSharedPreferences("pref", MODE_PRIVATE);
    pref.edit()
            .putString("Name",etName.getText().toString())
            .putString("DOB",etDOB.getText().toString())
            .putString("Email",etEmail.getText().toString())
            .putString("PhoneNo",etPhone.getText().toString())
            .commit();
            initDB();
finish();
        }
    }
    public  void finish(){
        setResult(4513);
        super.finish();
    }
    public void initDB(){
        try{
            // Create a database if it does not exist
            db = SQLiteDatabase.openDatabase("/data/data/com.example.myapplication1/MemberDB", null, SQLiteDatabase.CREATE_IF_NECESSARY);
            String sql = "CREATE TABLE gameRecord (GameID INTEGER PRIMARY KEY, dateAndTime datetime , opponent text, winOrLost bit);";
            db.execSQL(sql);
            db.execSQL("INSERT INTO gameRecord values"
                    + "(0, datetime() ,'may', 0); ");
            db.execSQL("INSERT INTO gameRecord values"
                    + "(1, datetime(), 'msy', 1); ");
            db.close();
        } catch (
                SQLiteException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
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
