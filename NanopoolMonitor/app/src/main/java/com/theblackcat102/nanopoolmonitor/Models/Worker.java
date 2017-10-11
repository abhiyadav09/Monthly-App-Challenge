package com.theblackcat102.nanopoolmonitor.Models;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by theblackcat on 8/10/17.
 */

public class Worker {
    String id;
    Double hashRate;
    ArrayList<Double> averageHashRate;
    final String TAG = "Worker";
    public Worker(JSONObject worker){
        try {
            this.id = worker.getString("id");
            this.averageHashRate = new ArrayList<>();
            this.hashRate = worker.getDouble("hashrate");
            int []hours = {1, 3, 6, 12, 24};
            for (int hour : hours) {
                averageHashRate.add(worker.getDouble("avg_h" + Integer.toString(hour)));
            }
        }catch (JSONException e){
            Log.d(TAG,e.toString());
        }
    }
    public double getHashRate(){
        return this.hashRate;
    }

    public ArrayList<Double> averageHashRate(){
        return averageHashRate;
    }
}
