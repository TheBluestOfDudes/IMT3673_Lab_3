package com.example.imt3673_lab_3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

public class Settings extends AppCompatActivity {

    private TextView mTxtGravity;
    private TextView mTxtThreshold;
    private Button mBtnFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        this.mTxtGravity = findViewById(R.id.txt_gravity);
        this.mTxtThreshold = findViewById(R.id.txt_threshold);
        this.mBtnFinish = findViewById(R.id.btn_finish);

        this.mTxtGravity.setText(MainActivity.GRAVITY.toString());
        this.mTxtThreshold.setText(MainActivity.THRESH.toString());
        registerChange();
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
        this.mTxtThreshold.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(mTxtThreshold.getText().toString().matches("")){
                    mBtnFinish.setEnabled(false);
                }
                else if (!(mTxtThreshold.getText().toString().matches("\\d+(?:\\.\\d+)?"))){
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
            MainActivity.GRAVITY = Float.parseFloat(this.mTxtGravity.getText().toString());
            MainActivity.THRESH = (Float.parseFloat(this.mTxtThreshold.getText().toString()));
            finish();
        });
    }
}
