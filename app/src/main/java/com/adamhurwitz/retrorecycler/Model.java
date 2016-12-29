package com.adamhurwitz.retrorecycler;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Model {

    private ArrayList<Item> courses;

    public ArrayList<Item> getItems() {
        return courses;
    }

    public static class Item implements Parcelable {
        String title;
        String image;

        public String getTitle() {
            return title;
        }

        public String getImageUrl() {
            return image;
        }

        protected Item(Parcel in) {
            title = in.readString();
            image = in.readString();
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(title);
            parcel.writeString(image);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<Item> CREATOR = new Creator<Item>() {
            @Override
            public Item createFromParcel(Parcel in) {
                return new Item(in);
            }

            @Override
            public Item[] newArray(int size) {
                return new Item[size];
            }
        };
    }
}
