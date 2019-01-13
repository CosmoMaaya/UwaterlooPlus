package com.uwaterlooplus.uwaterlooplus;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.os.Vibrator;
import android.widget.Toast;

import com.uwaterlooplus.uwaterlooplus.Fragment.NewAdventureFragment;

public class LockedActivity extends AppCompatActivity {


    private TextView mTvTimerText;
//    private Button mBtnStart;
    private CountDownTimer countDownTimer;
    private static long timeLeftinMilliconds; //10 mins
    private long mEndTime = System.currentTimeMillis() + timeLeftinMilliconds;
    private boolean timerRunning;
    private boolean leaved = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locked);

        mTvTimerText = findViewById(R.id.textView_countdown);
        timerRunning = true;
//        mBtnStart = findViewById(R.id.button_start);
//
//        mBtnStart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startStop();
//            }
//        });

        updateTimer();
        startTimer();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(countDownTimer != null){
            stopTimer();
        }

        SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putBoolean("leaved",leaved);

        editor.apply();
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);

        leaved = prefs.getBoolean("leaved",false);

        if(leaved){
            Toast.makeText(LockedActivity.this,"Sorry you failed",Toast.LENGTH_SHORT);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        vibrate();

    }

    //    public void startStop(){
//        if(timerRunning){
//            stopTimer();
//        }else {
//            startTimer();
//        }
//    }

    public void startTimer(){
        countDownTimer = new CountDownTimer(timeLeftinMilliconds,100) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftinMilliconds = millisUntilFinished;
                updateTimer();
            }

            @Override
            public void onFinish() {

            }
        }.start();
//        mBtnStart.setText("Pause");
//        timerRunning = true;
    }

    public void stopTimer(){
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

//        mBtnStart.setText("Start");
//
//        timerRunning = false;
    }

    public void updateTimer(){
        int minutes = (int) timeLeftinMilliconds / 60000;
        int seconds = (int) timeLeftinMilliconds % 60000 / 1000;

        if(minutes < 100) {
            String timeLeftFormatted = String.format("%02d:%02d", minutes, seconds);
            mTvTimerText.setText(timeLeftFormatted);
        }else {
            String timeLeftFormatted = String.format("%03d:%02d", minutes, seconds);
            mTvTimerText.setText(timeLeftFormatted);
        }

    }


    //To make sure every time is saved

//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putLong("millisLeft",timeLeftinMilliconds);
//        outState.putBoolean("timerRunning", timerRunning);
//        outState.putLong("endTime",mEndTime);
//        outState.putBoolean("leaved",leaved);
//    }
//
//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        if(savedInstanceState != null) {
//            timeLeftinMilliconds = savedInstanceState.getLong("millisLeft");
//            timerRunning = savedInstanceState.getBoolean("timerRunning");
//            leaved = true;
//            updateTimer();
//
//            if(leaved){
//                Toast.makeText(LockedActivity.this,"Sorry you failed",Toast.LENGTH_LONG);
//                onDestroy();
//            }
//
//            if (timerRunning) {
//                mEndTime = savedInstanceState.getLong("endTime");
//                timeLeftinMilliconds = mEndTime - System.currentTimeMillis();
//                startTimer();
//            }
//        }
//    }

    public static void setTimeLeftMilliconds (long inputMillisSeconds){
        timeLeftinMilliconds = inputMillisSeconds;
    }

    public void vibrate(){
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(500);
        }
    }

}
