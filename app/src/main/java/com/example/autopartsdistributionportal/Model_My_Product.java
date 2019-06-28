package com.example.autopartsdistributionportal;

public class Model_My_Product {

    private String p_id;
    private String p_name;
    private String p_img;
    private String p_status;
    private String p_unit_price;
    private String quantity;

    public Model_My_Product(String p_id, String p_name, String p_img, String p_status, String p_unit_price, String quantity) {
        this.p_id = p_id;
        this.p_name = p_name;
        this.p_img = p_img;
        this.p_status = p_status;
        this.p_unit_price = p_unit_price;
        this.quantity = quantity;
    }

    public String getP_id() {
        return p_id;
    }

    public String getP_name() {
        return p_name;
    }

    public String getP_img() {
        return p_img;
    }

    public String getP_status() {
        return p_status;
    }

    public String getP_unit_price() {
        return p_unit_price;
    }

    public String getQuantity() {
        return quantity;
    }
}
