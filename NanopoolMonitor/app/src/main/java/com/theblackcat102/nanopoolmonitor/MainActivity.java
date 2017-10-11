package com.theblackcat102.nanopoolmonitor;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.theblackcat102.nanopoolmonitor.Models.CrytoCurrency;
import com.theblackcat102.nanopoolmonitor.Models.ChartItem;
import com.theblackcat102.nanopoolmonitor.Models.Wallet;
import com.theblackcat102.nanopoolmonitor.adapter.StatusAdapter;
import com.theblackcat102.nanopoolmonitor.api.APIParser;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    APIParser parser;
    CrytoCurrency crytoCurrency;
    private RecyclerView mRecyclerView;
    private StatusAdapter statusAdapter;
    private String address;
    private SwipeRefreshLayout mySwipeRefreshLayout;

    private String walletID;
    private String walletType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        statusAdapter = new StatusAdapter(crytoCurrency,this);
        mRecyclerView.setAdapter(statusAdapter);
        address = "t1ZySCfWbNzqXPnoAnmTSMnsQSjCNDaChKK";
        Intent intent = getIntent();
        List<Wallet> wallets;
        try {
            wallets = Wallet.listAll(Wallet.class);
        }catch(SQLiteException e){
            e.printStackTrace();
            wallets = new ArrayList<>();
        }
        if(wallets.size() == 0){
            Intent walletIntent = new Intent(this, NewWallet.class);
            startActivity(walletIntent);
        }else if(intent.hasExtra(Wallet.WALLET_ID) && intent.hasExtra(Wallet.WALLET_TYPE)){
            walletID = intent.getStringExtra(Wallet.WALLET_ID);
            walletType = intent.getStringExtra(Wallet.WALLET_TYPE);
        }else{
            Wallet wallet = wallets.get(wallets.size()-1);
            walletID = wallet.getWalletID();
            walletType = wallet.getType();
        }
        if(!networkAvailable()) {
            Toast.makeText(this, "無網路連線", Toast.LENGTH_SHORT).show();
        }else{
            try{
                this.parser = new APIParser(walletID,walletType);
                updateUI();
            }catch (Exception e){
                Log.d("Main",e.toString());
            }
        }
        mySwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        updateUI();
                    }
                }
        );
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private boolean networkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void updateUI() {
        OkHttpClient client = parser.getClient();
        client.newCall(parser.getStatus()).enqueue(new Callback() {
            @Override public void onFailure(@Nullable Call call,@Nullable  IOException e) {
                if(e != null)
                    e.printStackTrace();
            }

            @Override
            public void onResponse(@Nullable Call call,@Nullable Response response) throws IOException {

                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                    JSONObject obj = new JSONObject(responseBody.string());
                    final CrytoCurrency crytoCurrency = new CrytoCurrency(obj,parser.getCryptoType(),address);
                    Log.d("Main",Double.toString(crytoCurrency.getHashRate()));

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            statusAdapter.setNewData(crytoCurrency);
                            statusAdapter.notifyDataSetChanged();
                            setProcessing(false);
                            if (mySwipeRefreshLayout.isRefreshing()) {
                                mySwipeRefreshLayout.setRefreshing(false);
                            }
                        }
                    });
                }catch (JSONException e){
                    Log.d("Main",e.toString());
                }
            }
        });

        client.newCall(parser.getChartData()).enqueue(new Callback() {
            @Override public void onFailure(@Nullable Call call,@Nullable  IOException e) {
                if(e != null)
                    e.printStackTrace();
            }

            @Override
            public void onResponse(@Nullable Call call,@Nullable Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                    JSONObject obj = new JSONObject(responseBody.string());
                    final ArrayList<ChartItem> data = CrytoCurrency.getChartData(obj);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            statusAdapter.updateChartData(data);
                            statusAdapter.notifyDataSetChanged();
                            if (mySwipeRefreshLayout.isRefreshing()) {
                                mySwipeRefreshLayout.setRefreshing(false);
                            }
                        }
                    });
                }catch (JSONException e){
                    Log.d("Main",e.toString());
                }
            }
        });
    }

    private void setProcessing(boolean isProcessing) {
        if(isProcessing) {
            mRecyclerView.setVisibility(View.GONE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }

}
