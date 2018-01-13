package com.ganapathyram.theatre.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Creative IT Works on 27-Oct-17.
 */

@Entity
public class Login {
    @Id
    public Long id;
    public String userName;
    public String pin;
    public String status;
    public String loginType;
    @Generated(hash = 277687945)
    public Login(Long id, String userName, String pin, String status,
            String loginType) {
        this.id = id;
        this.userName = userName;
        this.pin = pin;
        this.status = status;
        this.loginType = loginType;
    }
    @Generated(hash = 1827378950)
    public Login() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUserName() {
        return this.userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getPin() {
        return this.pin;
    }
    public void setPin(String pin) {
        this.pin = pin;
    }
    public String getStatus() {
        return this.status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getLoginType() {
        return this.loginType;
    }
    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }
}