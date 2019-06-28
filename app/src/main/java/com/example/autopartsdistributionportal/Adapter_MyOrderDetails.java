package com.example.autopartsdistributionportal;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class Adapter_MyOrderDetails extends RecyclerView.Adapter<Adapter_MyOrderDetails.ViewHolder> {
    List<Model_MyOrderDetails> myorderdetails;
    Context context;

    public Adapter_MyOrderDetails(List<Model_MyOrderDetails> myorderdetails, Context context)
    {
        this.myorderdetails = myorderdetails;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_myorderdetails,parent,false);
        return new Adapter_MyOrderDetails.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final Model_MyOrderDetails items = myorderdetails.get(i);
        viewHolder.productname.setText(items.getProduct_name());
        viewHolder.quantity.setText(items.getQuantity());
        viewHolder.unitprice.setText(items.getUnitprice());
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView productname, quantity, unitprice;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productname = itemView.findViewById(R.id.productnamedetails);
            quantity = itemView.findViewById(R.id.quantitydetails);
            unitprice = itemView.findViewById(R.id.unitpricedetails);
        }
    }
}
