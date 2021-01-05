package com.flagcamp.secondhands.ui.home;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.flagcamp.secondhands.R;
import com.flagcamp.secondhands.databinding.HomeProductItemBinding;
import com.flagcamp.secondhands.model.Product;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HomeProductAdapter extends RecyclerView.Adapter<HomeProductAdapter.HomeProductViewHolder> {
    interface ItemCallback {
        void onOpenDetails(Product product);
        void onAddFav(Product product);
        void onRemoveFav(Product product);
    }
    public ItemCallback itemCallback;



    public void setItemCallback(ItemCallback itemCallback) {
        this.itemCallback = itemCallback;
    }
    private List<Product> products = new ArrayList<>();

    public void setProducts(List<Product> productList){
        products.clear();;
        products.addAll(productList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HomeProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_product_item, parent, false);
        return new HomeProductViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull HomeProductViewHolder holder, int position){
        Product product = products.get(position);
        if(product.favorite == true){
            holder.favoriteImageView.setImageResource(R.drawable.ic_favorite_24dp);
        }else{
            holder.favoriteImageView.setImageResource(R.drawable.ic_favorite_border_24dp);
        }

        holder.itemPriceView.setText("$" + product.price);

        holder.favoriteImageView.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(product.favorite){
                    itemCallback.onRemoveFav(product);
                }else{
                    itemCallback.onAddFav(product);
                }
                product.favorite = !product.favorite;
                if(product.favorite){
                    holder.favoriteImageView.setImageResource(R.drawable.ic_favorite_24dp);
                }else{
                    holder.favoriteImageView.setImageResource(R.drawable.ic_favorite_border_24dp);
                }
                updateItemAtIndex(position, product);
                notifyDataSetChanged();
            }
        });
//        holder.favoriteImageView.setOnClickListener(v-> itemCallback.onRemoveFav(product));
        //holder.itemImageView.setImageResource(R.drawable.ic_save_black_24dp);
//        String dummyImages = "https://firebasestorage.googleapis.com/v0/b/androidsecondhandmarket.appspot.com/o/testImgs_v1%2FTrump1.jpg?alt=media&token=a7da5dad-2354-4979-b155-75fb1e9e845d*https://firebasestorage.googleapis.com/v0/b/androidsecondhandmarket.appspot.com/o/testImgs_v1%2FTrump2.jpg?alt=media&token=d4eccd9e-678a-49f3-abd8-739fa62ed275*https://firebasestorage.googleapis.com/v0/b/androidsecondhandmarket.appspot.com/o/testImgs_v1%2FTrump3.jpg?alt=media&token=50d33d62-d9e2-42b7-9084-62743b06fb5b";
//        product.imageUrls = dummyImages;
        if (product.imageUrls == null || product.imageUrls.length() == 0) {
            holder.itemImageView.setImageResource(R.drawable.image_unavailable);
        } else {
            Picasso.get().load(product.imageUrls.split("\\*")[0]).into(holder.itemImageView);

        }
        holder.itemImageView.setOnClickListener(v -> itemCallback.onOpenDetails(product));
        holder.itemImageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                itemCallback.onOpenDetails(product);
            }
        });
    }

    @Override
    public int getItemCount(){
        return products.size();
    }
    public void updateItemAtIndex(int position, Product product) {
        products.set(position, product);
        notifyItemRangeChanged(position, 1);
        notifyDataSetChanged();
    }


    public static class HomeProductViewHolder extends RecyclerView.ViewHolder{
        ImageView favoriteImageView;
        ImageView itemImageView;
        ImageView itemPriceIconView;
        TextView itemPriceView;
        public HomeProductViewHolder(@NonNull View itemView){
            super(itemView);
            HomeProductItemBinding binding = HomeProductItemBinding.bind(itemView);
            favoriteImageView = binding.homeItemFavorite;
            itemImageView = binding.homeItemImage;
            itemPriceIconView = binding.homeItemPriceIcon;
            itemPriceView = binding.homeItemPrice;
        }

    }
}
