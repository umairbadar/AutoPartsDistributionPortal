package com.example.autopartsdistributionportal;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class Adapter_Customer extends RecyclerView.Adapter<Adapter_Customer.ViewHolder> {

    List<Model_Customer> list;
    Context context;

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

        Model_Customer item = list.get(i);

        holder.tv_orderRef.setText("Order Ref: " + item.getOrder_ref());
        holder.tv_totalAmount.setText("Total Amount: " + item.getTotal_amount());
        holder.tv_deliveryDate.setText("Delivery Date: " + item.getDelivery_date());
        String status = item.getStatus();
        if (status.equals("0")){
            holder.tv_payment.setText("Payment Pending!");
            holder.tv_payment.setBackgroundColor(Color.parseColor("#d9534f"));

        } else if (status.equals("null")){
            holder.tv_payment.setText("Receiving Confirmation!");
            holder.tv_payment.setBackgroundColor(Color.parseColor("#f0ad4e"));

        } else if (status.equals("Recieved")){
            holder.tv_payment.setText("Payment Received");
            holder.tv_payment.setBackgroundColor(Color.parseColor("#5cb85c"));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tv_orderRef, tv_totalAmount, tv_deliveryDate,tv_payment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_orderRef = itemView.findViewById(R.id.tv_orderRef);
            tv_totalAmount = itemView.findViewById(R.id.tv_totalAmount);
            tv_deliveryDate = itemView.findViewById(R.id.tv_deliveryDate);
            tv_payment = itemView.findViewById(R.id.tv_payment);
        }
    }
}
