package com.example.myapplication.Main;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Database.Activity;
import com.example.myapplication.R;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private Context mContext ;
    private List<Activity> mData ;


    public RecyclerViewAdapter(Context mContext, List<Activity> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view ;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardview_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.age.setText(String.valueOf(mData.get(position).getAge()));
        holder.name.setText(mData.get(position).getName());
        holder.profile_picture.setImageResource(mData.get(position).getThumbnail());
        holder.distance.setText(String.valueOf(mData.get(position).getDistance())+" km");
        if (mData.get(position).getDaysTo() == 0.0) {
            holder.daysTo.setText("Today");
        }
        else if (mData.get(position).getDaysTo() == 1.0) {
            holder.daysTo.setText("1 day");
        }
        else {
            holder.daysTo.setText(String.valueOf(mData.get(position).getDaysTo())+" days");
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, Activity_Details.class);

                intent.putExtra("text",mData.get(position).getText());
                intent.putExtra("name",mData.get(position).getName());
                intent.putExtra("age", mData.get(position).getAge());
                intent.putExtra("activity",mData.get(position).getActivity());
                intent.putExtra("image",mData.get(position).getThumbnail());
                intent.putExtra("distance",mData.get(position).getDistance());
                intent.putExtra("daysTo",mData.get(position).getDaysTo());
                // start the activity
                mContext.startActivity(intent);

            }
        });



    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView age;
        TextView distance;
        TextView daysTo;
        ImageView profile_picture;
        CardView cardView ;

        public MyViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.name_searching) ;
            age = itemView.findViewById(R.id.age);
            profile_picture = (ImageView) itemView.findViewById(R.id.profile_picture_searching);
            cardView = (CardView) itemView.findViewById(R.id.cardview_id);
            distance = itemView.findViewById(R.id.distance);
            daysTo = itemView.findViewById(R.id.daysTo);

        }
    }


}
