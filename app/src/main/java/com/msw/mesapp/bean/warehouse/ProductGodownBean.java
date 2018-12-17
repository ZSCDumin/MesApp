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
    private Department department;
    private String weight;
    private User payer;
    private List<ProductGodown> productGodowns;

    public ProductGodownBean(String model, Department departmentCode, String weight, User payerCode, List<ProductGodown> productGodowns) {
        this.model = model;
        this.department = departmentCode;
        this.weight = weight;
        this.payer = payerCode;
        this.productGodowns = productGodowns;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Department getDepartmentCode() {
        return department;
    }

    public void setDepartmentCode(Department departmentCode) {
        this.department = departmentCode;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public User getPayerCode() {
        return payer;
    }

    public void setPayerCode(User payerCode) {
        this.payer = payerCode;
    }

    public List<ProductGodown> getProductGodowns() {
        return productGodowns;
    }

    public void setProductGodowns(List<ProductGodown> productGodowns) {
        this.productGodowns = productGodowns;
    }
}
