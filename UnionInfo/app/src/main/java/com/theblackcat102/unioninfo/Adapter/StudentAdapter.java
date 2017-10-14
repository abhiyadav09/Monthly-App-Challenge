package com.theblackcat102.unioninfo.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.theblackcat102.unioninfo.MainActivity;
import com.theblackcat102.unioninfo.Models.Student;
import com.theblackcat102.unioninfo.R;

import java.util.ArrayList;

/**
 * Created by theblackcat on 13/10/17.
 */

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {
    private static final String TAG = "StatusAdapter";
    private MainActivity mContext;
    ArrayList<Student> items;
    public StudentAdapter(Context mContext){
        this.mContext = (MainActivity) mContext;
        this.items = new ArrayList<>();
        items.add(new Student("zhi rui","eee","3242354","male"));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.list_adapter, parent, false);
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(StudentAdapter.ViewHolder holder, int position) {
        Student item = items.get(position);
        holder.studentId.setText(item.getStudentID());
        holder.studentName.setText(item.getStudentName());

    }

//    @Override
//    public long getItemId(int position){
//        return items.get(position);
//    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView studentName;
        TextView studentId;

        ViewHolder(View itemView) {
            super(itemView);
            studentId = (TextView) itemView.findViewById(R.id.stu_id);
            studentName = (TextView) itemView.findViewById(R.id.stu_name);
        }

        @Override
        public void onClick(View view) {
//            mContext.onRestaurantClick(model);
        }
    }

    public void updateStudentView(ArrayList<Student> student){
        this.items = student;
        if(this.items.size() == 0){
            items.add(new Student("","","查無此人","male"));
        }
        this.notifyDataSetChanged();
    }

}