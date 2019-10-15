package com.example.workbaidumap;

import java.util.Date;

/**
 * 关于定位的信息存储发送的类
 */
public class Location {
    // 纬度
    private double latitude;
    // 经度
    private double longitude;
    // 用户id
    private String userID;
    // 定位时间
    private Date locationTime;

    /**
     *
     * @param latitude 纬度
     * @param longitude 经度
     * @param userID 用户id
     * @param locationTime 定位时间
     */
    public Location(double latitude, double longitude, String userID, Date locationTime) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.userID = userID;
        this.locationTime = locationTime;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Date getLocationTime() {
        return locationTime;
    }

    public void setLocationTime(Date locationTime) {
        this.locationTime = locationTime;
    }
}
