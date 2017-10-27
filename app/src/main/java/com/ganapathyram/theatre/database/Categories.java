package com.ganapathyram.theatre.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Prakash on 10/25/2017.
 */

@Entity
public class Categories {
    @Id
    public Long categoryId;

    public String categoryName;
    public String categoryUid;
    public String active;
    @Generated(hash = 250281246)
    public Categories(Long categoryId, String categoryName, String categoryUid,
            String active) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.categoryUid = categoryUid;
        this.active = active;
    }
    @Generated(hash = 267348489)
    public Categories() {
    }
    public Long getCategoryId() {
        return this.categoryId;
    }
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
    public String getCategoryName() {
        return this.categoryName;
    }
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    public String getCategoryUid() {
        return this.categoryUid;
    }
    public void setCategoryUid(String categoryUid) {
        this.categoryUid = categoryUid;
    }
    public String getActive() {
        return this.active;
    }
    public void setActive(String active) {
        this.active = active;
    }

}
