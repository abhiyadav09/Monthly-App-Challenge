package com.theblackcat102.nanopoolmonitor.Models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by theblackcat on 8/10/17.
 */

public class CrytoCurrency {
    final Double ZEC_MIN = 0.01;
    final Double ETH_MIN = 0.2;
    final Double XMR_MIN = 1.0;
    final Double ETC_MIN = 1.0;
    ArrayList<Worker> workers_list;
    String account_name;
    Double balance;
    Double totalHashRate;
    Double unconfirmHashRate;
    String type;
    Double minPayment;
    public CrytoCurrency(JSONObject obj,String type,String address){
        try {
            JSONObject data = (JSONObject) obj.get("data");
            Iterator<String> iter = data.keys();
            while(iter.hasNext()){
                Log.d("Keys",iter.next());
            }
            this.totalHashRate = data.getDouble("hashrate");
            this.account_name = data.getString("account");
            this.balance = data.getDouble("balance");
            this.unconfirmHashRate = data.getDouble("unconfirmed_balance");
            JSONArray workers = data.getJSONArray("workers");
            this.workers_list = new ArrayList<>();
            for (int i = 0; i < workers.length(); i++) {
                JSONObject worker = workers.getJSONObject(i);
                workers_list.add(new Worker(worker));
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        this.type = type;
        switch (type){
            case "zec":
                minPayment = ZEC_MIN;
                break;
            case "etc":
                minPayment = ETC_MIN;
                break;
            case "eth":
                minPayment = ETH_MIN;
                break;
            case "xmr":
                minPayment = XMR_MIN;
                break;
        }
    }
    public double getBalance(){
        if(this.balance == null)
            return 0;
        return this.balance;
    }
    public double getHashRate(){
        if(this.totalHashRate == null)
            return 0;
        return this.totalHashRate.doubleValue();
    }
    public double getPaymentProgress(){
        return this.balance/minPayment;
    }
    public double getPendingProgress(){
        return this.unconfirmHashRate/minPayment;
    }
    public String getType(){ return type; }
    public ArrayList<Worker> getWorker(){
        return workers_list;
    }

    public static ArrayList<ChartItem> getChartData(JSONObject obj){
        ArrayList<ChartItem> data = new ArrayList<>();
        try {
            if (obj.getBoolean("status")) {
                JSONArray dataList = obj.getJSONArray("data");
                for (int i = 0; i < dataList.length(); i++) {
                    JSONObject node = dataList.getJSONObject(i);
                    data.add(new ChartItem(node.getLong("date"), node.getInt("hashrate"), node.getInt("shares")));
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return data;
    }

}
