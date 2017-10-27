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
    public Long pin;
    public String status;
    @Generated(hash = 1783392859)
    public Login(Long pin, String status) {
        this.pin = pin;
        this.status = status;
    }
    @Generated(hash = 1827378950)
    public Login() {
    }
    public Long getPin() {
        return this.pin;
    }
    public void setPin(Long pin) {
        this.pin = pin;
    }
    public String getStatus() {
        return this.status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}
