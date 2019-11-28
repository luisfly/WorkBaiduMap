package com.example.workbaidumap;

import java.io.Serializable;

/**
 * 司机信息
 */
public class Driver extends HttpMessageObject implements Serializable {
    // 司机账号
    private String sDriverNO;
    // 司机账号密码
    private String sPassword;
    // 司机姓名
    private String sDriverName;

    public Driver() {

    }

    public Driver(String driverNO, String password) {
        sDriverNO = driverNO;
        sPassword = password;
    }

    public Driver(String sDriverNO, String sPassword, String sDriverName) {
        this.sDriverNO = sDriverNO;
        this.sPassword = sPassword;
        this.sDriverName = sDriverName;
    }

    public String getDriverNO() {
        return sDriverNO;
    }

    public void setDriverNO(String driverNO) {
        sDriverNO = driverNO;
    }

    public String getPassword() {
        return sPassword;
    }

    public void setPassword(String password) {
        sPassword = password;
    }

    public String getDriverName() {
        return sDriverName;
    }

    public void setDriverName(String driverName) {
        sDriverName = driverName;
    }
}
