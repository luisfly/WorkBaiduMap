package com.example.FitEntity;

/**
 * 门店数据实体
 */
public class Store extends HttpMessageObject {
    private String sStoreNO;
    private String sStoreDesc;
    private String sDCNO;

    public Store() {

    }

    public Store(String sStoreNO, String sStoreDesc) {
        this.sStoreNO = sStoreNO;
        this.sStoreDesc = sStoreDesc;
    }

    public Store(String sStoreNO, String sStoreDesc, String sDCNO) {
        this.sStoreNO = sStoreNO;
        this.sStoreDesc = sStoreDesc;
        this.sDCNO = sDCNO;
    }

    public String getsStoreNO() {
        return sStoreNO;
    }

    public void setsStoreNO(String sStoreNO) {
        this.sStoreNO = sStoreNO;
    }

    public String getsStoreDesc() {
        return sStoreDesc;
    }

    public void setsStoreDesc(String sStoreDesc) {
        this.sStoreDesc = sStoreDesc;
    }

    public String getsDCNO() {
        return sDCNO;
    }

    public void setsDCNO(String sDCNO) {
        this.sDCNO = sDCNO;
    }
}
