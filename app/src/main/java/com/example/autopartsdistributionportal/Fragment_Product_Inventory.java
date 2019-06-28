package com.example.autopartsdistributionportal;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Fragment_Product_Inventory extends Fragment {

    private String id;
    private TextView tv_product_name, tv_category, tv_parent_sub_category, tv_brand_name,
            tv_oem_number, tv_price_of_store, tv_stock_qty, tv_unit_price,tv_head1,tv_head2;
    private EditText et_add_new_quantity, et_add_new_price, et_insert_new_quantity, et_insert_new_price;
    private Button btn_update;
    private SweetAlertDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_inventory, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {

            id = getArguments().getString("id");
        }

        //New Quantity and Price
        et_add_new_quantity = view.findViewById(R.id.et_add_new_qty);
        et_add_new_price = view.findViewById(R.id.et_add_new_price);

        et_insert_new_quantity = view.findViewById(R.id.et_insert_new_qty);
        et_insert_new_price = view.findViewById(R.id.et_insert_new_price);

        btn_update = view.findViewById(R.id.btn_update);

        tv_product_name = view.findViewById(R.id.tv_product_name);
        tv_category = view.findViewById(R.id.tv_category);
        tv_parent_sub_category = view.findViewById(R.id.tv_parent_sub_category);
        tv_brand_name = view.findViewById(R.id.tv_brand_name);
        tv_oem_number = view.findViewById(R.id.tv_oem_number);
        tv_price_of_store = view.findViewById(R.id.tv_price_of_store);
        tv_stock_qty = view.findViewById(R.id.tv_stock_qty);
        tv_unit_price = view.findViewById(R.id.tv_unit_price);
        tv_head1 = view.findViewById(R.id.tv_head1);
        tv_head2 = view.findViewById(R.id.tv_head2);

        getProductInventory();

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkValidation();
            }
        });


    }

    public void getProductInventory() {

        progressDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        progressDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        progressDialog.setTitleText("Please Wait");
        progressDialog.setContentText("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String URL = Activity_Login.IP + "addProductInventory/" + id;

        StringRequest req = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            progressDialog.dismissWithAnimation();
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject object = jsonObject.getJSONObject("editsupplierproduct");
                            JSONObject innerObj = object.getJSONObject("findproduct");
                            tv_product_name.setText("Product Name: " + innerObj.getString("name"));
                            tv_oem_number.setText("OEM Number: " + innerObj.getString("oem_num"));
                            //tv_price_of_store.setText(innerObj.getString("oem_num"));

                            String qty = object.getString("quantity");
                            String unit_price = object.getString("unit_price");
                            if (qty.equals("null") && unit_price.equals("null")) {
                                tv_stock_qty.setVisibility(View.GONE);
                                tv_unit_price.setVisibility(View.GONE);
                                tv_head1.setVisibility(View.GONE);
                                tv_head2.setVisibility(View.GONE);
                                et_insert_new_quantity.setVisibility(View.VISIBLE);
                                et_insert_new_price.setVisibility(View.VISIBLE);
                            } else {
                                et_add_new_quantity.setVisibility(View.VISIBLE);
                                et_add_new_price.setVisibility(View.VISIBLE);
                            }
                            tv_stock_qty.setText(qty);
                            tv_unit_price.setText(unit_price);

                            JSONObject catObj = innerObj.getJSONObject("find_category");
                            tv_category.setText("Category: " + catObj.getString("name"));
                            JSONObject subCatObj = innerObj.getJSONObject("find_sub_category");
                            tv_parent_sub_category.setText("Parent Sub Category: " + subCatObj.getString("name"));
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
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(req);
    }

    public void checkValidation() {

        if (et_add_new_quantity.isShown()) {
            if (TextUtils.isEmpty(et_add_new_quantity.getText().toString())) {
                et_add_new_quantity.setError("Please Enter Quantity");
                et_add_new_quantity.requestFocus();
            } else {
                postDataAddNewQty();
            }
        } else {
            if (TextUtils.isEmpty(et_insert_new_quantity.getText().toString())) {
                et_insert_new_quantity.setError("Please Enter Quantity");
                et_insert_new_quantity.requestFocus();
            } else if (TextUtils.isEmpty(et_insert_new_price.getText().toString())) {
                et_insert_new_price.setError("Please Enter Price");
                et_insert_new_price.requestFocus();
            } else {
                postDataInsertNewQty();
            }
        }
    }

    public void postDataAddNewQty() {

        progressDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        progressDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        progressDialog.setTitleText("Please Wait");
        progressDialog.setContentText("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String URL = Activity_Login.IP + "updateProductInventory";

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
                                ft.replace(R.id.content_main, fragment);
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
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), error.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("id", id);

                map.put("new_quantity", et_add_new_quantity.getText().toString());
                map.put("new_unit_price", et_add_new_price.getText().toString());

                map.put("old_quantity", tv_stock_qty.getText().toString());
                map.put("old_unit_price", tv_unit_price.getText().toString());

                //null
                map.put("quantity", "null");
                map.put("unit_price", "null");

                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(req);
        /*Toast.makeText(getContext(),"new Quantity: " + et_add_new_quantity.getText().toString()
                +" new Price: " + et_add_new_price.getText().toString() + " old qty: " + tv_stock_qty.getText().toString()
        + " old price: " + tv_unit_price.getText().toString(),Toast.LENGTH_LONG).show();*/
        //Toast.makeText(getContext(),et_add_new_price.getText().toString(),Toast.LENGTH_LONG).show();

    }

    public void postDataInsertNewQty() {

        progressDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        progressDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        progressDialog.setTitleText("Please Wait");
        progressDialog.setContentText("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String URL = Activity_Login.IP + "updateProductInventory";

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
                                ft.replace(R.id.content_main, fragment);
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
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), error.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("id", id);

                //null
                map.put("new_quantity", "null");
                map.put("new_unit_price", "null");

                //null
                map.put("old_quantity", "null");
                map.put("old_unit_price", "null");

                map.put("quantity", et_insert_new_quantity.getText().toString());
                map.put("unit_price", et_insert_new_price.getText().toString());

                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(req);

    }
}
