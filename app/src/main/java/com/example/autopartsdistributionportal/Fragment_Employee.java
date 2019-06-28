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

public class Fragment_Employee extends Fragment {

    private RecyclerView emp_list;
    private RecyclerView.Adapter adapter;
    private List<Model_Employee> arr_list;

    private String created_by;
    private SharedPreferences sharedPreferences;

    private Button btn_add_new;

    private SweetAlertDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_employee, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btn_add_new = view.findViewById(R.id.btn_add_new);

        sharedPreferences = getActivity().getSharedPreferences("MyPre", Context.MODE_PRIVATE);
        created_by = sharedPreferences.getString("userid","");

        emp_list = view.findViewById(R.id.emp_list);
        emp_list.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        arr_list = new ArrayList<>();
        getEmployees();

        btn_add_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new Fragment_Add_Employee();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_main,fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
    }

    public  void getEmployees(){

        progressDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        progressDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        progressDialog.setTitleText("Please Wait");
        progressDialog.setContentText("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String URL = Activity_Login.IP + "getEmployees/" + created_by;
        StringRequest req = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String msg = jsonObject.getString("msg");
                            if (msg.equals("success")){
                                progressDialog.dismissWithAnimation();
                                JSONArray jsonArray = jsonObject.getJSONArray("employee_reference");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String id = object.getString("id");
                                    JSONObject innerObj = object.getJSONObject("findemployee");
                                    String name = innerObj.getString("name");
                                    String contact_no = innerObj.getString("contact_no");
                                    String cnic = innerObj.getString("nic");
                                    String email = innerObj.getString("email");
                                    String status = innerObj.getString("status");
                                    JSONObject obj = innerObj.getJSONObject("findempdesignation");
                                    String designation_id = obj.getString("id");
                                    String designation = obj.getString("designation_name");
                                    if (status.equals("1")){
                                        Model_Employee item = new Model_Employee(
                                                id,
                                                designation_id,
                                                designation,
                                                name,
                                                contact_no,
                                                email,
                                                "Active",
                                                cnic);
                                        arr_list.add(item);
                                    } else {
                                        Model_Employee item = new Model_Employee(
                                                id,
                                                designation_id,
                                                designation,
                                                name,
                                                contact_no,
                                                email,
                                                "Inactive",
                                                cnic);
                                        arr_list.add(item);
                                    }
                                }
                                adapter = new Adapter_Employee(arr_list,getContext());
                                emp_list.setAdapter(adapter);

                            } else {
                                progressDialog.dismissWithAnimation();
                                Toast.makeText(getContext(),msg,
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

    public void deleteEmployee(final String emp_id){
    }
}
