package com.msw.mesapp.bean.warehouse;

import java.util.List;

/**
 * 产品入库实体类
 * <p>
 * model
 * departmentCode
 * weight
 * payerCode
 * productGodowns
 */
public class ProductGodownBean {

    private String model;
    private String departmentCode;
    private String weight;
    private String payerCode;
    private List<ProductGodown> productGodowns;

    public ProductGodownBean(String model, String departmentCode, String weight, String payerCode, List<ProductGodown> productGodowns) {
        this.model = model;
        this.departmentCode = departmentCode;
        this.weight = weight;
        this.payerCode = payerCode;
        this.productGodowns = productGodowns;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getPayerCode() {
        return payerCode;
    }

    public void setPayerCode(String payerCode) {
        this.payerCode = payerCode;
    }

    public List<ProductGodown> getProductGodowns() {
        return productGodowns;
    }

    public void setProductGodowns(List<ProductGodown> productGodowns) {
        this.productGodowns = productGodowns;
    }
}
