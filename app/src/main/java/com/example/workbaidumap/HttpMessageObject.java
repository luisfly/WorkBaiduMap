package com.example.workbaidumap;

/**
 * 数据发送附带信息统一管理类
 */
public class HttpMessageObject {

    // 发送数据时默认必须带的授权信息数据
    private String UserNO = "90000";
    private String AuthCode = "AE31GVSE453SEF4561EWES487654EFDS";
    // 执行的业务过程
    private String BusinessName;

    public void setUserNO(String userNO) {
        UserNO = userNO;
    }

    public void setAuthCode(String authCode) {
        AuthCode = authCode;
    }

    public void setBusinessName(String businessName) {
        BusinessName = businessName;
    }

    public String getUserNO() {
        return UserNO;
    }

    public String getAuthCode() {
        return AuthCode;
    }

    public String getBusinessName() {
        return BusinessName;
    }


}
