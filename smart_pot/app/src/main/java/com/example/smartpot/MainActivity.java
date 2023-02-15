package com.example.smartpot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        TextView textViewToAnimate = findViewById(R.id.clickToLaunch);
        final Runnable r = () -> YoYo.with(Techniques.Flash).duration(3000).repeat(2000).playOn(textViewToAnimate);

        new Handler().postDelayed(r, 1000);
    }

    public void launchHome( View view ) {
        Log.d("IMPORTANT_LOGGING", "CLICKED TO LAUNCH HOME :D");

        Intent intent = new Intent(this, HomePage.class);
        final Runnable r = () -> startActivity(intent);

        new Handler().postDelayed(r, 10);
    }
}