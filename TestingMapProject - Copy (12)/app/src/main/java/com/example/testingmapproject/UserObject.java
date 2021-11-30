package com.example.testingmapproject;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class UserObject implements Parcelable {

    private String petName;
    private String breed;
    //private String startDate;
    private Integer mood;

    //private ArrayList <WalkObject> userWalks;


    public UserObject(String petName, String breed, Integer mood)
    {
        this.petName = petName;
        this.breed = breed;
        this.mood = mood;

        //this.userWalks = walkArray;
    }

    // getters


    protected UserObject(Parcel in) {
        petName = in.readString();
        breed = in.readString();
        if (in.readByte() == 0) {
            mood = null;
        } else {
            mood = in.readInt();
        }
    }

    public static final Creator<UserObject> CREATOR = new Creator<UserObject>() {
        @Override
        public UserObject createFromParcel(Parcel in) {
            return new UserObject(in);
        }

        @Override
        public UserObject[] newArray(int size) {
            return new UserObject[size];
        }
    };

    public String getPetName() {
        return petName;
    }

    public String getBreed() {
        return breed;
    }

    public Integer getMood() {
        return mood;
    }


    // setters

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public void setMood(Integer mood) {
        this.mood = mood;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(petName);
        parcel.writeString(breed);
        if (mood == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(mood);
        }
    }
}


