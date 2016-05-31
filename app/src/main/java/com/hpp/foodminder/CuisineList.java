package com.hpp.foodminder;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hpp.foodminder.adapter.CuisineAdapter;
import com.hpp.foodminder.adapter.DividerItemDecoration;
import com.hpp.foodminder.db.DatabaseHelper;
import com.hpp.foodminder.models.CuisineModel;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class CuisineList extends SlidingDrawerActivity {

    private DatabaseHelper databaseHelper = null;
    Dao<CuisineModel, Integer> cuisineDao;
    ArrayList<CuisineModel> dbList = new ArrayList<>();

    EditText cuisineName;
    Button add;

    private RecyclerView recyclerView;
    private CuisineAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuisine_list);
        initViews();
    }


    void initViews() {
        cuisineName = (EditText) findViewById(R.id.et_CuisineName);
        add = (Button) findViewById(R.id.add_Cuisine);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mAdapter = new CuisineAdapter(dbList);
        try {
            // This is how, a reference of DAO object can be done
            cuisineDao = getHelper().getCuisineDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Name = cuisineName.getText().toString();
                if (!Name.isEmpty()) {
                    CuisineModel cuisineModel = new CuisineModel();
                    cuisineModel.setName(Name);
                    updateCuisines(cuisineModel);
                    getDBCuisine();
                    cuisineName.setText("");
                } else {
                    Toast.makeText(CuisineList.this, "Please enter cuisine name", Toast.LENGTH_SHORT);
                }
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
            }

            @Override
            public void onLongClick(View view, int position) {
                final CuisineModel model = dbList.get(position);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        CuisineList.this);
                // set title
                alertDialogBuilder.setTitle("Delete Cuisine");
                // set dialog message
                alertDialogBuilder
                        .setMessage("Are you sure you want to delete this cuisine")
                        .setCancelable(true)
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                try {
                                    DeleteBuilder<CuisineModel, Integer> deleteBuilder = cuisineDao.deleteBuilder();
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
        getDBCuisine();

    }

    @Override
    protected void onResume() {
        super.onResume();
        ImageView mHome = (ImageView) toolbar.findViewById(R.id.home);
        TextView mText = (TextView) toolbar.findViewById(R.id.toolbar_title);
        Typeface tf = Typeface.createFromAsset(getAssets(), "Pacifico.ttf");
        mText.setTypeface(tf);
        mHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i  = new Intent(CuisineList.this,MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
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
        recyclerView.setAdapter(new CuisineAdapter(dbList));
    }
    //endregion


    //region Item Click Listener Class
    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private CuisineList.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final CuisineList.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
    //endregion


}
