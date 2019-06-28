package com.example.autopartsdistributionportal;

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

public class Fragment_Add_Employee extends Fragment {

    private SharedPreferences sharedPreferences;

    private Spinner spn_designation, spn_status;

    private ArrayList<String> arr_designations;
    private ArrayList<String> arr_designations_id;

    private String designation_id, status_id, customer_id, status, id = "null", d_id;

    private ArrayList<String> arr_status;

    private EditText et_emp_name, et_emp_contact, et_emp_cnic, et_emp_email;
    private Button btn_add;

    private SweetAlertDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_employee, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        et_emp_name = view.findViewById(R.id.et_emp_name);
        et_emp_contact = view.findViewById(R.id.et_emp_contact);
        et_emp_cnic = view.findViewById(R.id.et_emp_cnic);
        et_emp_email = view.findViewById(R.id.et_emp_email);
        spn_status = view.findViewById(R.id.spn_status);

        sharedPreferences = getActivity().getSharedPreferences("MyPre", Context.MODE_PRIVATE);
        customer_id = sharedPreferences.getString("userid", "");

        btn_add = view.findViewById(R.id.btn_add);

        spn_designation = view.findViewById(R.id.spn_designation);
        arr_designations = new ArrayList<>();
        arr_designations_id = new ArrayList<>();
        getDesignations();

        spn_designation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                designation_id = arr_designations_id.get(position);
                //Toast.makeText(getContext(),designation_id,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


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

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spn_designation.getSelectedItemPosition() == 0) {
                    Toast.makeText(getContext(), "Please Select Designation",
                            Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(et_emp_name.getText().toString())) {
                    et_emp_name.setError("Please Enter Name");
                    et_emp_name.requestFocus();
                } else if (TextUtils.isEmpty(et_emp_contact.getText().toString())) {
                    et_emp_contact.setError("Please Enter Contact No.");
                    et_emp_cnic.requestFocus();
                } else if (TextUtils.isEmpty(et_emp_cnic.getText().toString())) {
                    et_emp_cnic.setError("Please Enter CNIC");
                    et_emp_cnic.requestFocus();
                } else if (TextUtils.isEmpty(et_emp_email.getText().toString())) {
                    et_emp_email.setError("Please Enter Email");
                    et_emp_email.requestFocus();
                } else if (spn_status.getSelectedItemPosition() == 0) {
                    Toast.makeText(getContext(), "Please Select Status",
                            Toast.LENGTH_LONG).show();
                } else {
                    postEmployeeData();
                }
            }
        });

    }

    public void getDesignations() {

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
                            if (msg.equals("success")) {
                                progressDialog.dismissWithAnimation();
                                arr_designations_id.add("0");
                                arr_designations.add("Select Designation");
                                JSONArray jsonArray = jsonObject.getJSONArray("employee_designations");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    String id = obj.getString("id");
                                    String designation_name = obj.getString("designation_name");
                                    arr_designations_id.add(id);
                                    arr_designations.add(designation_name);
                                }
                                spn_designation.setAdapter(new ArrayAdapter<>(getContext(),
                                        android.R.layout.simple_spinner_dropdown_item, arr_designations));

                                if (getArguments() != null) {

                                    et_emp_name.setText(getArguments().getString("name"));
                                    et_emp_contact.setText(getArguments().getString("contact_no"));
                                    et_emp_cnic.setText(getArguments().getString("cnic"));
                                    et_emp_email.setText(getArguments().getString("email"));

                                    status = getArguments().getString("status");
                                    if (status.equals("Active")) {
                                        spn_status.setSelection(1);
                                    } else {
                                        spn_status.setSelection(2);
                                    }
                                    id = getArguments().getString("id");

                                    d_id = getArguments().getString("designation_id");
                                    spn_designation.setSelection(Integer.parseInt(d_id));
                                }
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
                        Toast.makeText(getContext(),error.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(req);

    }

    public void postEmployeeData() {
        progressDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        progressDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        progressDialog.setTitleText("Please Wait");
        progressDialog.setContentText("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String URL = Activity_Login.IP + "CU_Employee";
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

                                Fragment fragment = new Fragment_Employee();
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
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("emp_designation_id", designation_id);
                map.put("name", et_emp_name.getText().toString());
                map.put("contact_no", et_emp_contact.getText().toString());
                map.put("nic", et_emp_cnic.getText().toString());
                map.put("email", et_emp_email.getText().toString());
                map.put("status", status_id);
                map.put("customer_id", customer_id);
                map.put("id", id);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(req);
    }
}
