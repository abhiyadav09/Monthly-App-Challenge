package com.theblackcat102.nanopoolmonitor.Models;

import java.util.ArrayList;

/**
 * Created by theblackcat on 8/10/17.
 */

public class ChartItem{
    long date;
    int shares;
    int hashRate;
    public ChartItem(long date, int hashRate, int shares){
        this.shares = shares;
        this.date =date;
        this.hashRate = hashRate;
    }

    public int getShares(){
        return shares;
    }
    public int getHashRate(){
        return hashRate;
    }
    public long getDate(){
        return date;
    }
}
