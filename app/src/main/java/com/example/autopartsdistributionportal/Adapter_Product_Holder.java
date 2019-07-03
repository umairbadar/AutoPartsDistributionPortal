package com.example.autopartsdistributionportal;

import android.content.Context;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class Adapter_Product_Holder extends RecyclerView.Adapter<Adapter_Product_Holder.ViewHolder> {

    List<Model_Product_Holder> list;
    Context context;

    public Adapter_Product_Holder(List<Model_Product_Holder> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product_holder, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {

        final Model_Product_Holder item = list.get(i);

        holder.tv_product_holder.setText("Product Holder: " + item.getProduct_holder());
        holder.tv_supplier_name.setText("Supplier Name: " + item.getSupplier_name());
        holder.tv_supplier_type.setText("Supplier Type: " + item.getSupplier_type());
        holder.tv_email.setText("Email: " + item.getEmail());
        holder.tv_cell_phone.setText("Cell Phone: " + item.getCell_phone());
        holder.tv_nic.setText("Nic/NTN Number: " + item.getNic());
        holder.tv_status.setText("Status: " + item.getStatus());

        holder.layout_view_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment fragment = new Fragment_Add_Product_Holder();
                Bundle args = new Bundle();
                args.putString("condition", "edit");
                args.putString("type_id", item.getCustomer_type_id());
                args.putString("group_id", item.getCustomer_group_id());
                args.putString("status_id", item.getStatus_id());
                fragment.setArguments(args);
                FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_main,fragment);
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

        TextView tv_product_holder, tv_supplier_name, tv_supplier_type, tv_email, tv_cell_phone, tv_nic, tv_status;
        LinearLayout layout_view_edit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_product_holder = itemView.findViewById(R.id.tv_product_holder);
            tv_supplier_name = itemView.findViewById(R.id.tv_supplier_name);
            tv_supplier_type = itemView.findViewById(R.id.tv_supplier_type);
            tv_email = itemView.findViewById(R.id.tv_email);
            tv_cell_phone = itemView.findViewById(R.id.tv_cell_phone);
            tv_nic = itemView.findViewById(R.id.tv_nic);
            tv_status = itemView.findViewById(R.id.tv_status);

            layout_view_edit = itemView.findViewById(R.id.layout_view_edit);
        }
    }
}
