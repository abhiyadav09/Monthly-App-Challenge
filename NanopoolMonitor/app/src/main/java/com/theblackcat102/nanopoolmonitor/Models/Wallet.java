package com.theblackcat102.nanopoolmonitor.Models;

import com.orm.SugarRecord;

/**
 * Created by theblackcat on 9/10/17.
 */

public class Wallet extends SugarRecord {
    public static final String WALLET_TYPE = "com.theblackcat.wallet_type";
    public static final String WALLET_ID = "com.theblackcat.wallet_id";
    String type;
    String wallet_id;

    public Wallet(){
        super();
    }
    public Wallet(String type,String wallet_id){
        super();
        this.type = type;
        this.wallet_id = wallet_id;

    }
    public String getType(){
        return type;
    }

    public String getWalletID(){
        return wallet_id;
    }

    public void setType(String type){
        this.type = type;
    }

    public void setWallet_id(String wallet_id){
        this.wallet_id = wallet_id;
    }
}
