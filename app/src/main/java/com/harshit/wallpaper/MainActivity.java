package com.harshit.wallpaper;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.harshit.wallpaper.Adapter.LoadImageRecyclearAdapter;
import com.infideap.drawerbehavior.Advance3DDrawerLayout;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends AppCompatActivity  {

    EditText searchValue;
    ImageView search_bt;
    DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;

    private RecyclerView mRecyclearView;
    private LoadImageRecyclearAdapter loadImageRecyclearAdapter;
    private ArrayList<ItemList> itemLists;
    private RequestQueue requestQueue=null;

    RelativeLayout loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchValue = findViewById(R.id.searchValue);
        search_bt = findViewById(R.id.search_bt);
        loading = findViewById(R.id.progress);
        loading.setVisibility(View.GONE);

        mRecyclearView = findViewById(R.id.imageRecycle);
        mRecyclearView.setHasFixedSize(true);
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
                Log.d("TAG",s);
                parseJSON(s);
            }
        });


      //  getSupportFragmentManager().beginTransaction().replace(R.id.contatiner, new ImageList()).commit();

    }


    @Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

            builder.setMessage("Do You Want To exit ?");
            builder.setTitle(R.string.app_name)
                    .setIcon(getResources().getDrawable(R.drawable.likeborder));
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
        itemLists.clear();
        Log.d("TAG",s);
        String url = "https://pixabay.com/api/?key=17722502-a97d71ebd63cefda99f7e1b20&q="+s+"&image_type=photo&pretty=true&per_page=50";

        JsonObjectRequest request ;
            request = new JsonObjectRequest(Request.Method.GET
                    , url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    try {

                        JSONArray jsonArray = response.getJSONArray("hits");
                        Log.d("TAG",jsonArray.toString());
                        for (int i =0; i<jsonArray.length();i++ ){

                            JSONObject hits = jsonArray.getJSONObject(i);

                            int likes = hits.getInt("likes");
                            int doenload = hits.getInt("downloads");
                            int favorites = hits.getInt("favorites");
                            String previewURL = hits.getString("webformatURL");
                            String downloadImage = hits.getString("webformatURL");
                            int views = hits.getInt("views");
                            int id = hits.getInt("id");

                            itemLists.add( new ItemList(favorites,likes,doenload,views,previewURL,downloadImage,id));

                            loadImageRecyclearAdapter = new LoadImageRecyclearAdapter(getApplicationContext(),itemLists);
                            mRecyclearView.setAdapter(loadImageRecyclearAdapter);
                        }
                        loading.setVisibility(View.GONE);
                    } catch (JSONException e) {
                        loading.setVisibility(View.GONE);
                        TastyToast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_LONG,TastyToast.ERROR);
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    loading.setVisibility(View.GONE);
                    TastyToast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG,TastyToast.ERROR);
                }
            });

            requestQueue.add(request);
        }
    }




