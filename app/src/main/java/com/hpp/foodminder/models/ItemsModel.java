package com.hpp.foodminder.models;

import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;

/**
 * Created by Sharjeel on 6/9/2016.
 */

public class ItemsModel implements Serializable {

    @DatabaseField(columnName = "ItemName")
    private String ItemName;

    @DatabaseField(columnName = "Rating")
    private String Rating;

    @DatabaseField(columnName = "Notes")
    private String Notes;

    @DatabaseField(columnName = "Pic")
    private String Pic;

    @DatabaseField(generatedId = true, allowGeneratedIdInsert = true)
    private int ItemId;

    @DatabaseField(foreign = true)
    private int Id;

    public ItemsModel() {
    }


    public int getItemId() {
        return ItemId;
    }

    public void setItemId(int ItemId) {
        this.ItemId = ItemId;
    }

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getName() {
        return ItemName;
    }

    public void setName(String ItemName) {
        this.ItemName = ItemName;
    }

    public String getRating() {
        return Rating;
    }

    public void setRating(String Rating) {
        this.Rating = Rating;
    }

    public String getNotes() {
        return Notes;
    }

    public void setNotes(String Notes) {
        this.Notes = Notes;
    }

    public String getPic() {
        return Pic;
    }

    public void setPic(String Pic) {
        this.Pic = Pic;
    }
}

