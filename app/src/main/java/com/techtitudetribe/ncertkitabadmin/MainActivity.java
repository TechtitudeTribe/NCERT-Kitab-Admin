package com.techtitudetribe.ncertkitabadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private LinearLayout one, two, three, four, five, six, seven, eight, nine, ten, eleven, twelve;
    private String board, title;
    private TextView mainScreenTitle;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        board = getIntent().getStringExtra("board");
        title = getIntent().getStringExtra("title");

        mainScreenTitle = (TextView) findViewById(R.id.main_screen_title);
        back = (ImageView) findViewById(R.id.main_screen_back);
        one = (LinearLayout) findViewById(R.id.standard_one);
        two = (LinearLayout) findViewById(R.id.standard_two);
        three = (LinearLayout) findViewById(R.id.standard_three);
        four = (LinearLayout) findViewById(R.id.standard_four);
        five = (LinearLayout) findViewById(R.id.standard_five);
        six = (LinearLayout) findViewById(R.id.standard_six);
        seven = (LinearLayout) findViewById(R.id.standard_seven);
        eight = (LinearLayout) findViewById(R.id.standard_eight);
        nine = (LinearLayout) findViewById(R.id.standard_nine);
        ten = (LinearLayout) findViewById(R.id.standard_ten);
        eleven = (LinearLayout) findViewById(R.id.standard_eleven);
        twelve = (LinearLayout) findViewById(R.id.standard_twelve);

        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,SubjectListActivity.class);
                intent.putExtra("board",board);
                intent.putExtra("standard","1");
                startActivity(intent);
            }
        });

        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,SubjectListActivity.class);
                intent.putExtra("board",board);
                intent.putExtra("standard","2");
                startActivity(intent);
            }
        });

        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,SubjectListActivity.class);
                intent.putExtra("board",board);
                intent.putExtra("standard","3");
                startActivity(intent);
            }
        });

        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,SubjectListActivity.class);
                intent.putExtra("board",board);
                intent.putExtra("standard","4");
                startActivity(intent);
            }
        });

        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,SubjectListActivity.class);
                intent.putExtra("board",board);
                intent.putExtra("standard","5");
                startActivity(intent);
            }
        });

        six.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,SubjectListActivity.class);
                intent.putExtra("board",board);
                intent.putExtra("standard","6");
                startActivity(intent);
            }
        });

        seven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,SubjectListActivity.class);
                intent.putExtra("board",board);
                intent.putExtra("standard","7");
                startActivity(intent);
            }
        });

        eight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,SubjectListActivity.class);
                intent.putExtra("board",board);
                intent.putExtra("standard","8");
                startActivity(intent);
            }
        });

        nine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,SubjectListActivity.class);
                intent.putExtra("board",board);
                intent.putExtra("standard","9");
                startActivity(intent);
            }
        });

        ten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,SubjectListActivity.class);
                intent.putExtra("board",board);
                intent.putExtra("standard","10");
                startActivity(intent);
            }
        });

        eleven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,SubjectListActivity.class);
                intent.putExtra("board",board);
                intent.putExtra("standard","11");
                startActivity(intent);
            }
        });

        twelve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,SubjectListActivity.class);
                intent.putExtra("board",board);
                intent.putExtra("standard","12");
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.super.onBackPressed();
            }
        });

        mainScreenTitle.setText(title);
    }
}