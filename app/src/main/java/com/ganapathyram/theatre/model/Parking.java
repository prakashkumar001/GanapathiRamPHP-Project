package com.ganapathyram.theatre.model;

/**
 * Created by Prakash on 9/21/2017.
 */

public class Parking {
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
}
