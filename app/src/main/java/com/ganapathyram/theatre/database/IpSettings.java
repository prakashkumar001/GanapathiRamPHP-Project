package com.ganapathyram.theatre.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Creative IT Works on 10-Jan-18.
 */

@Entity
public class IpSettings {
    @Id
    Long id;
    String baseIpAdress="";
    String firstClassPrinterIp="";
    String balConyClassPrinterIp="";
    @Generated(hash = 1524226058)
    public IpSettings(Long id, String baseIpAdress, String firstClassPrinterIp,
            String balConyClassPrinterIp) {
        this.id = id;
        this.baseIpAdress = baseIpAdress;
        this.firstClassPrinterIp = firstClassPrinterIp;
        this.balConyClassPrinterIp = balConyClassPrinterIp;
    }
    @Generated(hash = 3440445)
    public IpSettings() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getBaseIpAdress() {
        return this.baseIpAdress;
    }
    public void setBaseIpAdress(String baseIpAdress) {
        this.baseIpAdress = baseIpAdress;
    }
    public String getFirstClassPrinterIp() {
        return this.firstClassPrinterIp;
    }
    public void setFirstClassPrinterIp(String firstClassPrinterIp) {
        this.firstClassPrinterIp = firstClassPrinterIp;
    }
    public String getBalConyClassPrinterIp() {
        return this.balConyClassPrinterIp;
    }
    public void setBalConyClassPrinterIp(String balConyClassPrinterIp) {
        this.balConyClassPrinterIp = balConyClassPrinterIp;
    }
}
