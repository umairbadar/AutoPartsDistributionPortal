package com.example.autopartsdistributionportal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Fragment_Add_Product_Holder extends Fragment {

    private SharedPreferences sharedPreferences;
    private PlaceAutocompleteFragment places;
    private String customer_id, condition, type_id, t_id, group_id, status_id, placename, p_id = "0", c_id = "0", lat, lng, a_id, g_id, s_id = "0";
    private TextView tv_head, tv_password, tv_cpassword, tv_address, tv_lat, tv_lng;
    private Spinner spn_supplier_type, spn_supplier_group, spn_status, spn_province, spn_city, spn_area;
    private EditText et_name, et_email, et_phone, et_nic, et_joining_date, et_username, et_password, et_cpassword, et_zip_code;
    private ArrayList<String> arr_supplier_type;
    private ArrayList<String> arr_supplier_type_id;
    private ArrayList<String> arr_supplier_group;
    private ArrayList<String> arr_supplier_group_id;
    private ArrayList<String> arr_status;
    private ArrayList<String> arr_province;
    private ArrayList<String> arr_province_id;
    private ArrayList<String> arr_city_id;
    private ArrayList<String> arr_city;
    private ArrayList<String> arr_area;
    private ArrayList<String> arr_area_id;
    private Button btn_add;
    private static View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        /*View view = inflater.inflate(R.layout.fragment_add_product_holder, container, false);
        return view;*/

        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.fragment_add_product_holder, container, false);
        } catch (InflateException e) {
            /* map is already there, just return view as it is */
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPreferences = getActivity().getSharedPreferences("MyPre", Context.MODE_PRIVATE);
        customer_id = sharedPreferences.getString("userid", "");

        places = (PlaceAutocompleteFragment) getActivity().getFragmentManager().
                findFragmentById(R.id.place_autocomplete_fragment);
        AutocompleteFilter autocompleteFilter = new AutocompleteFilter.Builder().setCountry("PK").build();
        places.setFilter(autocompleteFilter);
        places.getView().setBackgroundColor(Color.parseColor("#DDDDDD"));

        btn_add = view.findViewById(R.id.btn_submit);
        tv_lat = view.findViewById(R.id.tv_latitude);
        tv_lng = view.findViewById(R.id.tv_longitude);
        tv_address = view.findViewById(R.id.tv_address);
        tv_head = view.findViewById(R.id.tv_head);
        tv_password = view.findViewById(R.id.tv_password);
        tv_cpassword = view.findViewById(R.id.tv_cpassword);
        et_name = view.findViewById(R.id.et_name);
        et_email = view.findViewById(R.id.et_email);
        et_phone = view.findViewById(R.id.et_phone);
        et_nic = view.findViewById(R.id.et_nic);
        et_joining_date = view.findViewById(R.id.et_joining_date);
        et_username = view.findViewById(R.id.et_username);
        et_password = view.findViewById(R.id.et_password);
        et_cpassword = view.findViewById(R.id.et_cpassword);
        et_zip_code = view.findViewById(R.id.et_zip_code);

        //Spinners
        spn_area = view.findViewById(R.id.spn_area);
        arr_area = new ArrayList<>();
        arr_area_id = new ArrayList<>();

        spn_province = view.findViewById(R.id.spn_province);
        arr_province = new ArrayList<>();
        arr_province_id = new ArrayList<>();
        //getCities();

        spn_city = view.findViewById(R.id.spn_city);
        arr_city = new ArrayList<>();
        arr_city_id = new ArrayList<>();
        getProvinces();


        spn_supplier_type = view.findViewById(R.id.spn_supplier_type);
        arr_supplier_type = new ArrayList<>();
        arr_supplier_type_id = new ArrayList<>();
        getSupplierTypes();

        spn_supplier_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                t_id = arr_supplier_type_id.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spn_supplier_group = view.findViewById(R.id.spn_supplier_group);
        arr_supplier_group = new ArrayList<>();
        arr_supplier_group_id = new ArrayList<>();
        getSupplierGroups();

        spn_supplier_group.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                g_id = arr_supplier_group_id.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spn_status = view.findViewById(R.id.spn_status);
        arr_status = new ArrayList<>();
        arr_status.add("Select Status");
        arr_status.add("Active");
        arr_status.add("Inactive");
        spn_status.setAdapter(new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, arr_status));

        spn_status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spn_status.getSelectedItem() == "Active") {
                    s_id = "1";
                } else if (spn_status.getSelectedItem() == "Inactive") {
                    s_id = "2";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        places.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                placename = (String) place.getAddress();
                tv_address.setText(placename);
                lat = String.valueOf(place.getLatLng().latitude);
                lng = String.valueOf(place.getLatLng().longitude);
                tv_lat.setText(lat);
                tv_lng.setText(lng);

            }

            @Override
            public void onError(Status status) {

            }
        });


        spn_province.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                p_id = arr_province_id.get(i);
                if (!p_id.equals("0")) {
                    arr_city.clear();
                    arr_city_id.clear();
                    getCities();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spn_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                c_id = arr_city_id.get(i);
                if (!c_id.equals("0")) {
                    arr_area.clear();
                    arr_area_id.clear();
                    getAreas();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spn_area.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                a_id = arr_area_id.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Validation();
            }
        });
    }

    public void getSupplierTypes() {

        String URL = "http://172.16.10.202:8000/api/getSupplierTypesandGroups";
        StringRequest req = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            arr_supplier_type.add("Select Supplier Type");
                            arr_supplier_type_id.add("0");
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("customertypes");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                String name = object.getString("name");
                                String id = object.getString("id");
                                arr_supplier_type.add(name);
                                arr_supplier_type_id.add(id);
                            }
                            spn_supplier_type.setAdapter(new ArrayAdapter<>(getContext(),
                                    android.R.layout.simple_spinner_dropdown_item, arr_supplier_type));

                            if (getArguments() != null) {

                                et_name.setText(getArguments().getString("name"));
                                et_email.setText(getArguments().getString("email"));
                                et_phone.setText(getArguments().getString("phone"));
                                et_nic.setText(getArguments().getString("nic"));

                                condition = getArguments().getString("condition");

                                type_id = getArguments().getString("type_id");
                                spn_supplier_type.setSelection(Integer.parseInt(type_id) - 4);

                                group_id = getArguments().getString("group_id");
                                status_id = getArguments().getString("status_id");
                                spn_status.setSelection(Integer.parseInt(status_id));

                                if (condition.equals("add")) {
                                    tv_head.setText("Add Product Holder");
                                } else {
                                    tv_head.setText("Edit Product Holder");
                                    et_password.setVisibility(View.GONE);
                                    tv_password.setVisibility(View.GONE);
                                    et_cpassword.setVisibility(View.GONE);
                                    tv_cpassword.setVisibility(View.GONE);
                                }
                            }

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

    public void getSupplierGroups() {

        String URL = "http://172.16.10.202:8000/api/getSupplierTypesandGroups";
        StringRequest req = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            arr_supplier_group.add("Select Supplier Group");
                            arr_supplier_group_id.add("0");
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("customergroups");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                String name = object.getString("name");
                                String id = object.getString("id");
                                arr_supplier_group.add(name);
                                arr_supplier_group_id.add(id);
                            }
                            spn_supplier_group.setAdapter(new ArrayAdapter<>(getContext(),
                                    android.R.layout.simple_spinner_dropdown_item, arr_supplier_group));

                            if (getArguments() != null) {

                                condition = getArguments().getString("condition");

                                type_id = getArguments().getString("type_id");
                                //spn_supplier_type.setSelection(Integer.parseInt(type_id) - 4);

                                group_id = getArguments().getString("group_id");
                                status_id = getArguments().getString("status_id");
                                spn_supplier_group.setSelection(Integer.parseInt(group_id));

                                if (condition.equals("add")) {
                                    tv_head.setText("Add Product Holder");
                                } else {
                                    tv_head.setText("Edit Product Holder");
                                    et_password.setVisibility(View.GONE);
                                    tv_password.setVisibility(View.GONE);
                                    et_cpassword.setVisibility(View.GONE);
                                    tv_cpassword.setVisibility(View.GONE);
                                }
                            }

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

    public void getProvinces() {

        String URL = "http://172.16.10.202:8000/api/getProvinces";
        StringRequest req = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            arr_province.add("Select Province");
                            arr_province_id.add("0");
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("provinces");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                String id = object.getString("id");
                                String name = object.getString("name");
                                arr_province.add(name);
                                arr_province_id.add(id);
                            }

                            spn_province.setAdapter(new ArrayAdapter<>(getContext(),
                                    android.R.layout.simple_spinner_dropdown_item, arr_province));
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

    public void getCities() {

        String URL = "http://172.16.10.202:8000/api/getCities/" + p_id;
        StringRequest req = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            arr_city_id.add("0");
                            arr_city.add("Select City");
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("cities");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                String id = object.getString("id");
                                String name = object.getString("name");
                                arr_city_id.add(id);
                                arr_city.add(name);
                            }

                            spn_city.setAdapter(new ArrayAdapter<>(getContext(),
                                    android.R.layout.simple_spinner_dropdown_item, arr_city));
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

    public void getAreas() {

        String URL = "http://172.16.10.202:8000/api/getAreas/1";
        StringRequest req = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            arr_area_id.add("0");
                            arr_area.add("Select City");
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("areas");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                String id = object.getString("id");
                                String name = object.getString("name");
                                arr_area_id.add(id);
                                arr_area.add(name);
                            }

                            spn_area.setAdapter(new ArrayAdapter<>(getContext(),
                                    android.R.layout.simple_spinner_dropdown_item, arr_area));
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

    public void Validation() {

        if (spn_supplier_type.getSelectedItemPosition() == 0) {
            Toast.makeText(getContext(), "Please Select Supplier Type",
                    Toast.LENGTH_LONG).show();
        } else if (spn_supplier_group.getSelectedItemPosition() == 0) {
            Toast.makeText(getContext(), "Please Select Supplier Group",
                    Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(et_name.getText().toString())) {
            et_name.setError("Please enter Name");
            et_name.requestFocus();
        } else if (TextUtils.isEmpty(et_email.getText().toString())) {
            et_email.setError("Please enter Email");
            et_email.requestFocus();
        } else if (TextUtils.isEmpty(et_phone.getText().toString())) {
            et_phone.setError("Please enter Phone");
            et_phone.requestFocus();
        } else if (TextUtils.isEmpty(et_nic.getText().toString())) {
            et_nic.setError("Please enter NIC/NTN Number");
            et_nic.requestFocus();
        } else if (TextUtils.isEmpty(et_joining_date.getText().toString())) {
            et_joining_date.setError("Please enter Joining Date");
            et_joining_date.requestFocus();
        } else if (TextUtils.isEmpty(et_username.getText().toString())) {
            et_username.setError("Please enter Username");
            et_username.requestFocus();
        } else if (TextUtils.isEmpty(et_password.getText().toString())) {
            et_password.setError("Please enter Password");
            et_password.requestFocus();
        } else if (TextUtils.isEmpty(et_cpassword.getText().toString())) {
            et_cpassword.setError("Please enter Confirm Password");
            et_cpassword.requestFocus();
        } else if (!et_password.getText().toString().equals(et_cpassword.getText().toString())) {
            et_password.setError("Password not match");
            et_password.requestFocus();
        } else if (spn_status.getSelectedItemPosition() == 0) {
            Toast.makeText(getContext(), "Please Select Status",
                    Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(tv_address.getText().toString().trim())) {
            Toast.makeText(getContext(), "Please enter Address",
                    Toast.LENGTH_LONG).show();

        } else if (spn_province.getSelectedItemPosition() == 0) {
            Toast.makeText(getContext(), "Please select Province",
                    Toast.LENGTH_LONG).show();
        } else if (spn_city.getSelectedItemPosition() == 0) {
            Toast.makeText(getContext(), "Please select City",
                    Toast.LENGTH_LONG).show();
        } else if (spn_area.getSelectedItemPosition() == 0) {
            Toast.makeText(getContext(), "Please select Area",
                    Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(et_zip_code.getText().toString())) {
            et_zip_code.setError("Please enter Zip Code");
            et_zip_code.requestFocus();
        } else {
            /*Toast.makeText(getContext(), p_id + " " + c_id + " " + a_id,
                    Toast.LENGTH_LONG).show();*/
            postData();
        }
    }

    public void postData() {

        String URL = "http://172.16.10.202:8000/api/insertProductAgent";
        StringRequest req = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String msg = jsonObject.getString("msg");
                            if (msg.equals("success")) {
                                Fragment fragment = new Fragment_Product_Holder();
                                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                ft.replace(R.id.content_main, fragment);
                                ft.addToBackStack(null);
                                ft.commit();
                            }
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
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("customer_id", customer_id);
                map.put("customer_type_id", t_id);
                map.put("customer_group_id", g_id);
                map.put("name", et_name.getText().toString().trim());
                map.put("email", et_email.getText().toString().trim());
                map.put("phone", et_phone.getText().toString().trim());
                map.put("nic", et_nic.getText().toString().trim());
                map.put("joiningdate", et_joining_date.getText().toString().trim());
                map.put("username", et_username.getText().toString().trim());
                map.put("password", et_password.getText().toString().trim());
                map.put("status", s_id);
                map.put("address", tv_address.getText().toString());
                map.put("province_id", p_id);
                map.put("city_id", c_id);
                map.put("area_id", a_id);
                map.put("longitude", tv_lng.getText().toString());
                map.put("latitude", tv_lat.getText().toString());
                map.put("zipcode", et_zip_code.getText().toString());
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(req);
    }
}
