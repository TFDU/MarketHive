package com.flagcamp.secondhands.ui.order;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.flagcamp.secondhands.CurrentUserSingleton;
import com.flagcamp.secondhands.R;
import com.flagcamp.secondhands.databinding.OrderRequestItemBinding;
import com.flagcamp.secondhands.model.OrderRequest;

import java.util.ArrayList;
import java.util.List;

public class OrderRequestAdapter extends RecyclerView.Adapter<OrderRequestAdapter.OrderRequestViewHolder>{

    // 1. Supporting data:
    private List<OrderRequest> requests = new ArrayList<>();
    private OrderRequestAdapter.ItemCallback itemCallback;

    public void setRequests(List<OrderRequest> requestList) {
        requests.clear();
        requests.addAll(requestList);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        requests.remove(position);
        notifyItemRemoved(position);
    }

    interface ItemCallback {
        void onOpenDetails(OrderRequest orderRequest);
        void onOpenProfile(OrderRequest orderRequest);
        void onAccept(OrderRequest orderRequest, int position);
        void onDecline(OrderRequest orderRequest, int position);
    }

    public void setItemCallback(OrderRequestAdapter.ItemCallback itemCallback) {
        this.itemCallback = itemCallback;
    }

    // 2. Adapter overrides:
    // TODO
    @NonNull
    @Override
    public OrderRequestAdapter.OrderRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_request_item, parent, false);
        return new OrderRequestAdapter.OrderRequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderRequestAdapter.OrderRequestViewHolder holder, int position) {
        OrderRequest orderRequest = requests.get(position);
        holder.itemnameTextView.setText(orderRequest.product.productName);
        holder.buyernameTextView.setText(orderRequest.user.name);
        holder.itemView.setOnClickListener(v -> itemCallback.onOpenDetails(orderRequest));
        holder.acceptButton.setOnClickListener(v -> itemCallback.onAccept(orderRequest, position));
        holder.declineButton.setOnClickListener(v -> itemCallback.onDecline(orderRequest, position));
        holder.profileButton.setOnClickListener(v -> {
            CurrentUserSingleton currentUser = CurrentUserSingleton.getInstance();
            if (orderRequest.user.userUID.equals(currentUser.getUserId())) {
                itemCallback.onOpenProfile(orderRequest);
            }
        });
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }


    // 3. SavedNewsViewHolder:
    // TODO
    public static class OrderRequestViewHolder extends RecyclerView.ViewHolder {

        TextView itemnameTextView;
        TextView buyernameTextView;
        Button acceptButton;
        Button declineButton;
        Button profileButton;


        public OrderRequestViewHolder(@NonNull View itemView) {
            super(itemView);
            OrderRequestItemBinding binding = OrderRequestItemBinding.bind(itemView);
            itemnameTextView = binding.orderRequestItemItemNameContent;
            buyernameTextView = binding.orderRequestItemBuyerNameContent;
            acceptButton = binding.orderRequestAcceptButton;
            declineButton = binding.orderRequestDeclineButton;
            profileButton = binding.orderRequestBuyerProfileButton;
        }
    }
}
