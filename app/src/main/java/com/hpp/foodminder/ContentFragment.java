package com.hpp.foodminder;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Admin on 04-06-2015.
 */
public class ContentFragment extends Fragment {
    private RecyclerView recyclerView;
    View v;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.content_fragment, container, false);
        initViews();
        return v;
    }

    void initViews() {
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);

    }
}
