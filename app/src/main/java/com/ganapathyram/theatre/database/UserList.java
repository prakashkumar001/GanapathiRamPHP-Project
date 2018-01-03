package com.ganapathyram.theatre.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Prakash on 1/3/2018.
 */

@Entity
public class UserList {
    @Id
    public String userId;
    public String userName;
    @Generated(hash = 1701642717)
    public UserList(String userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }
    @Generated(hash = 2088493246)
    public UserList() {
    }
    public String getUserId() {
        return this.userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getUserName() {
        return this.userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

}
