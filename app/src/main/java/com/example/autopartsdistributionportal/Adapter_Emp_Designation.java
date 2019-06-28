package com.example.autopartsdistributionportal;

import android.content.Context;
import android.graphics.Color;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class Adapter_Emp_Designation extends RecyclerView.Adapter<Adapter_Emp_Designation.ViewHolder> {

    List<Model_Emp_Designation> list;
    Context context;

    public Adapter_Emp_Designation(List<Model_Emp_Designation> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_emp_designation, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {

        final Model_Emp_Designation item = list.get(i);

        String designation = item.getDesignation();
        String status = item.getStatus();

        if (item.getStatus().equals("Active")){
            holder.tv_status.setTextColor(Color.parseColor("#003076"));
        } else {
            holder.tv_status.setTextColor(Color.parseColor("#e2492f"));
        }

        holder.tv_designation.setText(designation);
        holder.tv_status.setText(status);
        holder.layout_view_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment fragment = new Fragment_Add_Emp_Designation();
                Bundle args = new Bundle();
                args.putString("designation", item.getDesignation());
                args.putString("status", item.getStatus());
                args.putString("id", item.getId());
                fragment.setArguments(args);
                FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_main,fragment);
                ft.addToBackStack(null);
                ft.commit();
                //Toast.makeText(context,item.getId(),Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView tv_designation,tv_status;
        LinearLayout layout_view_edit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_designation = itemView.findViewById(R.id.tv_designation);
            tv_status = itemView.findViewById(R.id.tv_status);
            layout_view_edit = itemView.findViewById(R.id.layout_view_edit);
        }
    }
}
