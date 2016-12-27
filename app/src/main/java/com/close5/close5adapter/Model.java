package com.close5.close5adapter;

import java.util.ArrayList;

public class Model {
    private ArrayList<Item> courses;

    public ArrayList<Item> getItems() {
        return courses;
    }

    public static class Item {
        String title;
        String image;

        public String getTitle() {
            return title;
        }

        public String getImageUrl() {
            return image;
        }

    }
}
