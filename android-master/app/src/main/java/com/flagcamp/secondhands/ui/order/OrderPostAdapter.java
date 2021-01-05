package com.flagcamp.secondhands.ui.order;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.flagcamp.secondhands.R;
import com.flagcamp.secondhands.databinding.OrderPostItemBinding;
import com.flagcamp.secondhands.model.Product;

import java.util.ArrayList;
import java.util.List;

public class OrderPostAdapter extends RecyclerView.Adapter<OrderPostAdapter.OrderPostViewHolder>{

    private List<Product> posts = new ArrayList<>();
    private OrderPostAdapter.ItemCallback itemCallback;

    public void setPosts(List<Product> postList) {
        posts.clear();
        posts.addAll(postList);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        posts.remove(position);
        notifyItemRemoved(position);
    }

    interface ItemCallback {
        void onOpenDetails(Product product);
        void onDelete(Product product, int position);
    }

    public void setItemCallback(OrderPostAdapter.ItemCallback itemCallback) {
        this.itemCallback = itemCallback;
    }

    // 2. Adapter overrides:
    // TODO
    @NonNull
    @Override
    public OrderPostAdapter.OrderPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_post_item, parent, false);
        return new OrderPostAdapter.OrderPostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderPostAdapter.OrderPostViewHolder holder, int position) {
        Product orderPost = posts.get(position);
        holder.itemnameTextView.setText(orderPost.productName);
        holder.dateTextView.setText(orderPost.timestamp);
        holder.itemView.setOnClickListener(v -> itemCallback.onOpenDetails(orderPost));
        holder.deleteButton.setOnClickListener(v -> itemCallback.onDelete(orderPost, position));
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }


    // 3. SavedNewsViewHolder:
    // TODO
    public static class OrderPostViewHolder extends RecyclerView.ViewHolder {

        TextView itemnameTextView;
        TextView dateTextView;
        Button deleteButton;


        public OrderPostViewHolder(@NonNull View itemView) {
            super(itemView);
            OrderPostItemBinding binding = OrderPostItemBinding.bind(itemView);
            itemnameTextView = binding.orderPostItemItemNameContent;
            dateTextView = binding.orderPostItemDateContent;
            deleteButton = binding.orderPostDeleteButton;
        }
    }

}
