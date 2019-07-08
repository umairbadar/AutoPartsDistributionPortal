package com.example.autopartsdistributionportal;


public class Model_MyOrder {

    private String order_reference;
    private String customer_name;
    private String delivery_date;
    private String priority;
    private String sitename;
    private String status;
    private String inventory_status;
    private String order_id;
    private String id;

    public Model_MyOrder(String id, String order_id, String inventory_status, String status, String sitename, String order_reference, String customer_name, String delivery_date, String priority) {
        this.order_reference = order_reference;
        this.customer_name = customer_name;
        this.delivery_date = delivery_date;
        this.priority = priority;
        this.sitename = sitename;
        this.status = status;
        this.inventory_status = inventory_status;
        this.order_id = order_id;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public String getInventory_status() {
        return inventory_status;
    }

    public String getStatus() {
        return status;
    }

    public String getSitename() {
        return sitename;
    }

    public String getOrder_reference() {
        return order_reference;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public String getDelivery_date() {
        return delivery_date;
    }

    public String getPriority() {
        return priority;
    }
}
