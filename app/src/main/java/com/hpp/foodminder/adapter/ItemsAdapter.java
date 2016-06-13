package com.hpp.foodminder.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hpp.foodminder.R;
import com.hpp.foodminder.models.ItemsModel;
import com.hpp.foodminder.models.RestaurantModel;

import java.util.List;

/**
 * Created by Sharjeel on 06/05/2016.
 */
public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.MyViewHolder>{

    private List<ItemsModel> itemsModelList;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView Name, Cuisine, Address,Phone;

        public MyViewHolder(View view) {
            super(view);
            Name = (TextView) view.findViewById(R.id.rest_name);
            Cuisine = (TextView) view.findViewById(R.id.cuisine_name);
        }
    }

    public ItemsAdapter(List<ItemsModel> restaurantModelList) {
        this.itemsModelList = restaurantModelList;
    }

    @Override
    public ItemsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_restaurant_view, parent, false);

        return new ItemsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ItemsAdapter.MyViewHolder holder, int position) {
        ItemsModel model = itemsModelList.get(position);
        holder.Cuisine.setText("Rating : "+model.getRating());
        holder.Name.setText(model.getName());
    }

    @Override
    public int getItemCount() {
        return itemsModelList.size();
    }
}
