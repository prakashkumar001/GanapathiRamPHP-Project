package com.ganapathyram.theatre.model;

/**
 * Created by Prakash on 10/24/2017.
 */

public class Dashboard {

    public String moduleName;
    public String moduleUid;
    public String active;

    public Dashboard(String moduleName, String moduleUid, String active) {
        this.moduleName = moduleName;
        this.moduleUid = moduleUid;
        this.active = active;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getModuleUid() {
        return moduleUid;
    }

    public void setModuleUid(String moduleUid) {
        this.moduleUid = moduleUid;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }
}
