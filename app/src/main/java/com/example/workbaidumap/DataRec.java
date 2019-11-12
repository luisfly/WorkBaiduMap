package com.example.workbaidumap;


import java.util.List;

/**
 * 用于接收 fit 传送过来的 json 格式数据
 */
public class DataRec {
    private int isSuccess = 0;
    private String sCode = null;
    private String sMessage = null;
    private List<Content> sContent;

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

    public List<Content> getsContent() {
        return sContent;
    }

    public void setsContent(List<Content> sContent) {
        this.sContent = sContent;
    }

    /* 内部类 */
    public class Content{
        private int index;
        private String tableName;
        // 接收实际传输的回传数据
        private List<Driver> data;

        public Content() {

        }

        public Content(int index, String tableName, List<Driver> data) {
            this.index = index;
            this.tableName = tableName;
            this.data = data;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public String getTableName() {
            return tableName;
        }

        public void setTableName(String tableName) {
            this.tableName = tableName;
        }

        public List<Driver> getData() {
            return data;
        }

        public void setData(List<Driver> data) {
            this.data = data;
        }
    }
}
