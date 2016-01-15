package com.stores.dallas.ui;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.stores.dallas.R;
import com.stores.dallas.controller.StoresController;
import com.stores.dallas.model.StoresInfo;

import com.stores.dallas.adapter.StoresAdapter;
import com.stores.dallas.util.Config;
import com.stores.dallas.util.DialogUtils;
import com.stores.dallas.util.PermissionUtil;
import com.stores.dallas.util.StoreUtils;
import com.stores.dallas.util.TinyDB;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class StoresMainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private static final String TAG = StoresMainActivity.class.getSimpleName();

    //Creating a List of stores with their info.
    private List<StoresInfo> storesInfoList;
    private ProgressDialog pDialog;

    //Creating Views
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    TinyDB tinydb;
    /**
     * Root of the layout of this Activity.
     */
    private View mLayout;

    /**
     * Id to identify a network access permission request.
     */
    private static final int REQUEST_NETWORK = 0;

    /**
     * Permissions required to read and write calendars.Used by this activity,CalendarEventFragment.
     * Visible calendar fragment.
     */
    private static String[] PERMISSIONS_NETWORK = {Manifest.permission.ACCESS_NETWORK_STATE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stores_main);

        mLayout = findViewById(R.id.storesLayout);
        tinydb = new TinyDB(this);

        boolean permissions = tinydb.getBoolean("PERMISSIONSREQUIRED");


        //Initializing Views
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //Initializing our stores info list
        storesInfoList = new ArrayList<>();
        //Finally initializing our adapter
        adapter = new StoresAdapter(storesInfoList, this);

        //Adding adapter to recyclerview
        recyclerView.setAdapter(adapter);

        if (Build.VERSION.SDK_INT >= 23 && !permissions) {
            checkNetworkPermisssion();
        } else {
            checkNetworkAndLoad();

        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_NETWORK) {
            Log.i(TAG, "Received response for contact permissions request.");

            // We have requested multiple permissions for calendar, so all of them need to be
            // checked.
            if (PermissionUtil.verifyPermissions(grantResults)) {
                // All required permissions have been granted, display calendar,visible fragments.
                Snackbar.make(mLayout, R.string.permision_available_network,
                        Snackbar.LENGTH_SHORT)
                        .show();
                tinydb.putBoolean("PERMISSIONSREQUIRED", true);
                loadStores();
            } else {
                Log.i(TAG, "Calendar permissions were NOT granted.");
                Snackbar.make(mLayout, R.string.permissions_not_granted,
                        Snackbar.LENGTH_SHORT)
                        .show();
            }

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    public void showProgressDialog() {
        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();
    }

    private void hideProgressDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

    public void loadStores() {


        Cache cache = StoresController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(Config.STORES_URL);
        if (entry != null) {
            // fetch the data from cache
            try {
                String data = new String(entry.data, "UTF-8");
                try {
                    parseData(new JSONObject(data));
                } catch (JSONException e) {
                    e.printStackTrace();
                    DialogUtils.createErrorDialog(getResources().getString(R.string.store_volley_cache_error), e.getMessage().toString(), this);

                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                DialogUtils.createErrorDialog(getResources().getString(R.string.store_volley_cache_error), e.getMessage().toString(), this);

            }

        } else {

            // making fresh volley request and getting json
            JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.GET,
                    Config.STORES_URL, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    hideProgressDialog();
                    VolleyLog.d(TAG, "Response: " + response.toString());
                    if (response != null) {
                        parseData(response);
                        hideProgressDialog();
                        //setProgressBarIndeterminateVisibility(false);
                    }

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    hideProgressDialog();
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                    DialogUtils.createErrorDialog(getResources().getString(R.string.store_volley_network_error), error.getMessage().toString(), StoresMainActivity.this);

                }

            });

            // Adding request to volley request queue
            StoresController.getInstance().addToRequestQueue(jsonReq);

        }
    }

    //This method will parse json data
    private void parseData(JSONObject response) {
        JSONArray storeArray = null;
        try {
            storeArray = response.getJSONArray("stores");
            for (int i = 0; i < storeArray.length(); i++) {
                JSONObject storeChild = (JSONObject) storeArray
                        .get(i);
                StoresInfo storesInfo = new StoresInfo();


                storesInfo.setStoreImageUrl(storeChild.getString(Config.TAG_STORE_LOGO_URL));
                storesInfo.setName(storeChild.getString(Config.TAG_STORE_NAME));
                storesInfo.setStoreID(storeChild.getInt(Config.TAG_STORE_ID));
                storesInfo.setStoreZipcode(storeChild.getString(Config.TAG_STORE_ZIPCODE));
                storesInfo.setStorePhone(storeChild.getString(Config.TAG_STORE_PHONE));
                storesInfo.setStoreCity(storeChild.getString(Config.TAG_STORE_CITY));
                storesInfo.setStoreAddress(storeChild.getString(Config.TAG_STORE_ADDRESS));


                storesInfoList.add(storesInfo);
            }
            // notify data changes to reddit adapter
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * This is required for MarshMallow.
     */
    public void checkNetworkPermisssion() {
        Log.i(TAG, "Checking network permissions.");

        // Verify that all required contact permissions have been granted.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            // network permissions have not been granted.
            Log.i(TAG, "Network permissions has NOT been granted. Requesting permissions.");
            requestNetworkPermissions();
        } else {

            // Network permissions have been granted.Load the stores.
            Log.i(TAG,
                    "Network permissions have already been granted. Displaying contact details.");
            checkNetworkAndLoad();

        }
    }

    /**
     * Requests the Network permissions.
     * If the permission has been denied previously, a SnackBar will prompt the user to grant the
     * permission, otherwise it is requested directly.
     */
    private void requestNetworkPermissions() {
        // BEGIN_INCLUDE(network_permission_request)
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_NETWORK_STATE)) {

            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example, if the request has been denied previously.
            Log.i(TAG,
                    "Displaying network permission rationale to provide additional context.");

            // Display a SnackBar with an explanation and a button to trigger the request.
            Snackbar.make(mLayout, R.string.permission_network_rationale,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat
                                    .requestPermissions(StoresMainActivity.this, PERMISSIONS_NETWORK,
                                            REQUEST_NETWORK);
                        }
                    })
                    .show();
        } else {
            // Network permissions have not been granted yet. Request them directly.
            ActivityCompat.requestPermissions(this, PERMISSIONS_NETWORK, REQUEST_NETWORK);
        }
        // END_INCLUDE(network_permission_request)
    }

    public void checkNetworkAndLoad() {
        //Calling method to get store data
        boolean isNetworkAvailaible = StoreUtils.isNetworkAvailable(this);
        if (isNetworkAvailaible) {
            loadStores();
        } else {
            DialogUtils.createErrorDialog(getResources().getString(R.string.store_network_error), getResources().getString(R.string.store_network_message), this);
        }
    }
}
