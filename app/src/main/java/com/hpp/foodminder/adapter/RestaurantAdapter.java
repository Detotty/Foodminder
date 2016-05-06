package com.hpp.foodminder.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hpp.foodminder.R;

/**
 * Created by Sharjeel on 06/05/2016.
 */
public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.MyViewHolder>{

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView Name, Cuisine, Address,Phone;

        public MyViewHolder(View view) {
            super(view);
            Name = (TextView) view.findViewById(R.id.rest_name);
            Cuisine = (TextView) view.findViewById(R.id.cuisine_name);
        }
    }

    @Override
    public RestaurantAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RestaurantAdapter.MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
