package com.msw.mesapp.bean.warehouse;

public class ProductOutBean {

    private String id;
    private String batchNumber;
    private String outStatus;

    public ProductOutBean(String id, String batchNumber, String outStatus) {
        this.id = id;
        this.batchNumber = batchNumber;
        this.outStatus = outStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public String getOutStatus() {
        return outStatus;
    }

    public void setOutStatus(String outStatus) {
        this.outStatus = outStatus;
    }
}
