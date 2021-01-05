package com.flagcamp.secondhands.ui.order;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.flagcamp.secondhands.R;
import com.flagcamp.secondhands.databinding.SaleHistoryItemBinding;
import com.flagcamp.secondhands.model.Order;

import java.util.ArrayList;
import java.util.List;

public class SaleHistoryAdapter extends RecyclerView.Adapter<SaleHistoryAdapter.SaleHistoryViewHolder>{


    // 1. Supporting data:
    private List<Order> orders = new ArrayList<>();
    private SaleHistoryAdapter.ItemCallback itemCallback;

    public void setOrders(List<Order> orderList) {
        if (orderList != null) {
            orders.clear();
            orders.addAll(orderList);
            notifyDataSetChanged();
        }
    }

    interface ItemCallback {
        void onOpenDetails(Order order);
    }

    public void setItemCallback(SaleHistoryAdapter.ItemCallback itemCallback) {
        this.itemCallback = itemCallback;
    }

    // 2. Adapter overrides:
    // TODO
    @NonNull
    @Override
    public SaleHistoryAdapter.SaleHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sale_history_item, parent, false);
        return new SaleHistoryAdapter.SaleHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SaleHistoryAdapter.SaleHistoryViewHolder holder, int position) {
        Order order = orders.get(position);
        holder.itemnameTextView.setText(order.product.productName);
        holder.buyernameTextView.setText(order.customer.name);
        holder.itemView.setOnClickListener(v -> itemCallback.onOpenDetails(order));
        holder.ratingBar.setIsIndicator(true);
        holder.ratingBar.setRating((float)order.rating);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }


    // 3. SavedNewsViewHolder:
    // TODO
    public static class SaleHistoryViewHolder extends RecyclerView.ViewHolder {

        TextView itemnameTextView;
        TextView buyernameTextView;
        RatingBar ratingBar;

        public SaleHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            SaleHistoryItemBinding binding = SaleHistoryItemBinding.bind(itemView);
            itemnameTextView = binding.saleHistoryItemItemNameContent;
            buyernameTextView = binding.saleHistoryItemBuyerNameContent;
            ratingBar = binding.saleHistoryItemRatingbar;
        }
    }
}
