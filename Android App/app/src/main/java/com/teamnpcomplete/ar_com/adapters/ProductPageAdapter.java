package com.teamnpcomplete.ar_com.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.teamnpcomplete.ar_com.R;

import java.util.ArrayList;

public class ProductPageAdapter extends PagerAdapter {

    ArrayList<String> imageUrlArrayList;
    LayoutInflater inflater;
    Context context;

    public ProductPageAdapter(Context context, ArrayList<String> imageUrlArrayList){
        this.imageUrlArrayList = imageUrlArrayList;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return imageUrlArrayList.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        View imageLayout = inflater.inflate(R.layout.pager_slide, container, false);

        ImageView imageView = imageLayout.findViewById(R.id.pager_image);

        String url = imageUrlArrayList.get(position);

        Glide.with(context).load(url).into(imageView);

        container.addView(imageLayout);
        return  imageLayout;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }
}
