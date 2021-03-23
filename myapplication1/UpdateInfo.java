package com.example.myapplication1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class UpdateInfo extends AppCompatActivity {
    EditText etName,etDOB,etEmail,etPhone;
    Button btnUpdate;
    Intent intentSV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_info);
        SharedPreferences sharedPreferences = getSharedPreferences("pref", MODE_PRIVATE);
        btnUpdate=findViewById(R.id.btnRegister);
        etName=findViewById(R.id.etName);
        etDOB=findViewById(R.id.etDOB);
        etEmail=findViewById(R.id.etEmail);
        etPhone=findViewById(R.id.etPhone);
        etName.setText(sharedPreferences.getString("Name",""));
        etDOB.setText(sharedPreferences.getString("DOB",""));
        etEmail.setText(sharedPreferences.getString("Email",""));
        etPhone.setText(sharedPreferences.getString("PhoneNo",""));
    }
    public void onclickUpdate(View v){
        SharedPreferences pref=getSharedPreferences("pref", MODE_PRIVATE);
        pref.edit()
                .putString("Name",etName.getText().toString())
                .putString("DOB",etDOB.getText().toString())
                .putString("Email",etEmail.getText().toString())
                .putString("PhoneNo",etPhone.getText().toString())
                .apply();
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
