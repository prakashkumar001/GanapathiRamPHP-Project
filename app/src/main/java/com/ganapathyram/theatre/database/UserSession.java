package com.ganapathyram.theatre.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Prakash on 11/21/2017.
 */

@Entity
public class UserSession {
    @Id
    Long id;
    public String startTime;
    public String endtime;
    @Generated(hash = 1773628469)
    public UserSession(Long id, String startTime, String endtime) {
        this.id = id;
        this.startTime = startTime;
        this.endtime = endtime;
    }
    @Generated(hash = 875065627)
    public UserSession() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getStartTime() {
        return this.startTime;
    }
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
    public String getEndtime() {
        return this.endtime;
    }
    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }
}
