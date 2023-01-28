package com.example.codefest.studentservice.model;


import java.io.Serializable;

public class Department implements Serializable {

    private String departmentName;
    private Long departmentId;
    private String departmentAddress;
    private String departmentCode;

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentAddress() {
        return departmentAddress;
    }

    public void setDepartmentAddress(String departmentAddress) {
        this.departmentAddress = departmentAddress;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public Department(String departmentName, Long departmentId, String departmentAddress, String departmentCode) {
        this.departmentName = departmentName;
        this.departmentId = departmentId;
        this.departmentAddress = departmentAddress;
        this.departmentCode = departmentCode;
    }

    public Department() {
    }
}
