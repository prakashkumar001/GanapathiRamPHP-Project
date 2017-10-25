package com.ganapathyram.theatre.model;

/**
 * Created by Prakash on 10/25/2017.
 */

public class Categories {
          public String categoryId;
    public String categoryName;
    public String  categoryUid;
    public String  active;

    public String getCategoryId() {
        return categoryId;
    }

    public Categories(String categoryId, String categoryName, String categoryUid, String active) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.categoryUid = categoryUid;
        this.active = active;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryUid() {
        return categoryUid;
    }

    public void setCategoryUid(String categoryUid) {
        this.categoryUid = categoryUid;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }
}
