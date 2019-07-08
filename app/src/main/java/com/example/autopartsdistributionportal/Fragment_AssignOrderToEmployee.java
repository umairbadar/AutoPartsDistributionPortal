package com.example.autopartsdistributionportal;

import android.content.Context;
import android.content.SharedPreferences;
import android.icu.text.UnicodeSetSpanner;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Fragment_AssignOrderToEmployee extends Fragment {

    private SharedPreferences sharedPreferences;
    private String created_by, order_responsible, company_driver, vehicle, agent;
    private String orderRef,order_id;
    private RadioButton radioButton01, radioButton02, radioButton03;
    private LinearLayout layout1, layout2, layout3;
    private RadioGroup radioGroup;
    private TextView tv_head;
    private Spinner spn_order_responsible, spn_company_driver, spn_vehicle, spn_agent;
    private ArrayList<String> arr_order_responsible;
    private ArrayList<String> arr_order_responsible_id;
    private ArrayList<String> arr_company_driver;
    private ArrayList<String> arr_company_driver_id;
    private ArrayList<String> arr_vehicle;
    private ArrayList<String> arr_vehicle_id;
    private ArrayList<String> arr_agent;
    private ArrayList<String> arr_agent_id;
    private Button btn_assign_employee1, btn_assign_employee2, btn_assign_employee3;
    private EditText et_driver_name, et_vehicle_no, et_driver_nic_no, et_driver_contact_no;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_assignordertoemployee, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPreferences = getActivity().getSharedPreferences("MyPre", Context.MODE_PRIVATE);
        created_by = sharedPreferences.getString("userid", "");

        if (getArguments() != null) {
            orderRef = getArguments().getString("OrderRef");
            order_id = getArguments().getString("order_id");
        }

        btn_assign_employee1 = view.findViewById(R.id.btn_assign_employee1);
        btn_assign_employee2 = view.findViewById(R.id.btn_assign_employee2);
        btn_assign_employee3 = view.findViewById(R.id.btn_assign_employee3);

        tv_head = view.findViewById(R.id.tv_head);
        radioButton01 = view.findViewById(R.id.radio1);
        radioButton02 = view.findViewById(R.id.radio2);
        radioButton03 = view.findViewById(R.id.radio3);
        radioGroup = view.findViewById(R.id.radioGroup);

        layout1 = view.findViewById(R.id.layout1);
        layout2 = view.findViewById(R.id.layout2);
        layout3 = view.findViewById(R.id.layout3);

        et_driver_name = view.findViewById(R.id.et_driver_name);
        et_vehicle_no = view.findViewById(R.id.et_vehicle_no);
        et_driver_nic_no = view.findViewById(R.id.et_driver_nic_no);
        et_driver_contact_no = view.findViewById(R.id.et_driver_contact_no);

        tv_head.setText("Assign Employee for Order No. " + orderRef);

        spn_order_responsible = view.findViewById(R.id.spn_order_responsible);
        arr_order_responsible = new ArrayList<>();
        arr_order_responsible_id = new ArrayList<>();
        getOrderResponsible();


        spn_company_driver = view.findViewById(R.id.spn_company_driver);
        arr_company_driver = new ArrayList<>();
        arr_company_driver_id = new ArrayList<>();
        getCompanyDriver();

        spn_vehicle = view.findViewById(R.id.spn_vehicle);
        arr_vehicle = new ArrayList<>();
        arr_vehicle_id = new ArrayList<>();
        getCompanyVehicle();

        spn_agent = view.findViewById(R.id.spn_agent);
        arr_agent = new ArrayList<>();
        arr_agent_id = new ArrayList<>();
        getAgent();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radio1) {
                    layout1.setVisibility(View.VISIBLE);
                    layout2.setVisibility(View.GONE);
                    layout3.setVisibility(View.GONE);
                    spn_order_responsible.setVisibility(View.VISIBLE);
                } else if (checkedId == R.id.radio2) {
                    layout1.setVisibility(View.GONE);
                    layout2.setVisibility(View.VISIBLE);
                    spn_order_responsible.setVisibility(View.VISIBLE);
                    layout3.setVisibility(View.GONE);
                } else if (checkedId == R.id.radio3) {
                    layout1.setVisibility(View.GONE);
                    layout2.setVisibility(View.GONE);
                    layout3.setVisibility(View.VISIBLE);
                    spn_order_responsible.setVisibility(View.VISIBLE);
                } else {
                    layout1.setVisibility(View.GONE);
                    layout2.setVisibility(View.GONE);
                    layout3.setVisibility(View.GONE);
                    spn_order_responsible.setVisibility(View.GONE);
                }
            }
        });

        spn_order_responsible.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                order_responsible = arr_order_responsible_id.get(position);
                //Toast.makeText(getContext(),order_responsible,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spn_company_driver.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                company_driver = arr_company_driver_id.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spn_vehicle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                vehicle = arr_vehicle_id.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spn_agent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                agent = arr_agent_id.get(position);
                Toast.makeText(getContext(),agent + " " + order_id,
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_assign_employee1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spn_order_responsible.getSelectedItemPosition() == 0) {
                    Toast.makeText(getContext(), "Please Select Order Responsible",
                            Toast.LENGTH_LONG).show();
                } else if (spn_company_driver.getSelectedItemPosition() == 0) {
                    Toast.makeText(getContext(), "Please Select Company Driver",
                            Toast.LENGTH_LONG).show();
                } else if (spn_vehicle.getSelectedItemPosition() == 0) {
                    Toast.makeText(getContext(), "Please Select Vehicle",
                            Toast.LENGTH_LONG).show();
                } else {
                    postCompanyDelivery();
                }
            }
        });

        btn_assign_employee2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spn_order_responsible.getSelectedItemPosition() == 0) {
                    Toast.makeText(getContext(), "Please Select Order Responsible",
                            Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(et_driver_name.getText().toString())) {
                    et_driver_name.setError("Please Enter Driver Name");
                    et_driver_name.requestFocus();
                } else if (TextUtils.isEmpty(et_vehicle_no.getText().toString())) {
                    et_vehicle_no.setError("Please Enter Vehicle No");
                    et_vehicle_no.requestFocus();
                } else if (TextUtils.isEmpty(et_driver_nic_no.getText().toString())) {
                    et_driver_nic_no.setError("Please Enter Driver CNIC");
                    et_driver_nic_no.requestFocus();
                } else if (TextUtils.isEmpty(et_driver_contact_no.getText().toString())) {
                    et_driver_contact_no.setError("Please Enter Driver Contact No");
                    et_driver_contact_no.requestFocus();
                } else {
                    postCustomerSelfPickup();
                }
            }
        });

        btn_assign_employee3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spn_order_responsible.getSelectedItemPosition() == 0) {
                    Toast.makeText(getContext(), "Please Select Order Responsible",
                            Toast.LENGTH_LONG).show();
                } else if (spn_agent.getSelectedItemPosition() == 0) {
                    Toast.makeText(getContext(), "Please Select Agent",
                            Toast.LENGTH_LONG).show();
                } else {
                    postAgentsDelivery();
                }
            }
        });
    }

    public void getOrderResponsible() {

        String URL = "http://172.16.10.202:8000/api/getEmployeeVehicleAgent/" + created_by;
        StringRequest req = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            arr_order_responsible.add("Select Employee for Order Responsibility");
                            arr_order_responsible_id.add("0");
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("OrderResposible");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                JSONObject innerObject1 = object.getJSONObject("findemployee");
                                JSONObject innerObject2 = innerObject1.getJSONObject("findempdesignation");
                                String designation_name = innerObject2.getString("designation_name");
                                if (designation_name.equals("Order Responsible")) {
                                    String name = innerObject1.getString("name");
                                    String id = innerObject1.getString("id");
                                    arr_order_responsible.add(name);
                                    arr_order_responsible_id.add(id);
                                }
                            }
                            spn_order_responsible.setAdapter(new ArrayAdapter<>(getContext(),
                                    android.R.layout.simple_spinner_dropdown_item, arr_order_responsible));
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

    public void getCompanyDriver() {
        String URL = "http://172.16.10.202:8000/api/getEmployeeVehicleAgent/" + created_by;
        StringRequest req = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            arr_company_driver.add("Select Company's Driver");
                            arr_company_driver_id.add("0");
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("OrderResposible");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                JSONObject innerObject1 = object.getJSONObject("findemployee");
                                JSONObject innerObject2 = innerObject1.getJSONObject("findempdesignation");
                                String designation_name = innerObject2.getString("designation_name");
                                if (designation_name.equals("Driver") || designation_name.equals("Rider")) {
                                    String name = innerObject1.getString("name");
                                    String id = innerObject1.getString("id");
                                    arr_company_driver.add(name);
                                    arr_company_driver_id.add(id);
                                }
                            }
                            spn_company_driver.setAdapter(new ArrayAdapter<>(getContext(),
                                    android.R.layout.simple_spinner_dropdown_item, arr_company_driver));
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

    public void getCompanyVehicle() {
        String URL = "http://172.16.10.202:8000/api/getEmployeeVehicleAgent/" + created_by;
        StringRequest req = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            arr_vehicle.add("Select Vehicle");
                            arr_vehicle_id.add("0");
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("orderVehicle");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                String id = object.getString("id");
                                String veh_name = object.getString("veh_name");
                                arr_vehicle.add(veh_name);
                                arr_vehicle_id.add(id);
                            }
                            spn_vehicle.setAdapter(new ArrayAdapter<>(getContext(),
                                    android.R.layout.simple_spinner_dropdown_item, arr_vehicle));
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

    public void getAgent() {
        String URL = "http://172.16.10.202:8000/api/getEmployeeVehicleAgent/" + created_by;
        StringRequest req = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            arr_agent.add("Select Agent");
                            arr_agent_id.add("0");
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("orderAgent");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                String id = object.getString("id");
                                String name = object.getString("name");
                                arr_agent.add(name);
                                arr_agent_id.add(id);
                            }
                            spn_agent.setAdapter(new ArrayAdapter<>(getContext(),
                                    android.R.layout.simple_spinner_dropdown_item, arr_agent));
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

    public void postCompanyDelivery(){
        String URL = "http://172.16.10.202:8000/api/assignOrderEmployee";
        StringRequest req = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String msg = jsonObject.getString("msg");
                            if (msg.equals("success")){
                                Fragment fragment = new Fragment_MyOrders();
                                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                ft.replace(R.id.content_main,fragment);
                                ft.addToBackStack(null);
                                ft.commit();
                            } else {
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
                        Toast.makeText(getContext(),error.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("optradio", "customer_pickup");
                map.put("order_id", order_id);
                map.put("customer_id", created_by);
                map.put("employee_id", order_responsible);
                map.put("vehicle_id", vehicle);
                map.put("driver_id", company_driver);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(req);
    }

    public void postCustomerSelfPickup(){
        String URL = "http://172.16.10.202:8000/api/assignOrderEmployee";
        StringRequest req = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String msg = jsonObject.getString("msg");
                            if (msg.equals("success")){
                                Fragment fragment = new Fragment_MyOrders();
                                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                ft.replace(R.id.content_main,fragment);
                                ft.addToBackStack(null);
                                ft.commit();
                            } else {
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
                        Toast.makeText(getContext(),error.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("optradio", "customer_pickup");
                map.put("order_id", order_id);
                map.put("customer_id", created_by);
                map.put("employee_id", order_responsible);
                map.put("driver_name", et_driver_name.getText().toString());
                map.put("veh_no", et_vehicle_no.getText().toString());
                map.put("driver_nic", et_driver_nic_no.getText().toString());
                map.put("driver_contact", et_driver_contact_no.getText().toString());
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(req);
    }

    public void postAgentsDelivery(){
        String URL = "http://172.16.10.202:8000/api/assignOrderEmployee";
        StringRequest req = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String msg = jsonObject.getString("msg");
                            if (msg.equals("success")){
                                Fragment fragment = new Fragment_MyOrders();
                                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                ft.replace(R.id.content_main,fragment);
                                ft.addToBackStack(null);
                                ft.commit();
                            } else {
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
                        Toast.makeText(getContext(),error.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("optradio", "agent_delivery");
                map.put("order_id", order_id);
                map.put("customer_id", created_by);
                map.put("employee_id", order_responsible);
                map.put("product_holder_id", agent);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(req);
    }
}
