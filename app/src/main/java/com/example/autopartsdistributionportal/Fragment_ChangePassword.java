package com.example.autopartsdistributionportal;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import dmax.dialog.SpotsDialog;

public class Fragment_ChangePassword extends Fragment {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String id;
    private Button btn_pswrd_update;
    private EditText et_oldpassword, et_newpassword, et_cpassword;
    private SweetAlertDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_changepassword, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPreferences = getActivity().getSharedPreferences("MyPre", Context.MODE_PRIVATE);
        id = sharedPreferences.getString("userid","");

        et_oldpassword = view.findViewById(R.id.et_oldpassword);
        et_newpassword = view.findViewById(R.id.et_newpassword);
        et_cpassword = view.findViewById(R.id.et_cpassword);
        btn_pswrd_update = view.findViewById(R.id.btn_pswrd_update);
        btn_pswrd_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkValidation();
            }
        });
    }

    public void checkValidation() {

        if (TextUtils.isEmpty(et_oldpassword.getText().toString())) {
            et_oldpassword.setError("Please enter old password");
            et_oldpassword.requestFocus();
        } else if (TextUtils.isEmpty(et_newpassword.getText().toString())) {
            et_newpassword.setError("Please enter new password");
            et_newpassword.requestFocus();
        } else if (TextUtils.isEmpty(et_cpassword.getText().toString())) {
            et_cpassword.setError("Please enter confirm password");
            et_cpassword.requestFocus();
        } else if (!et_newpassword.getText().toString().equals(et_cpassword.getText().toString())) {
            et_newpassword.setError("Password not match");
            et_newpassword.requestFocus();
        } else {
            changePassword();
        }
    }

    public void changePassword() {

        progressDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        progressDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        progressDialog.setTitleText("Please Wait");
        progressDialog.setContentText("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String URL = Activity_Login.IP + "changePassword";
        StringRequest req = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String msg = jsonObject.getString("msg");
                            if (msg.equals("success")){
                                progressDialog.dismissWithAnimation();
                                Toast.makeText(getContext(),"Please Login to continue",
                                        Toast.LENGTH_LONG).show();
                                editor = sharedPreferences.edit();
                                editor.putBoolean("saveLogin", false);
                                editor.apply();

                                startActivity(new Intent(getContext(),Activity_Login.class));

                            } else {
                                progressDialog.dismissWithAnimation();
                                Toast.makeText(getContext(), msg,
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
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("id", id);
                map.put("old_pass", et_oldpassword.getText().toString());
                map.put("password", et_newpassword.getText().toString());
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(req);
    }
}
