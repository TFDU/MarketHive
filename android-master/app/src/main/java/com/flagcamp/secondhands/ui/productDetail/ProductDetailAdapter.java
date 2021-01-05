package com.flagcamp.secondhands.ui.productDetail;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.flagcamp.secondhands.R;
import com.flagcamp.secondhands.databinding.ProductDetailImageBinding;
import com.flagcamp.secondhands.model.Image;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailAdapter extends RecyclerView.Adapter<ProductDetailAdapter.ProductDetailImageViewHolder> {
    private List<Image> images = new ArrayList<>();

    public void setImages(List<Image> imageList){
        images.clear();;
        images.addAll(imageList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductDetailImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_detail_image, parent, false);
        return new ProductDetailImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductDetailImageViewHolder holder, int position){
        Image image = images.get(position);
        //holder.productImageView.setImageResource(R.drawable.ic_save_black_24dp);
        Picasso.get().load(image.urlToImage).into(holder.productImageView);

    }

    @Override
    public int getItemCount(){
        return images.size();
    }


    public static class ProductDetailImageViewHolder extends RecyclerView.ViewHolder{
        ImageView productImageView;
        public ProductDetailImageViewHolder(@NonNull View itemView){
            super(itemView);
            ProductDetailImageBinding binding = ProductDetailImageBinding.bind(itemView);
            productImageView = binding.productDetailImage;
        }
    }
}
