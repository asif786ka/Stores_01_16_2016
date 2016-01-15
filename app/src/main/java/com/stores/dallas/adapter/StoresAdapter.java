package com.stores.dallas.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.stores.dallas.R;
import com.stores.dallas.controller.StoresController;
import com.stores.dallas.model.StoresInfo;
import com.stores.dallas.ui.StoresDetailActivity;
import com.stores.dallas.util.Config;


import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Asif on 01/10/2015.
 */
public class StoresAdapter extends RecyclerView.Adapter<StoresAdapter.ViewHolder> {

    private ImageLoader imageLoader = StoresController.getInstance().getImageLoader();

    private Context context;

    //List of stores info
    List<StoresInfo> storesInfoList;

    public StoresAdapter(List<StoresInfo> storesInfoList, Context context) {
        super();
        //Getting all the superheroes
        this.storesInfoList = storesInfoList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stores_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        StoresInfo storesInfo = storesInfoList.get(position);

        if (imageLoader != null) {
            imageLoader.get(storesInfo.getStoreImageUrl(), ImageLoader.getImageListener(holder.storeImageView, R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert));
            holder.storeImageView.setImageUrl(storesInfo.getStoreImageUrl(), imageLoader);
        }
        holder.textViewName.setText(storesInfo.getName());
        holder.textViewStoreID.setText(String.valueOf(storesInfo.getStoreID()));
        holder.textViewZipcode.setText(storesInfo.getStoreZipcode());
        holder.textViewPhone.setText(storesInfo.getStorePhone());
        holder.textViewCity.setText(storesInfo.getStoreCity());
        holder.textViewAddress.setText(storesInfo.getStoreAddress());

        holder.storeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Recycle Click" + position, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(context, StoresDetailActivity.class);
                intent.putExtra(StoresDetailActivity.STORE_PARCELABLE,storesInfoList.get(position));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return storesInfoList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public NetworkImageView storeImageView;
        public TextView textViewName;
        public TextView textViewStoreID;
        public TextView textViewZipcode;
        public TextView textViewPhone;
        public TextView textViewCity;
        public TextView textViewAddress;

        public ViewHolder(final View itemView) {
            super(itemView);
            storeImageView = (NetworkImageView) itemView.findViewById(R.id.imageViewStores);
            textViewName = (TextView) itemView.findViewById(R.id.storeName);
            textViewStoreID = (TextView) itemView.findViewById(R.id.storeID);
            textViewZipcode = (TextView) itemView.findViewById(R.id.storeZipcode);
            textViewPhone = (TextView) itemView.findViewById(R.id.storePhone);
            textViewCity = (TextView) itemView.findViewById(R.id.storeCity);
            textViewAddress = (TextView) itemView.findViewById(R.id.storeAddress);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        }
    }
}
