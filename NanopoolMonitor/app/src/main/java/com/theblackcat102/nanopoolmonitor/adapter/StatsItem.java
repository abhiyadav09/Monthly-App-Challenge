package com.theblackcat102.nanopoolmonitor.adapter;

import android.util.Log;

import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.theblackcat102.nanopoolmonitor.Models.ChartItem;

import java.util.ArrayList;

/**
 * Created by theblackcat on 8/10/17.
 */

public class StatsItem {
    private String title;
    private String value;
    private float progress;
    private float secondProgress;
    private ArrayList<ChartItem> data;
    public StatsItem(String title,String value,Double progress,Double secondProgress){
        this.title = title;
        this.value = value;
        this.progress = progress.floatValue();
        this.secondProgress = secondProgress.floatValue();
    }

    public StatsItem(String title, ArrayList<ChartItem> data){
        this.title = title;
        this.value = "";
        this.progress = (float)-1.0;
        this.secondProgress = (float)-1.0;
        this.data = data;
    }

    public String getTitle(){
        return title;
    }

    public String getValue(){
        return value;
    }

    public float getProgress(){
        return progress;
    }

    public float getSecondProgress(){
        return secondProgress;
    }

    public BarGraphSeries<DataPoint> getBarData(){

        int startPoint = 0;
        int size = 100;
        if(data.size() < 100) {
            size = data.size();
        }else{
            startPoint = data.size()-100;
        }
        DataPoint[] dataPoints = new DataPoint[size];
        for(int i=startPoint;i<data.size();i++){
            dataPoints[i-startPoint] = new DataPoint(i-startPoint,data.get(i).getShares());
        }
        return new BarGraphSeries<>(dataPoints);
    }
}
