package com.example.autopartsdistributionportal;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class Fragment_My_Product extends Fragment {

    private RecyclerView my_product_list;
    private RecyclerView.Adapter adapter;
    private List<Model_My_Product> arr_list;
    private SweetAlertDialog progressDialog;

    private String created_by;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_product, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPreferences = getActivity().getSharedPreferences("MyPre", Context.MODE_PRIVATE);
        created_by = sharedPreferences.getString("userid", "");

        my_product_list = view.findViewById(R.id.my_product_list);
        my_product_list.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        arr_list = new ArrayList<>();
        getProducts();
    }

    public void getProducts() {

        progressDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        progressDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        progressDialog.setTitleText("Please Wait");
        progressDialog.setContentText("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String URL = Activity_Login.IP + "getProduct/" + created_by;

        StringRequest req = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String msg = jsonObject.getString("msg");
                            if (msg.equals("success")) {
                                progressDialog.dismissWithAnimation();
                                JSONArray jsonArray = jsonObject.getJSONArray("products");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String p_id = object.getString("id");
                                    String product_id = object.getString("product_id");
                                    String quantity = object.getString("quantity");
                                    String unit_price = object.getString("unit_price");
                                    String status = object.getString("status");
                                    JSONObject innerObj = object.getJSONObject("findproduct");
                                    String name = innerObj.getString("name");
                                    JSONObject Obj = innerObj.getJSONObject("findimage");
                                    String image = "http://172.16.10.202:8000/product-images/" + Obj.getString("image");
                                    if (status.equals("1")) {
                                        if (quantity.equals("null") && unit_price.equals("null")) {

                                            Model_My_Product items = new Model_My_Product(
                                                    p_id,
                                                    product_id,
                                                    name,
                                                    image,
                                                    "Active",
                                                    "0",
                                                    "0"
                                            );
                                            arr_list.add(items);
                                        } if (quantity.equals("null")){

                                            Model_My_Product items = new Model_My_Product(
                                                    p_id,
                                                    product_id,
                                                    name,
                                                    image,
                                                    "Active",
                                                    unit_price,
                                                    "0"
                                            );

                                        } if (unit_price.equals("null")){

                                            Model_My_Product items = new Model_My_Product(
                                                    p_id,
                                                    product_id,
                                                    name,
                                                    image,
                                                    "Active",
                                                    "0",
                                                    quantity
                                            );

                                        } else {
                                            Model_My_Product items = new Model_My_Product(
                                                    p_id,
                                                    product_id,
                                                    name,
                                                    image,
                                                    "Active",
                                                    unit_price,
                                                    quantity
                                            );
                                            arr_list.add(items);
                                        }

                                    } else {

                                        if (quantity.equals("null")) {

                                            Model_My_Product items = new Model_My_Product(
                                                    p_id,
                                                    product_id,
                                                    name,
                                                    image,
                                                    "Active",
                                                    unit_price,
                                                    "0"
                                            );
                                            arr_list.add(items);
                                        } else {
                                            Model_My_Product items = new Model_My_Product(
                                                    p_id,
                                                    product_id,
                                                    name,
                                                    image,
                                                    "Active",
                                                    unit_price,
                                                    quantity
                                            );
                                            arr_list.add(items);
                                        }
                                    }
                                }
                                adapter = new Adapter_My_Product(arr_list, getContext());
                                my_product_list.setAdapter(adapter);
                            } else {
                                progressDialog.dismissWithAnimation();
                                Toast.makeText(getContext(), "Not Found",
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
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(req);
    }
}
