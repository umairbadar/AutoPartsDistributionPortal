package com.example.autopartsdistributionportal;

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

public class Fragment_MyOrderDetails extends Fragment {

    private RecyclerView recyclerVieworderdetails;
    private RecyclerView.Adapter adapter;
    private List<Model_MyOrderDetails> myorderdetails;
    View v;
    private String createdby, orderid;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_myorderdetails, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null){
            orderid = getArguments().getString("order_id");
            createdby = getArguments().getString("customer_id");
        }

        recyclerVieworderdetails = view.findViewById(R.id.recyclerview);
        recyclerVieworderdetails.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        myorderdetails = new ArrayList<>();
        getProductDetails();
    }

    public void getProductDetails(){
        String URL = "http://172.16.10.202:8000/api/viewOrderDetail/"+ orderid +"/" + createdby;
        StringRequest req = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("viewOrder");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                JSONObject innerObject1 = object.getJSONObject("find_order_detail");
                                String qty = innerObject1.getString("quantity");
                                String unit_price = innerObject1.getString("unitprice");
                                JSONObject innerObject2 = innerObject1.getJSONObject("findproduct");
                                String name = innerObject2.getString("name");
                                Model_MyOrderDetails item = new Model_MyOrderDetails(
                                        name,
                                        qty,
                                        unit_price
                                );
                                myorderdetails.add(item);
                            }
                            adapter = new Adapter_MyOrderDetails(myorderdetails,getContext());
                            recyclerVieworderdetails.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(),error.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(req);

    }
}
