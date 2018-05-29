package com.msw.mesapp.bean.warehouse;

/**
 * Created by huang on 2017/11/1.
 */
public class MaterialInBean {
    private String id; //序号
    private String batchNumber; //批号
    private String materialStyle; //原料类型
    private String unit; //单位
    private String weight; //数量

    public MaterialInBean(String id, String batchNumber, String unit, String weight) {
        this.id = id;
        this.batchNumber = batchNumber;
        this.unit = unit;
        this.weight = weight;
    }

    public MaterialInBean(String id, String materialStyle, String batchNumber, String unit, String weight) {
        this.id = id;
        this.materialStyle = materialStyle;
        this.batchNumber = batchNumber;
        this.unit = unit;
        this.weight = weight;
    }

    public String getMaterialStyle() {
        return materialStyle;
    }

    public void setMaterialStyle(String materialStyle) {
        this.materialStyle = materialStyle;
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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

}
