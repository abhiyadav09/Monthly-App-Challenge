package com.theblackcat102.wearablentu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.app.PendingIntent;
import android.app.NotificationManager;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;
import android.media.RingtoneManager;
import android.media.Ringtone;
import android.net.Uri;
import android.widget.ListView;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.support.v7.widget.Toolbar;
import java.util.Locale;

public class Status extends AppCompatActivity {
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        mDrawerList = (ListView)findViewById(R.id.navList);

    }
    private void addDrawerItems() {
        String[] osArray = { "Android", "iOS", "Windows", "OS X", "Linux" };
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);
    }
    public void soundAlarm(View view){
        Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        if(alert == null){
            alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            if(alert == null) {
                // alert backup is null, using 2nd backup
                alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            }
        }
        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), alert);
        r.play();
    }
    public void closeProgram(View view){

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.network).setContentTitle("App 運行中...").setContentText("正在檢測你的心理狀態");

        Intent resultIntent = new Intent(this,Status.class);


        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        int mNotificationId = 001;
// Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
// Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
        /* start my service here:
        Intent intent = new Intent(this,BackgroundService.class);
        startService(intent);
        */
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
        Toast.makeText(getApplicationContext(), "App 已經開始運行", Toast.LENGTH_LONG).show();

    }


}
