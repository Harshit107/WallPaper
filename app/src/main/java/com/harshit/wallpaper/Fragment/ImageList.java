package com.harshit.wallpaper.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.harshit.wallpaper.Adapter.LoadImageRecyclearAdapter;
import com.harshit.wallpaper.ItemList;
import com.harshit.wallpaper.R;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ImageList extends Fragment {



    private RecyclerView mRecyclearView;
    private LoadImageRecyclearAdapter loadImageRecyclearAdapter;
    private ArrayList<ItemList> itemLists;
    private RequestQueue requestQueue;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    mRecyclearView = view.findViewById(R.id.imageRecycle);
        mRecyclearView.setHasFixedSize(true);
        mRecyclearView.setLayoutManager(new LinearLayoutManager(getContext()));
        itemLists = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(getContext());
        parseJSON();

    }

    private void parseJSON() {
        String url = "https://pixabay.com/api/?key=17722502-a97d71ebd63cefda99f7e1b20&q=flower&image_type=photo&pretty=true";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET
                , url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray jsonArray = response.getJSONArray("hits");
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

                        loadImageRecyclearAdapter = new LoadImageRecyclearAdapter(getContext(),itemLists);
                        mRecyclearView.setAdapter(loadImageRecyclearAdapter);


                    }


                } catch (JSONException e) {
                    TastyToast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG,TastyToast.ERROR);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                TastyToast.makeText(getContext(),error.getMessage(),Toast.LENGTH_LONG,TastyToast.ERROR);


            }
        });
        requestQueue.add(request);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_image_list, container, false);
    }
}
