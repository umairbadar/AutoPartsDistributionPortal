package com.example.autopartsdistributionportal;

public class Model_Product_Holder {

    private String product_holder;
    private String supplier_name;
    private String supplier_type;
    private String email;
    private String cell_phone;
    private String nic;
    private String status;
    private String customer_group_id;
    private String customer_type_id;
    private String status_id;

    public Model_Product_Holder(String status_id, String customer_type_id, String customer_group_id, String product_holder, String supplier_name, String supplier_type, String email, String cell_phone, String nic, String status) {
        this.product_holder = product_holder;
        this.supplier_name = supplier_name;
        this.supplier_type = supplier_type;
        this.email = email;
        this.cell_phone = cell_phone;
        this.nic = nic;
        this.status = status;
        this.customer_type_id = customer_type_id;
        this.customer_group_id = customer_group_id;
        this.status_id = status_id;
    }

    public String getStatus_id() {
        return status_id;
    }

    public String getProduct_holder() {
        return product_holder;
    }

    public String getSupplier_name() {
        return supplier_name;
    }

    public String getSupplier_type() {
        return supplier_type;
    }

    public String getEmail() {
        return email;
    }

    public String getCell_phone() {
        return cell_phone;
    }

    public String getNic() {
        return nic;
    }

    public String getStatus() {
        return status;
    }

    public String getCustomer_group_id() {
        return customer_group_id;
    }

    public String getCustomer_type_id() {
        return customer_type_id;
    }
}
