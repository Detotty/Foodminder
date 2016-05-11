package com.hpp.foodminder.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hpp.foodminder.R;
import com.hpp.foodminder.models.CuisineModel;

import java.util.List;

/**
 * Created by Sharjeel on 11/05/2016.
 */
public class CuisineAdapter extends RecyclerView.Adapter<CuisineAdapter.MyViewHolder> {

    private List<CuisineModel> cuisineModelList;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView Name, Cuisine, Address, Phone;

        public MyViewHolder(View view) {
            super(view);
            Cuisine = (TextView) view.findViewById(R.id.cuisine_name);
        }
    }

    public CuisineAdapter(List<CuisineModel> cuisineModelList) {
        this.cuisineModelList = cuisineModelList;
    }

    @Override
    public CuisineAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_cuisine_view, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CuisineAdapter.MyViewHolder holder, int position) {
        CuisineModel model = cuisineModelList.get(position);
        holder.Cuisine.setText(model.getName());
    }

    @Override
    public int getItemCount() {
        return cuisineModelList.size();
    }
}
