package com.perfalcon.balav.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Baking implements Parcelable {
    int id;
    String name;
    List<Ingredient> ingredients ;
    List<Step>steps;
    int servings;
    String image;

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    @Override
    public String toString() {
        return "Baking{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", ingredients=" + ingredients +
                ", steps=" + steps +
                ", servings=" + servings +
                ", image='" + image + '\'' +
                '}';
    }



    public List<Ingredient> getIngredients() {
        return ingredients;
    }
    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
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

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt (this.id);
        dest.writeString (this.name);
        dest.writeList (this.ingredients);
        dest.writeList (this.steps);
        dest.writeInt (this.servings);
        dest.writeString (this.image);
    }

    public Baking() {
    }
 protected Baking(Parcel in) {
        this.id = in.readInt ();
        this.name = in.readString ();
        this.ingredients = new ArrayList<Ingredient> ();
        in.readList (this.ingredients, Ingredient.class.getClassLoader ());
        this.steps = new ArrayList<Step> ();
        in.readList (this.steps, Step.class.getClassLoader ());
        this.servings = in.readInt ();
        this.image = in.readString ();
    }

    public static final Parcelable.Creator<Baking> CREATOR = new Parcelable.Creator<Baking> () {
        @Override
        public Baking createFromParcel(Parcel source) {
            return new Baking (source);
        }

        @Override
        public Baking[] newArray(int size) {
            return new Baking[size];
        }
    };
}
