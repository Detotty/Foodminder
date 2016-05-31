package com.hpp.foodminder.models;

import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;

/**
 * Created by Sharjeel on 06/05/2016.
 */
public class RestaurantModel implements Serializable {

    @DatabaseField(columnName = "RestName")
    private String RestName;

    @DatabaseField(columnName = "Cuisine")
    private String Cuisine;

    @DatabaseField(columnName = "Address")
    private String Address;

    @DatabaseField(columnName = "Phone")
    private String Phone;

    @DatabaseField(generatedId = true, allowGeneratedIdInsert = true)
    private int Id;

    public RestaurantModel() {
    }

    public RestaurantModel(String RestName, String cuisine, String address, String phone) {
        this.RestName = RestName;
        this.Cuisine = cuisine;
        this.Address = address;
        this.Phone = phone;
    }
    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getName() {
        return RestName;
    }

    public void setName(String RestName) {
        this.RestName = RestName;
    }

    public String getCuisine() {
        return Cuisine;
    }

    public void setCuisine(String Cuisine) {
        this.Cuisine = Cuisine;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String genre) {
        this.Address = genre;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String Phone) {
        this.Phone = Phone;
    }
}
