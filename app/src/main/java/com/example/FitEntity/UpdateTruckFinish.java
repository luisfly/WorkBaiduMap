package com.example.FitEntity;

import com.example.workbaidumap.HttpMessageObject;

/**
 * 交货时，使用的传输交货完成的数据实体
 */
public class UpdateTruckFinish extends HttpMessageObject {
    private String PaperNO;
    private String StoreNO;
    private String SignDriver;
    private String TruckLoadNO;
    private String LastUpdateTime;

    public UpdateTruckFinish() {

    }

    public UpdateTruckFinish(String paperNO, String storeNO, String signDriver, String truckLoadNO, String lastUpdateTime) {
        PaperNO = paperNO;
        StoreNO = storeNO;
        SignDriver = signDriver;
        TruckLoadNO = truckLoadNO;
        LastUpdateTime = lastUpdateTime;
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
}
