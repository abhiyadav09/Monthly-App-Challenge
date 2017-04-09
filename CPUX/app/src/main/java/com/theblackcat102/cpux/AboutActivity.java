package com.theblackcat102.cpux;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setDefaultTab(R.id.tab_about);

        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch(tabId){
                    case R.id.tab_smartphone:
                        Log.d("sensor","stats");
                        Intent stats = new Intent(getApplicationContext(),InfoActivity.class);
                        startActivity(stats);
                        break;
                    case R.id.tab_cpu:
                        Log.d("sensor","cpu");
                        Intent cpu_page = new Intent(getApplicationContext(), CPUActivity.class);
                        startActivity(cpu_page);
                        break;
                    case R.id.tab_sensor:
                        Intent sensor = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(sensor);
                        Log.d("sensor","sensor");
                        break;
//                    case R.id.tab_about:
//                        Intent about = new Intent(getApplicationContext(),AboutActivity.class);
//                        startActivity(about);
//                        Log.d("sensor","about");
//                        break;
                }
            }
        });
    }
}
