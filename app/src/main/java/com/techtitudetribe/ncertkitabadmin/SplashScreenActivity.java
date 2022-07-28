package com.techtitudetribe.ncertkitabadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class SplashScreenActivity extends AppCompatActivity {

    private LinearLayout englishMedium, hindiMedium;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        englishMedium = (LinearLayout) findViewById(R.id.splash_screen_english);
        hindiMedium = (LinearLayout) findViewById(R.id.splash_screen_hindi);




        englishMedium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SplashScreenActivity.this,MainActivity.class);
                intent.putExtra("board","English");
                intent.putExtra("title","NCERT Books\n(English Medium)");
                startActivity(intent);
            }
        });

        hindiMedium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SplashScreenActivity.this,MainActivity.class);
                intent.putExtra("board","Hindi");
                intent.putExtra("title","NCERT पुस्तकें\n(हिंदी माध्यम)");
                startActivity(intent);
            }
        });


    }
}