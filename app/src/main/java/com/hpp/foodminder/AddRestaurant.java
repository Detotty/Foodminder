package com.hpp.foodminder;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.hpp.foodminder.adapter.CuisineAdapter;
import com.hpp.foodminder.db.DatabaseHelper;
import com.hpp.foodminder.models.CuisineModel;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class AddRestaurant extends SlidingDrawerActivity {

    EditText Add, phone, name;
    Button save, cancel, location;
    TextView or;
    Spinner selectCuisine;

    private DatabaseHelper databaseHelper = null;
    Dao<CuisineModel, Integer> cuisineDao;
    ArrayList<CuisineModel> dbList = new ArrayList<>();
    ArrayList<String> cuisineList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_restaurant);
        initViews();
    }

    void initViews() {
        location = (Button) findViewById(R.id.btn_location);
        Add = (EditText) findViewById(R.id.et_add);
        phone = (EditText) findViewById(R.id.et_phone);
        name = (EditText) findViewById(R.id.et_name);
        selectCuisine = (Spinner) findViewById(R.id.sp_cuisine);
        getDBCuisine();
        for (CuisineModel cuisine : dbList
                ) {
            cuisineList.add(cuisine.getName());
        }
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cuisineList);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        selectCuisine.setAdapter(dataAdapter);
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

                if (!name.getText().toString().isEmpty()) {

                } else {
                    Toast.makeText(AddRestaurant.this, "Please enter restaurant name", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //region Database Handling
    // This is how, DatabaseHelper can be initialized for future use
    private DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
        return databaseHelper;
    }

    public void updateCuisines(CuisineModel cuisineModel) {
        try {
            cuisineDao.createOrUpdate(cuisineModel);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getDBCuisine() {
        dbList = new ArrayList<>();
        try {
            // This is how, a reference of DAO object can be done
            Dao<CuisineModel, Integer> cuisineModels = getHelper().getCuisineDao();
            // Get our query builder from the DAO
            final QueryBuilder<CuisineModel, Integer> queryBuilder = cuisineModels.queryBuilder();
            // We need only Students who are associated with the selected Teacher, so build the query by "Where" clause
            // Prepare our SQL statement
            final PreparedQuery<CuisineModel> preparedQuery = queryBuilder.prepare();
            // Fetch the list from Database by queryingit
            final Iterator<CuisineModel> studentsIt = cuisineModels.queryForAll().iterator();
            // Iterate through the StudentDetails object iterator and populate the comma separated String
            while (studentsIt.hasNext()) {
                final CuisineModel sDetails = studentsIt.next();
                dbList.add(sDetails);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Collections.reverse(dbList);
    }
    //endregion

}
