package com.example.autopartsdistributionportal;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ShareActionProvider;
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

import dmax.dialog.SpotsDialog;

public class Activity_Login extends AppCompatActivity {

    EditText et_username, et_password;
    Button btn_login;
    public static String IP = "http://172.16.10.202:8000/api/";
    String Login_URL = Activity_Login.IP + "login";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    AlertDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__login);

        sharedPreferences = getSharedPreferences("MyPre", Context.MODE_PRIVATE);

        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        btn_login = findViewById(R.id.btn_login);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isNetworkAvailable();
            }
        });
    }

    public void isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            CheckFieldValidation();
        } else if (networkInfo == null) {
            Toast.makeText(Activity_Login.this, "No internet Connection",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void CheckFieldValidation() {

        //Toast.makeText(getApplicationContext(),et_username.getText().toString(),Toast.LENGTH_LONG).show();

        if (TextUtils.isEmpty(et_username.getText().toString())) {
            et_username.setError("Please enter Username");
            et_username.requestFocus();
        } else if (TextUtils.isEmpty(et_password.getText().toString())) {
            et_password.setError("Please enter Password");
            et_password.requestFocus();
        } else {
            userLogin();
        }
    }

    public void userLogin() {

        progressDialog = new SpotsDialog(this,R.style.CustomProgress);
        progressDialog.show();
        progressDialog.setCancelable(false);

        StringRequest req = new StringRequest(Request.Method.POST, Login_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String msg = jsonObject.getString("msg");
                            if (msg.equals("success")) {
                                progressDialog.dismiss();
                                editor = sharedPreferences.edit();
                                JSONObject object = jsonObject.getJSONObject("customer");
                                String id = object.getString("id");
                                String fname = object.getString("name");
                                String lname = object.getString("lname");
                                String username = object.getString("username");
                                String email = object.getString("email");
                                editor.putString("userid", id);
                                editor.putString("fname", fname);
                                editor.putString("lname", lname);
                                editor.putString("username", username);
                                editor.putString("email", email);
                                editor.putBoolean("saveLogin", true);
                                editor.apply();
                                finish();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), msg,
                                        Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error){
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), error.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("username", et_username.getText().toString());
                map.put("password", et_password.getText().toString());
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(req);
    }
}
