package com.example.tomato_robot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import java.util.ArrayList;

public class MainScreen extends AppCompatActivity {

//    private ClientSocketThread clientSocketThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        Button btn_harvest_history = findViewById(R.id.btn_harvest_history);
        Button btn_intake_history = findViewById(R.id.btn_intake_history);
        Button btn_robot_setting = findViewById(R.id.btn_robot_setting);
        Button btn_close = findViewById(R.id.btn_close);
        Button btn_complete = findViewById(R.id.btn_complete);
        VideoView video_view = findViewById(R.id.video_view);

        View act_state_rect = findViewById(R.id.act_state_rect);
        ImageView act_state_img = findViewById(R.id.act_state_img);
        TextView act_state_text = findViewById(R.id.act_state_text);

        AppVariable app_variable = (AppVariable) getApplicationContext();


//        app_variable.initSocketThread("10.0.2.2",12345); // 로컬주소
        app_variable.initSocketThread("192.168.0.205", 12345); // 젯슨 공유기 ip

        if(app_variable.act_state.equals("operating")){
            act_state_rect.setBackground(getResources().getDrawable(R.drawable.act_enabled_background));
            act_state_img.setImageDrawable(getResources().getDrawable(R.drawable.act_icon));
            act_state_text.setText(app_variable.act_state);
        }

        act_state_rect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (app_variable.act_state.equals("waiting")){
                    app_variable.act_state = ("operating");
                    act_state_rect.setBackground(getResources().getDrawable(R.drawable.act_enabled_background));
                    act_state_img.setImageDrawable(getResources().getDrawable(R.drawable.act_icon));
                    act_state_text.setText(app_variable.act_state);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Uri uri = Uri.parse("android.resource://com.example.tomato_robot/" +
                                            R.raw.startharvest);

                                    video_view.setVideoURI(uri);
                                    video_view.start();
                                }
                            });
                            app_variable.setNew_message("operating");
                        }
                    }
                    ).start();

                }
                else if (app_variable.act_state.equals("operating")){
                    app_variable.act_state = ("waiting");
                    act_state_rect.setBackground(getResources().getDrawable(R.drawable.act_disabled_background));
                    act_state_img.setImageDrawable(getResources().getDrawable(R.drawable.sleep_icon));
                    act_state_text.setText(app_variable.act_state);
                    app_variable.setNew_message("waiting");
                }
            }
        });

        btn_harvest_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainScreen.this,HarvestHistory.class);
                startActivity(intent);
            }
        });

        btn_intake_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainScreen.this,IntakeHistory.class);
                startActivity(intent);
            }
        });

        btn_robot_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainScreen.this,RobotSetting.class);
                startActivity(intent);
            }
        });

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                app_variable.setNew_message("close");
            }
        });

        btn_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (app_variable.act_state.equals("operating")){
                    app_variable.act_state = ("waiting");
                    act_state_rect.setBackground(getResources().getDrawable(R.drawable.act_disabled_background));
                    act_state_img.setImageDrawable(getResources().getDrawable(R.drawable.sleep_icon));
                    act_state_text.setText(app_variable.act_state);
                    app_variable.setNew_message("waiting");

                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Uri uri = Uri.parse("android.resource://com.example.tomato_robot/" +
                                            R.raw.stopharvest);

                                    video_view.setVideoURI(uri);
                                    video_view.start();
                                }
                            });
                            app_variable.uno_start = "false";
                            app_variable.setNew_message("complete");
                        }
                    }
                    ).start();
                }
            }
        });
    }



}