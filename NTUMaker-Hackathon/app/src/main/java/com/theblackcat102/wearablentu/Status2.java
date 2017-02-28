package com.theblackcat102.wearablentu;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class Status2 extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    boolean start;
    BluetoothAdapter mBtAdapter;
    // Bluetooth
    private ListView list;
    private ArrayAdapter<String> adapter;
    Set<BluetoothDevice> deviceSet;
    private ArrayList<String> arrayList;
    String fiilliste[];
    InputStream mmInStream;
    OutputStream mmOutStrem;
    private final static int REQUEST_ENABLE_BT = 1;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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

        Intent intent = new Intent(this,BackgroundService.class);
        startService(intent);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.network).setContentTitle("App 運行中...").setContentText("正在檢測你的心理狀態");

        Intent resultIntent = new Intent(this,Status2.class);


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
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.status2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_camera) {
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
        } else if (id == R.id.nav_gallery) {
            try{
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:0903182184"));
                startActivity(callIntent);
            }
            catch(SecurityException e){
                e.printStackTrace();
            }
        } else if (id == R.id.nav_slideshow) {
            Log.d("Bluetooth","background started");
            mBtAdapter = BluetoothAdapter.getDefaultAdapter();
            mBtAdapter.startDiscovery( );
            new Thread()
            {
                public void run() {
                    Log.d("Bluetooth", "Started threading");
                    try {
                        Set<BluetoothDevice> result = mBtAdapter.getBondedDevices();
                        if(result == null){
                            Log.d("Bluetooth","result parsed failed");
                        }else {
                            for (BluetoothDevice resultSet : result) {
                                if (resultSet.toString().equals("98:D3:32:30:A5:C0")) {
                                    // ConnectThread connection(resultSet);
                                    Log.d("Bluetooth", resultSet.toString());
                                    BluetoothSocket mmSocket = resultSet.createRfcommSocketToServiceRecord(MY_UUID);
                                    mmSocket.connect();  // blocking call
                                    int bytes;
                                    byte[] buffer = new byte[512];
                                    while (true) {
                                        try {
                                            mmInStream = mmSocket.getInputStream();
                                            mmOutStrem = mmSocket.getOutputStream();
                                            bytes = mmInStream.read(buffer);
                                            String readed = new String(buffer, 0, bytes);
                                            //Log.d("Bluetooth",mmInStream.toString());
                                            //Log.d("Bluetooth",mmOutStrem.toString());
                                            Log.d("Bluetooth", readed);
                                        } catch (Exception e) {
                                            break;
                                        }
                                        Thread.sleep(1000);
                                    }
                                    //                            Method method = resultSet.getClass().getMethod("getUuids", null);
                                    //                            Log.d("Bluetooth",method.toString());
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.run();
        } else if (id == R.id.nav_manage) {
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
            Toast.makeText(getApplicationContext(), "退出Prevent Asistant", Toast.LENGTH_LONG).show();

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
