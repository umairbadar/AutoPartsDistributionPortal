package com.example.autopartsdistributionportal;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class Adapter_CheckAgentProduct extends RecyclerView.Adapter<Adapter_CheckAgentProduct.ViewHolder> {

    List<Model_CheckProductAgent> list;
    Context context;

    public Adapter_CheckAgentProduct(List<Model_CheckProductAgent> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_agent_product, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        final Model_CheckProductAgent item = list.get(i);

        holder.tv_pname.setText("Product Name: " + item.getName());
        holder.tv_pqty.setText("Quantity: " + item.getQuantity());
        holder.tv_pstorePrice.setText("Store Price" + item.getStore_price());
        holder.tv_punitPrice.setText("Unit Price" + item.getUnit_price());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tv_pname,tv_pqty,tv_pstorePrice,tv_punitPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_pname = itemView.findViewById(R.id.tv_pname);
            tv_pqty = itemView.findViewById(R.id.tv_pqty);
            tv_pstorePrice = itemView.findViewById(R.id.tv_pstorePrice);
            tv_punitPrice = itemView.findViewById(R.id.tv_punitPrice);
        }
    }
}
