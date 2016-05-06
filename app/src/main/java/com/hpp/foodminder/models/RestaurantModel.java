package com.hpp.foodminder.models;

import java.io.Serializable;

/**
 * Created by Sharjeel on 06/05/2016.
 */
public class RestaurantModel implements Serializable {

    private String Name;
    private String Cuisine;
    private String Address;
    private String Phone;
    private int Id;

    public RestaurantModel() {
    }

    public RestaurantModel(String name, String cuisine, String address, String phone) {
        this.Name = name;
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
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
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
