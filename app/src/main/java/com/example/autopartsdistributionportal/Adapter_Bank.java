package com.example.autopartsdistributionportal;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class Adapter_Bank extends RecyclerView.Adapter<Adapter_Bank.ViewHolder> {

    List<Model_Bank> list;
    Context context;

    public Adapter_Bank(List<Model_Bank> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bank, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        Model_Bank item = list.get(i);

        holder.tv_customerName.setText("Customer Name: " + item.getAccount_title());
        holder.tv_bankName.setText("Bank Name: " + item.getBank_name());
        holder.tv_branchName.setText("Branch Name: " + item.getBranch_name());
        holder.tv_accountNo.setText("Account No: " + item.getAccount_no());
        holder.tv_accountBalance.setText("Account Balance: Rs." + item.getAccount_balance());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tv_customerName,tv_bankName,tv_branchName,tv_accountNo,tv_accountBalance;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_customerName = itemView.findViewById(R.id.tv_customerName);
            tv_bankName = itemView.findViewById(R.id.tv_bankName);
            tv_branchName = itemView.findViewById(R.id.tv_branchName);
            tv_accountNo = itemView.findViewById(R.id.tv_accountNo);
            tv_accountBalance = itemView.findViewById(R.id.tv_accountBalance);
        }
    }
}
