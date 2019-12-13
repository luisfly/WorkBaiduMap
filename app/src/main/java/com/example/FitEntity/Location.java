package com.example.FitEntity;

/**
 * 关于定位的信息存储发送的类
 */
public class Location extends HttpMessageObject {
  // 纬度
  private double Latitude;
  // 经度
  private double Longitude;
  // 用户id
  private String DriverNO;
  // 定位时间
  private String LocationTime;

  public Location(double nLatitude, double nLongitude, String sDriverNO) {
    this.Latitude = nLatitude;
    this.Longitude = nLongitude;
    this.DriverNO = sDriverNO;
  }

  /**
   *
   * @param latitude 纬度
   * @param longitude 经度
   * @param DriverNO 用户id
   * @param locationTime 定位时间
   */
  public Location(double latitude, double longitude, String DriverNO, String locationTime) {
    this.Latitude = latitude;
    this.Longitude = longitude;
    this.DriverNO = DriverNO;
    this.LocationTime = locationTime;
  }

  public double getLatitude() {
    return Latitude;
  }

  public void setLatitude(double latitude) {
    this.Latitude = latitude;
  }

  public double getLongitude() {
    return Longitude;
  }

  public void setLongitude(double longitude) {
    this.Longitude = longitude;
  }

  public String getUserID() {
    return DriverNO;
  }

  public void setUserID(String userID) {
    this.DriverNO = userID;
  }

  public String getLocationTime() {
    return LocationTime;
  }

  public void setLocationTime(String locationTime) {
    this.LocationTime = locationTime;
  }

}
