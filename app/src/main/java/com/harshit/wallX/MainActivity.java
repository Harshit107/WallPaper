package com.harshit.wallX;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.harshit.wallX.Adapter.LoadImageRecyclearAdapter;
import com.infideap.drawerbehavior.Advance3DDrawerLayout;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
 {

    EditText searchValue;
    ImageView search_bt;
    DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;

    private RecyclerView mRecyclearView;
    private LoadImageRecyclearAdapter loadImageRecyclearAdapter;
    private ArrayList<ItemList> itemLists;
    private RequestQueue requestQueue = null;
    NavigationView navigationView;

    RelativeLayout loading;


    RelativeLayout splash,mainRelative;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchValue = findViewById(R.id.searchValue);
        search_bt = findViewById(R.id.search_bt);
        loading = findViewById(R.id.progress);
        loading.setVisibility(View.GONE);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        splash = findViewById(R.id.splash);
        mainRelative = findViewById(R.id.mainRelative);











        mRecyclearView = findViewById(R.id.imageRecycle);
        //mRecyclearView.setHasFixedSize(true);
        mRecyclearView.setLayoutManager(new LinearLayoutManager(this));
        itemLists = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);

        //---------------   ToolBar and Navigation drawer   ---------------
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = (Advance3DDrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((Advance3DDrawerLayout) drawer).setViewRotation(GravityCompat.START, 15);
        //((Advance3DDrawerLayout) drawer).setRadius(GravityCompat.START, 25);
        ((Advance3DDrawerLayout) drawer).setViewScale(GravityCompat.START, 0.9f); //set height scale for main view (0f to 1f)
        ((Advance3DDrawerLayout) drawer).setViewElevation(GravityCompat.START, 20); //set main view elevation when drawer open (dimension)
        ((Advance3DDrawerLayout) drawer).setViewScrimColor(GravityCompat.START, Color.TRANSPARENT); //set drawer overlay coloe (color)
        ((Advance3DDrawerLayout) drawer).setDrawerElevation(25f); //set drawer elevation (dimension)
        ((Advance3DDrawerLayout) drawer).setContrastThreshold(3); //set maximum of contrast ratio between white text and background color.
        ((Advance3DDrawerLayout) drawer).setRadius(GravityCompat.START, 25); //set end container's corner radius (dimension)

        //---------------------  End --------------------

        parseJSON("");

        search_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String s = searchValue.getText().toString().trim();
                Log.d("TAG", s);
                itemLists.clear();
                parseJSON("q="+s);
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /* Create an Intent that will start the MainActivity. */
                Animation animSlideUp = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.push_up_in);
                splash.startAnimation(animSlideUp);

                parseJSON("");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mainRelative.setVisibility(View.VISIBLE);
                        splash.setVisibility(View.GONE);

                    }
                },1000);

            }
        }, 3000);

    }


    @Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

            builder.setMessage("Do You Want To exit ?");
            builder.setTitle(R.string.app_name)
                    .setIcon(getResources().getDrawable(R.drawable.logo));
            builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent it = new Intent(getApplicationContext(), Exit.class);
                    it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(it);

                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
        } else {
            getSupportFragmentManager().popBackStack();
        }


    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

    private void parseJSON(String s) {

        loading.setVisibility(View.VISIBLE);

        Log.d("TAG", s);
        String url = Config.FURLL+s+Config.LLURL;

        JsonObjectRequest request;
        request = new JsonObjectRequest(Request.Method.GET
                , url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    JSONArray jsonArray = response.getJSONArray("hits");
                   // Log.d("TAG", jsonArray.toString());
                    if (jsonArray.length() == 0) {
                        sankeBar("No Image Found! Try Again With Different Search", getWindow().getDecorView().getRootView());
                    }
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject hits = jsonArray.getJSONObject(i);

                        int likes = hits.getInt("likes");
                        int doenload = hits.getInt("downloads");
                        int favorites = hits.getInt("favorites");
                        String previewURL = hits.getString("webformatURL");
                        String downloadImage = hits.getString("largeImageURL");
                        int views = hits.getInt("views");
                        int id = hits.getInt("id");

                        itemLists.add(new ItemList(favorites, likes, doenload, views, previewURL, downloadImage, id));
                        loadImageRecyclearAdapter = new LoadImageRecyclearAdapter(getApplicationContext(), itemLists, MainActivity.this,
                                new LoadImageRecyclearAdapter.OnLoadMoreListener() {
                                    @Override
                                    public void onLoadMore() {
                                        Log.d("onLoad","Ok");
                                    }
                                });
                        mRecyclearView.setAdapter(loadImageRecyclearAdapter);
                        mRecyclearView.getRecycledViewPool().clear();
                        loadImageRecyclearAdapter.notifyDataSetChanged();

                    }
                    loading.setVisibility(View.GONE);
                } catch (JSONException e) {
                    loading.setVisibility(View.GONE);
                    TastyToast.makeText(getApplicationContext(),"Error"+ e.getMessage(), Toast.LENGTH_LONG, TastyToast.ERROR);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.setVisibility(View.GONE);
                TastyToast.makeText(getApplicationContext(), "fError"+error.getMessage(), Toast.LENGTH_LONG, TastyToast.ERROR);
            }
        });

        requestQueue.add(request);

    }

    public void sankeBar(String msg, View view) {
        Snackbar snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        snackbar.show();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        drawer.closeDrawers();
        switch (item.getItemId()) {
            case R.id.nav_home:
                itemLists.clear();
                parseJSON("");
                loadImageRecyclearAdapter.notifyDataSetChanged();

                return true;
                case R.id.nav_liked:

                SharedPreferenceValue s = new SharedPreferenceValue(getApplicationContext());
                ArrayList<String> arr = new ArrayList<>();
                arr = s.allLinkedImages();
                itemLists.clear();

                Log.d("map values", arr.toString());
                if(arr.size()==0) {
                    sankeBar("No Liked Image Found", getCurrentFocus());
                    return false;
                }

                for(int i = 0;i<arr.size();i++){

                    parseJSON("id="+arr.get(i));
                }
                loadImageRecyclearAdapter.notifyDataSetChanged();

                return true;

            case R.id.nav_fav:

                SharedPreferenceValue fav = new SharedPreferenceValue(getApplicationContext());
                ArrayList<String> favArr = new ArrayList<>();
                favArr = fav.allFavImages();
                itemLists.clear();
                if(favArr.isEmpty()) {
                    sankeBar("No Favourite Image Found", getCurrentFocus());
                    return false;
                }

                for(int i = 0;i<favArr.size();i++){

                    parseJSON("id="+favArr.get(i));
                }
                loadImageRecyclearAdapter.notifyDataSetChanged();
                return true;

            case R.id.nav_rate:

                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
                return true;

            case R.id.nav_share:
                shareApp(getApplicationContext());
                return true;

            case R.id.nav_about:

                return true;
        }


        return false;
    }

    public static void shareApp(Context context)
    {
        final String appPackageName = context.getPackageName();
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Check out the App at:\n https://play.google.com/store/apps/details?id=" + appPackageName);
        sendIntent.setType("text/plain");
        context.startActivity(sendIntent);
    }


}




