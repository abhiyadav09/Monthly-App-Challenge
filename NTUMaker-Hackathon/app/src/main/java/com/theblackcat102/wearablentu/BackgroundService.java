package com.theblackcat102.wearablentu;

/**
 * Created by theblackcat on 25/2/17.
 */


import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;
import android.content.IntentFilter;


public class BackgroundService extends Service{
    boolean start;
    BluetoothAdapter mBtAdapter;
    // Bluetooth
    private ListView list;
    private ArrayAdapter<String> adapter;
    Set<BluetoothDevice>  deviceSet;
    private ArrayList<String> arrayList;
    String fiilliste[];
    InputStream mmInStream;
    OutputStream mmOutStrem;
    private final static int REQUEST_ENABLE_BT = 1;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    public BackgroundService(){
        start = true;
    }

    @Override
    public int onStartCommand(Intent intent,int flags,int startID) {
//        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        final Handler handler = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                super.handleMessage(msg);
                Toast.makeText(BackgroundService.this, "5 secs has passed", Toast.LENGTH_SHORT).show();
            }

        };
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
    //    adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, arrayList);
  //      list = (ListView) findViewById(R.id.bluetoothList);

        mBtAdapter.startDiscovery( );
    //    if( !mBtAdapter.isEnabled( ) ) {
   //         Intent enableIntent = new Intent( BluetoothAdapter.ACTION_REQUEST_ENABLE );
   //         startActivityForResult( enableIntent, REQUEST_ENABLE_BT ); }
  //      IntentFilter filter = new IntentFilter( BluetoothDevice.ACTION_FOUND );
   //     this.registerReceiver( mReceiver, filter );

        // 搜尋的過程結束後，也呼叫我們的函式
//        filter = new IntentFilter( BluetoothAdapter.ACTION_DISCOVERY_FINISHED );
 //       this.registerReceiver( mReceiver, filter );
        new Thread()
        {
            public void run() {
                Log.d("Bluetooth","Started threading");
                try {
                    Set<BluetoothDevice> result = mBtAdapter.getBondedDevices();
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
                                }catch(Exception e){
                                    break;
                                }
                                Thread.sleep(1000);
                            }
                            //                            Method method = resultSet.getClass().getMethod("getUuids", null);
                            //                            Log.d("Bluetooth",method.toString());
                        }
                    }
                }catch(IOException e){
                    e.printStackTrace();
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
//                while(true) {
//                    try {
//                        /*
//                            do constant re calling to bt
//                        */
//                        Thread.sleep(5000);
//                        Log.d("BackgroundService", "it's alived!");
//                        handler.sendEmptyMessage(0);
//                    } catch (InterruptedException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//                }
            }
        }.start();

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy(){
        start = false;
        super.onDestroy();
        Log.d("BackgroundService","onDestroy");
    }

    @Override
    public IBinder onBind(Intent intent){
        return null;
    }

}
