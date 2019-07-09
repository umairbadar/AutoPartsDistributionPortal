package com.example.autopartsdistributionportal;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class Adapter_ViewTransactions extends RecyclerView.Adapter<Adapter_ViewTransactions.ViewHolder> {

    List<Model_ViewTransactions> list;
    Context context;

    public Adapter_ViewTransactions(List<Model_ViewTransactions> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_viewtransactions, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {

        Model_ViewTransactions item = list.get(i);

        holder.tv_orderRef.setText("Reference Number: " + item.getRef_num());
        holder.tv_previousBalance.setText("Previous Balance: " + item.getPrevious_balance());
        holder.tv_addedAmount.setText("Added Amount: " + item.getAdded_Amount());
        holder.tv_updatedBalance.setText("Updated Balance: " + item.getUpdated_Balance());
        holder.tv_paymentMethod.setText("Payment Method: " + item.getPayment_Method());
        holder.tv_paymentMode.setText("Payment Mode: " + item.getPayment_Mode());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tv_orderRef, tv_previousBalance, tv_addedAmount, tv_updatedBalance, tv_paymentMethod, tv_paymentMode;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_orderRef = itemView.findViewById(R.id.tv_orderRef);
            tv_previousBalance = itemView.findViewById(R.id.tv_previousBalance);
            tv_addedAmount = itemView.findViewById(R.id.tv_addedAmount);
            tv_updatedBalance = itemView.findViewById(R.id.tv_updatedBalance);
            tv_paymentMethod = itemView.findViewById(R.id.tv_paymentMethod);
            tv_paymentMode = itemView.findViewById(R.id.tv_paymentMode);
        }
    }
}
