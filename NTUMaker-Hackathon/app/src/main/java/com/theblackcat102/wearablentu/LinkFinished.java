package com.theblackcat102.wearablentu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;

public class LinkFinished extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_finished);
    }

    public void goSync(View view){
        Intent intent = new Intent(this,Syncing.class);
        startActivity(intent);
    }
}
