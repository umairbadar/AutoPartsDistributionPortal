package com.example.autopartsdistributionportal;

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

public class Fragment_MyOrders extends Fragment {

    private String created_by;
    private SharedPreferences sharedPreferences;

    private RecyclerView recyclerviewmyorder;
    private RecyclerView.Adapter adapter;
    private List<Model_MyOrder> myorders;

    private SweetAlertDialog progressDialog;

    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_myorders, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPreferences = getActivity().getSharedPreferences("MyPre", Context.MODE_PRIVATE);
        created_by = sharedPreferences.getString("userid","");

        recyclerviewmyorder = view.findViewById(R.id.recyclerviewmyorder);
        recyclerviewmyorder.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,
                false));
        myorders = new ArrayList<>();
        getOrders();

    }

    private void getOrders() {

        progressDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        progressDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        progressDialog.setTitleText("Please Wait");
        progressDialog.setContentText("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String URL = "http://172.16.10.202:8000/api/getCustomerMyOrder/" + created_by;
        StringRequest req = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            progressDialog.dismissWithAnimation();
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("myorder");
                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject object = jsonArray.getJSONObject(i);
                                String status = object.getString("status");
                                String id = object.getString("id");
                                String inventory_status = object.getString("inventory_status");
                                String order_id = object.getString("order_id");
                                JSONObject innerObject1 = object.getJSONObject("find_order");
                                String orderRef = innerObject1.getString("order_reference");
                                String priority = innerObject1.getString("prority");
                                String deliveryDate = innerObject1.getString("deliverydate");
                                JSONObject innerObject2 = innerObject1.getJSONObject("find_customer");
                                String customerName = innerObject2.getString("name");
                                JSONObject innerObject3 = object.getJSONObject("findsite");
                                String name = innerObject3.getString("name");
                                JSONObject innerobject4 = innerObject3.getJSONObject("find_owner");
                                String site = innerobject4.getString("name");
                                String sitename = site + " " + name;

                                Model_MyOrder item = new Model_MyOrder(
                                        id,
                                        order_id,
                                        inventory_status,
                                        status,
                                        sitename,
                                        orderRef,
                                        customerName,
                                        deliveryDate,
                                        priority
                                );
                                myorders.add(item);
                            }

                            adapter = new Adapter_MyOrder(myorders,getContext());
                            recyclerviewmyorder.setAdapter(adapter);
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
