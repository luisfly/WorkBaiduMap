package com.example.FitEntity;

import com.example.control.NerItem;

/**
 * 装载号数据接收类
 * 需要实现getTitleText以及getDetailText的方法
 */
public class TruckTask extends HttpMessageObject {
    // 装载单号
    private String TruckPaperNO;
    // 装载号
    private String TruckLoadNO;
    // 运输司机编号
    private String DriverNO;
    // 运输司机名
    private String Driver;
    // 起点仓
    private String DCNO;
    // 中转仓
    private String SiteNO;
    // 发车时间，对应数据库的createDate
    private String StartTime;
    // 结束时间，对应数据库的changeDate
    private String EndTime;

    public TruckTask() {

    }

    public TruckTask(String truckPaperNO, String driverNO, String truckLoadNO, String driver, String startTime, String endTime,
        String dcNO, String siteNO) {
        TruckPaperNO = truckPaperNO;
        DriverNO = driverNO;
        TruckLoadNO = truckLoadNO;
        Driver = driver;
        DCNO = dcNO;
        SiteNO = siteNO;
        StartTime = startTime;
        EndTime = endTime;
    }

    public String getTruckPaperNO() {
        return TruckPaperNO;
    }

    public void setTruckPaperNO(String truckPaperNO) {
        TruckPaperNO = truckPaperNO;
    }

    public String getDriverNO() {
        return DriverNO;
    }

    public void setDriverNO(String driverNO) {
        DriverNO = driverNO;
    }

    public String getTruckLoadNO() {
        return TruckLoadNO;
    }

    public void setTruckLoadNO(String truckLoadNO) {
        TruckLoadNO = truckLoadNO;
    }

    public String getDriver() {
        return Driver;
    }

    public void setDriver(String driver) {
        Driver = driver;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    public String getDCNO() {
        return DCNO;
    }

    public void setDCNO(String dcNO) {
        DCNO = dcNO;
    }

    public String getSiteNO() {
        return SiteNO;
    }

    public void setSiteNO(String siteNO) {
        SiteNO = siteNO;
    }

    @Override
    public String getTitleText() {
        return "装载单号: " + TruckPaperNO;
    }

    @Override
    public String getDetailText() {
        return "装载号: " + TruckLoadNO + "\n起点仓: " + DCNO;
    }
}
