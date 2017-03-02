package com.theblackcat102.cpux;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager mSensorManager;
    String[] names;
    String[] content;
    ListView sensorLists;
    private List<Sensor> deviceSensors;
    private SensorAdapter adapter;

    private float gravity[] = new float[3];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        deviceSensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);

        names = new String[deviceSensors.size()];
        content = new String[deviceSensors.size()];
        for (int i = 0; i < deviceSensors.size(); i++)
            names[i] = deviceSensors.get(i).getName();
//
//        }
        sensorLists = (ListView) findViewById(R.id.sensorListView);
        adapter = new SensorAdapter(this, content,names);
        sensorLists.setAdapter(adapter);
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d("sensors",sensor.getName());
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {

        for(int i=0;i<deviceSensors.size();i++) {
            if(event.sensor.getName() == names[i]) {
                content[i] = sensorFormater(event);
            }
        }
        adapter.updateValue(content);
        // Do something with this sensor value.
    }
    @Override
    protected void onResume() {
        super.onResume();
        for(int i=0;i<deviceSensors.size();i++) {
            mSensorManager.registerListener(this, deviceSensors.get(i), SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    private String sensorFormater(SensorEvent event){
        String content = "";
        float alpha = (float)0.8;
        if(event.sensor.getName().toString().contains("Accelerometer")){
            char axis = 'W';
            for(int k=0;k< 3;k++) {
                axis += 1;
                content += axis+"= "+String.format("%.2f", event.values[k])  +" m/s^2  ";
            }
            return content;
        }
        else if(event.sensor.getName().toString().contains("Magnetometer")){
            float []magnetic = new float[3];


            magnetic[0] = event.values[0];
            magnetic[1] = event.values[1];
            magnetic[2] = event.values[2];

            float[] R = new float[9];
            float[] I = new float[9];
            SensorManager.getRotationMatrix(R, I, gravity, magnetic);
            float [] A_D = event.values.clone();
            float [] A_W = new float[3];
            A_W[0] = R[0] * A_D[0] + R[1] * A_D[1] + R[2] * A_D[2];
            A_W[1] = R[3] * A_D[0] + R[4] * A_D[1] + R[5] * A_D[2];
            A_W[2] = R[6] * A_D[0] + R[7] * A_D[1] + R[8] * A_D[2];

            return "Field"+" X :"+String.format("%.2f",A_W[0])+" Y :"+String.format("%.2f",A_W[1])+" Z :"+String.format("%.2f",A_W[2]);
        }
        else if(event.sensor.getName().toString().contains("Gyroscope")){
            char axis = 'W';
            for(int k=0;k< 3;k++) {
                axis += 1;
                content += axis+"= "+String.format("%.2f", event.values[k])  +" rad/s  ";
            }
            return content;
        }
        else if(event.sensor.getName().toString().contains("Gravity")){
            char axis = 'W';
            //float alpha= 0;
            for(int k=0;k< 3;k++) {
                axis += 1;
                content += axis+"= "+String.format("%.2f", event.values[k])  +" m/s^s  ";
            }
            gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
            gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
            gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];
            return content;
        }
        else if(event.sensor.getName().toString().contains("Vector")){
            char axis = 'W';
            for(int k=0;k< 3;k++) {
                axis += 1;
                content += axis+"= "+String.format("%.2f", event.values[k])  +"  ";
            }
            return content;
        }
        else if(event.sensor.getName().toString().contains("Orientation")){
            if(event.values.length == 3){
                content += "Azimuth= "+String.format("%.2f",event.values[0]) +"  ";
                content += "Pitch= "+String.format("%.2f",event.values[0]) +"  ";
                content += "Roll= "+String.format("%.2f",event.values[0]) +"  ";
            }
            else{
                for(int k=0;k<event.values.length;k++){
                    content += String.format("%.2f",event.values[k])+"  ";
                }
            }
            return content;
        }
        else if(event.sensor.getName().toString().contains("Pressure")){
            content = event.values[0]+" hPa";
            return content;
        }
        else if(event.sensor.getName().toString().contains("ALS")){
            content = event.values[0]+" lux";
            return content;
        }
        else if(event.sensor.getName().toString().contains("PRX")){
            content = event.values[0]+" cm";
            return content;
        }
        else if(event.sensor.getName().toString().contains("Step")){
            content = event.values[0]+" ";
            if(event.sensor.getName().toString().contains("Counter")){
                content += "steps";
            }
            return content;
        }
        else{
            for(int k=0;k< event.values.length;k++) {
                content += String.format("%.2f", event.values[k])  +",";
            }
        }
        return content;
    }
}
