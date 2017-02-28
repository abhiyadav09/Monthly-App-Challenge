package com.theblackcat102.wearablentu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;


public class Syncing extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syncing);
        new Thread()
        {
            public void run() {
                try {
                    Thread.sleep(2000);

                    Intent intent = new Intent(Syncing.this,Status2.class);
                    startActivity(intent);
                } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }.start();
    }

}
