package com.msw.mesapp.bean.quality;

/**
 * Created by Mr.Meng on 2018/3/26.
 */


import com.bin.david.form.annotation.SmartColumn;

import java.util.List;

/**
 * Created by huang on 2017/11/1.
 */
public class QualityBean {
    private String name; //项目
    private String unit; //单位
    private String purchase1; //采购标准1
    private String purchase2; //采购标准2
    private String result;
    private ChildData childData;


    public QualityBean(String name, String unit, String purchase1, String purchase2, String result, ChildData childData) {
        this.name = name;
        this.unit = unit;
        this.purchase1 = purchase1;
        this.purchase2 = purchase2;
        this.result = result;
        this.childData = childData;

    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getPurchase1() {
        return purchase1;
    }

    public void setPurchase1(String purchase1) {
        this.purchase1 = purchase1;
    }

    public String getPurchase2() {
        return purchase2;
    }

    public void setPurchase2(String purchase2) {
        this.purchase2 = purchase2;
    }

    public ChildData getChildData() {
        return childData;
    }

    public void setChildData(ChildData childData) {
        this.childData = childData;
    }

/////////////////////////////////////////////////////////////

    public static class ChildData {

        @SmartColumn(id =5,name = "子类",autoCount = true)
        private String child;

        public ChildData(String child) {
            this.child = child;
        }

        public String getChild() {
            return child;
        }

        public void setChild(String child) {
            this.child = child;
        }
    }

}
