package com.example.autopartsdistributionportal;


public class Model_Vehicle {

    private String vehicle_id;
    private String vehicle_name;
    private String vehicle_brand;
    private String vehicle_model;
    private String vehicle_license_plate;
    private String vehicle_purchased_date;
    private String vehicle_type;
    private String vehicle_type_id;
    private String status;

    public Model_Vehicle(String vehicle_id, String vehicle_name, String vehicle_brand, String vehicle_model, String vehicle_license_plate, String vehicle_purchased_date, String vehicle_type, String vehicle_type_id, String status) {
        this.vehicle_id = vehicle_id;
        this.vehicle_name = vehicle_name;
        this.vehicle_brand = vehicle_brand;
        this.vehicle_model = vehicle_model;
        this.vehicle_license_plate = vehicle_license_plate;
        this.vehicle_purchased_date = vehicle_purchased_date;
        this.vehicle_type = vehicle_type;
        this.vehicle_type_id = vehicle_type_id;
        this.status = status;
    }

    public String getVehicle_id() {
        return vehicle_id;
    }

    public String getVehicle_name() {
        return vehicle_name;
    }

    public String getVehicle_brand() {
        return vehicle_brand;
    }

    public String getVehicle_model() {
        return vehicle_model;
    }

    public String getVehicle_license_plate() {
        return vehicle_license_plate;
    }

    public String getVehicle_purchased_date() {
        return vehicle_purchased_date;
    }

    public String getVehicle_type() {
        return vehicle_type;
    }

    public String getVehicle_type_id() {
        return vehicle_type_id;
    }

    public String getStatus() {
        return status;
    }
}
