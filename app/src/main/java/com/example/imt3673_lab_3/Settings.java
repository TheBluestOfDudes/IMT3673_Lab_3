package com.example.imt3673_lab_3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class Settings extends AppCompatActivity {

    //UI elements
    private TextView mTxtGravity;
    private TextView mTxtTreshold;      //Could not get a seekbar to function, so i used a plain text view
    private Button mBtnFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //Getting UI elements
        this.mTxtGravity = findViewById(R.id.txt_gravity);
        this.mTxtTreshold = findViewById(R.id.txt_treshold);
        this.mBtnFinish = findViewById(R.id.btn_finish);

        //Innitialize gravitiy and threshold to
        this.mTxtGravity.setText(MainActivity.GRAVITY.toString());
        this.mTxtTreshold.setText(MainActivity.TRESH.toString());

        //Registers changes to the text views
        registerChange();
        //Registers button click
        registerButton();
    }

    private void registerChange(){
        this.mTxtGravity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //Ensures that whenever the text view is changed, it is both a number and not blank
                if(mTxtGravity.getText().toString().matches("")){
                    mBtnFinish.setEnabled(false);
                }
                else if (!(mTxtGravity.getText().toString().matches("\\d+(?:\\.\\d+)?"))){
                    mBtnFinish.setEnabled(false);
                }
                else{
                    mBtnFinish.setEnabled(true);
                }
            }
        });
        this.mTxtTreshold.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //Ensures that whenever the text view is changed, it is both a number and not blank
                if(mTxtTreshold.getText().toString().matches("")){
                    mBtnFinish.setEnabled(false);
                }
                else if (!(mTxtTreshold.getText().toString().matches("\\d+(?:\\.\\d+)?"))){
                    mBtnFinish.setEnabled(false);
                }
                else{
                    mBtnFinish.setEnabled(true);
                }
            }
        });
    }

    private void registerButton(){
        this.mBtnFinish.setOnClickListener(v->{
            //Updates gravity and treshold in mainactivity
            MainActivity.GRAVITY = Float.parseFloat(this.mTxtGravity.getText().toString());
            MainActivity.TRESH = (Integer.parseInt(this.mTxtTreshold.getText().toString()));
            finish();
        });
    }
}
