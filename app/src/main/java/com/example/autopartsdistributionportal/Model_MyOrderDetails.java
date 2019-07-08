package com.example.autopartsdistributionportal;

import java.util.List;

public class Model_MyOrderDetails {

    private String product_name;
    private String quantity;
    private String unitprice;

    public Model_MyOrderDetails(String product_name, String quantity, String unitprice) {
        this.product_name = product_name;
        this.quantity = quantity;
        this.unitprice = unitprice;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getUnitprice() {
        return unitprice;
    }

}
