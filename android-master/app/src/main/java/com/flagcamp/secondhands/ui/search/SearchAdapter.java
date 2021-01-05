package com.flagcamp.secondhands.ui.search;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.flagcamp.secondhands.R;
import com.flagcamp.secondhands.databinding.SearchProductItemBinding;
import com.flagcamp.secondhands.model.Product;
import com.flagcamp.secondhands.ui.profile.ProfileFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
    private List<Product> productList = new ArrayList<>();

    // 1. Supporting data:
    public void setProducts(List<Product> productList, int page, int pageSize) {
        if (page == 1) {
            this.productList.clear();
        }
//        else if (page > 2) {
//            List<Product> newList = new ArrayList<>();
//            for (int i = pageSize; i < this.productList.size(); i++) {
//                newList.add(this.productList.get(i));
//            }
//            this.productList = newList;
//        }
        this.productList.addAll(productList);
        notifyDataSetChanged();
    }

    public void updateItemAtIndex(int position, Product product) {
        productList.set(position, product);
        notifyItemRangeChanged(position, 1);
    }

    // Setup listener
    interface ItemCallback {
        void onOpenDetails(Product product);
        void onRemoveFav(Product product);
        void onAddFav(Product product);
    }

    public ItemCallback itemCallback;

    public void setItemCallback(ItemCallback itemCallback) {
        this.itemCallback = itemCallback;
    }

    // 2. Adapter overrides:
    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_product_item, parent, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        Product product = productList.get(position);
        if (product.favorite) {
            holder.favoriteImageView.setImageResource(R.drawable.ic_favorite_24dp);
        } else {
            holder.favoriteImageView.setImageResource(R.drawable.ic_favorite_border_24dp);
        }
        holder.favoriteImageView.setOnClickListener(v -> {
            if (product.favorite) {
                itemCallback.onRemoveFav(product);
            } else {
                itemCallback.onAddFav(product);
            }
            product.favorite = !product.favorite;
            updateItemAtIndex(position, product);
        });

        holder.itemView.setOnClickListener(v -> itemCallback.onOpenDetails(product));

        holder.itemTitleTextView.setText(product.productName);
        holder.itemPriceTextView.setText(product.price);
        holder.itemLocationView.setText(product.state);

        if (product.imageUrls == null || product.imageUrls.length() == 0){
            holder.itemImageView.setImageResource(R.drawable.image_unavailable);
        } else {
            Picasso.get().load(product.imageUrls.split("\\*")[0]).into(holder.itemImageView);
        }

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    // 3. SearchViewHolder:
    public static class SearchViewHolder extends RecyclerView.ViewHolder {

        ImageView favoriteImageView;
        ImageView itemImageView;
        TextView itemTitleTextView;
        TextView itemPriceTextView;
        TextView itemLocationView;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);

            SearchProductItemBinding binding = SearchProductItemBinding.bind(itemView);
            favoriteImageView = binding.searchItemFavorite;
            itemImageView = binding.searchItemImage;
            itemTitleTextView = binding.searchItemTitle;
            itemPriceTextView = binding.searchItemPrice;
            itemLocationView = binding.searchItemLocation;
        }
    }
}
