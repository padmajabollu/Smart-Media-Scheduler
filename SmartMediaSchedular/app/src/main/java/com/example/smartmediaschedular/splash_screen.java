package com.example.smartmediaschedular;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class splash_screen extends AppCompatActivity {

    Animation topAnim,bottomAnim;
    TextView title;
    ImageView logo;
    private static int SPLASH_SCREEN=5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash_screen);

        topAnim= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.top_animation);
        bottomAnim=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.bottom_animation);

        logo=(ImageView) findViewById(R.id.splash_img);
        title=(TextView) findViewById(R.id.splash_cont);

        logo.setAnimation(topAnim);
        title.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
              //  intent.putExtra("status","");
                startActivity(intent);
                finish();
            }
        },SPLASH_SCREEN);

    }
}
