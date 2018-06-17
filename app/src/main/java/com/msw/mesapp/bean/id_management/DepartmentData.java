package com.msw.mesapp.bean.id_management;

public class DepartmentData {
    private String department_name;
    private String department_code;

    public DepartmentData(String department_name, String department_code) {
        this.department_name = department_name;
        this.department_code = department_code;
    }

    public String getDepartment_name() {
        return department_name;
    }

    public void setDepartment_name(String department_name) {
        this.department_name = department_name;
    }

    public String getDepartment_code() {
        return department_code;
    }

    public void setDepartment_code(String department_code) {
        this.department_code = department_code;
    }
}
