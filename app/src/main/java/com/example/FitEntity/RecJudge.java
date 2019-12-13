package com.example.FitEntity;

/**
 * 在正式接收数据前，预接收数据，判断 fit 数据获取是否成功
 * 然后再通过
 */
public class RecJudge {
    private int isSuccess;
    private String sCode;
    private String sMessage;

    public RecJudge(int isSuccess, String sCode, String sMessage) {
        this.isSuccess = isSuccess;
        this.sCode = sCode;
        this.sMessage = sMessage;
    }

    public RecJudge(int isSuccess) {
        this.isSuccess = isSuccess;
    }

    public int getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(int isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getsCode() {
        return sCode;
    }

    public void setsCode(String sCode) {
        this.sCode = sCode;
    }

    public String getsMessage() {
        return sMessage;
    }

    public void setsMessage(String sMessage) {
        this.sMessage = sMessage;
    }
}
