package com.example.autopartsdistributionportal;

import android.widget.Spinner;

public class Model_MyOrder {
  private String order_reference;
  private String customer_name;
  private String delivery_date;
  private String priority;
  private String invoice;
  private String gate_pass_in;
  private String gate_pass_out;
  private String delivery_note;
  private String status;


  public Model_MyOrder(String order_reference, String customer_name, String delivery_date, String priority,/* String invoice, String gate_pass_in,
                     String gate_pass_out, String delivery_note ,*/ String status )
  {
    this.order_reference = order_reference;
    this.customer_name = customer_name;
    this.delivery_date = delivery_date;
    this.priority = priority;
//    this.invoice = invoice;
//    this.gate_pass_in = gate_pass_in;
//    this.gate_pass_out = gate_pass_out;
//    this.delivery_note = delivery_note;
    this.status = status;


  }
  public String getOrder_reference()
  {
    return order_reference;
  }
  public String getCustomer_name()
  {
    return customer_name;
  }
  public String getDelivery_date()
  {
    return delivery_date;
  }
  public String getPriority()
  {
    return priority;
  }
  public String getInvoice()
  {
    return invoice;
  }
  public String getGate_pass_in()
  {
    return gate_pass_in;
  }
  public String getGate_pass_out()
  {
    return gate_pass_out;
  }
  public String getDelivery_note()
  {
    return delivery_note;
  }
  public String getStatus()
  {
    return status;
  }

}
