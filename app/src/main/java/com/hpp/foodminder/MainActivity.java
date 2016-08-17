package com.hpp.foodminder;

import android.app.backup.BackupManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.hpp.foodminder.adapter.RestaurantAdapter;
import com.hpp.foodminder.db.DatabaseHelper;
import com.hpp.foodminder.models.CuisineModel;
import com.hpp.foodminder.models.RestaurantModel;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

public class MainActivity extends SlidingDrawerActivity {


    Button AddRest;
    EditText Name;
    Dao<RestaurantModel, Integer> restaurantDao;
    private DatabaseHelper databaseHelper = null;
    ArrayList<RestaurantModel> dbList = new ArrayList<>();
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);
        /*try {
            BackupManager bm = new BackupManager(this);
            bm.dataChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
*/
        AddRest = (Button) findViewById(R.id.add_rest);
        Name = (EditText) findViewById(R.id.et_CardName);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        AddRest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AddRestaurant.class);
                i.putExtra("Name", Name.getText().toString());
                startActivity(i);

            }
        });
        try {
            // This is how, a reference of DAO object can be done
            restaurantDao = getHelper().getRestaurantDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addOnItemTouchListener(new CuisineList.RecyclerTouchListener(getApplicationContext(), recyclerView, new CuisineList.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent i = new Intent(MainActivity.this, ViewRestaurant.class);
                i.putExtra("Rest", dbList.get(position));
                startActivity(i);
            }

            @Override
            public void onLongClick(View view, int position) {
                final RestaurantModel model = dbList.get(position);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        MainActivity.this);
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
                                    DeleteBuilder<RestaurantModel, Integer> deleteBuilder = restaurantDao.deleteBuilder();
                                    deleteBuilder.where().eq("Id", model.getId());
                                    deleteBuilder.delete();
                                    getDBCuisine();
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

    @Override
    protected void onResume() {
        super.onResume();
        ImageView mHome = (ImageView) toolbar.findViewById(R.id.home);
        mHome.setImageDrawable(getResources().getDrawable(R.drawable.filter));
        TextView mText = (TextView) toolbar.findViewById(R.id.toolbar_title);
        Typeface tf = Typeface.createFromAsset(getAssets(), "Pacifico.ttf");
        mText.setTypeface(tf);
        getDBCuisine();

        mHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(MainActivity.this)
                        .title(R.string.sort_by)
                        .items(R.array.sort_items)
                        .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                /**
                                 * If you use alwaysCallSingleChoiceCallback(), which is discussed below,
                                 * returning false here won't allow the newly selected radio button to actually be selected.
                                 **/
                                if (text.equals("Restaurant")) {
                                    Collections.sort(dbList, new Comparator<RestaurantModel>() {
                                        public int compare(RestaurantModel v1, RestaurantModel v2) {
                                            return v1.getName().compareTo(v2.getName());
                                        }
                                    });
                                    recyclerView.setAdapter(new RestaurantAdapter(dbList));
                                } else {
                                    Collections.sort(dbList, new Comparator<RestaurantModel>() {
                                        public int compare(RestaurantModel v1, RestaurantModel v2) {
                                            return v1.getCuisine().compareTo(v2.getCuisine());
                                        }
                                    });
                                    recyclerView.setAdapter(new RestaurantAdapter(dbList));
                                }

                                return true;
                            }
                        })
                        .positiveText(R.string.choose)
                        .show();
            }
        });
        Name.setText("");
    }
    /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    private DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
        return databaseHelper;
    }


    public void getDBCuisine() {
        dbList = new ArrayList<>();
        try {
            // This is how, a reference of DAO object can be done
            Dao<RestaurantModel, Integer> cuisineModels = getHelper().getRestaurantDao();
            // Get our query builder from the DAO
            final QueryBuilder<RestaurantModel, Integer> queryBuilder = cuisineModels.queryBuilder();
            // We need only Students who are associated with the selected Teacher, so build the query by "Where" clause
            // Prepare our SQL statement
            final PreparedQuery<RestaurantModel> preparedQuery = queryBuilder.prepare();
            // Fetch the list from Database by queryingit
            final Iterator<RestaurantModel> studentsIt = cuisineModels.queryForAll().iterator();
            // Iterate through the StudentDetails object iterator and populate the comma separated String
            while (studentsIt.hasNext()) {
                final RestaurantModel sDetails = studentsIt.next();
                dbList.add(sDetails);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Collections.reverse(dbList);
        recyclerView.setAdapter(new RestaurantAdapter(dbList));
    }


}
