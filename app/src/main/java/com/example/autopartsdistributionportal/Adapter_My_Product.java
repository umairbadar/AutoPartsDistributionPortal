package com.example.autopartsdistributionportal;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

public class Adapter_My_Product extends RecyclerView.Adapter<Adapter_My_Product.ViewHolder>{

    List<Model_My_Product> products;
    Context context;

    public Adapter_My_Product(List<Model_My_Product> products, Context context) {
        this.products = products;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_my_product, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {

        final Model_My_Product items = products.get(i);


        holder.tv_name.setText("Product Name: " + items.getP_name());
        holder.tv_qty.setText("Quantity: " + items.getQuantity());
        holder.tv_unit_price.setText("Unit Price: " + items.getP_unit_price());
        holder.tv_status.setText(items.getP_status());

        Picasso.with(context)
                .load(items.getP_img())
                .into(holder.p_img);

        holder.btn_add_invertory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment fragment = new Fragment_Product_Inventory();
                Bundle args = new Bundle();
                args.putString("id", items.getP_id());
                fragment.setArguments(args);
                FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_main,fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        holder.btn_assign_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment fragment = new Fragment_Assign_My_Product();
                Bundle args = new Bundle();
                args.putString("product_name", items.getP_name());
                args.putString("p_id", items.getP_id());
                args.putString("product_id", items.getProduct_id());
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
        return products.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView p_img;
        TextView tv_name,tv_qty,tv_unit_price,tv_status;
        Button btn_add_invertory,btn_assign_product;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            p_img = itemView.findViewById(R.id.img_product);
            tv_name = itemView.findViewById(R.id.tv_product_name);
            tv_qty = itemView.findViewById(R.id.tv_product_qty);
            tv_unit_price = itemView.findViewById(R.id.tv_product_unit_price);
            tv_status = itemView.findViewById(R.id.tv_product_status);
            btn_add_invertory = itemView.findViewById(R.id.btn_add_inventory);
            btn_assign_product = itemView.findViewById(R.id.btn_assign_product);
        }
    }
}