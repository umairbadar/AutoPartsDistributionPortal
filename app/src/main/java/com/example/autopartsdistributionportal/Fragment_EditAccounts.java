package com.example.autopartsdistributionportal;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Fragment_EditAccounts extends Fragment {

    SweetAlertDialog progressDialog;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private Button btn_update;
    private EditText et_fname,et_lname,et_username,et_email;
    private String fname,lname,username,email,id;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_editaccounts, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPreferences = getActivity().getSharedPreferences("MyPre", Context.MODE_PRIVATE);

        et_fname  = view.findViewById(R.id.et_fname);
        et_lname  = view.findViewById(R.id.et_lname);
        et_email  = view.findViewById(R.id.et_email);
        et_username  = view.findViewById(R.id.et_username);

        id = sharedPreferences.getString("userid","");
        fname = sharedPreferences.getString("fname","");
        lname = sharedPreferences.getString("lname","");
        email = sharedPreferences.getString("email","");
        username = sharedPreferences.getString("username","");

        et_fname.setText(fname);
        et_lname.setText(lname);
        et_username.setText(username);
        et_email.setText(email);

        btn_update = view.findViewById(R.id.btn_update);
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(et_fname.getText().toString())){
                    et_fname.setError("Please Enter First Name");
                    et_fname.requestFocus();
                } else if (TextUtils.isEmpty(et_lname.getText().toString())){
                    et_lname.setError("Please Enter Last Name");
                    et_lname.requestFocus();
                } else if (TextUtils.isEmpty(et_username.getText().toString())){
                    et_username.setError("Please Enter Username");
                    et_username.requestFocus();
                } else if (TextUtils.isEmpty(et_email.getText().toString())){
                    et_email.setError("Please enter Email Address");
                    et_email.requestFocus();
                } else if (!et_email.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")){
                    progressDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE);
                    progressDialog.setTitleText("Error");
                    progressDialog.setContentText("Please Enter Valid Email Address");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                } else {
                    updateUserData();
                }

            }
        });
    }

    public void updateUserData(){

        progressDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        progressDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        progressDialog.setTitleText("Please Wait");
        progressDialog.setContentText("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String URL = Activity_Login.IP + "updateAccount";

        StringRequest req = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String msg = jsonObject.getString("msg");
                            if (msg.equals("success")){
                                progressDialog.dismissWithAnimation();
                                editor = sharedPreferences.edit();
                                editor.putString("fname", et_fname.getText().toString());
                                editor.putString("lname", et_lname.getText().toString());
                                editor.putString("username", et_username.getText().toString());
                                editor.putString("email", et_email.getText().toString());
                                editor.apply();

                                progressDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE);
                                progressDialog.setTitleText("Success");
                                progressDialog.setContentText("Data Updated");
                                progressDialog.setCancelable(false);
                                progressDialog.show();

                                Fragment fragment = new Fragment_EditAccounts();
                                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                ft.replace(R.id.content_main, fragment);
                                ft.addToBackStack(null);
                                ft.commit();

                            } else {
                                progressDialog.dismissWithAnimation();
                                Toast.makeText(getContext(),"Error",
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
                        Toast.makeText(getContext(), error.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("id", id);
                map.put("name", et_fname.getText().toString());
                map.put("lname", et_lname.getText().toString());
                map.put("email", et_email.getText().toString());
                map.put("username", et_username.getText().toString());
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(req);
    }
}
