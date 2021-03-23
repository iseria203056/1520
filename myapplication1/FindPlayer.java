package com.example.myapplication1;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class FindPlayer extends AppCompatActivity {
    FetchPageTask task = null;
    TextView tvName,tvCountry,tvID,tvStates;
    Button btnOK;
    Intent intentSV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_player);
        tvStates=findViewById(R.id.tvStates);
        tvName=findViewById(R.id.tvName);
        tvCountry=findViewById(R.id.tvCountry);
        tvID=findViewById(R.id.tvID);
        btnOK=findViewById(R.id.btnOK);
        tvName.setVisibility(View.GONE);
        tvCountry.setVisibility(View.GONE);
        tvID.setVisibility(View.GONE);
        btnOK.setVisibility(View.GONE);
        if (task == null || task.getStatus().equals(AsyncTask.Status.FINISHED)) {
            task = new FetchPageTask();
            task.execute("https://4qm49vppc3.execute-api.us-east-1.amazonaws.com/Prod/itp4501_api/opponent/0");


        }

    }
        public void onClickOK(View v){
            Intent goHands = new Intent(this, SelectHand.class);
            goHands.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
            goHands.putExtra("name",tvName.getText().toString().replace("Name:",""));
            goHands.putExtra("ID",tvID.getText().toString().replace("ID:",""));
            startActivity(goHands);
            finish();
        }

       private class FetchPageTask extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... values) {
            InputStream inputStream = null;
            String result = "";
            URL url = null;
            try {
                tvStates.setText("finding a player");
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
                Log.d("doInBackground", "get data complete");
                inputStream.close();
            } catch (Exception e) {
                result = e.getMessage();
            }
            return result;
        }

        protected void onPostExecute(String result) {
            try {

                Log.d("doInBackground", result);
                JSONObject jObj = new JSONObject(result);
                Log.d("JSONObject evelate", "id:"+jObj.getString("id")+"name:"+jObj.getString("name")+"country"+jObj.getString("country"));
                Log.d("JSONObject evelate", "id:"+jObj.getString("id")+"name:"+jObj.getString("name")+"country"+jObj.getString("country"));
                tvStates.setText("Player has been found");
                tvCountry.setText(tvCountry.getText()+jObj.getString("country"));
                tvID.setText(tvID.getText()+jObj.getString("id"));
                tvName.setText(tvName.getText()+jObj.getString("name"));
                tvName.setVisibility(View.VISIBLE);
                tvCountry.setVisibility(View.VISIBLE);
                tvID.setVisibility(View.VISIBLE);
                btnOK.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                String error = e.getMessage();
                tvStates.setText("server error,try it later");
                Log.d("JSONObject evelate", "id:"+error);
            }

        }
    }
    protected void onPause(){
        super.onPause();
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);//check power of mon
        if(!pm.isScreenOn()){
            Log.d("screen", "onPause: screen off");
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