package com.hpp.foodminder.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hpp.foodminder.R;
import com.hpp.foodminder.models.CuisineModel;
import com.hpp.foodminder.models.RestaurantModel;

import java.util.List;

/**
 * Created by Sharjeel on 06/05/2016.
 */
public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.MyViewHolder>{

    private List<RestaurantModel> restaurantModelList;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView Name, Cuisine, Address,Phone;

        public MyViewHolder(View view) {
            super(view);
            Name = (TextView) view.findViewById(R.id.rest_name);
            Cuisine = (TextView) view.findViewById(R.id.cuisine_name);
        }
    }

    public RestaurantAdapter(List<RestaurantModel> restaurantModelList) {
        this.restaurantModelList = restaurantModelList;
    }

    @Override
    public RestaurantAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_restaurant_view, parent, false);

        return new RestaurantAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RestaurantAdapter.MyViewHolder holder, int position) {
        RestaurantModel model = restaurantModelList.get(position);
        holder.Cuisine.setText(model.getCuisine());
        holder.Name.setText(model.getName());
    }

    @Override
    public int getItemCount() {
        return restaurantModelList.size();
    }
}
