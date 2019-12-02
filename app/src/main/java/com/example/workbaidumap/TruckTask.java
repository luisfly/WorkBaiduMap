package com.example.workbaidumap;

public class TruckTask extends HttpMessageObject {
    private String TruckPaperNO;
    private String DriverNO;
    private String TruckLoadNO;
    private String Driver;
    private String DCNO;
    private String SiteNO;
    private String StartTime;
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
}
