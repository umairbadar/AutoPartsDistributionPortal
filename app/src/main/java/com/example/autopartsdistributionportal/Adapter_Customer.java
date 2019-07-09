package com.example.autopartsdistributionportal;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class Adapter_Customer extends RecyclerView.Adapter<Adapter_Customer.ViewHolder> {

    List<Model_Customer> list;
    Context context;
    AlertDialog progressDialog;

    public Adapter_Customer(List<Model_Customer> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_customer, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {

        final Model_Customer item = list.get(i);

        holder.tv_orderRef.setText("Order Ref: " + item.getOrder_ref());
        holder.tv_totalAmount.setText("Total Amount: " + item.getTotal_amount());
        holder.tv_deliveryDate.setText("Delivery Date: " + item.getDelivery_date());

        String status = item.getStatus();
        if (status.equals("No Account Created")) {
            holder.tv_payment.setText("No Account Created!");
            holder.tv_payment.setBackgroundColor(Color.parseColor("#d9534f"));

        } else if (status.equals("0")) {
            holder.tv_payment.setText("Payment Pending!");
            holder.tv_payment.setBackgroundColor(Color.parseColor("#d9534f"));

        } else if (status.equals("null")) {
            holder.tv_payment.setText("Receiving Confirmation!");
            holder.tv_payment.setBackgroundColor(Color.parseColor("#f0ad4e"));

            //Action
            holder.tv_payment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    progressDialog = new SpotsDialog(context, R.style.CustomProgress);
                    progressDialog.show();
                    progressDialog.setCancelable(false);

                    String URL = "http://172.16.10.202:8000/api/supplierFinanceConfirmation";

                    StringRequest req = new StringRequest(Request.Method.POST, URL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        String msg = jsonObject.getString("msg");
                                        if (msg.equals("success")) {
                                            progressDialog.dismiss();
                                            Toast.makeText(context, "Payment Received",
                                                    Toast.LENGTH_LONG).show();
                                            AppCompatActivity activity = (AppCompatActivity) v.getContext();
                                            Fragment fragment = new Fragment_Transactions();
                                            FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                                            ft.replace(R.id.content_main, fragment);
                                            ft.addToBackStack(null);
                                            ft.commit();

                                        } else {
                                            progressDialog.dismiss();
                                            Toast.makeText(context, msg,
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
                                    progressDialog.dismiss();
                                    Toast.makeText(context, error.getMessage(),
                                            Toast.LENGTH_LONG).show();
                                }
                            }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("order_id", item.getOrder_id());
                            map.put("customer_id", Fragment_Transactions.created_by);
                            return map;
                        }
                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(context);
                    requestQueue.add(req);
                }
            });

        } else if (status.equals("Recieved")) {
            holder.tv_payment.setText("Payment Received");
            holder.tv_payment.setBackgroundColor(Color.parseColor("#5cb85c"));
        }

        holder.tv_view_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment fragment = new Fragment_ViewTransactions();
                Bundle args = new Bundle();
                args.putString("order_id", item.getOrder_id());
                fragment.setArguments(args);
                FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_main, fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_orderRef, tv_totalAmount, tv_deliveryDate, tv_payment, tv_view_details;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_orderRef = itemView.findViewById(R.id.tv_orderRef);
            tv_totalAmount = itemView.findViewById(R.id.tv_totalAmount);
            tv_deliveryDate = itemView.findViewById(R.id.tv_deliveryDate);
            tv_payment = itemView.findViewById(R.id.tv_payment);
            tv_view_details = itemView.findViewById(R.id.tv_view_details);
        }
    }
}
