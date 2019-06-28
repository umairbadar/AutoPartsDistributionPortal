package com.example.autopartsdistributionportal;

import android.app.AlertDialog;
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
import dmax.dialog.SpotsDialog;

public class Fragment_Employee_Designation extends Fragment {

    private RecyclerView emp_designation_list;
    private RecyclerView.Adapter adapter;
    private List<Model_Emp_Designation> arr_list;
    private SweetAlertDialog progressDialog;
    private Button btn_add_new;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_employee_designation, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btn_add_new = view.findViewById(R.id.btn_add_new);

        emp_designation_list = view.findViewById(R.id.emp_designation_list);
        emp_designation_list.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        arr_list = new ArrayList<>();
        getEmpDesignation();

        btn_add_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new Fragment_Add_Emp_Designation();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_main, fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
    }

    public void getEmpDesignation(){

        progressDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        progressDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        progressDialog.setTitleText("Please Wait");
        progressDialog.setContentText("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String URL = Activity_Login.IP + "getEmpDesignation_getVehTypes";
        StringRequest req = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String msg = jsonObject.getString("msg");
                            //Toast.makeText(getContext(),msg,Toast.LENGTH_LONG).show();
                            progressDialog.dismissWithAnimation();
                            JSONArray jsonArray = jsonObject.getJSONArray("employee_designations");
                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject object = jsonArray.getJSONObject(i);
                                String designation = object.getString("designation_name");
                                String status = object.getString("status");
                                String id = object.getString("id");
                                if (status.equals("1")){
                                    Model_Emp_Designation item = new Model_Emp_Designation(
                                            id,
                                            designation,
                                            "Active"
                                    );
                                    arr_list.add(item);
                                } else {

                                    Model_Emp_Designation item = new Model_Emp_Designation(
                                            id,
                                            designation,
                                            "Inactive"
                                    );
                                    arr_list.add(item);
                                }
                            }
                            adapter = new Adapter_Emp_Designation(arr_list,getContext());
                            emp_designation_list.setAdapter(adapter);

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
