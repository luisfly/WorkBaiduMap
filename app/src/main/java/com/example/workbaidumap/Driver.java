package com.example.workbaidumap;

import java.io.Serializable;

/**
 * 司机信息
 */
public class Driver implements Serializable {
    // 司机账号
    private String sDriverNO;
    // 司机账号密码
    private String sPassword;
    // 司机姓名
    private String sDriverName;

    // 发送数据时默认必须带的授权信息数据
    private String UserNO = "90000";
    private String AuthCode = "AE31GVSE453SEF4561EWES487654EFDS";
    // 执行的业务过程
    private String BusinessName = "Login";

    public Driver(String driverNO, String password) {
        sDriverNO = driverNO;
        sPassword = password;
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
