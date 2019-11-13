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
  private String DriverNO;
  // 定位时间
  private String locationTime;

  // fit 发送数据时默认必须带的授权信息数据
  private String UserNO = "90000";
  private String AuthCode = "AE31GVSE453SEF4561EWES487654EFDS";
  // 执行的业务过程
  private String BusinessName = "AddLocation";

  /**
   *
   * @param latitude 纬度
   * @param longitude 经度
   * @param DriverNO 用户id
   * @param locationTime 定位时间
   */
  public Location(double latitude, double longitude, String DriverNO, String locationTime) {
    this.latitude = latitude;
    this.longitude = longitude;
    this.DriverNO = DriverNO;
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
    return DriverNO;
  }

  public void setUserID(String userID) {
    this.DriverNO = userID;
  }

  public String getLocationTime() {
    return locationTime;
  }

  public void setLocationTime(String locationTime) {
    this.locationTime = locationTime;
  }

}
