package com.hpp.foodminder;

import android.content.Context;
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

import com.hpp.foodminder.Gps.Lattitude_Logitude;
import com.hpp.foodminder.adapter.CuisineAdapter;
import com.hpp.foodminder.db.DatabaseHelper;
import com.hpp.foodminder.models.CuisineModel;
import com.hpp.foodminder.models.RestaurantModel;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import org.json.JSONArray;
import org.json.JSONException;

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
    Dao<RestaurantModel, Integer> restaurantDao;
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
        try {
            // This is how, a reference of DAO object can be done
            restaurantDao = getHelper().getRestaurantDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        getDBCuisine();
        cuisineList.add("Select Cuisine");
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

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDefaultLocation(AddRestaurant.this);
            }
        });

    }

    public void updateCuisines(RestaurantModel restaurantModel) {
        try {
            restaurantDao.createOrUpdate(restaurantModel);
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
                    RestaurantModel restaurantModel = new RestaurantModel();
                    restaurantModel.setName(name.getText().toString());
                    restaurantModel.setAddress(Add.getText().toString());
                    restaurantModel.setPhone(phone.getText().toString());
                    restaurantModel.setCuisine(selectCuisine.getSelectedItem().toString());
                    updateCuisines(restaurantModel);

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

    public void updateRestaunts(CuisineModel cuisineModel) {
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





    void getDefaultLocation(Context context) {

        Lattitude_Logitude gps = new Lattitude_Logitude(context);

        //String countryZipCode = null;

        // check if GPS enabled
        if(gps.canGetLocation()){

            double latitude ;
            double longitude;
            String countryName;
            String countryID;



            try {

                String address = gps.onLocationCountry();

                if(address!= null) {
                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();
                    countryName = gps.getCountryName();
                    countryID = gps.getCountryCode();
                    String cityName = gps.getCityName();
                    android.util.Log.e(" condition..", "address ..." + address + ": City Name:" + cityName);

                    Add.setText(address + ", " +cityName);

//                if(countryID!= null && countryID.length()>0 &&  !countryID.equals("null")){
//
//                    String[] rl = context.getResources().getStringArray(R.array.CountryCodes);
//
//                    for(int i = 0; i<rl.length; i++ ){
//
//                        String[] g = rl[i].split(",");
//                        if(g[1].trim().equals(countryID.trim())){
//                            countryZipCode  = g[0];
//                            break;
//                        }
//
//                    }
//                }

                    android.util.Log.d(context.getClass().getSimpleName(), "GPS:" + gps.toString());
                }


            }catch (Exception e) {
                e.printStackTrace();
                //countryEditText.setText("");
//                Toast.makeText(context, R.string.location_msg3, Toast.LENGTH_LONG).show();
            }




//            mobile_no.requestFocus();
//            Handler mHandler= new Handler();
//            mHandler.post(new Runnable() {
//            	public void run() {
//            		InputMethodManager inputMethodManager =  (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
//            		inputMethodManager.toggleSoftInputFromWindow(mobile_no.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
//            		mobile_no.requestFocus();
//            	}
//            });
//            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        }else{
            android.util.Log.e(" else condition..", "call pop up for location ..");
        }
    }

}
