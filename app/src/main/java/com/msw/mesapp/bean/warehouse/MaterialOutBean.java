package com.msw.mesapp.bean.warehouse;

import com.bin.david.form.annotation.SmartColumn;

/**
 * Created by huang on 2017/11/1.
 */
public class MaterialOutBean {
    private String result; //审核结果
    private String suggestion; //批号
    private String auditor; //单位
    private String date; //数量

    private String id;
    private String rawType;
    private String batchNumber;
    private String unit;
    private String weight;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRawType() {
        return rawType;
    }

    public void setRawType(String rawType) {
        this.rawType = rawType;
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

    public MaterialOutBean(String auditor, String result, String suggestion, String date) {
        this.result = result;
        this.suggestion = suggestion;
        this.auditor = auditor;
        this.date = date;
    }

    public MaterialOutBean(String id, String rawType, String batchNumber, String unit, String weight) {
        this.id = id;
        this.rawType = rawType;
        this.batchNumber = batchNumber;
        this.unit = unit;
        this.weight = weight;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }

    public String getAuditor() {
        return auditor;
    }

    public void setAuditor(String auditor) {
        this.auditor = auditor;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
