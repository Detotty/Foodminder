package com.hpp.foodminder.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.hpp.foodminder.MainActivity;
import com.hpp.foodminder.R;
import com.hpp.foodminder.models.CuisineModel;
import com.hpp.foodminder.models.ItemsModel;
import com.hpp.foodminder.models.RestaurantModel;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by Sharjeel on 11/30/2015.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    /************************************************
     * Suggested Copy/Paste code. Everything from here to the done block.
     ************************************************/

    private static final String DATABASE_NAME = "favorites.db";
    private static final int DATABASE_VERSION = 1;

    private Dao<CuisineModel, Integer> cuisineModelIntegerDao;
    private Dao<RestaurantModel, Integer> restaurantModelIntegerDao;
    private Dao<ItemsModel, Integer> itemsModelIntegerDao;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
    }

    /************************************************
     * Suggested Copy/Paste Done
     ************************************************/

    @Override
    public void onCreate(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource) {
        try {

            // Create tables. This onCreate() method will be invoked only once of the application life time i.e. the first time when the application starts.

            TableUtils.createTableIfNotExists(connectionSource, CuisineModel.class);
            TableUtils.createTableIfNotExists(connectionSource, RestaurantModel.class);
            TableUtils.createTableIfNotExists(connectionSource, ItemsModel.class);



        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Unable to create datbases", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource, int oldVer, int newVer) {
        try {
            // In case of change in database of next version of application, please increase the value of DATABASE_VERSION variable, then this method will be invoked
            //automatically. Developer needs to handle the upgrade logic here, i.e. create a new table or a new column to an existing table, take the backups of the
            // existing database etc.
            TableUtils.dropTable(connectionSource, CuisineModel.class, true);
            TableUtils.dropTable(connectionSource, RestaurantModel.class, true);
            TableUtils.dropTable(connectionSource, ItemsModel.class, true);


            onCreate(sqliteDatabase, connectionSource);

        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Unable to upgrade database from version " + oldVer + " to new "
                    + newVer, e);
        }
    }

    // Create the getDao methods of all database tables to access those from android code.
    // Insert, delete, read, update everything will be happened through DAOs

    public Dao<CuisineModel, Integer> getCuisineDao() throws SQLException {
        if (cuisineModelIntegerDao == null) {
            cuisineModelIntegerDao = getDao(CuisineModel.class);
        }
        return cuisineModelIntegerDao;
    }

    public Dao<RestaurantModel, Integer> getRestaurantDao() throws SQLException {
        if (restaurantModelIntegerDao == null) {
            restaurantModelIntegerDao = getDao(RestaurantModel.class);
        }
        return restaurantModelIntegerDao;
    }

    public Dao<ItemsModel, Integer> getItemsDao() throws SQLException {
        if (itemsModelIntegerDao == null) {
            itemsModelIntegerDao = getDao(ItemsModel.class);
        }
        return itemsModelIntegerDao;
    }

    public void close() {
        super.close();
        /*this.cuisineModelIntegerDao = null;
        this.restaurantModelIntegerDao = null;
        this.itemsModelIntegerDao = null;*/
    }

}
