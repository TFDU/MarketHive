package com.flagcamp.secondhands.ui.order;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.flagcamp.secondhands.R;
import com.flagcamp.secondhands.databinding.MyRequestItemBinding;
import com.flagcamp.secondhands.model.OrderRequest;

import java.util.ArrayList;
import java.util.List;

public class MyRequestAdapter extends RecyclerView.Adapter<MyRequestAdapter.MyRequestViewHolder>{
    // 1. Supporting data:
    private List<OrderRequest> orderRequests = new ArrayList<>();
    private MyRequestAdapter.ItemCallback itemCallback;

    public void setOrderRequests(List<OrderRequest> orderRequestList) {
        orderRequests.clear();
        orderRequests.addAll(orderRequestList);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        orderRequests.remove(position);
        notifyItemRemoved(position);
    }

    interface ItemCallback {
        void onOpenDetails(OrderRequest myOrderRequest);
        void onCancel(OrderRequest myOrderRequest, int position);
    }

    public void setItemCallback(MyRequestAdapter.ItemCallback itemCallback) {
        this.itemCallback = itemCallback;
    }

    public MyRequestAdapter.MyRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_request_item, parent, false);
        return new MyRequestAdapter.MyRequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRequestAdapter.MyRequestViewHolder holder, int position) {
        OrderRequest myOrderRequest = orderRequests.get(position);
        holder.itemnameTextView.setText(myOrderRequest.product.productName);
        holder.sellernameTextView.setText(myOrderRequest.product.user.name);
        holder.itemView.setOnClickListener(v -> itemCallback.onOpenDetails(myOrderRequest));
        holder.cancelButton.setOnClickListener(v -> itemCallback.onCancel(myOrderRequest, position));
    }

    @Override
    public int getItemCount() {
        return orderRequests.size();
    }


    public static class MyRequestViewHolder extends RecyclerView.ViewHolder {

        TextView itemnameTextView;
        TextView sellernameTextView;
        Button cancelButton;


        public MyRequestViewHolder(@NonNull View itemView) {
            super(itemView);
            MyRequestItemBinding binding = MyRequestItemBinding.bind(itemView);
            itemnameTextView = binding.myRequestItemItemNameContent;
            sellernameTextView = binding.myRequestItemSellerNameContent;
            cancelButton = binding.myRequestCancelButton;
        }
    }

}
