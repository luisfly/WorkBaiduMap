package com.example.FitEntity;

/**
 * 商品接收实体
 */
public class Goods extends HttpMessageObject {
    // 商品描述
    private String GoodsDesc;
    // 条形码
    private String Barcode;
    // 数量
    private int Qty;

    public Goods() {

    }

    public Goods(String goodsDesc, String barcode, int qty) {
        GoodsDesc = goodsDesc;
        Barcode = barcode;
        Qty = qty;
    }

    public String getGoodsDesc() {
        return GoodsDesc;
    }

    public void setGoodsDesc(String goodsDesc) {
        GoodsDesc = goodsDesc;
    }

    public String getBarcode() {
        return Barcode;
    }

    public void setBarcode(String barcode) {
        Barcode = barcode;
    }

    public int getQty() {
        return Qty;
    }

    public void setQty(int qty) {
        Qty = qty;
    }

    @Override
    public String getDetailText() {
        return "商品名称: " + GoodsDesc + "\n条码: " + Barcode + "\n数量: " + Qty + "\n";
    }
}
