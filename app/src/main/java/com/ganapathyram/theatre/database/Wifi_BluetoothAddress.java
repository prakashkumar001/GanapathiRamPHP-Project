package com.ganapathyram.theatre.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Prakash on 11/8/2017.
 */

@Entity
public class Wifi_BluetoothAddress {
    @Id
    Long id;
    public String bluetoothAddress;
    public String wifiAddress;
    @Generated(hash = 1327390957)
    public Wifi_BluetoothAddress(Long id, String bluetoothAddress,
            String wifiAddress) {
        this.id = id;
        this.bluetoothAddress = bluetoothAddress;
        this.wifiAddress = wifiAddress;
    }
    @Generated(hash = 2050988964)
    public Wifi_BluetoothAddress() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getBluetoothAddress() {
        return this.bluetoothAddress;
    }
    public void setBluetoothAddress(String bluetoothAddress) {
        this.bluetoothAddress = bluetoothAddress;
    }
    public String getWifiAddress() {
        return this.wifiAddress;
    }
    public void setWifiAddress(String wifiAddress) {
        this.wifiAddress = wifiAddress;
    }

}
