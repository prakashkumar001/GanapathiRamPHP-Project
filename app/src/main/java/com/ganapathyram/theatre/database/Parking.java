package com.ganapathyram.theatre.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Prakash on 9/21/2017.
 */

@Entity
public class Parking {
    @Id
    public String name;
    public int icon;
    public String startTime;
    public String chargesToBePaid;
    public String venueId;

    public Parking(String name, int icon) {
        this.name = name;
        this.icon = icon;
    }

    public Parking(String name, String startTime, String chargesToBePaid, String venueId) {
        this.name = name;
        this.startTime = startTime;
        this.chargesToBePaid = chargesToBePaid;
        this.venueId = venueId;
    }

    @Generated(hash = 600870753)
    public Parking(String name, int icon, String startTime, String chargesToBePaid,
            String venueId) {
        this.name = name;
        this.icon = icon;
        this.startTime = startTime;
        this.chargesToBePaid = chargesToBePaid;
        this.venueId = venueId;
    }

    @Generated(hash = 1089554184)
    public Parking() {
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIcon() {
        return this.icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getStartTime() {
        return this.startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getChargesToBePaid() {
        return this.chargesToBePaid;
    }

    public void setChargesToBePaid(String chargesToBePaid) {
        this.chargesToBePaid = chargesToBePaid;
    }

    public String getVenueId() {
        return this.venueId;
    }

    public void setVenueId(String venueId) {
        this.venueId = venueId;
    }
}
