package com.hpp.foodminder;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hpp.foodminder.models.RestaurantModel;

public class ViewRestaurant extends SlidingDrawerActivity {

    RestaurantModel restaurantModel = new RestaurantModel();
    TextView name, phone, address;

    public  static  boolean isRest = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_restaurant);
        restaurantModel = (RestaurantModel) getIntent().getSerializableExtra("Rest");
        initViews();
    }


    void initViews() {

        name = (TextView) findViewById(R.id.tv_name);
        phone = (TextView) findViewById(R.id.tv_phone);
        address = (TextView) findViewById(R.id.tv_add);

        name.setText(restaurantModel.getName());
        if (restaurantModel.getPhone() != null)
            phone.setText(restaurantModel.getPhone());
        else
            phone.setVisibility(View.GONE);
        if (restaurantModel.getAddress() != null)
            address.setText(restaurantModel.getAddress());
        else
            address.setVisibility(View.GONE);

        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ViewRestaurant.this, AddRestaurant.class);
                i.putExtra("Rest", restaurantModel);
                isRest = true;
                startActivity(i);
            }
        });
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + Uri.encode(restaurantModel.getPhone())));
                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(callIntent);
            }
        });
        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + restaurantModel.getAddress());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ImageView mHome = (ImageView) toolbar.findViewById(R.id.home);
        mHome.setImageDrawable(getResources().getDrawable(R.drawable.plus));
        TextView mText = (TextView) toolbar.findViewById(R.id.toolbar_title);
        Typeface tf = Typeface.createFromAsset(getAssets(), "Pacifico.ttf");
        mText.setTypeface(tf);
        mHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(ViewRestaurant.this, AddItem.class);
                startActivity(i);
            }
        });
    }
}
