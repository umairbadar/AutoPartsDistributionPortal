package com.example.autopartsdistributionportal;

public class Model_Emp_Designation {

    private String id;
    private String designation;
    private String status;

    public Model_Emp_Designation(String id, String designation, String status) {
        this.id = id;
        this.designation = designation;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getDesignation() {
        return designation;
    }

    public String getStatus() {
        return status;
    }
}
