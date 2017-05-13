package com.adamhurwitz.retrorecycler;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Model {

    private ArrayList<Course> courses;

    public ArrayList<Course> getCourses() {
        return courses;
    }

    public static class Course implements Parcelable {
        String title;
        String image;
        String homepage;

        public String getTitle() {return title;}

        public String getImageUrl() {
            return image;
        }

        public String getHomepage() {return homepage;}

        protected Course(Parcel in) {
            title = in.readString();
            image = in.readString();
            homepage = in.readString();
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(title);
            parcel.writeString(image);
            parcel.writeString(homepage);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<Course> CREATOR = new Creator<Course>() {
            @Override
            public Course createFromParcel(Parcel in) {
                return new Course(in);
            }

            @Override
            public Course[] newArray(int size) {
                return new Course[size];
            }
        };
    }
}
