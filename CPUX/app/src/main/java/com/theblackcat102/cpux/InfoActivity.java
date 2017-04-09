package com.theblackcat102.cpux;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.ConsoleHandler;

public class InfoActivity extends AppCompatActivity {
    private List<String> memoryInfo;
    String [] info;
    ListView memoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setDefaultTab(R.id.tab_smartphone);
        memoryInfo = getMemoryInfo();
        if(memoryInfo.size() > 0){
            info = new String[memoryInfo.size()];
            for(int i=0;i<memoryInfo.size();i++){
                info[i] = memoryInfo.get(i);
            }
            memoryList = (ListView)findViewById(R.id.memoryListView);
            ArrayAdapter <String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,info);
            memoryList.setAdapter(adapter);
        }

        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch(tabId){
//                    case R.id.tab_smartphone:
//                        Log.d("sensor","stats");
//                        Intent stats = new Intent(getApplicationContext(),InfoActivity.class);
//                        startActivity(stats);
//                        break;
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
                    case R.id.tab_about:
                        Intent about = new Intent(getApplicationContext(),AboutActivity.class);
                        startActivity(about);
                        Log.d("sensor","about");
                        break;
                }
            }
        });
    }


    private List<String> getMemoryInfo() {
        List<String> result = new ArrayList<>();
        try {
            Process proc = Runtime.getRuntime().exec("cat /proc/meminfo");
            InputStream is = proc.getInputStream();
            result = getStringFromInputStream(is);
            //Log.d("memory",result);

        }
        catch (IOException e) {
            Log.e("TAG", "------ getMemoryInfo " + e.getMessage());
        }
        return result;
    }

    private List<String> getStringFromInputStream(InputStream is) {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line = null;
        List<String> memoryInfo = new ArrayList<String>();
        try {
            while((line = br.readLine()) != null) {
                memoryInfo.add(line);
                formatConverter(line);
                //sb.append(line); /* test code */
                //sb.append("\n");
            }
        }
        catch (IOException e) {
            Log.e("TAG", "------ getStringFromInputStream " + e.getMessage());
        }
        finally {
            if(br != null) {
                try {
                    br.close();
                }
                catch (IOException e) {
                    Log.e("TAG", "------ getStringFromInputStream " + e.getMessage());
                }
            }
        }

        return memoryInfo;
    }
    private String formatConverter(String line){
        String number = line.replace("^[0-9]","");
        Log.d("decoder",number);
        return number;
    }
}


