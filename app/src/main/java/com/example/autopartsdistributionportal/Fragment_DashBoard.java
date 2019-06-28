package com.example.autopartsdistributionportal;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Fragment_DashBoard extends Fragment {

    private TextView tv_pending_count,tv_process_count,tv_enroute_count,tv_delivered_count;
    private SharedPreferences sharedPreferences;
    private String id;
    private SwipeRefreshLayout pullToRefresh;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pullToRefresh = view.findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getCount(); // your code
                pullToRefresh.setRefreshing(false);
            }
        });

        sharedPreferences = getActivity().getSharedPreferences("MyPre", Context.MODE_PRIVATE);
        id = sharedPreferences.getString("userid","");

        tv_pending_count = view.findViewById(R.id.tv_pending_count);
        tv_process_count = view.findViewById(R.id.tv_process_count);
        tv_enroute_count = view.findViewById(R.id.tv_enroute_count);
        tv_delivered_count = view.findViewById(R.id.tv_delivered_count);

        getCount();
    }

    public void getCount(){

        String URL = Activity_Login.IP + "statusCount/" + id;

        StringRequest req = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String msg = jsonObject.getString("msg");
                            if (msg.equals("success")){
                                tv_pending_count.setText(jsonObject.getString("pendingstatus"));
                                tv_process_count.setText(jsonObject.getString("processstatus"));
                                tv_enroute_count.setText(jsonObject.getString("enroutestatus"));
                                tv_delivered_count.setText(jsonObject.getString("deliveredstatus"));
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
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(req);
    }
}
