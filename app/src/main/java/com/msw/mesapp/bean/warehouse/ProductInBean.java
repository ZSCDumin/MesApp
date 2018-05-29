package com.msw.mesapp.bean.warehouse;

public class ProductInBean {
    private String id;
    private String batchNumber;
    private String weight;
    private String unit;

    public ProductInBean(String id, String batchNumber, String weight, String unit) {
        this.id = id;
        this.batchNumber = batchNumber;
        this.weight = weight;
        this.unit = unit;
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

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
