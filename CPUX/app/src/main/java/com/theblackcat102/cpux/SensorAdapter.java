package com.theblackcat102.cpux;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;



/**
 * Created by theblackcat on 1/3/17.
 */

public class SensorAdapter extends ArrayAdapter {
    String[] nameArray;
    String[] valueArray;
    public SensorAdapter(Context context,String[] values,String[] names){
        super(context, R.layout.sensor_adapter_design,R.id.sensorListView,names);
        this.nameArray = new String [names.length];
        this.valueArray = new String [values.length];

        for(int i=0;i<values.length;i++) {
            valueArray[i]= values[i];
            nameArray[i] = names[i];
        }

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.sensor_adapter_design,parent,false);

        TextView name = (TextView) row.findViewById(R.id.sensorName);
        TextView value =(TextView) row.findViewById(R.id.sensorValue);

        name.setText(nameArray[position]);
        value.setText(valueArray[position]);

        return row;
    }

    public void updateValue(String[] newValues){
        valueArray = newValues;
        this.notifyDataSetChanged();
    }
}
