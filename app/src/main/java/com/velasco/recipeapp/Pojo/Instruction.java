package com.velasco.recipeapp.Pojo;

import android.os.Parcel;
import android.os.Parcelable;

public class Instruction implements Parcelable {

    private int id;
    private String description;
    private int recipe;


    public Instruction(int id, String description, int recipe) {
        this.id = id;
        this.description = description;
        this.recipe = recipe;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRecipe() {
        return recipe;
    }

    public void setRecipe(int recipe) {
        this.recipe = recipe;
    }

    protected Instruction(Parcel in) {
        id = in.readInt();
        description = in.readString();
        recipe = in.readInt();
    }


    public static final Creator<Instruction> CREATOR = new Creator<Instruction>() {
        @Override
        public Instruction createFromParcel(Parcel in) {
            return new Instruction(in);
        }

        @Override
        public Instruction[] newArray(int size) {
            return new Instruction[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(description);
        dest.writeInt(recipe);
    }
}
