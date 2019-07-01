package com.example.autopartsdistributionportal;

public class Model_CheckProductAgent {

    private String name;
    private String store_price;
    private String quantity;
    private String unit_price;


    public Model_CheckProductAgent(String name, String store_price, String quantity, String unit_price) {
        this.name = name;
        this.store_price = store_price;
        this.quantity = quantity;
        this.unit_price = unit_price;
    }

    public String getName() {
        return name;
    }

    public String getStore_price() {
        return store_price;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getUnit_price() {
        return unit_price;
    }
}
