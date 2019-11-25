package com.example.workbaidumap;


/**
 * 仓库数据实体类
 * 主要是接收从数据库传来的仓库资料
 */
public class DCEntity extends HttpMessageObject {
    public String sDCNO;
    public String sDCDesc;

    public DCEntity() {

    }

    public DCEntity(String sDCNO, String sDCDesc) {
        this.sDCNO = sDCNO;
        this.sDCDesc = sDCDesc;
    }

    public String getsDCNO() {
        return sDCNO;
    }

    public void setsDCNO(String sDCNO) {
        this.sDCNO = sDCNO;
    }

    public String getsDCDesc() {
        return sDCDesc;
    }

    public void setsDCDesc(String sDCDesc) {
        this.sDCDesc = sDCDesc;
    }
}
