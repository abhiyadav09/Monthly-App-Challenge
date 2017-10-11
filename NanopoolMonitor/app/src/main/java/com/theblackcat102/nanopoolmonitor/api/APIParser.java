package com.theblackcat102.nanopoolmonitor.api;

import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;

public class APIParser{
    private String walletAddress;
    private OkHttpClient client;
    private final String baseUrl = "https://api.nanopool.org/v1/";
    private final String generalUrls = "/user/";
    private final String hashHistory = "/history/";
    private final String hashRateChart = "/hashratechart/";
    private final String TAG = "APIParser";
    private String crypto;
    public APIParser(String walletAddress,String cryptoType) throws Exception{
        this.walletAddress = walletAddress;
        this.client = new OkHttpClient();
        if(!( cryptoType.equals("zec") || cryptoType.equals("eth")|| cryptoType.equals("etc") || cryptoType.equals("xmr"))){
            throw new Exception("Invalid Cryto Currency Type!");
        }
        this.crypto = cryptoType;
//        if(!checkExist()){
//            throw new Exception("Invalid Account");
//        }
    }

    public OkHttpClient getClient(){
        return client;
    }
    private boolean checkExist(){
        final Request request = new Request.Builder()
                .url(this.baseUrl+this.crypto+"/accountexist/"+this.walletAddress)
                .build();
        try{
            Response response = this.client.newCall(request).execute();
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            JSONObject obj = new JSONObject( response.body().string());
            return (boolean)obj.get("status");
        }catch (IOException e){
            Log.d(TAG,"Parse Failure");
        }catch (JSONException e){
            Log.d(TAG,"JSON parse failed");
        }catch (NullPointerException e){
            Log.d(TAG,"reponse failed");
        }
        return false;
    }

    public Request getStatus() {
        String url = this.baseUrl + this.crypto + generalUrls + this.walletAddress;
        Log.d(TAG,url);
        return new Request.Builder()
                .url(url)
                .build();

    }
    public Request getChartData(){
        String url = this.baseUrl + this.crypto + hashRateChart + this.walletAddress;
        Log.d(TAG,url);
        return new Request.Builder()
                .url(url)
                .build();
    }

    public String getCryptoType(){
        return this.crypto;
    }
}