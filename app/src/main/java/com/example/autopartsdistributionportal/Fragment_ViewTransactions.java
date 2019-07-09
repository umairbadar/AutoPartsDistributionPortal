package com.example.autopartsdistributionportal;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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

public class Fragment_ViewTransactions extends Fragment {

    private String order_id,customer_id;
    private SharedPreferences sharedPreferences;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<Model_ViewTransactions> arr_list;
    private TextView tv_error;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_viewtransactions, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null){
            order_id = getArguments().getString("order_id");
        }

        tv_error = view.findViewById(R.id.tv_error);

        sharedPreferences = getActivity().getSharedPreferences("MyPre", Context.MODE_PRIVATE);
        customer_id = sharedPreferences.getString("userid","NA");

        recyclerView = view.findViewById(R.id.recyclerViewTransactions);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        arr_list = new ArrayList<>();
        getTransactions();
    }

    public void getTransactions(){

        String URL = "http://172.16.10.202:8000/api/getCustomerTransactionDetails/" + order_id + "/" + customer_id;
        StringRequest req = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String msg = jsonObject.getString("msg");
                            if (msg.equals("success")){
                                JSONObject object = jsonObject.getJSONObject("data");
                                JSONArray jsonArray = object.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++){
                                    JSONObject innerObject = jsonArray.getJSONObject(i);
                                    String reference_number = innerObject.getString("reference_number");
                                    String previous_balance = innerObject.getString("previous_balance");
                                    String add_amount = innerObject.getString("add_amount");
                                    String new_balance = innerObject.getString("new_balance");
                                    JSONObject innerObject2 = innerObject.getJSONObject("find_payment_method");
                                    String payment_method = innerObject2.getString("name");
                                    JSONObject innerObject3 = innerObject.getJSONObject("find_payment_mode");
                                    String payment_mode = innerObject3.getString("name");

                                    Model_ViewTransactions item = new Model_ViewTransactions(
                                            reference_number,
                                            previous_balance,
                                            add_amount,
                                            new_balance,
                                            payment_method,
                                            payment_mode
                                    );
                                    arr_list.add(item);
                                }
                                adapter = new Adapter_ViewTransactions(arr_list,getContext());
                                recyclerView.setAdapter(adapter);

                            } else {
                                recyclerView.setVisibility(View.GONE);
                                tv_error.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(),error.getStackTrace().toString(),
                                Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(req);
    }
}
