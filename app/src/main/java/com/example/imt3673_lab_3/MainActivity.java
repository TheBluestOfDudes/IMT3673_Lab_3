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

    //UI elements
    private TextView mTxtScore;
    private Button mBtnSettings;

    //Sensors and SensorManager
    private SensorManager mSensorManager;
    private Sensor mAcc;
    private Sensor mMagnet;

    //Arrays for determining phone orientation
    private float[] mAcceleration;
    private float[] mGeomagnetic;
    //The acceleration of the phone
    private Float mLinearAcc;

    //Checks if a throw is currently happening
    private boolean mIsThrowing = false;
    //Checks if the device has the required sensors
    private boolean mHasSensors = true;

    //Set value for earth's gravity
    private static final Float EARTHGRAVITY = 9.81f;
    //Gravity value for throw event. Default = EARTHGRAVITY
    public static Float GRAVITY = EARTHGRAVITY;
    //The sensitivity threshold
    public static Integer TRESH = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Getting UI elements
        this.mTxtScore = findViewById(R.id.txt_score);
        this.mBtnSettings = findViewById(R.id.btn_settings);
        //Getting sensors
        this.mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        this.mAcc = this.mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.mMagnet = this.mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        //Checks that the needed sensors are there
        if((this.mAcc == null) || (this.mMagnet == null)){
            this.mTxtScore.setText("Sensors missing on device!");
            this.mHasSensors = false;
            this.mBtnSettings.setEnabled(false);
        }

        //Sets up settings button
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

        //Checks if the event is triggered by the accelerometer
        if (e.sensor.getType() == Sensor.TYPE_ACCELEROMETER){

            //Short name array so I wont need to type e.values
            float[] v = e.values;
            this.mAcceleration = v;

            //Calculate acceleration of the phone
            this.mLinearAcc = ((float )Math.sqrt((v[0]*v[0])+(v[1]*v[1])+(v[2]*v[2])))-this.EARTHGRAVITY;


        }

        //Checks if the even is triggered by the magnet sensor
        if (e.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){

            //Getting magnet values
            this.mGeomagnetic = e.values;
        }

        //Makes sure that data was acquired
        if(this.mAcceleration != null && this.mGeomagnetic != null){
            //Arrays for getting the phone's orientation
            float[] R = new float[9];
            float[] I = new float[9];

            //See if we could get a matrix
            boolean success = SensorManager.getRotationMatrix(R, I, mAcceleration, mGeomagnetic);
            if(success){

                //Gets phone orientation
                float[] o = new float[3];
                o = SensorManager.getOrientation(R, o);

                //Checks if phone is facing up (more or less)
                if((o[2] >= 0.0 && o[2] <= 1.0)&&(o[1] >= -1.0 && o[1] <= 1.0)){

                    //Checks if the acceleration reaches our threshold
                    if((this.mLinearAcc >= this.TRESH) && (this.mIsThrowing == false)){
                        //Sets throw event to true, and disables settings button.
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

        //Starts taking in sensor data
        if(this.mHasSensors){
            mSensorManager.registerListener(this, this.mAcc, SensorManager.SENSOR_DELAY_UI);
            mSensorManager.registerListener(this, this.mMagnet, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        //Turn Sensors off
        mSensorManager.unregisterListener(this);
    }

    public void throwEvent(Float v){

        //The current time during the throw
        Float time = 0.0f;
        //The time it will take to reach max height. Using the equation t=2*vy/g
        Float timeMax = (2*v)/this.GRAVITY;
        //Float value to increase time
        Float dT = 0.0001f;
        //Value for the velocity during the throw
        Float vel = v;
        //Value for the height during the throw
        Float height = 0.0f;
        //Sound effect
        MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.ding);
        while(time < timeMax){
            //Increment time and update velocity and height with each iteration
            time += dT;
            vel = (v-(this.GRAVITY*time));
            height = (vel*time)-((this.GRAVITY*time*time)/2);
            height *= -1f;
            this.mTxtScore.setText(height.toString()+ "m, was your max height");
        }

        //Play sound when done
        mp.start();
        //Set end throw event and re enable button
        this.mIsThrowing = false;
        this.mBtnSettings.setEnabled(true);

        /*
        *
        *   Could not figure out how to show the number rising while the throw happened.
        *   The loop finished too quickly. Unsure about height value given. Seems too high,
        *   if assuming the measurement is in meters.
        *
        * */
    }
}
