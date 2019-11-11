package com.example.workbaidumap;

/**
 * 司机信息
 */
public class Driver {
    // 司机账号
    private String DriverNO;
    // 司机账号密码
    private String Password;
    // 司机姓名
    private String DriverName;

    // 发送数据时默认必须带的授权信息数据
    private String UserNO = "90000";
    private String AuthCode = "AE31GVSE453SEF4561EWES487654EFDS";
    // 执行的业务过程
    private String BusinessName = "Login";

    public Driver(String driverNO, String password) {
        DriverNO = driverNO;
        Password = password;
    }

    public String getDriverNO() {
        return DriverNO;
    }

    public void setDriverNO(String driverNO) {
        DriverNO = driverNO;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getDriverName() {
        return DriverName;
    }

    public void setDriverName(String driverName) {
        DriverName = driverName;
    }

    public void setUserNO(String userNO) {
        UserNO = userNO;
    }

    public void setAuthCode(String authCode) {
        AuthCode = authCode;
    }

    public void setBusinessName(String businessName) {
        BusinessName = businessName;
    }
}
