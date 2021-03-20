package com.teamnpcomplete.ar_com.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.teamnpcomplete.ar_com.Classes.Product;
import com.teamnpcomplete.ar_com.ProductDetailActivity;
import com.teamnpcomplete.ar_com.R;

import java.util.ArrayList;

public class ProductAdapterDataHolder extends RecyclerView.Adapter<ProductAdapterDataHolder.ProductDataViewHolder>{

    private Context mCtx;
    private ArrayList<Product> productList;
    private RecyclerView recyclerView;

    public ProductAdapterDataHolder(Context mCtx, ArrayList<Product> productList, RecyclerView recyclerView){
        this.mCtx = mCtx;
        this.productList = productList;
        this.recyclerView = recyclerView;
    }

    public ProductDataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.product_details_card, parent, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int itemPosition = recyclerView.getChildLayoutPosition(view);
                Product item = productList.get(itemPosition);
                mCtx.startActivity(new Intent(mCtx, ProductDetailActivity.class).putExtra("productId", item.getId()));
            }
        });
        return new ProductDataViewHolder(view);
    }

    public void onBindViewHolder(ProductDataViewHolder holder, int position) {
        Product product = productList.get(position);

        holder.title.setText(product.getName());
        holder.shortDesc.setText(product.getShortDesc());
        holder.rating.setText(String.valueOf(product.getRating()));
        holder.price.setText("INR " + (int)product.getPrice());

        Glide.with(mCtx).load(product.getImageURLs().get(0)).into(holder.image);
    }
    
    public int getItemCount() {
        return productList.size();
    }

    class ProductDataViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView title, shortDesc, rating, price;

        public ProductDataViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.textViewTitleShimmer);
            shortDesc = itemView.findViewById(R.id.textViewShortDescShimmer);
            rating = itemView.findViewById(R.id.textViewRating);
            price = itemView.findViewById(R.id.textViewPrice);
            image = itemView.findViewById(R.id.product_image_shimmer);
        }

    }

}

