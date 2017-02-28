package com.theblackcat102.wearablentu;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.String;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class ConnectPage extends AppCompatActivity {
    private final static int REQUEST_ENABLE_BT = 1;
    BluetoothAdapter mBtAdapter;
    ListView list;
    Set<BluetoothDevice>  deviceSet;
    BluetoothDevice device;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    String fiilliste[];
    InputStream mmInStream;
    OutputStream mmOutStrem;
    byte[] command=new byte[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_page);
        list=(ListView)findViewById(R.id.listView);
        command[2]=(byte)0x0D;
        command[3]=(byte)0x0A;
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                for (BluetoothDevice bd : deviceSet) {
                    if (bd.getName().equals(fiilliste[position])) {
                        device = bd;
                    }
                }
                try {
                    BluetoothSocket mmSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
                    mmSocket.connect();  // blocking call
                    mmInStream = mmSocket.getInputStream();
                    mmOutStrem = mmSocket.getOutputStream();

                } catch (IOException e) {
                    Log.d("Bluetooth","error here");
                }
            }
        });

        android.support.v7.widget.SwitchCompat red=(android.support.v7.widget.SwitchCompat)findViewById(R.id.red);
        red.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked)
                {
                    command[1]=49;
                }
                else
                {
                    command[1]=48;
                }
                if (mmOutStrem!=null)
                {

                    try {
                        mmOutStrem.write(command);
                        mmOutStrem.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        android.support.v7.widget.SwitchCompat green=(android.support.v7.widget.SwitchCompat)findViewById(R.id.green);

        green.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    command[0]=49;
                }
                else
                {
                    command[0]=48;
                }
                if (mmOutStrem!=null)
                {

                    try {
                        mmOutStrem.write(command);
                        mmOutStrem.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        // 檢查藍牙是否開啟
        if (!mBtAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }

        mBtAdapter.startDiscovery();
        // 搜尋到藍牙裝置時，呼叫我們的函式
        IntentFilter filter = new IntentFilter( BluetoothDevice.ACTION_FOUND );
        this.registerReceiver( mReceiver, filter );

        // 搜尋的過程結束後，也呼叫我們的函式
        filter = new IntentFilter( BluetoothAdapter.ACTION_DISCOVERY_FINISHED );
        this.registerReceiver(mReceiver, filter);
    }

    private BroadcastReceiver mReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            ArrayList<String> stringarray=new ArrayList<String>();
            ArrayAdapter<String> adapter;

            if (intent.getAction()== BluetoothDevice.ACTION_FOUND)
            {
                deviceSet= mBtAdapter.getBondedDevices();
                for (BluetoothDevice bd:deviceSet)
                {
                    stringarray.add(bd.getName());
                }
            }
            fiilliste=new String[stringarray.size()];
            stringarray.toArray(fiilliste);

            adapter = new ArrayAdapter<String>(ConnectPage.this,android.R.layout.simple_list_item_1, fiilliste);

            list.setAdapter(adapter);
        }
    };
}
