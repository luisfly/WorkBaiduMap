package com.example.FitEntity;

/**
 * 关于定位的信息存储发送的类
 */
public class Location extends HttpMessageObject {
  // 纬度
  private double nLatitude;
  // 经度
  private double nLongitude;
  // 用户id
  private String sDriverNO;
  // 定位时间
  private String dLocationTime;

  public Location(double nLatitude, double nLongitude, String sDriverNO) {
    this.nLatitude = nLatitude;
    this.nLongitude = nLongitude;
    this.sDriverNO = sDriverNO;
  }

  /**
   *
   * @param latitude 纬度
   * @param longitude 经度
   * @param DriverNO 用户id
   * @param locationTime 定位时间
   */
  public Location(double latitude, double longitude, String DriverNO, String locationTime) {
    this.nLatitude = latitude;
    this.nLongitude = longitude;
    this.sDriverNO = DriverNO;
    this.dLocationTime = locationTime;
  }

  public double getLatitude() {
    return nLatitude;
  }

  public void setLatitude(double latitude) {
    this.nLatitude = latitude;
  }

  public double getLongitude() {
    return nLongitude;
  }

  public void setLongitude(double longitude) {
    this.nLongitude = longitude;
  }

  public String getUserID() {
    return sDriverNO;
  }

  public void setUserID(String userID) {
    this.sDriverNO = userID;
  }

  public String getLocationTime() {
    return dLocationTime;
  }

  public void setLocationTime(String locationTime) {
    this.dLocationTime = locationTime;
  }

}
