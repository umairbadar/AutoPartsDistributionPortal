package com.example.autopartsdistributionportal;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import dmax.dialog.SpotsDialog;

public class Fragment_Vehicle extends Fragment {

    private SharedPreferences sharedPreferences;
    private String id;

    private RecyclerView vehicle_list;
    private RecyclerView.Adapter adapter;
    private List<Model_Vehicle> arr_list;

    private Button btn_add_new;

    private SweetAlertDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vehicle, container, false);
        return  view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btn_add_new = view.findViewById(R.id.btn_add_new);

        sharedPreferences = getActivity().getSharedPreferences("MyPre", Context.MODE_PRIVATE);
        id = sharedPreferences.getString("userid","");

        vehicle_list = view.findViewById(R.id.vehicle_list);
        vehicle_list.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        arr_list = new ArrayList<>();
        getVehicles();

        btn_add_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new Fragment_Add_Vehicle();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_main,fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
    }

    public void getVehicles(){

        progressDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        progressDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        progressDialog.setTitleText("Please Wait");
        progressDialog.setContentText("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String URL = Activity_Login.IP + "getVehicles/" + id;
        StringRequest req = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String msg = jsonObject.getString("msg");
                            JSONArray jsonArray = jsonObject.getJSONArray("vehicles");
                            if (msg.equals("success")){
                                progressDialog.dismissWithAnimation();
                                for (int i = 0; i < jsonArray.length(); i++){
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String vehicle_id = object.getString("id");
                                    String vehicle_name = object.getString("veh_name");
                                    String vehicle_brand = object.getString("veh_brand");
                                    String vehicle_model = object.getString("veh_model");
                                    String vehicle_license_plate = object.getString("veh_license_plate");
                                    String vehicle_purchased_date = object.getString("veh_purchased_date");
                                    String status = object.getString("status");
                                    JSONObject innerObj = object.getJSONObject("findvehicletype");
                                    String vehicle_type = innerObj.getString("type_name");
                                    String vehicle_type_id = innerObj.getString("id");

                                    if (status.equals("1")) {

                                        Model_Vehicle item = new Model_Vehicle(
                                                vehicle_id,
                                                vehicle_name,
                                                vehicle_brand,
                                                vehicle_model,
                                                vehicle_license_plate,
                                                vehicle_purchased_date,
                                                vehicle_type,
                                                vehicle_type_id,
                                                "Active"
                                        );
                                        arr_list.add(item);
                                    } else {
                                        Model_Vehicle item = new Model_Vehicle(
                                                vehicle_id,
                                                vehicle_name,
                                                vehicle_brand,
                                                vehicle_model,
                                                vehicle_license_plate,
                                                vehicle_purchased_date,
                                                vehicle_type,
                                                vehicle_type_id,
                                                "Inactive"
                                        );
                                        arr_list.add(item);
                                    }
                                }
                                adapter = new Adapter_Vehicle(arr_list,getContext());
                                vehicle_list.setAdapter(adapter);
                            } else {
                                progressDialog.dismissWithAnimation();
                                Toast.makeText(getContext(),msg,
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
                        progressDialog.dismissWithAnimation();
                        Toast.makeText(getContext(),error.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(req);
    }
}
