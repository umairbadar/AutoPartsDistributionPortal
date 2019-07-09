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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Fragment_Transactions extends Fragment {

    private SharedPreferences sharedPreferences;
    private String username;
    private TextView tv_head, tv_balance;
    private Spinner spn_condition;
    private ArrayList<String> arr_condition;
    private RecyclerView recyclerViewBank, recyclerViewCustomer;
    private RecyclerView.Adapter adapter, customerAdapter;
    private List<Model_Bank> bank_list;
    private List<Model_Customer> customer_list;
    private String discount_amount, status;
    private static DecimalFormat df2 = new DecimalFormat("#");
    public static String created_by;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transactions, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPreferences = getActivity().getSharedPreferences("MyPre", Context.MODE_PRIVATE);
        created_by = sharedPreferences.getString("userid", "");
        username = sharedPreferences.getString("username", "");

        tv_head = view.findViewById(R.id.tv_head);
        tv_head.setText(username + " Transactions");

        spn_condition = view.findViewById(R.id.spn_condition);
        arr_condition = new ArrayList<>();
        arr_condition.add("Select One");
        arr_condition.add("Bank Information");
        arr_condition.add("Customer Order");
        spn_condition.setAdapter(new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, arr_condition));
        tv_balance = view.findViewById(R.id.tv_balance);
        getAccountBalance();

        recyclerViewBank = view.findViewById(R.id.recyclerviewBank);
        recyclerViewBank.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        bank_list = new ArrayList<>();

        recyclerViewCustomer = view.findViewById(R.id.recyclerviewCustomer);
        recyclerViewCustomer.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        customer_list = new ArrayList<>();

        spn_condition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spn_condition.getSelectedItemPosition() == 0) {
                    recyclerViewBank.setVisibility(View.GONE);
                    recyclerViewCustomer.setVisibility(View.GONE);
                } else if (spn_condition.getSelectedItemPosition() == 1) {
                    bank_list.clear();
                    getBankDetails();
                    recyclerViewBank.setVisibility(View.VISIBLE);
                    recyclerViewCustomer.setVisibility(View.GONE);
                } else if (spn_condition.getSelectedItemPosition() == 2) {
                    customer_list.clear();
                    getCustomerDetails();
                    recyclerViewBank.setVisibility(View.GONE);
                    recyclerViewCustomer.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void getBankDetails() {

        String URL = "http://172.16.10.202:8000/api/getCustomerTransaction/" + created_by;
        StringRequest req = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("accounts");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                String account_balance = object.getString("account_balance");
                                String account_no = object.getString("account_no");
                                String branch_name = object.getString("branch_name");
                                JSONObject innerObject = object.getJSONObject("findbnk");
                                String bank_name = innerObject.getString("name");

                                Model_Bank item = new Model_Bank(
                                        username,
                                        bank_name,
                                        branch_name,
                                        account_no,
                                        account_balance
                                );

                                bank_list.add(item);
                            }

                            adapter = new Adapter_Bank(bank_list, getContext());
                            recyclerViewBank.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), error.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(req);
    }

    public void getCustomerDetails() {

        String URL = "http://172.16.10.202:8000/api/getCustomerTransaction/" + created_by;
        StringRequest req = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            discount_amount = jsonObject.getString("discount_amount");
                            JSONArray jsonArray = jsonObject.getJSONArray("assign_orders");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                String order_id = object.getString("order_id");
                                JSONObject innerObject = object.getJSONObject("find_order");
                                String order_reference = innerObject.getString("order_reference");
                                String sub_total_amount = innerObject.getString("sub_total_amount");
                                String deliverydate = innerObject.getString("deliverydate");

                                if (jsonObject.has("account") && !jsonObject.isNull("account")) {
                                    JSONObject account = jsonObject.getJSONObject("account");
                                    String account_id = account.getString("id");

                                    if (object.has("find_supplier_payment") && !object.isNull("find_supplier_payment")) {
                                        JSONObject parent = object.getJSONObject("find_supplier_payment");
                                        String supplier_account_id = parent.getString("account_id");
                                        if (account_id.equals(supplier_account_id)) {

                                            if (parent.isNull("status")) {
                                                status = "null"; //Payment Confirmation Action
                                            } else {
                                                status = parent.getString("status"); //Received; Payment Recieved
                                            }

                                        } else {
                                            status = "0"; // Payment Pending
                                        }

                                    } else {
                                        status = "0"; // Payment Pending
                                    }

                                } else {
                                    status = "No Account Created"; //No Account
                                }

                                double initial_amount = Double.parseDouble(sub_total_amount) *
                                        Double.parseDouble(discount_amount);
                                double final_amount = Double.parseDouble(sub_total_amount) - initial_amount;

                                Model_Customer item = new Model_Customer(
                                        order_id,
                                        order_reference,
                                        String.valueOf(df2.format(final_amount)),
                                        deliverydate,
                                        status
                                );

                                customer_list.add(item);
                            }

                            customerAdapter = new Adapter_Customer(customer_list, getContext());
                            recyclerViewCustomer.setAdapter(customerAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), error.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(req);
    }

    public void getAccountBalance() {

        String URL = "http://172.16.10.202:8000/api/getCustomerTransaction/" + created_by;
        StringRequest req = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("accounts");
                            JSONObject innerObject = jsonArray.getJSONObject(0);
                            String account_balance = innerObject.getString("account_balance");
                            tv_balance.setText("Rs. " + account_balance);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), error.getStackTrace().toString(),
                                Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(req);
    }
}
