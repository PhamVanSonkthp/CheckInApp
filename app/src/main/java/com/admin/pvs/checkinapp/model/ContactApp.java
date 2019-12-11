package com.admin.pvs.checkinapp.model;

import android.graphics.drawable.Drawable;

public class ContactApp {
    private String appname = "";
    private String pname = "";
    private String versionName = "";
    private int versionCode = 0;
    private String path = "";
    private Drawable icon;
    private String ram;
    private String sizeApp;
    private String targetSdkVersion;
    private String permisions;
    private String minSDK;
    private String support;

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getRam() {
        return ram;
    }

    public void setRam(String ram) {
        this.ram = ram;
    }

    public String getSizeApp() {
        return sizeApp;
    }

    public void setSizeApp(String sizeApp) {
        this.sizeApp = sizeApp;
    }

    public String getTargetSdkVersion() {
        return targetSdkVersion;
    }

    public void setTargetSdkVersion(String targetSdkVersion) {
        this.targetSdkVersion = targetSdkVersion;
    }

    public String getPermisions() {
        return permisions;
    }

    public void setPermisions(String permisions) {
        this.permisions = permisions;
    }

    public String getMinSDK() {
        return minSDK;
    }

    public void setMinSDK(String minSDK) {
        this.minSDK = minSDK;
    }

    public String getSupport() {
        return support;
    }

    public void setSupport(String support) {
        this.support = support;
    }

    public ContactApp(String appname, String pname, String versionName, int versionCode, String path, Drawable icon, String ram, String sizeApp, String targetSdkVersion, String permisions, String minSDK, String support) {
        this.appname = appname;
        this.pname = pname;
        this.versionName = versionName;
        this.versionCode = versionCode;
        this.path = path;
        this.icon = icon;
        this.ram = ram;
        this.sizeApp = sizeApp;
        this.targetSdkVersion = targetSdkVersion;
        this.permisions = permisions;
        this.minSDK = minSDK;
        this.support = support;
    }
}
