package com.example.autopartsdistributionportal;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class Adapter_MyOrder extends RecyclerView.Adapter<Adapter_MyOrder.ViewHolder> {
    List <Model_MyOrder> myorders;
    Context context;
    public Adapter_MyOrder(List<Model_MyOrder> myorders, Context context)
    {
        this.myorders = myorders;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_myorder,parent,false);
        return new Adapter_MyOrder.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final Model_MyOrder items = myorders.get(i);
        viewHolder.orderref.setText(items.getOrder_reference());
        viewHolder.customer_name.setText(items.getCustomer_name());
        viewHolder.deldate.setText(items.getDelivery_date());
        viewHolder.pririty.setText(items.getPriority());
        viewHolder.status.setText(items.getStatus());
        viewHolder.view_invoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"This is your invoice",Toast.LENGTH_LONG).show();
            }
        });
        viewHolder.gate_pass_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"Gate Pass In", Toast.LENGTH_LONG).show();
            }
        });
        viewHolder.gate_pass_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"Gate Pass Out", Toast.LENGTH_LONG).show();
            }
        });
        viewHolder.delivery_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"Delivery Order",Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return myorders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView orderref, customer_name, deldate, pririty, status;
        Button view_invoice, gate_pass_in, gate_pass_out, delivery_note, order_details;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderref = itemView.findViewById(R.id.orderreference);
            customer_name = itemView.findViewById(R.id.customername);
            pririty = itemView.findViewById(R.id.priority);
            deldate = itemView.findViewById(R.id.deliverydate);
            status = itemView.findViewById(R.id.statusmyorder);
            view_invoice = itemView.findViewById(R.id.invoice);
            gate_pass_in = itemView.findViewById(R.id.gatepassin);
            gate_pass_out = itemView.findViewById(R.id.gatepassout);
            delivery_note = itemView.findViewById(R.id.deliverynote);

        }
    }
}
