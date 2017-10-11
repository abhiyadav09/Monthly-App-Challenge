package com.theblackcat102.nanopoolmonitor.adapter;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.jjoe64.graphview.GraphView;
import com.theblackcat102.nanopoolmonitor.MainActivity;
import com.theblackcat102.nanopoolmonitor.Models.ChartItem;
import com.theblackcat102.nanopoolmonitor.Models.CrytoCurrency;
import com.theblackcat102.nanopoolmonitor.R;

import java.util.ArrayList;

/**
 * Created by theblackcat on 8/10/17.
 */

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.ViewHolder> {
    private static final String TAG = "StatusAdapter";
    private MainActivity mContext;
    CrytoCurrency crytoCurrency;
    ArrayList<StatsItem> items;
    public StatusAdapter(CrytoCurrency crytoCurrency,Context mContext){
        this.crytoCurrency = crytoCurrency;
        this.mContext = (MainActivity) mContext;
        this.items = new ArrayList<>();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.workers, parent, false);
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(StatusAdapter.ViewHolder holder, int position) {
        StatsItem item = items.get(position);
        if(item.getValue().equals("")) {
            holder.value.setVisibility(View.GONE);
            holder.progressBar.setVisibility(View.GONE);
            holder.graph.setVisibility(View.VISIBLE);
            if(item.getTitle().equals("History Shares")){
                holder.graph.addSeries(item.getBarData());
                holder.title.setText(item.getTitle());
            }

        }
        else{
            holder.title.setText(item.getTitle());
            holder.value.setText(item.getValue());
            if(item.getProgress() == -1.0 || item.getSecondProgress() == -1.0){
                holder.progressBar.setVisibility(View.GONE);
            }else{
                holder.progressBar.setProgress(item.getSecondProgress());
                holder.progressBar.setSecondaryProgress(item.getProgress());
            }
        }

    }

//    @Override
//    public long getItemId(int position){
//        return items.get(position);
//    }

    @Override public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView value;
        TextView title;
        RoundCornerProgressBar progressBar;
        GraphView graph;
        ViewHolder(View itemView) {
            super(itemView);
            value = (TextView) itemView.findViewById(R.id.value);
            title = (TextView) itemView.findViewById(R.id.title);
            progressBar = (RoundCornerProgressBar) itemView.findViewById(R.id.progressbar);
            graph = (GraphView) itemView.findViewById(R.id.graph);
        }

        @Override
        public void onClick(View view) {
//            mContext.onRestaurantClick(model);
        }
    }

    public void setNewData(CrytoCurrency crytoCurrency){
        this.crytoCurrency = crytoCurrency;
        String unit = "";
        switch(crytoCurrency.getType()){
            case "zec":
                unit = "Sols/s";
                break;
            default:
                unit = "H/s";
                break;
        }
        if(items.size() >= 3 && (items.get(0).getTitle().equals("Hash Rate "+unit) || items.get(0).getTitle().equals("History Shares")))
            this.items.clear();
        String strHashRate = "Current Rate: "+Double.toString(crytoCurrency.getHashRate())+"\n";
        if(crytoCurrency.getWorker().size() > 0){
            ArrayList<Double>hashRate = crytoCurrency.getWorker().get(0).averageHashRate();
            strHashRate += "Avg 1 Hr "+Double.toString(hashRate.get(0))+" | Avg 3 Hr "+Double.toString(hashRate.get(1));
        }
        items.add(new StatsItem("Hash Rate "+unit,strHashRate,-1.0,-1.0));
        items.add(new StatsItem("Balance",Double.toString(crytoCurrency.getBalance()),crytoCurrency.getPaymentProgress(),crytoCurrency.getPendingProgress()));
    }

    public void updateChartData(ArrayList<ChartItem> data){
        items.add(new StatsItem("History Shares",data));
    }
}
