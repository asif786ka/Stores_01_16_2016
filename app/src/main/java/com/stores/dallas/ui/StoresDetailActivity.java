package com.stores.dallas.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.squareup.picasso.Picasso;
import com.stores.dallas.R;
import com.stores.dallas.adapter.StoresAdapter;
import com.stores.dallas.controller.StoresController;
import com.stores.dallas.model.StoresInfo;
import com.stores.dallas.util.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class StoresDetailActivity extends AppCompatActivity {


    private static final String TAG = StoresDetailActivity.class.getSimpleName();
    private ImageLoader imageLoader = StoresController.getInstance().getImageLoader();

    public static final String STORE_PARCELABLE = "STOREPARCELABLE";

    private int selectedpos;
    private ImageView storeImageView;
    private TextView textViewName ;
    private TextView textViewStoreID;
    private TextView textViewZipcode;
    private TextView textViewPhone;
    private TextView textViewCity;
    private TextView textViewAddress;
    List<StoresInfo> storesInfoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stores_detail_screen);
        storeImageView = (ImageView) findViewById(R.id.imageViewStores);
        textViewName = (TextView) findViewById(R.id.storeName);
        textViewStoreID = (TextView) findViewById(R.id.storeID);
        textViewZipcode = (TextView) findViewById(R.id.storeZipcode);
        textViewPhone = (TextView) findViewById(R.id.storePhone);
        textViewCity = (TextView)  findViewById(R.id.storeCity);
        textViewAddress = (TextView) findViewById(R.id.storeAddress);

        // Fetching data from a parcelable object passed from MainActivity
        StoresInfo selectedStore = getIntent().getParcelableExtra(STORE_PARCELABLE);
        setUpDetailView(selectedStore);


    }

    public void setUpDetailView(StoresInfo selectedstore){


        Picasso.with(StoresDetailActivity.this)
                .load(selectedstore.getStoreImageUrl())
                .into(storeImageView);
        textViewName.setText(selectedstore.getName());
        textViewStoreID.setText(String.valueOf(selectedstore.getStoreID()));
        textViewZipcode.setText(selectedstore.getStoreZipcode());
        textViewPhone.setText(selectedstore.getStoreZipcode());
        textViewCity.setText(selectedstore.getStoreCity());
        textViewAddress.setText(selectedstore.getStoreAddress());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
