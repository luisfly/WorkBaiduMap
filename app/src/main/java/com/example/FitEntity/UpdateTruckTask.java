package com.example.FitEntity;

/**
 * 交货时，使用的传输交货完成的数据实体
 * 判断处 GSON 当选项为 null 时将不发送该项
 *
 */
public class UpdateTruckTask extends HttpMessageObject {
    private String PaperNO;
    private String StoreNO;
    private String StartDriver;
    private String SignDriver;
    private String DriverNO;
    private String TruckLoadNO;
    // 不需要传，fit 系统自动生成 LastUpdateTime,手动上传后会报错
    private String LastUpdateTime;
    private String DCNO;

    // 当前位置信息，在交货时需要验证，判断位置是否正确
    // 纬度
    private double Latitude;
    // 经度
    private double Longitude;

    public UpdateTruckTask() {

    }

    public UpdateTruckTask(String paperNO, String storeNO, String signDriver, String driverNO, String truckLoadNO, String lastUpdateTime, String DCNO) {
        PaperNO = paperNO;
        StoreNO = storeNO;
        SignDriver = signDriver;
        DriverNO = driverNO;
        TruckLoadNO = truckLoadNO;
        LastUpdateTime = lastUpdateTime;
        this.DCNO = DCNO;
    }

    public String getPaperNO() {
        return PaperNO;
    }

    public void setPaperNO(String paperNO) {
        PaperNO = paperNO;
    }

    public String getStoreNO() {
        return StoreNO;
    }

    public void setStoreNO(String storeNO) {
        StoreNO = storeNO;
    }

    public String getTruckLoadNO() {
        return TruckLoadNO;
    }

    public void setTruckLoadNO(String truckLoadNO) {
        TruckLoadNO = truckLoadNO;
    }

    public String getLastUpdateTime() {
        return LastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        LastUpdateTime = lastUpdateTime;
    }

    public String getSignDriver() {
        return SignDriver;
    }

    public void setSignDriver(String signDriver) {
        SignDriver = signDriver;
    }

    public String getDriverNO() {
        return DriverNO;
    }

    public void setDriverNO(String driverNO) {
        DriverNO = driverNO;
    }

    public String getDCNO() {
        return DCNO;
    }

    public void setDCNO(String DCNO) {
        this.DCNO = DCNO;
    }

    public String getStartDriver() {
        return StartDriver;
    }

    public void setStartDriver(String startDriver) {
        StartDriver = startDriver;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }
}
