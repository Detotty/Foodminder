package com.hpp.foodminder;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hpp.foodminder.adapter.ItemsAdapter;
import com.hpp.foodminder.adapter.RestaurantAdapter;
import com.hpp.foodminder.db.DatabaseHelper;
import com.hpp.foodminder.models.ItemsModel;
import com.hpp.foodminder.models.RestaurantModel;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class ViewRestaurant extends SlidingDrawerActivity {

    RestaurantModel restaurantModel = new RestaurantModel();
    TextView name, phone, address;
    private RecyclerView recyclerView;
    Dao<ItemsModel, Integer> itemsDao;
    private DatabaseHelper databaseHelper = null;
    ArrayList<ItemsModel> dbList = new ArrayList<>();
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
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

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
                i.putExtra("Rest", restaurantModel);
                startActivity(i);
            }
        });

        try {
            // This is how, a reference of DAO object can be done
            itemsDao = getHelper().getItemsDao();
            getItemDb();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addOnItemTouchListener(new CuisineList.RecyclerTouchListener(getApplicationContext(), recyclerView, new CuisineList.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                final ItemsModel model = dbList.get(position);
                Intent i  = new Intent(ViewRestaurant.this,AddItem.class);
                i.putExtra("Item", model);
                i.putExtra("Rest", restaurantModel);
                startActivity(i);
                finish();
            }

            @Override
            public void onLongClick(View view, int position) {
                final ItemsModel model = dbList.get(position);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        ViewRestaurant.this);
                // set title
                alertDialogBuilder.setTitle("Delete Restaurant");
                // set dialog message
                alertDialogBuilder
                        .setMessage("Are you sure you want to delete this restaurant")
                        .setCancelable(true)
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                try {
                                    DeleteBuilder<ItemsModel, Integer> deleteBuilder = itemsDao.deleteBuilder();
                                    deleteBuilder.where().eq("ItemId", model.getItemId());
                                    deleteBuilder.delete();
                                    getItemDb();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }
        }));

    }


    private DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
        return databaseHelper;
    }


    public void getItemDb() {
        dbList = new ArrayList<>();
        try {
            // This is how, a reference of DAO object can be done
            Dao<ItemsModel, Integer> itemsModels = getHelper().getItemsDao();
            // Get our query builder from the DAO
            final QueryBuilder<ItemsModel, Integer> queryBuilder = itemsModels.queryBuilder();
            // We need only Students who are associated with the selected Teacher, so build the query by "Where" clause
            // Prepare our SQL statement
            final PreparedQuery<ItemsModel> preparedQuery = queryBuilder.prepare();
            // Fetch the list from Database by queryingit
            final Iterator<ItemsModel> studentsIt = itemsModels.queryForAll().iterator();
            // Iterate through the StudentDetails object iterator and populate the comma separated String
            while (studentsIt.hasNext()) {
                final ItemsModel sDetails = studentsIt.next();
                dbList.add(sDetails);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Collections.reverse(dbList);
        ArrayList<ItemsModel> finalList = new ArrayList<>();
        for (ItemsModel model: dbList
             ) {
            RestaurantModel rest = model.getRestaurantModel();
            if (rest.getId() == restaurantModel.getId()){
                finalList.add(model);
            }
        }
        recyclerView.setAdapter(new ItemsAdapter(finalList));
    }
}
