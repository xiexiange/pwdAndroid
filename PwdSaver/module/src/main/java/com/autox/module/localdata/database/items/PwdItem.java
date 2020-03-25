package com.autox.module.localdata.database.items;

public class PwdItem {
    private String type;
    private String platform;
    private String account;
    private String pwd;
    private long saveTime;
    public PwdItem(String type, String platform, String account, String pwd, long saveTime) {
        this.type = type;
        this.platform = platform;
        this.account = account;
        this.pwd = pwd;
        this.saveTime = saveTime;
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

}
