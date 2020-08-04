package com.harshit.pichub;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class Exit extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exit);
        finish();
    }
}
