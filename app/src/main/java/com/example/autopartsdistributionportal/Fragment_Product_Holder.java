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
import android.widget.Button;
import android.widget.TimePicker;
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

public class Fragment_Product_Holder extends Fragment {

    private SharedPreferences sharedPreferences;
    private String created_by;

    private RecyclerView product_holder_list;
    private RecyclerView.Adapter adapter;
    private List<Model_Product_Holder> arr_list;
    private SweetAlertDialog progressDialog;
    private Button btn_add;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_holder, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPreferences = getActivity().getSharedPreferences("MyPre", Context.MODE_PRIVATE);
        created_by = sharedPreferences.getString("userid", "");

        btn_add = view.findViewById(R.id.btn_add);

        product_holder_list = view.findViewById(R.id.recyclerview);
        product_holder_list.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        arr_list = new ArrayList<>();
        getProductHolder();

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new Fragment_Add_Product_Holder();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                Bundle args = new Bundle();
                args.putString("condition", "add");
                args.putString("type_id", "4");
                args.putString("group_id", "0");
                args.putString("status_id", "0");
                fragment.setArguments(args);
                ft.replace(R.id.content_main, fragment);
                ft.addToBackStack(null);
                ft.commit();

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

        String URl = "http://172.16.10.202:8000/api/getProductAgent/" + created_by;

        StringRequest req = new StringRequest(Request.Method.GET, URl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            progressDialog.dismissWithAnimation();
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("productagents");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                String product_holder = object.getString("name");
                                String customer_group_id = object.getString("customer_group_id");
                                String customer_type_id = object.getString("customer_type_id");
                                String email = object.getString("email");
                                String nic = object.getString("nic");
                                String cell_phone = object.getString("phone");
                                String status = object.getString("status");
                                JSONObject innerObject1 = object.getJSONObject("findcustomer");
                                String supplier_name = innerObject1.getString("name");
                                JSONObject innerObject2 = object.getJSONObject("findcsttype");
                                String supplier_type = innerObject2.getString("name");

                                if (status.equals("1")) {
                                    Model_Product_Holder item = new Model_Product_Holder(
                                            status,
                                            customer_type_id,
                                            customer_group_id,
                                            product_holder,
                                            supplier_name,
                                            supplier_type,
                                            email,
                                            cell_phone,
                                            nic,
                                            "Active"
                                    );
                                    arr_list.add(item);
                                } else {
                                    Model_Product_Holder item = new Model_Product_Holder(
                                            status,
                                            customer_type_id,
                                            customer_group_id,
                                            product_holder,
                                            supplier_name,
                                            supplier_type,
                                            email,
                                            cell_phone,
                                            nic,
                                            "Inactive"
                                    );
                                    arr_list.add(item);
                                }
                                adapter = new Adapter_Product_Holder(arr_list, getContext());
                                product_holder_list.setAdapter(adapter);
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
