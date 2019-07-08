package com.example.autopartsdistributionportal;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.internal.FallbackServiceBroker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Adapter_MyOrder extends RecyclerView.Adapter<Adapter_MyOrder.ViewHolder> {

    List<Model_MyOrder> myorders;
    Context context;
    private String created_by;
    private SharedPreferences sharedPreferences;

    public Adapter_MyOrder(List<Model_MyOrder> myorders, Context context) {
        this.myorders = myorders;
        this.context = context;
        sharedPreferences = context.getSharedPreferences("MyPre", Context.MODE_PRIVATE);
        created_by = sharedPreferences.getString("userid","");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_myorder, parent, false);
        return new Adapter_MyOrder.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {

        final Model_MyOrder item = myorders.get(i);
        holder.tvOrderRef.setText(item.getOrder_reference());
        holder.tvcustomerRef.setText(item.getCustomer_name());
        holder.tvdeliveryDate.setText(item.getDelivery_date());
        holder.tvpriority.setText(item.getPriority());
        holder.tvdeliveryAddress.setText(item.getSitename());
        holder.tvstatus.setText(item.getStatus());

        holder.btn_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment myFragment = new Fragment_MyOrderDetails();
                Bundle args = new Bundle();
                args.putString("order_id",item.getOrder_id());
                args.putString("customer_id",created_by);
                myFragment.setArguments(args);
                activity.getSupportFragmentManager().beginTransaction().
                        replace(R.id.content_main, myFragment).addToBackStack(null)
                        .commit();
            }
        });

        String status = item.getStatus();
        String inventory_status = item.getInventory_status();

        if (status.equals("Assigned") || status.equals("assigned")) {

            holder.btn_assignWorkOrder.setEnabled(true);
            holder.btn_inventoryOut.setEnabled(false);
            holder.btn_gatePassOut.setEnabled(false);
            holder.btn_deliveryNote.setEnabled(false);

            holder.btn_assignWorkOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    Fragment myFragment = new Fragment_AssignOrderToEmployee();
                    Bundle args = new Bundle();
                    args.putString("OrderRef", item.getOrder_reference());
                    args.putString("order_id", item.getOrder_id());
                    myFragment.setArguments(args);
                    activity.getSupportFragmentManager().beginTransaction().
                            replace(R.id.content_main, myFragment).addToBackStack(null)
                            .commit();
                }
            });

        } else if (status.equals("Process") && inventory_status.equals("Process")) {

            holder.btn_assignWorkOrder.setEnabled(false);
            holder.btn_inventoryOut.setEnabled(true);
            holder.btn_gatePassOut.setEnabled(false);
            holder.btn_deliveryNote.setEnabled(false);
            holder.btn_assignWorkOrder.setBackgroundColor(Color.parseColor("#1a9a55"));
            holder.btn_assignWorkOrder.setText("Assigned");
            holder.btn_inventoryOut.setBackgroundColor(Color.parseColor("#f0ad4e"));
            holder.btn_inventoryOut.setText("Click here to Complete");
            holder.btn_inventoryOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    String URL = "http://172.16.10.202:8000/api/supplierInventoryOut/" + item.getOrder_id() + "/" + created_by;

                    StringRequest req = new StringRequest(Request.Method.GET, URL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        String msg = jsonObject.getString("msg");
                                        if (msg.equals("success")){
                                            AppCompatActivity activity = (AppCompatActivity) v.getContext();
                                            Fragment myFragment = new Fragment_MyOrders();
                                            activity.getSupportFragmentManager().beginTransaction().
                                                    replace(R.id.content_main, myFragment).addToBackStack(null)
                                                    .commit();
                                        } else {
                                            Toast.makeText(context,"Not Enough Quantity",
                                                    Toast.LENGTH_LONG).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(context,error.getMessage(),
                                            Toast.LENGTH_LONG).show();
                                }
                            });
                    RequestQueue requestQueue = Volley.newRequestQueue(context);
                    requestQueue.add(req);
                }
            });

        } else if (status.equals("Process") && inventory_status.equals("Completed")) {

            holder.btn_assignWorkOrder.setEnabled(false);
            holder.btn_inventoryOut.setEnabled(false);
            holder.btn_gatePassOut.setEnabled(true);
            holder.btn_deliveryNote.setEnabled(true);
            holder.btn_assignWorkOrder.setBackgroundColor(Color.parseColor("#1a9a55"));
            holder.btn_inventoryOut.setBackgroundColor(Color.parseColor("#1a9a55"));
            holder.btn_assignWorkOrder.setText("Assigned");
            holder.btn_inventoryOut.setText("Completed");
            holder.btn_gatePassOut.setText("Generate Gate Pass Out");
            holder.btn_gatePassOut.setBackgroundColor(Color.parseColor("#5bc0de"));
            holder.btn_deliveryNote.setText("Generate Delivery Order");
            holder.btn_deliveryNote.setBackgroundColor(Color.parseColor("#5bc0de"));
            holder.btn_gatePassOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    String URL = "http://172.16.10.202:8000/api/printGatePassOut/" + item.getOrder_id() + "/" + created_by;

                    StringRequest req = new StringRequest(Request.Method.GET, URL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        String msg = jsonObject.getString("msg");
                                        if (msg.equals("success")){
                                            AppCompatActivity activity = (AppCompatActivity) v.getContext();
                                            Fragment myFragment = new Fragment_MyOrders();
                                            activity.getSupportFragmentManager().beginTransaction().
                                                    replace(R.id.content_main, myFragment).addToBackStack(null)
                                                    .commit();
                                        } else {
                                            Toast.makeText(context,msg,
                                                    Toast.LENGTH_LONG).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(context,error.getMessage(),
                                            Toast.LENGTH_LONG).show();
                                }
                            });
                    RequestQueue requestQueue = Volley.newRequestQueue(context);
                    requestQueue.add(req);
                }
            });

            holder.btn_deliveryNote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    String URL = "http://172.16.10.202:8000/api/printDeliveryOrder/" + item.getOrder_id() + "/" + created_by;

                    StringRequest req = new StringRequest(Request.Method.GET, URL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        String msg = jsonObject.getString("msg");
                                        if (msg.equals("success")){
                                            AppCompatActivity activity = (AppCompatActivity) v.getContext();
                                            Fragment myFragment = new Fragment_MyOrders();
                                            activity.getSupportFragmentManager().beginTransaction().
                                                    replace(R.id.content_main, myFragment).addToBackStack(null)
                                                    .commit();
                                        } else {
                                            Toast.makeText(context,msg,
                                                    Toast.LENGTH_LONG).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(context,error.getMessage(),
                                            Toast.LENGTH_LONG).show();
                                }
                            });
                    RequestQueue requestQueue = Volley.newRequestQueue(context);
                    requestQueue.add(req);
                }
            });

        } else if (status.equals("Enroute")) {

            holder.btn_assignWorkOrder.setEnabled(false);
            holder.btn_inventoryOut.setEnabled(false);
            holder.btn_gatePassOut.setEnabled(false);
            holder.btn_deliveryNote.setEnabled(false);
            holder.btn_assignWorkOrder.setBackgroundColor(Color.parseColor("#1a9a55"));
            holder.btn_inventoryOut.setBackgroundColor(Color.parseColor("#1a9a55"));
            holder.btn_assignWorkOrder.setText("Assigned");
            holder.btn_inventoryOut.setText("Completed");
            holder.btn_gatePassOut.setText("Gate Pass Out");
            holder.btn_gatePassOut.setBackgroundColor(Color.parseColor("#5bc0de"));
            holder.btn_deliveryNote.setText("Delivery Order");
            holder.btn_deliveryNote.setBackgroundColor(Color.parseColor("#5bc0de"));
            holder.btnaction.setEnabled(true);
            holder.btnaction.setText("Deliver?");
            holder.btnaction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    String URL = "http://172.16.10.202:8000/api/assignedOrderStatus/" + item.getId() + "/" + created_by;

                    StringRequest req = new StringRequest(Request.Method.GET, URL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        String msg = jsonObject.getString("msg");
                                        if (msg.equals("success")){
                                            AppCompatActivity activity = (AppCompatActivity) v.getContext();
                                            Fragment myFragment = new Fragment_MyOrders();
                                            activity.getSupportFragmentManager().beginTransaction().
                                                    replace(R.id.content_main, myFragment).addToBackStack(null)
                                                    .commit();
                                        } else {
                                            Toast.makeText(context,msg,
                                                    Toast.LENGTH_LONG).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(context,error.getMessage(),
                                            Toast.LENGTH_LONG).show();
                                }
                            });
                    RequestQueue requestQueue = Volley.newRequestQueue(context);
                    requestQueue.add(req);
                }
            });

        } else if (status.equals("Delivered")){

            holder.btn_assignWorkOrder.setEnabled(false);
            holder.btn_inventoryOut.setEnabled(false);
            holder.btn_gatePassOut.setEnabled(false);
            holder.btn_deliveryNote.setEnabled(false);
            holder.btn_assignWorkOrder.setBackgroundColor(Color.parseColor("#1a9a55"));
            holder.btn_inventoryOut.setBackgroundColor(Color.parseColor("#1a9a55"));
            holder.btn_assignWorkOrder.setText("Assigned");
            holder.btn_inventoryOut.setText("Completed");
            holder.btn_gatePassOut.setText("Gate Pass Out");
            holder.btn_gatePassOut.setBackgroundColor(Color.parseColor("#5bc0de"));
            holder.btn_deliveryNote.setText("Delivery Order");
            holder.btn_deliveryNote.setBackgroundColor(Color.parseColor("#5bc0de"));
        }
    }

    @Override
    public int getItemCount() {
        return myorders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvstatus,tvOrderRef, tvcustomerRef, tvdeliveryDate, tvpriority, tvdeliveryAddress;
        Button btnaction,btn_assignWorkOrder, btn_inventoryOut, btn_gatePassOut, btn_deliveryNote,btn_view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvstatus = itemView.findViewById(R.id.tv_status);
            tvOrderRef = itemView.findViewById(R.id.tv_orderRef);
            tvcustomerRef = itemView.findViewById(R.id.tv_customerName);
            tvdeliveryDate = itemView.findViewById(R.id.tv_deliveryDate);
            tvpriority = itemView.findViewById(R.id.tv_priority);
            tvdeliveryAddress = itemView.findViewById(R.id.tv_deliveryAddress);
            btn_assignWorkOrder = itemView.findViewById(R.id.btn_assignWorkOrder);
            btn_inventoryOut = itemView.findViewById(R.id.btn_inventoryOut);
            btn_gatePassOut = itemView.findViewById(R.id.btn_gatePassOut);
            btn_deliveryNote = itemView.findViewById(R.id.btn_deliveryNote);
            btn_view = itemView.findViewById(R.id.btn_view);
            btnaction = itemView.findViewById(R.id.btn_action);

        }
    }
}
