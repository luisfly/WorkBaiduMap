package com.example.control;

/**
 * 卡片式列表使用的对象
 * 要接入卡片式弹窗必须实现本接口
 */
public interface NerItem {

    /**
     * 设置标题文本
     * @return
     */
    public String getTitleText();

    /**
     * 设置明细文本
     * @return
     */
    public String getDetailText();
}
