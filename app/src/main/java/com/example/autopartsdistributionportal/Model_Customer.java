package com.example.autopartsdistributionportal;

public class Model_Customer {

    private String order_ref;
    private String total_amount;
    private String delivery_date;
    private String status;

    public Model_Customer(String order_ref, String total_amount, String delivery_date, String status) {
        this.order_ref = order_ref;
        this.total_amount = total_amount;
        this.delivery_date = delivery_date;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public String getOrder_ref() {
        return order_ref;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public String getDelivery_date() {
        return delivery_date;
    }
}
