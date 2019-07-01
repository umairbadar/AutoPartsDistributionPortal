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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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

public class Fragment_CheckProductAgent extends Fragment {

    private RecyclerView agent_product_list;
    private RecyclerView.Adapter adapter;
    private List<Model_CheckProductAgent> arr_list;

    private SharedPreferences sharedPreferences;
    private Spinner spn_product_holder;
    private ArrayList<String> arr_product_holder;
    private ArrayList<String> arr_product_holder_id;

    private SweetAlertDialog progressDialog;
    private String customer_id,product_holder_id = "0";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checkproductagent, container, false);
        return view;
}

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPreferences = getActivity().getSharedPreferences("MyPre", Context.MODE_PRIVATE);
        customer_id = sharedPreferences.getString("userid", "");

        agent_product_list = view.findViewById(R.id.recyclerviewmyorder);
        agent_product_list.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        arr_list = new ArrayList<>();

        spn_product_holder = view.findViewById(R.id.spn_product_holder);
        arr_product_holder = new ArrayList<>();
        arr_product_holder_id = new ArrayList<>();
        getProductHolder();

        spn_product_holder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                product_holder_id = arr_product_holder_id.get(position);
                if (product_holder_id.equals("0")){
                    agent_product_list.setVisibility(View.GONE);
                } else{
                    agent_product_list.setVisibility(View.VISIBLE);
                    getAgentProducts();
                }
                //Toast.makeText(getContext(),product_holder_id,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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

    public void getAgentProducts(){

        String URL = "http://172.16.10.202:8000/api/getAgentProduct/" + product_holder_id;

        StringRequest req = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("productagents");
                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject object = jsonArray.getJSONObject(i);
                                String name = object.getString("name");
                                String store_price = object.getString("store_price");
                                String quantity = object.getString("quantity");
                                String unit_price = object.getString("unit_price");

                                Model_CheckProductAgent item = new Model_CheckProductAgent(
                                        name,
                                        store_price,
                                        quantity,
                                        unit_price
                                );
                                arr_list.add(item);
                            }
                            adapter = new Adapter_CheckAgentProduct(arr_list,getContext());
                            agent_product_list.setAdapter(adapter);
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
