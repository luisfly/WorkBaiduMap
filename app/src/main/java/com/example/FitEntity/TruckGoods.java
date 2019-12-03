package com.example.FitEntity;

/**
 * 运输中商品明细接收类
 */
public class TruckGoods extends HttpMessageObject {
    private String TruckPaperNO;
    private String TruckLoadNO;
    private String GoodsNO;
    private double Qty;
    private String StoreNO;
    private String DCNO;
    private double CaseUnits;

    public TruckGoods() {

    }

    public TruckGoods(String paperNO, String loadNO, String goodsNO, double nQty, String sStoreNO, String dcNO, double caseUnits) {
        TruckPaperNO = paperNO;
        TruckLoadNO = loadNO;
        GoodsNO = goodsNO;
        Qty = nQty;
        StoreNO = sStoreNO;
        DCNO = dcNO;
        CaseUnits = caseUnits;
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

    public String getGoodsNO() {
        return GoodsNO;
    }

    public void setGoodsNO(String goodsNO) {
        GoodsNO = goodsNO;
    }

    public double getQty() {
        return Qty;
    }

    public void setQty(double qty) {
        Qty = qty;
    }

    public String getStoreNO() {
        return StoreNO;
    }

    public void setStoreNO(String storeNO) {
        StoreNO = storeNO;
    }

    public String getDCNO() {
        return DCNO;
    }

    public void setDCNO(String DCNO) {
        this.DCNO = DCNO;
    }

    public double getCaseUnits() {
        return CaseUnits;
    }

    public void setCaseUnits(double caseUnits) {
        CaseUnits = caseUnits;
    }
}
