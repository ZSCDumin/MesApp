package com.msw.mesapp.bean.warehouse;

public class ProductGodown {
    private String batchNumber;
    private String weight;

    public ProductGodown(String batchNumber, String weight) {
        this.batchNumber = batchNumber;
        this.weight = weight;
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
}
