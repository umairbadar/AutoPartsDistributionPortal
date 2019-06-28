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
import android.widget.TextView;
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

public class Fragment_Assign_My_Product extends Fragment {

    private SharedPreferences sharedPreferences;
    private String p_name,p_id, status_id, customer_id,product_holder_id;
    private TextView tv_product_name;
    private EditText et_quantity;
    private Button btn_assigned;
    private Spinner spn_status, spn_product_holder;

    private ArrayList<String> arr_status;
    private ArrayList<String> arr_product_holder;
    private ArrayList<String> arr_product_holder_id;

    private SweetAlertDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_assign_my_product, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPreferences = getActivity().getSharedPreferences("MyPre", Context.MODE_PRIVATE);
        customer_id = sharedPreferences.getString("userid", "");

        if (getArguments() != null) {

            p_name = getArguments().getString("product_name");
            p_id = getArguments().getString("product_id");
        }

        tv_product_name = view.findViewById(R.id.tv_product_name);
        et_quantity = view.findViewById(R.id.et_quantity);
        spn_product_holder = view.findViewById(R.id.spn_product_holder);
        spn_status = view.findViewById(R.id.spn_status);
        btn_assigned = view.findViewById(R.id.btn_assigned);

        tv_product_name.setText("Product Name: " + p_name);

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

        arr_product_holder = new ArrayList<>();
        arr_product_holder_id = new ArrayList<>();
        getProductHolder();

        spn_product_holder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                product_holder_id = arr_product_holder_id.get(position);
                //Toast.makeText(getContext(),product_holder_id,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btn_assigned.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spn_product_holder.getSelectedItemPosition() == 0){
                    Toast.makeText(getContext(), "Please Select Product Holder",
                            Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(et_quantity.getText().toString())){
                    et_quantity.setError("Please enter Quantity");
                    et_quantity.requestFocus();
                } else if (spn_status.getSelectedItemPosition() == 0){
                    Toast.makeText(getContext(), "Please Select Status",
                            Toast.LENGTH_LONG).show();
                } else {
                    postProductHolder();
                }
            }
        });
    }

    public void getProductHolder() {

        progressDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        progressDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        progressDialog.setTitleText("Please Wait");
        progressDialog.setContentText("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String URL = Activity_Login.IP + "getAssignedProductAgent/" + customer_id;

        StringRequest req = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String msg = jsonObject.getString("msg");
                            if (msg.equals("success")){
                                progressDialog.dismissWithAnimation();
                                arr_product_holder_id.add("0");
                                arr_product_holder.add("Select Product Holder");
                                JSONArray jsonArray = jsonObject.getJSONArray("productholders");
                                for (int i = 0; i < jsonArray.length(); i++){
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String id = object.getString("id");
                                    String name = object.getString("name");
                                    arr_product_holder_id.add(id);
                                    arr_product_holder.add(name);
                                }

                                spn_product_holder.setAdapter(new ArrayAdapter<>(getContext(),
                                        android.R.layout.simple_spinner_dropdown_item, arr_product_holder));
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

    public void postProductHolder(){

        Toast.makeText(getContext(),product_holder_id + " " + p_id, Toast.LENGTH_LONG).show();

        progressDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        progressDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        progressDialog.setTitleText("Please Wait");
        progressDialog.setContentText("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String URL = Activity_Login.IP + "updateAssignedProductAgent";

        StringRequest req = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String msg = jsonObject.getString("msg");
                            if (msg.equals("success")){
                                progressDialog.dismissWithAnimation();
                                Fragment fragment = new Fragment_My_Product();
                                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                ft.replace(R.id.content_main,fragment);
                                ft.commit();
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
                map.put("product_agent_id", product_holder_id);
                map.put("supplier_product_id", p_id);
                map.put("quantity", et_quantity.getText().toString().trim());
                map.put("status", status_id);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(req);
    }
}
