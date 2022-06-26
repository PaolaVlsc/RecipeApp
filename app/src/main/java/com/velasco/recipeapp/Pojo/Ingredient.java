package com.velasco.recipeapp.Pojo;

import android.os.Parcel;
import android.os.Parcelable;

public class Ingredient implements Parcelable {
    private int id;
    private String name;
    private float quantity;
    private String measurement;
    private int recipe;

    public Ingredient(int id, String name, float quantity, String measurement, int recipe) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.measurement = measurement;
        this.recipe = recipe;
    }

    protected Ingredient(Parcel in) {
        id = in.readInt();
        name = in.readString();
        quantity = in.readFloat();
        measurement = in.readString();
        recipe = in.readInt();
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public String getMeasurement() {
        return measurement;
    }

    public void setMeasurement(String measurement) {
        this.measurement = measurement;
    }

    public int getRecipe() {
        return recipe;
    }

    public void setRecipe(int recipe) {
        this.recipe = recipe;
    }

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeFloat(quantity);
        dest.writeString(measurement);
        dest.writeInt(recipe);
    }
}
