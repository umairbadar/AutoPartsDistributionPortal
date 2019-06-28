package com.example.autopartsdistributionportal;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class Fragment_MyOrders extends Fragment {
    private RecyclerView recyclerviewmyorder;
    private RecyclerView.Adapter adapter;
    private List<Model_MyOrder> myorders;

    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_myorders, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerviewmyorder = view.findViewById(R.id.recyclerviewmyorder);
        recyclerviewmyorder.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        myorders = new ArrayList<>();
        getorders();

    }

    private void getorders() {

        String order = "1234";
        String customer_name = "Hamza";
        String delivery_date = "29-4-19";
        String priority = "High";
        String Status = "Active";

        Model_MyOrder items = new Model_MyOrder(
                order,
                customer_name,
                delivery_date,
                priority,
                Status

        );
        myorders.add(items);
        adapter = new Adapter_MyOrder(myorders, getContext());
        recyclerviewmyorder.setAdapter(adapter);

    }
}
