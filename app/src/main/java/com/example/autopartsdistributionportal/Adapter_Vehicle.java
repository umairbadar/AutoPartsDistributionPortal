package com.example.autopartsdistributionportal;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Adapter_Vehicle extends RecyclerView.Adapter<Adapter_Vehicle.ViewHolder>{

    List<Model_Vehicle> list;
    Context context;

    public Adapter_Vehicle(List<Model_Vehicle> list, Context context)
    {
        this.list = list;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vehicle,parent,false);
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {

        final Model_Vehicle items = list.get(i);

        String vehicle_type = "<b>Vehicle Type: </b>" + items.getVehicle_type();
        String vehicle_name = "<b>Vehicle Name: </b>" + items.getVehicle_name();
        String vehicle_brand = "<b>Vehicle Brand: </b>" + items.getVehicle_brand();
        String vehicle_model = "<b>Vehicle Model: </b>" + items.getVehicle_model();
        String vehicle_no_plate = "<b>No Plate: </b>" + items.getVehicle_license_plate();
        String vehicle_purchased_date = "<b>Date of Buying: </b>" + items.getVehicle_purchased_date();
        String status = "<b>Status: </b>" + items.getStatus();

        if (items.getStatus().equals("Active")){
            holder.tv_status.setTextColor(Color.parseColor("#003076"));
        } else {
            holder.tv_status.setTextColor(Color.parseColor("#e2492f"));
        }

        holder.tv_vehicle_type.setText(Html.fromHtml(vehicle_type));
        holder.tv_name.setText(Html.fromHtml(vehicle_name));
        holder.tv_brand.setText(Html.fromHtml(vehicle_brand));
        holder.tv_model.setText(Html.fromHtml(vehicle_model));
        holder.tv_no_plate.setText(Html.fromHtml(vehicle_no_plate));
        holder.tv_date_of_buying.setText(Html.fromHtml(vehicle_purchased_date));
        holder.tv_status.setText(Html.fromHtml(status));

        holder.layout_view_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment fragment = new Fragment_Add_Vehicle();
                Bundle args = new Bundle();
                args.putString("vehicle_type_id", items.getVehicle_type_id());
                args.putString("veh_name", items.getVehicle_name());
                args.putString("veh_brand", items.getVehicle_brand());
                args.putString("veh_model", items.getVehicle_model());
                args.putString("veh_no_plate", items.getVehicle_license_plate());
                args.putString("date_of_purchased", items.getVehicle_purchased_date());
                args.putString("veh_id", items.getVehicle_id());
                args.putString("status", items.getStatus());
                fragment.setArguments(args);
                FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_main,fragment);
                ft.addToBackStack(null);
                ft.commit();
                //Toast.makeText(context,item.getId(),Toast.LENGTH_LONG).show();
            }
        });

        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("Won't be able to recover this record!")
                        .setConfirmText("Yes,delete it!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(final SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                String URL = Activity_Login.IP + "deleteVehicle";

                                StringRequest req = new StringRequest(Request.Method.POST, URL,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    JSONObject jsonObject = new JSONObject(response);
                                                    String msg = jsonObject.getString("msg");
                                                    if (msg.equals("success")){
                                                        //sDialog.dismissWithAnimation();
                                                        Fragment fragment = new Fragment_Vehicle();
                                                        FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
                                                        FragmentTransaction ft = fragmentManager.beginTransaction();
                                                        ft.replace(R.id.content_main,fragment);
                                                        ft.addToBackStack(null);
                                                        ft.commit();
                                                    }
                                                    else {
                                                        sDialog.dismissWithAnimation();
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
                                                Toast.makeText(context, error.getMessage(),
                                                        Toast.LENGTH_LONG).show();
                                            }
                                        }){
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        Map<String, String> map = new HashMap<String, String>();
                                        map.put("id", items.getVehicle_id());
                                        return map;
                                    }
                                };

                                RequestQueue requestQueue = Volley.newRequestQueue(context);
                                requestQueue.add(req);
                            }
                        })
                        .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_vehicle_type,tv_name,tv_brand,tv_model,tv_no_plate,tv_date_of_buying,tv_status;
        LinearLayout layout_view_edit;
        ImageView btn_delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            btn_delete = itemView.findViewById(R.id.btn_delete);
            layout_view_edit = itemView.findViewById(R.id.layout_view_edit);
            tv_vehicle_type = itemView.findViewById(R.id.tv_vehicle_type);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_brand = itemView.findViewById(R.id.tv_brand);
            tv_model = itemView.findViewById(R.id.tv_model);
            tv_no_plate = itemView.findViewById(R.id.tv_no_plate);
            tv_date_of_buying = itemView.findViewById(R.id.tv_date_of_buying);
            tv_status = itemView.findViewById(R.id.tv_status);

        }
    }
}