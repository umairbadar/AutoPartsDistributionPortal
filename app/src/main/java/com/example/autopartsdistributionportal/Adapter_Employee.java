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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Adapter_Employee extends RecyclerView.Adapter<Adapter_Employee.ViewHolder> {

    List<Model_Employee> list;
    Context context;

    public Adapter_Employee(List<Model_Employee> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_employee, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {

        final Model_Employee items = list.get(i);

        String designation = "<b>Designation: </b>" + items.getDesignation();
        String name = "<b>Name: </b>" + items.getName();
        final String contact_no = "<b>Contact No: </b>" + items.getContact_no();
        String email = "<b>Email: </b>" + items.getEmail();
        String status = "<b>Status: </b>" + items.getStatus();

        if (items.getStatus().equals("Active")){
            holder.tv_status.setTextColor(Color.parseColor("#003076"));
        } else {
            holder.tv_status.setTextColor(Color.parseColor("#e2492f"));
        }

        holder.tv_designation.setText(Html.fromHtml(designation));
        holder.tv_name.setText(Html.fromHtml(name));
        holder.tv_contact_no.setText(Html.fromHtml(contact_no));
        holder.tv_email.setText(Html.fromHtml(email));
        holder.tv_status.setText(Html.fromHtml(status));

        holder.layout_view_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment fragment = new Fragment_Add_Employee();
                Bundle args = new Bundle();
                args.putString("name", items.getName());
                args.putString("designation_id", items.getDesignation_id());
                args.putString("contact_no", items.getContact_no());
                args.putString("cnic", items.getCnic());
                args.putString("email", items.getEmail());
                args.putString("status", items.getStatus());
                args.putString("id", items.getId());
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
                                String URL = Activity_Login.IP + "deleteEmployee";

                                StringRequest req = new StringRequest(Request.Method.POST, URL,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    JSONObject jsonObject = new JSONObject(response);
                                                    String msg = jsonObject.getString("msg");
                                                    if (msg.equals("success")){
                                                       // sDialog.dismissWithAnimation();
                                                        Fragment fragment = new Fragment_Employee();
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
                                        map.put("id", items.getId());
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

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView tv_designation,tv_name,tv_contact_no,tv_email,tv_status;
        LinearLayout layout_view_edit;
        ImageView btn_delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            btn_delete = itemView.findViewById(R.id.btn_delete);
            layout_view_edit = itemView.findViewById(R.id.layout_view_edit);
            tv_designation = itemView.findViewById(R.id.tv_designation);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_contact_no = itemView.findViewById(R.id.tv_contact_no);
            tv_email = itemView.findViewById(R.id.tv_email);
            tv_status = itemView.findViewById(R.id.tv_status);
        }
    }
}
