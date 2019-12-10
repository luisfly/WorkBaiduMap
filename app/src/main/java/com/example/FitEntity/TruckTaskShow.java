package com.example.FitEntity;

/**
 * 发车时查询用接收数据实体
 */
public class TruckTaskShow extends HttpMessageObject {
    // 装车单号
    private String TruckPaperNO;
    // 装载号
    private String TruckLoadNO;
    // 装载数量
    private int LoadNum;
    // 门店去向
    private String StoreDesc;
    // 仓库编码
    private String DCNO;
    // 出发时间
    private String StartTime;
    // 商品种类数量
    private int GoodsSort;

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

    public int getGoodsSort() {
        return GoodsSort;
    }

    public void setGoodsSort(int goodsSort) {
        GoodsSort = goodsSort;
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
