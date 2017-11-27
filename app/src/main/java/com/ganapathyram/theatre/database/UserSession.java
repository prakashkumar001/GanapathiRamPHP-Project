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
    public String sessionId;
    public String userId;
    @Generated(hash = 63632947)
    public UserSession(Long id, String startTime, String endtime, String sessionId,
            String userId) {
        this.id = id;
        this.startTime = startTime;
        this.endtime = endtime;
        this.sessionId = sessionId;
        this.userId = userId;
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
    public String getSessionId() {
        return this.sessionId;
    }
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
    public String getUserId() {
        return this.userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
}
