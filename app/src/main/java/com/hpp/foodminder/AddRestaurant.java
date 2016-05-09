package com.hpp.foodminder;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AddRestaurant extends SlidingDrawerActivity {

    EditText Add, phone, name;
    Button save, cancel, location;
    TextView or;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_restaurant);
        initViews();
    }

    void initViews(){
        location = (Button) findViewById(R.id.btn_location);
        Add = (EditText) findViewById(R.id.et_add);
        phone = (EditText) findViewById(R.id.et_phone);
        name = (EditText) findViewById(R.id.et_name);

    }

    @Override
    protected void onResume() {
        super.onResume();
        ImageView mHome = (ImageView) toolbar.findViewById(R.id.home);
        mHome.setImageDrawable(getResources().getDrawable(R.drawable.check_mark));
        TextView mText = (TextView) toolbar.findViewById(R.id.toolbar_title);
        Typeface tf = Typeface.createFromAsset(getAssets(), "Pacifico.ttf");
        mText.setTypeface(tf);
        mHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!name.getText().toString().isEmpty()){

                }else{
                    Toast.makeText(AddRestaurant.this,"Please enter restaurant name",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
