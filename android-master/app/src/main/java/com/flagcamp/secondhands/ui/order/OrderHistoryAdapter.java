package com.flagcamp.secondhands.ui.order;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.flagcamp.secondhands.R;
import com.flagcamp.secondhands.databinding.OrderHistoryItemBinding;
import com.flagcamp.secondhands.model.Order;

import java.util.ArrayList;
import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderHistoryViewHolder>{

    // 1. Supporting data:
    private List<Order> orders = new ArrayList<>();
    private ItemCallback itemCallback;

    public void setOrders(List<Order> orderList) {
        if (orderList != null) {
            orders.clear();
            orders.addAll(orderList);
            notifyDataSetChanged();
        }
    }

    interface ItemCallback {
        void onOpenDetails(Order order);
        void onRating(RatingBar ratingBar, Order order);
    }

    public void setItemCallback(ItemCallback itemCallback) {
        this.itemCallback = itemCallback;
    }

    // 2. Adapter overrides:
    // TODO
    @NonNull
    @Override
    public OrderHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_history_item, parent, false);
        return new OrderHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryViewHolder holder, int position) {
        Order order = orders.get(position);
        holder.itemnameTextView.setText(order.product.productName);
        holder.sellernameTextView.setText(order.product.user.name);
        holder.itemView.setOnClickListener(v -> itemCallback.onOpenDetails(order));
        if (order.rating == 0) {
            Log.d("onOpenDetails", String.valueOf(holder.ratingBar.getRating()));
            holder.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    itemCallback.onRating(ratingBar, order);
                }
            });
        }
        else {
            holder.ratingBar.setIsIndicator(true);
            holder.ratingBar.setRating((float)order.rating);
        }

    }

    @Override
    public int getItemCount() {
        return orders.size();
    }


    // 3. SavedNewsViewHolder:
    // TODO
    public static class OrderHistoryViewHolder extends RecyclerView.ViewHolder {

        TextView itemnameTextView;
        TextView sellernameTextView;
        RatingBar ratingBar;

        public OrderHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            OrderHistoryItemBinding binding = OrderHistoryItemBinding.bind(itemView);
            itemnameTextView = binding.orderHistoryItemItemNameContent;
            sellernameTextView = binding.orderHistoryItemSellerNameContent;
            ratingBar = binding.orderHistoryItemRatingbar;
        }
    }
}
