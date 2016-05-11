package com.hpp.foodminder.models;

import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;

/**
 * Created by Sharjeel on 10/05/2016.
 */
public class CuisineModel implements Serializable {

    @DatabaseField(generatedId = true, allowGeneratedIdInsert = true)
    int Id;

    @DatabaseField(columnName = "Name")
    String Name;

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
}
