package com.harshit.pichub;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class SharedPreferenceValue {

    Context context;
    String valueUser;

    public SharedPreferenceValue(Context contex)
    {
        this.context=contex;
    }

    public void updatId(int id,int val)
    {
        SharedPreferences sharedPreferences=context.getSharedPreferences("users",MODE_PRIVATE);
        SharedPreferences.Editor edit=sharedPreferences.edit();
        edit.putString("id"+String.valueOf(id),String.valueOf(val));
        edit.apply();

    }
     public void removeId(int id)
    {
        SharedPreferences sharedPreferences=context.getSharedPreferences("users",MODE_PRIVATE);
        SharedPreferences.Editor edit=sharedPreferences.edit();
        edit.remove("id"+String.valueOf(id));
        edit.apply();

    }

    public String getId(int id)
    {
        SharedPreferences sharedPreferences=context.getSharedPreferences("users",MODE_PRIVATE);
        return sharedPreferences.getString("id"+String.valueOf(id),"-1");

    }
    public void updateFav(int id,int val)
    {
        SharedPreferences sharedPreferences=context.getSharedPreferences("users",MODE_PRIVATE);
        SharedPreferences.Editor edit=sharedPreferences.edit();
        edit.putString("fav"+String.valueOf(id),String.valueOf(val));
        edit.apply();

    }
    public void  removeFav(int id)
    {
        SharedPreferences sharedPreferences=context.getSharedPreferences("users",MODE_PRIVATE);
        SharedPreferences.Editor edit=sharedPreferences.edit();
        edit.remove("fav"+String.valueOf(id));
        edit.apply();

    }
    public String getFav(int id)
    {
        SharedPreferences sharedPreferences=context.getSharedPreferences("users",MODE_PRIVATE);
        return sharedPreferences.getString("fav"+String.valueOf(id),"-1");

    }
    public ArrayList<String> allLinkedImages(){
        SharedPreferences sharedPreferences=context.getSharedPreferences("users",MODE_PRIVATE);
        Map<String, ?> allEntries = sharedPreferences.getAll();
        ArrayList<String> arr = new ArrayList<>();
        Log.d("map values", "At Console");

        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            if(entry.getKey().startsWith("id")){
                arr.add(entry.getValue().toString());
            }
        }
        return arr;
    }
    public ArrayList<String> allFavImages(){
        SharedPreferences sharedPreferences=context.getSharedPreferences("users",MODE_PRIVATE);
        Map<String, ?> allEntries = sharedPreferences.getAll();
        ArrayList<String> arrFav = new ArrayList<>();
        Log.d("mapvalues", "At Console");

        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            Log.d("map values", entry.getValue().toString());
            if(entry.getKey().startsWith("fav") && !entry.getValue().equals("-1")){
                arrFav.add(entry.getValue().toString());
            }
        }
        return arrFav;
    }

}
