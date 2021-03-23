package com.example.myapplication1;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class ShowStat extends AppCompatActivity {
    Intent intentSV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new Panel(this));
    }

    public void onclick(View v){
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
    class Panel extends View {

        public Panel(Context context) {
            super(context);

        }

        public void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            SQLiteDatabase db = SQLiteDatabase.openDatabase("/data/data/com.example.myapplication1/MemberDB", null, SQLiteDatabase.OPEN_READONLY);

            Cursor c=db.rawQuery("SELECT COUNT(GameID) from gameRecord",null);//get nums of lost
            c.moveToFirst();
            Double gamesNum=c.getDouble(0);
            c=db.rawQuery("SELECT COUNT(winOrLost) from gameRecord Where winOrLost = '1' ",null);//get nums of win
            c.moveToFirst();
            Double winGamesNum=c.getDouble(0);
            c.close();
            db.close();
            Paint paint = new Paint();
            paint.setTextSize(100);//style of Title
            canvas.drawText("Game Stat", getWidth()/2-250, 130, paint);//Title

            int starty=getWidth()/2+800;//start point of bar || bottom of frame
            paint.setStyle(Paint.Style.STROKE);//style of frame
            canvas.drawRect(getWidth()/2-450, getWidth()/2-200, getWidth()/2+450, starty, paint);//draw frame


            paint.setColor(Color.BLUE);//style of win
            paint.setStyle(Paint.Style.FILL);
            paint.setTextSize(50);
            canvas.drawRect(getWidth()-250, starty+100, getWidth()-200, starty+150, paint);//tag of Win
            canvas.drawText(":Win",getWidth()-180,starty+143,paint);
            double stopy=(winGamesNum/gamesNum)*((starty)-(getWidth()/2-200));//length of win bar
            paint.setStrokeWidth(30);//style of bar
            canvas.drawLine(getWidth()/2-100,starty,getWidth()/2-100,  starty-(int)stopy,paint);//bar of Win

            paint.setColor(Color.BLACK);//style of scale
            paint.setStrokeWidth(1);
            canvas.drawLine(getWidth()/2-450,starty-(int)stopy,getWidth()/2+450,starty-(int)stopy,paint);//scale
            canvas.drawText((int)(winGamesNum.doubleValue())+"",getWidth()/2-500,starty-(int)stopy+15,paint);//scale's tag

            paint.setColor(Color.RED);//style of lose
            paint.setStrokeWidth(30);
            canvas.drawRect(getWidth()-250, starty+160, getWidth()-200, starty+210, paint);//tag of lost
            canvas.drawText(":Lost",getWidth()-180,starty+203,paint);
            stopy=((gamesNum-winGamesNum)/gamesNum)*((starty)-(getWidth()/2-200));//length of lost bar
            canvas.drawLine(getWidth()/2+100,starty,getWidth()/2+100,  starty-(int)stopy,paint);//bar of lost

            paint.setColor(Color.BLACK);//style of scale
            paint.setStrokeWidth(1);
            canvas.drawText("Number of game:"+(int)(gamesNum.doubleValue()),getWidth()-500,starty+265,paint);//tag of games num
            canvas.drawLine(getWidth()/2-450,starty-(int)stopy,getWidth()/2+450,starty-(int)stopy,paint);//scale
            canvas.drawText((int)(gamesNum-winGamesNum)+"",getWidth()/2-500,starty-(int)stopy+15,paint);//scale's tag
        }


    }
}


