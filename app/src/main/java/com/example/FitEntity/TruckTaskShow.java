package com.example.FitEntity;

/**
 * 发车时查询用接收数据实体
 */
public class TruckTaskShow extends HttpMessageObject {
    // 装车单号
    public String TruckPaperNO;
    // 装载号
    public String TruckLoadNO;
    // 装载数量
    public int LoadNum;
    // 门店去向
    public String StoreDesc;
    // 仓库编码
    public String DCNO;
    // 出发时间
    public String StartTime;

    public TruckTaskShow() {

    }

    public TruckTaskShow(String truckPaperNO, String truckLoadNO, int loadNum, String storeDesc,
            String dcNO, String startTime) {
        TruckPaperNO = truckPaperNO;
        TruckLoadNO = truckLoadNO;
        LoadNum = loadNum;
        StoreDesc = storeDesc;
        DCNO = dcNO;
        StartTime = startTime;
    }

    public String getTruckPaperNO() {
        return TruckPaperNO;
    }

    public void setTruckPaperNO(String truckPaperNO) {
        TruckPaperNO = truckPaperNO;
    }

    public String getTruckLoadNO() {
        return TruckLoadNO;
    }

    public void setTruckLoadNO(String truckLoadNO) {
        TruckLoadNO = truckLoadNO;
    }

    public int getLoadNum() {
        return LoadNum;
    }

    public void setLoadNum(int loadNum) {
        LoadNum = loadNum;
    }

    public String getStoreDesc() {
        return StoreDesc;
    }

    public void setStoreDesc(String storeDesc) {
        StoreDesc = storeDesc;
    }

    public String getDCNO() {
        return DCNO;
    }

    public void setDCNO(String DCNO) {
        this.DCNO = DCNO;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    @Override
    public String getTitleText() {
        return "装载号：" + TruckLoadNO;
    }

    @Override
    public String getDetailText() {
        return "目标门店：" + StoreDesc + "\n";
    }
}
