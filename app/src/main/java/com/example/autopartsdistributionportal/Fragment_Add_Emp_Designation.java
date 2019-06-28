package com.example.autopartsdistributionportal;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.icu.text.UnicodeSetSpanner;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import dmax.dialog.SpotsDialog;

public class Fragment_Add_Emp_Designation extends Fragment {

    private SharedPreferences sharedPreferences;

    private EditText et_designation;
    private Button btn_add;

    //Status
    private Spinner spn_status;
    private ArrayList<String> arr_status;
    private String spn_selected_item_position, created_by,id = "null",status;

    private SweetAlertDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_emp_designation, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPreferences = getActivity().getSharedPreferences("MyPre", Context.MODE_PRIVATE);
        created_by = sharedPreferences.getString("userid", "");

        et_designation = view.findViewById(R.id.et_designation);
        btn_add = view.findViewById(R.id.btn_add);

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
                spn_selected_item_position = String.valueOf(spn_status.getSelectedItemPosition());
                /*Toast.makeText(getContext(),spn_selected_item_position,
                        Toast.LENGTH_LONG).show();*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(et_designation.getText().toString())) {
                    et_designation.setError("Please enter Designation");
                    et_designation.requestFocus();
                } else if (spn_status.getSelectedItemPosition() == 0) {
                    Toast.makeText(getContext(), "Please Select Status",
                            Toast.LENGTH_LONG).show();
                } else {
                    postDesignation();
                }
            }
        });

        if (getArguments() != null){

            id = getArguments().getString("id");
            et_designation.setText(getArguments().getString("designation"));
            status = getArguments().getString("status");
            if (status.equals("Active")){
                spn_status.setSelection(1);
            } else {
                spn_status.setSelection(2);
            }
        }

    }

    public void postDesignation() {

        progressDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        progressDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        progressDialog.setTitleText("Please Wait");
        progressDialog.setContentText("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String URL = Activity_Login.IP + "CU_EmployeeDesignation";

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

                                Fragment fragment = new Fragment_Employee_Designation();
                                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                ft.replace(R.id.content_main, fragment);
                                ft.addToBackStack(null);
                                ft.commit();

                            } else {
                                progressDialog.dismissWithAnimation();
                                Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
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
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("designation_name", et_designation.getText().toString());
                map.put("created_by", created_by);
                map.put("status", spn_selected_item_position);
                map.put("id", id);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(req);
    }
}
