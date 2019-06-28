package com.example.autopartsdistributionportal;

public class Model_Employee {

    private String id;
    private String designation_id;
    private String designation;
    private String name;
    private String contact_no;
    private String email;
    private String status;
    private String cnic;

    public Model_Employee(String id, String designation_id, String designation, String name, String contact_no, String email, String status, String cnic) {
        this.id = id;
        this.designation_id = designation_id;
        this.designation = designation;
        this.name = name;
        this.contact_no = contact_no;
        this.email = email;
        this.status = status;
        this.cnic = cnic;
    }

    public String getDesignation_id() {
        return designation_id;
    }

    public String getCnic() {
        return cnic;
    }

    public String getId() {
        return id;
    }

    public String getDesignation() {
        return designation;
    }

    public String getName() {
        return name;
    }

    public String getContact_no() {
        return contact_no;
    }

    public String getEmail() {
        return email;
    }

    public String getStatus() {
        return status;
    }
}