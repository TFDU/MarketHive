package com.flagcamp.secondhands.ui.fav;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.flagcamp.secondhands.R;
import com.flagcamp.secondhands.databinding.FavProductBinding;
import com.flagcamp.secondhands.model.FavProduct;
import com.flagcamp.secondhands.model.Product;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.List;

public class FavProductAdapter extends RecyclerView.Adapter<FavProductAdapter.FavProductViewHolder> {
    private List<FavProduct> favList = new ArrayList<>();
    private ItemCallBack itemCallBack;

    public void updateFavList(List<FavProduct> list){
        favList.clear();
        favList.addAll(list);
        notifyDataSetChanged();
    }

    interface ItemCallBack{
        void onOpenDetail(Product product);
        void onRemoveFav(Product product);
    }

    public void setItemCallBack(ItemCallBack itemCallBack){
        this.itemCallBack = itemCallBack;
    }


    @NonNull
    @Override
    public FavProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fav_product,parent,false);
        return new FavProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavProductViewHolder holder, int position) {
        Product product = favList.get(position).getProduct();
        holder.productName.setText(product.productName);
        holder.productPrice.setText(product.price);
        holder.productLocation.setText(product.state);
        if(product.availability){
            holder.productStatus.setText("Available");
        }else{
            holder.productStatus.setText("Sold Out");
        }


        if(product.imageUrls == null || product.imageUrls.length()==0){
            holder.productPhoto.setImageResource(R.drawable.image_unavailable);
        }else{
            String urls = product.imageUrls;
            String url = urls.split("\\*")[0];
            Picasso.get().load(url).into(holder.productPhoto);
        }
        holder.favIcon.setOnClickListener(v-> itemCallBack.onRemoveFav(product));
        holder.itemView.setOnClickListener(v -> itemCallBack.onOpenDetail(product));
    }

    @Override
    public int getItemCount() {
        return favList.size();
    }


    public static class FavProductViewHolder extends RecyclerView.ViewHolder{

        TextView productName;
        TextView productPrice;
        TextView productLocation;
        TextView productStatus;
        ImageView favIcon;
        ImageView productPhoto;

        public FavProductViewHolder(@NonNull View itemView) {
            super(itemView);

            FavProductBinding binding = FavProductBinding.bind(itemView);
            productName = binding.favProductTitleContent;
            productPrice = binding.favProductPriceContent;
            productLocation = binding.favProductLocationContent;
            productStatus = binding.favProductStatusContent;
            favIcon = binding.favProductFavIcon;
            productPhoto = binding.favProductImageView;

        }
    }

}
