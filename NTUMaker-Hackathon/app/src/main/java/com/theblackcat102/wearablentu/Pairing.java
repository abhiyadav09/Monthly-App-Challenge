package com.theblackcat102.wearablentu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.app.*;
import android.bluetooth.*;
import android.content.BroadcastReceiver;
import android.content.Context;
import java.lang.reflect.Method;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.view.View;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.util.Set;
import java.util.ArrayList;
import java.io.IOException;
import java.util.UUID;
import java.io.StringWriter;
import java.io.InputStream;
import java.io.OutputStream;


public class Pairing extends AppCompatActivity {
    BluetoothAdapter mBtAdapter;
   // Bluetooth
    private ListView list;
    private ArrayAdapter<String> adapter;
    Set<BluetoothDevice>  deviceSet;
    private ArrayList<String> arrayList;
    String fiilliste[];
    InputStream mmInStream;
    OutputStream mmOutStrem;
    Set<BluetoothDevice> results;
    private final static int REQUEST_ENABLE_BT = 1;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pairing);
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        ListView bluetoothName = (ListView)findViewById(R.id.BluetoothList);

//        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, arrayList);
//        list = (ListView) findViewById(R.id.bluetoothList);
//
        mBtAdapter.startDiscovery( );
        results = mBtAdapter.getBondedDevices();
        ArrayList<String> myStringArray1 = new ArrayList<>();
        Log.d("Bluetooth",myStringArray1.toString());
        for(BluetoothDevice result : results){
            myStringArray1.add(result.toString());
        }
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, myStringArray1);
        bluetoothName.setAdapter(adapter);
//        if( !mBtAdapter.isEnabled( ) ) {
//            Intent enableIntent = new Intent( BluetoothAdapter.ACTION_REQUEST_ENABLE );
//            startActivityForResult( enableIntent, REQUEST_ENABLE_BT ); }
//        IntentFilter filter = new IntentFilter( BluetoothDevice.ACTION_FOUND );
//        this.registerReceiver( mReceiver, filter );
//
//        // 搜尋的過程結束後，也呼叫我們的函式
//        filter = new IntentFilter( BluetoothAdapter.ACTION_DISCOVERY_FINISHED );
//        this.registerReceiver( mReceiver, filter );
        new Thread()
        {
            public void run() {
                try {
                    Thread.sleep(4000);

                    Intent intent = new Intent(Pairing.this,LinkFinished.class);
                    startActivity(intent);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }.start();
        new Thread()
        {
            public void run() {
                try {

                    results = mBtAdapter.getBondedDevices();
                    Log.d("Bluetooth",results.toString());
                    Thread.sleep(1000);
                    for (BluetoothDevice resultSet : results) {
                        if(resultSet.toString().equals("98:D3:32:30:A5:C0")) {
                           // ConnectThread connection(resultSet);
                            Log.d("Bluetooth", resultSet.toString());
                            BluetoothSocket mmSocket = resultSet.createRfcommSocketToServiceRecord(MY_UUID);
                            Thread.sleep(500);
                            Thread.sleep(1000);
                            Log.d("Bluetooth",mmSocket.toString());
                            mmSocket.connect();  // blocking call
                            Log.d("Bluetooth","start here1");
                            int bytes;
                            byte[] buffer = new byte[250];
                            while(true){
                                mmInStream = mmSocket.getInputStream();
                                mmOutStrem = mmSocket.getOutputStream();
                                bytes = mmInStream.read(buffer);
                                String readed = new String(buffer, 0, bytes);
                                //Log.d("Bluetooth",mmInStream.toString());
                                //Log.d("Bluetooth",mmOutStrem.toString());
                                Log.d("Bluetooth",readed);
                                Thread.sleep(1000);
                            }
//                            Method method = resultSet.getClass().getMethod("getUuids", null);
//                            Log.d("Bluetooth",method.toString());
                        }
                    }
                    Intent intent = new Intent(Pairing.this,LinkFinished.class);
                    startActivity(intent);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }catch(IOException e){
                    e.printStackTrace();
                    try{
                        Class<?> clazz = mBtAdapter.getBondedDevices().getClass();
                        Class<?>[] paramTypes = new Class<?>[] {Integer.TYPE};
                        Method m = clazz.getMethod("createRfcommSocket", paramTypes);
                        Object[] params = new Object[] {Integer.valueOf(1)};
                        BluetoothSocket mmSocket = (BluetoothSocket) m.invoke(mBtAdapter.getBondedDevices(), params);
                        mmSocket.connect();
                        Log.d("Bluetooth","start here1");
                        int bytes;
                        byte[] buffer = new byte[250];
                        while(true){
                            mmInStream = mmSocket.getInputStream();
                            mmOutStrem = mmSocket.getOutputStream();
                            bytes = mmInStream.read(buffer);
                            String readed = new String(buffer, 0, bytes);
                            //Log.d("Bluetooth",mmInStream.toString());
                            //Log.d("Bluetooth",mmOutStrem.toString());
                            Log.d("Bluetooth",readed);
                            Thread.sleep(1000);
                        }
                    }catch (Exception err)
                    {
                        err.printStackTrace();
                        Log.d("Bluetooth","second falll back failed");
                    }
                    Log.d("Bluetooth","start here2");
                }
            }
        }.start();

    }



}
