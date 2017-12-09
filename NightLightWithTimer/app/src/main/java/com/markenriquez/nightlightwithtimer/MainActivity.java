package com.markenriquez.nightlightwithtimer;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.IOException;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.nfc.NdefRecord.createUri;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.viewPager)
    protected ViewPager viewPager;
    @BindView(R.id.count)
    protected TextView mCount;
    @BindView(R.id.btnPlayMusic)
    protected ImageButton btnPlayMusic;
    @BindView(R.id.btnLock)
    protected ImageButton btnLockScreen;
    @BindView(R.id.btnChangeBgColor)
    protected ImageButton btnChangeBgColor;
    @BindView(R.id.btnAdjustBrightness)
    protected ImageButton btnAdjustBrightness;
    @BindView(R.id.btnDisableBg)
    protected ImageButton btnDisableBg;
    @BindView(R.id.seekBar)
    protected SeekBar seekBar;
    @BindView(R.id.btnOK)
    protected Button btnOk;
    @BindView(R.id.llTopButtons)
    protected LinearLayout llTopButtons;


    private int sec = 0;
    private String selectedTime;
    private SlideAdapter myAdapter;
    private CountDownTimer timer1, timer2, timer3;
    public MediaPlayer mediaPlayer1, mediaPlayer2, mediaPlayer3;
    public Long s1;
    private Uri uri;
    private static String TAG = "MAIN";
    private boolean isScreenLocked;
    private boolean isTimer1Running, isTimer2Running, isTimer3Running;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        isScreenLocked = false;
        myAdapter = new SlideAdapter(this);
        viewPager.setAdapter(myAdapter);
        mediaPlayer1 = MediaPlayer.create(this, R.raw.playsong);
        mediaPlayer2 = MediaPlayer.create(this, R.raw.playsong);
        mediaPlayer3 = MediaPlayer.create(this, R.raw.playsong);
        final VideoView mfireworkVideo = (VideoView) findViewById(R.id.fireworkVideo);

        uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.vidvid);


        mfireworkVideo.setVideoURI(uri);
        mfireworkVideo.start();

        mfireworkVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
    }

    // HIDE ALL BUTTONS
    public void lockScreen(View view) {
        isScreenLocked = !isScreenLocked;
        btnLockScreen.setImageDrawable(getResources().getDrawable(isScreenLocked
                ? R.drawable.star : R.drawable.lock));
        llTopButtons.setVisibility(isScreenLocked ? View.GONE : View.VISIBLE);
        Toast.makeText(this, isScreenLocked ? "Screen locked" : "Screen unlocked",
                Toast.LENGTH_SHORT).show();
    }




    public void playMusic(View view) {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        final String items[] = new String[]{"1min", "5min", "10min"};
        adb.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface d, int n) {
                selectedTime = Arrays.asList(items).get(n);
            }
        });
        adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (selectedTime == "1min") {
                  //  if (isTimer2Running) mediaPlayer2.stop();
                    if (isTimer2Running) timer2.cancel();
                    if (isTimer3Running) timer3.cancel();

                    timer1 =  new CountDownTimer(10000, 1000) {

                        public void onTick(long millisUntilFinished) {
                            mCount.setText("seconds remaining: " + millisUntilFinished / 1000);
                            isTimer1Running = true;
                            mediaPlayer1.start();
                        }

                        public void onFinish() {
                            mCount.setText("done!");
                            isTimer1Running = false;
                            mediaPlayer1.stop();
                            mediaPlayer1.release();
                        }
                    }.start();

                } else if (selectedTime == "5min") {
                   // if (isTimer2Running) mediaPlayer1.stop();
                    if (isTimer1Running) timer1.cancel();
                    if (isTimer3Running) timer3.cancel();

                    timer2 =   new CountDownTimer(50000, 1000) {

                        public void onTick(long millisUntilFinished) {
                            mCount.setText("seconds remaining: " + millisUntilFinished / 1000);
                            isTimer2Running = true;
                            mediaPlayer2.start();

                        }

                        public void onFinish() {
                            mCount.setText("done!");
                            isTimer2Running = false;
                            mediaPlayer2.stop();
                            mediaPlayer2.release();
                        }
                    }.start();

                } else if (selectedTime == "10min") {
                    if (isTimer1Running) timer1.cancel();
                    if (isTimer2Running) timer2.cancel();

                    timer3 =  new CountDownTimer(100000, 1000) {

                        public void onTick(long millisUntilFinished) {
                            mCount.setText("seconds remaining: " + millisUntilFinished / 1000);
                            isTimer3Running = true;
                            mediaPlayer3.start();
                        }

                        public void onFinish() {
                            mCount.setText("done!");
                            isTimer3Running = false;
                            mediaPlayer3.stop();
                            mediaPlayer3.release();
                        }
                    }.start();

                } else {

                }
            }
        }).setTitle("Select time");
        adb.show();
    }











    public void changeBackgroundColor(View view) {

    }

    public void adjustBrightness(View view) {
        settingPermission();
        visibleViews(true);
        int brightness = getScreenBrightness();
        seekBar.setProgress(brightness);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean fromUser) {
                setScreenBrightness(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void closeBrightnessAdjustment(View view) {
        visibleViews(false);
    }

    private int getScreenBrightness() {
        int brightnessValue = Settings.System.getInt(
                this.getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS,
                0
        );
        return brightnessValue;
    }

    private void setScreenBrightness(int brightnessValue) {
        if(brightnessValue >= 0 && brightnessValue <= 255){
            Settings.System.putInt(
                    this.getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS, brightnessValue
            );
        }
    }

    private void visibleViews(boolean isVisible) {
        seekBar.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        btnOk.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        viewPager.setVisibility(isVisible ? View.GONE : View.VISIBLE);
    }

    public void settingPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(getApplicationContext())) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 200);
            }
        }
    }

    public void disableBackground(View view) {

    }

//    public class MyViewPager extends ViewPager {
//        private boolean isPagingEnabled = true;
//
//        public MyViewPager(Context context) {
//            super(context);
//        }
//
//        public MyViewPager(Context context, AttributeSet attrs) {
//            super(context, attrs);
//        }
//
//        public void setPagingEnabled(boolean isEnabled) {
//            this.isPagingEnabled = isPagingEnabled;
//        }
//
//        @Override
//        public boolean onTouchEvent(MotionEvent ev) {
//            return isPagingEnabled && super.onTouchEvent(ev);
//        }
//
//        @Override
//        public boolean onInterceptTouchEvent(MotionEvent ev) {
//            return isPagingEnabled && super.onInterceptTouchEvent(ev);
//        }
//    }

}
