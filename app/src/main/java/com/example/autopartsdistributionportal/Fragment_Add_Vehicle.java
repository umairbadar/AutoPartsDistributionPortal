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
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import dmax.dialog.SpotsDialog;

public class Fragment_Add_Vehicle extends Fragment {

    private SharedPreferences sharedPreferences;

    private Spinner spn_vehicle_type,spn_status;
    private ArrayList<String> arr_vehicle_type;
    private ArrayList<String> arr_vehicle_type_id;
    private ArrayList<String> arr_status;

    private EditText et_vehicle_name,et_brand_name,et_vehicle_model,et_no_plate,et_date_of_buying;
    private Button btn_add;

    private String vehicle_type_id,status_id,supplier_id,status,id = "null",veh_type_id;
    private SweetAlertDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_vehicle, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPreferences = getActivity().getSharedPreferences("MyPre", Context.MODE_PRIVATE);
        supplier_id = sharedPreferences.getString("userid","");

        //Spinners
        spn_vehicle_type = view.findViewById(R.id.spn_vehicle_type);
        arr_vehicle_type = new ArrayList<>();
        arr_vehicle_type_id = new ArrayList<>();
        getVehicleTypes();

        spn_vehicle_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                vehicle_type_id = arr_vehicle_type_id.get(position);
                //Toast.makeText(getContext(),vehicle_type_id,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spn_status = view.findViewById(R.id.spn_status);
        arr_status = new ArrayList<>();
        arr_status.add("Select Status");
        arr_status.add("Active");
        arr_status.add("Inactive");
        spn_status.setAdapter(new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, arr_status));

        spn_status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                status_id = String.valueOf(spn_status.getSelectedItemPosition());
                //Toast.makeText(getContext(),status_id,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Edittexts
        et_vehicle_name = view.findViewById(R.id.et_vehicle_name);
        et_brand_name = view.findViewById(R.id.et_brand_name);
        et_vehicle_model = view.findViewById(R.id.et_vehicle_model);
        et_no_plate = view.findViewById(R.id.et_no_plate);
        et_date_of_buying = view.findViewById(R.id.et_date_of_buying);

        //Button
        btn_add = view.findViewById(R.id.btn_add);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spn_vehicle_type.getSelectedItemPosition() == 0) {
                    Toast.makeText(getContext(), "Please Select Vehicle Type",
                            Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(et_vehicle_name.getText().toString())) {
                    et_vehicle_name.setError("Please Enter Vehicle Name");
                    et_vehicle_name.requestFocus();
                } else if (TextUtils.isEmpty(et_brand_name.getText().toString())) {
                    et_brand_name.setError("Please Enter Brand");
                    et_brand_name.requestFocus();
                } else if (TextUtils.isEmpty(et_vehicle_model.getText().toString())) {
                    et_vehicle_model.setError("Please Enter Vehicle Model");
                    et_vehicle_model.requestFocus();
                } else if (TextUtils.isEmpty(et_no_plate.getText().toString())) {
                    et_no_plate.setError("Please Enter No Plate");
                    et_no_plate.requestFocus();
                }else if (TextUtils.isEmpty(et_date_of_buying.getText().toString())) {
                    et_date_of_buying.setError("Please Enter Date of Purchased");
                    et_date_of_buying.requestFocus();
                } else if (spn_status.getSelectedItemPosition() == 0) {
                    Toast.makeText(getContext(), "Please Select Status",
                            Toast.LENGTH_LONG).show();
                } else {
                    postVehicleData();
                }
            }
        });
    }

    public void getVehicleTypes(){

        progressDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        progressDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        progressDialog.setTitleText("Please Wait");
        progressDialog.setContentText("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String URL = Activity_Login.IP + "getEmpDesignation_getVehTypes";
        StringRequest req = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String msg = jsonObject.getString("msg");
                            if (msg.equals("success")){
                                progressDialog.dismissWithAnimation();
                                arr_vehicle_type.add("Select Vehicle Type");
                                arr_vehicle_type_id.add("0");
                                JSONArray jsonArray = jsonObject.getJSONArray("vehicle_types");
                                for (int i = 0; i < jsonArray.length(); i++){
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String id = object.getString("id");
                                    String type_name = object.getString("type_name");
                                    arr_vehicle_type_id.add(id);
                                    arr_vehicle_type.add(type_name);
                                }
                                spn_vehicle_type.setAdapter(new ArrayAdapter<>(getContext(),
                                        android.R.layout.simple_spinner_dropdown_item, arr_vehicle_type));

                                if (getArguments() != null) {

                                    et_vehicle_name.setText(getArguments().getString("veh_name"));
                                    et_brand_name.setText(getArguments().getString("veh_brand"));
                                    et_vehicle_model.setText(getArguments().getString("veh_model"));
                                    et_no_plate.setText(getArguments().getString("veh_no_plate"));
                                    et_date_of_buying.setText(getArguments().getString("date_of_purchased"));

                                    status = getArguments().getString("status");
                                    if (status.equals("Active")) {
                                        spn_status.setSelection(1);
                                    } else {
                                        spn_status.setSelection(2);
                                    }
                                    id = getArguments().getString("veh_id");

                                    veh_type_id = getArguments().getString("vehicle_type_id");
                                    spn_vehicle_type.setSelection(Integer.parseInt(veh_type_id));
                                }
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

    public void postVehicleData(){

        progressDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        progressDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        progressDialog.setTitleText("Please Wait");
        progressDialog.setContentText("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String URL = Activity_Login.IP + "CU_Vehicle";
        StringRequest req = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String msg = jsonObject.getString("msg");
                            if (msg.equals("success")) {
                                progressDialog.dismissWithAnimation();

                                progressDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE);
                                progressDialog.setTitleText("Success");
                                progressDialog.setContentText("Data Submitted");
                                progressDialog.setCancelable(false);
                                progressDialog.show();

                                Fragment fragment = new Fragment_Vehicle();
                                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                ft.replace(R.id.content_main, fragment);
                                ft.addToBackStack(null);
                                ft.commit();
                            } else {
                                progressDialog.dismissWithAnimation();
                                Toast.makeText(getContext(), msg,
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
                        Toast.makeText(getContext(), error.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("veh_type", vehicle_type_id);
                map.put("veh_name", et_vehicle_name.getText().toString());
                map.put("veh_brand", et_brand_name.getText().toString());
                map.put("veh_model", et_vehicle_model.getText().toString());
                map.put("veh_license_plate", et_no_plate.getText().toString());
                map.put("veh_purchased_date", et_date_of_buying.getText().toString());
                map.put("status", status_id);
                map.put("supplier_id", supplier_id);
                map.put("id", id);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(req);
    }
}
