package com.example.imt3673_lab_3;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;



public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private TextView mTxtScore;
    private Button mBtnSettings;

    private SensorManager mSensorManager;
    private Sensor mAcc;
    private Sensor mMagnet;

    private float[] mAcceleration;
    private Float mLinearAcc;
    private float[] mGeomagnetic;

    private boolean mIsThrowing = false;
    private boolean mHasSensors = true;

    private static Float EARTHGRAVITY = 9.81f;
    public static Float GRAVITY = EARTHGRAVITY;
    public static Float THRESH = 8.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.mTxtScore = findViewById(R.id.txt_score);
        this.mBtnSettings = findViewById(R.id.btn_settings);
        this.mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        this.mAcc = this.mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.mMagnet = this.mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        if((this.mAcc == null) || (this.mMagnet == null)){
            this.mTxtScore.setText("Sensors missing on device!");
            this.mHasSensors = false;
            this.mBtnSettings.setEnabled(false);
        }

        this.mBtnSettings.setOnClickListener(v->{
            Intent i = new Intent(MainActivity.this, Settings.class);
            startActivity(i);
        });
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int acc){
        //Do stuff with accuracy
    }

    @Override
    public final void onSensorChanged(SensorEvent e){
        //Deal with changed data
        if (e.sensor.getType() == Sensor.TYPE_ACCELEROMETER){

            float[] v = e.values;
            this.mAcceleration = v;
            this.mLinearAcc = ((float )Math.sqrt((v[0]*v[0])+(v[1]*v[1])+(v[2]*v[2])))-this.EARTHGRAVITY;


        }
        if (e.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
            this.mGeomagnetic = e.values;
        }
        if(this.mAcceleration != null && this.mGeomagnetic != null){
            float[] R = new float[9];
            float[] I = new float[9];
            boolean success = SensorManager.getRotationMatrix(R, I, mAcceleration, mGeomagnetic);
            if(success){
                float[] o = new float[3];
                o = SensorManager.getOrientation(R, o);
                //Log.d("Vals", "X: " + this.mAcceleration[0] + ", Y: " + this.mAcceleration[1] + ", Z: " + this.mAcceleration[2]);
                //Checks if phone is facing up
                if((o[2] >= 0.0 && o[2] <= 1.0)&&(o[1] >= -1.0 && o[1] <= 1.0)){
                    if((this.mLinearAcc > this.THRESH) && (this.mIsThrowing == false)){
                        this.mIsThrowing = true;
                        this.mBtnSettings.setEnabled(false);
                        throwEvent(this.mLinearAcc);
                    }
                }
            }
        }

    }

    @Override
    protected void onResume(){
        super.onResume();
        if(this.mHasSensors){
            mSensorManager.registerListener(this, this.mAcc, SensorManager.SENSOR_DELAY_UI);
            mSensorManager.registerListener(this, this.mMagnet, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    public void throwEvent(Float v){
        Float time = 0.0f;
        Float timeMax = (2*v)/this.GRAVITY;     //t = 2*vy/g
        Float dT = 0.0001f;
        Float vel = v;
        Float height = 0.0f;
        MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.ding);
        while(time < timeMax){
            time += dT;
            vel = (v-(this.GRAVITY*time));
            height = (vel*time)-((this.GRAVITY*time*time)/2);
            height *= -1f;
            this.mTxtScore.setText(height.toString()+"M, was your max height");
        }
        mp.start();
        this.mIsThrowing = false;
        this.mBtnSettings.setEnabled(true);
    }
}
