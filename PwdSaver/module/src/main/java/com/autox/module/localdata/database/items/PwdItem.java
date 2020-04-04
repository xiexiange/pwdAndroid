package com.autox.module.localdata.database.items;

import java.io.Serializable;

public class PwdItem implements Serializable {
    private String type;
    private String platform;
    private String account;
    private String pwd;
    private String note;
    private long saveTime;
    public PwdItem(String type, String platform, String account, String pwd, long saveTime, String note) {
        this.type = type;
        this.platform = platform;
        this.account = account;
        this.pwd = pwd;
        this.saveTime = saveTime;
        this.note = note;
    }
    public String type() {
        return type;
    }
    public String platform() {
        return platform;
    }
    public String account() {
        return account;
    }
    public String pwd() {
        return pwd;
    }
    public long saveTime() {
        return saveTime;
    }
    public String note() {
        return note;
    }

}
