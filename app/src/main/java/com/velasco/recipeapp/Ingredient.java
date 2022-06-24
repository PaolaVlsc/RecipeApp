package com.velasco.recipeapp;

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
