package com.teamnpcomplete.ar_com;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.unity3d.player.UnityPlayerActivity;


public class MiddleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_middle);

        String assetPath = getIntent().getStringExtra("assetPath");
        String assetName = getIntent().getStringExtra("propertiesPath");


        Intent intent = new Intent(MiddleActivity.this, UnityPlayerActivity.class);

        intent.putExtra("arguments", true);
        intent.putExtra("host", "http://arcomm.herokuapp.com/");
        intent.putExtra("assetPath", "assets/"+assetPath);
        intent.putExtra("propertiesPath", "properties/"+assetName);
        //Toast.makeText(this, ""+ assetPath, Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, ""+ assetName, Toast.LENGTH_SHORT).show();
        startActivity(intent);

    }
}
