package com.harshit.wallX.Adapter;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;
import com.harshit.wallX.ItemList;
import com.harshit.wallX.MainActivity;
import com.harshit.wallX.R;
import com.harshit.wallX.SharedPreferenceValue;


import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

import static com.android.volley.VolleyLog.TAG;


public class LoadImageRecyclearAdapter extends RecyclerView.Adapter<LoadImageRecyclearAdapter.LoadImageViewHolder> {

    private Context context;
    private ArrayList<ItemList> itemLists;
    Activity activity;
    SharedPreferenceValue sharedPreferenceValue;

    private ScaleGestureDetector mScaleGestureDetector;
    private float mScaleFactor = 1.0f;

    HashSet<String> hs = new HashSet<>();

    OnLoadMoreListener onLoadMoreListener;

    public LoadImageRecyclearAdapter(Context context, ArrayList<ItemList> itemLists, Activity activity, OnLoadMoreListener onLoadMoreListener) {
        this.context = context;
        this.itemLists = itemLists;
        this.activity = activity;
        this.onLoadMoreListener = (OnLoadMoreListener) onLoadMoreListener;
    }

    public interface OnLoadMoreListener{
        void onLoadMore();
    }

    @NonNull
    @Override
    public LoadImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.recycle_list,parent,false);
        return new LoadImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final LoadImageViewHolder holder,
                                 int position) {

        if (position==getItemCount()-1){
            onLoadMoreListener.onLoadMore();

        }

        sharedPreferenceValue  = new SharedPreferenceValue(context);
      ItemList selectItem = itemLists.get(position);
      String imageUrl = selectItem.getImageUrl();
      final String downloadImageUrl = selectItem.getDownloadImageUrl();
      int likes = selectItem.getLikes();
      int fav = selectItem.getFavourite();
      int downloads = selectItem.getDownloads();
      int views = selectItem.getViews();
      final int id = selectItem.getmId();
      //Log.d("myImageUrl",downloadImageUrl);

       // Log.d("value",String.valueOf(downloads));

      holder.download.setText(String.valueOf(downloads));
      holder.views.setText(String.valueOf(views));
      holder.fav.setText(String.valueOf(fav));
      holder.likes.setText(String.valueOf(likes));
     // holder.displayImage.setText(imageUrl);

         RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.progress_animation)

                .error(android.R.drawable.stat_notify_error)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH)
                .dontAnimate()
                .dontTransform();

        Glide.with(context)
                .load(imageUrl)
                .apply(options)
                .into(holder.displayImage);



        final String shareFavID = sharedPreferenceValue.getFav(id);

        if( !shareFavID.equals("-1")) {
            hs.add("fav"+String.valueOf(id));
            holder.fav_bt.setColorFilter(ContextCompat.getColor(context, R.color.darkblue), android.graphics.PorterDuff.Mode.MULTIPLY);
        }
        else
        {
            holder.fav_bt.setColorFilter(ContextCompat.getColor(context, android.R.color.holo_orange_light), android.graphics.PorterDuff.Mode.MULTIPLY);

        }

        if(!sharedPreferenceValue.getId(id).equals("-1")) {
            hs.add("id"+String.valueOf(id));
            holder.likeImage.setColorFilter(ContextCompat.getColor(context, R.color.darkblue), android.graphics.PorterDuff.Mode.MULTIPLY);
        }
        else
        {
            holder.likeImage.setColorFilter(ContextCompat.getColor(context, android.R.color.holo_red_dark), android.graphics.PorterDuff.Mode.MULTIPLY);

        }

        //------------  Button

        holder.download_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isStoragePermissionGranted()) {
                    //Snackbar.make(view,"Downloading",Snackbar.LENGTH_LONG).show();
                    new DownloadImage().execute(downloadImageUrl);
                }
            }
        });

        holder.likeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            if(hs.contains("id"+String.valueOf(id))) {
                sharedPreferenceValue.removeId(id);
                hs.remove("id"+String.valueOf(id));
                sankeBar("Image Removed From Liked Images",view);
                holder.likeImage.setColorFilter(ContextCompat.getColor(context, android.R.color.holo_red_dark), android.graphics.PorterDuff.Mode.MULTIPLY);
            }
            else
            {
                hs.add("id"+String.valueOf(id));
                sharedPreferenceValue.updatId(id, id);
                sankeBar("Image Liked! ",view);
                holder.likeImage.setColorFilter(ContextCompat.getColor(context, R.color.darkblue), android.graphics.PorterDuff.Mode.MULTIPLY);

            }

            }
        });
        holder.fav_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(hs.contains("fav"+String.valueOf(id))) {
                    sharedPreferenceValue.removeFav(id);
                    hs.remove("fav"+String.valueOf(id));
                    sankeBar("Image Removed From Favourites",view);
                    holder.fav_bt.setColorFilter(ContextCompat.getColor(context, android.R.color.holo_orange_dark), android.graphics.PorterDuff.Mode.MULTIPLY);
                }
                else
                {
                    hs.add("fav"+String.valueOf(id));
                    sharedPreferenceValue.updateFav(id, id);
                    sankeBar("Image Added To Favourite ! ",view);
                    holder.fav_bt.setColorFilter(ContextCompat.getColor(context, R.color.darkblue), android.graphics.PorterDuff.Mode.MULTIPLY);

                }
            }
        });
        holder.share_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //shareImage(downloadImageUrl);
                if (isStoragePermissionGranted()) {

                    Uri bmpUri = getLocalBitmapUri(holder.displayImage);
                    if (bmpUri != null) {
                        // Construct a ShareIntent with link to image
                        if (isStoragePermissionGranted()) {
                            Intent shareIntent = new Intent();
                            shareIntent.setAction(Intent.ACTION_SEND);
                            shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out the App at: " +
                                    "\nhttps://play.google.com/store/apps/details?id=" + "com.harshit.wallX");
                            shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                            shareIntent.setType("image/*");
                            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                            StrictMode.setVmPolicy(builder.build());
                            context.startActivity(Intent.createChooser(shareIntent, "Send Via"));

                        }
                    } else {
                        // ...sharing failed, handle error
                        sankeBar("Error ", view);
                    }

                }
            }
        });
        holder.view_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               sankeBar("This Option is Not Enable Yet",view);

            }
        });
    }

    @Override
    public int getItemCount() {
        return itemLists.size();
    }


    //Inner Class For Views
    public class LoadImageViewHolder extends RecyclerView.ViewHolder{

        public TextView likes,download,views,fav;
        ImageView likeImage,displayImage;
        ImageView download_bt;
        ImageView view_bt;
        ImageView fav_bt;
        ImageView share_bt;
        public LoadImageViewHolder(@NonNull View itemView) {
            super(itemView);

            likeImage = itemView.findViewById(R.id.like);
            likes = itemView.findViewById(R.id.like_tv);
            download = itemView.findViewById(R.id.download);
            views = itemView.findViewById(R.id.views);
            displayImage = itemView.findViewById(R.id.image);
            fav = itemView.findViewById(R.id.favorites);
           download_bt = itemView.findViewById(R.id.download_bt);

           share_bt = itemView.findViewById(R.id.share);
         fav_bt = itemView.findViewById(R.id.favorites_bt);
         view_bt = itemView.findViewById(R.id.views_bt);



        }
    }
    private Uri getLocalBitmapUri(ImageView imageView) {
        // Extract Bitmap from ImageView drawable
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable){
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            File file =  new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), "share_image_" + System.currentTimeMillis() + ".png");
            file.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }





































    public class DownloadImage extends AsyncTask<String, Integer, String> {

        @Override
        public void onPreExecute() {
            super .onPreExecute();

            sankeBar("Downloading Image ",activity.getWindow().getDecorView().getRootView());

        }

        @Override
        protected String doInBackground(String... url) {
            File mydir = new File(Environment.getExternalStorageDirectory() + "/WallX");
            if (!mydir.exists()) {
                mydir.mkdirs();
            }
            //Log.d("myImageDownload",url[0].toString());


            DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            Uri downloadUri = Uri.parse(url[0]);
            DownloadManager.Request request = new DownloadManager.Request(downloadUri);

            SimpleDateFormat dateFormat = new SimpleDateFormat("mmddyyyyhhmmss");
            String date = dateFormat.format(new Date());

            request.setAllowedNetworkTypes(
                    DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                    .setAllowedOverRoaming(false)
                    .setTitle("Downloading Image ")
                    .setDestinationInExternalPublicDir("/WallX", date + ".jpg");

            manager.enqueue(request);
            return mydir.getAbsolutePath() + File.separator + date + ".jpg";
        }

        @Override
        public void onPostExecute(String s) {
            super .onPostExecute(s);

            //sankeBar("Image Saved",activity.getWindow().getDecorView().getRootView());
        }
    }


    // Check Permission of downloading

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (activity.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                // Log.v(TAG,"Permission is granted");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            //Log.v(TAG,"Permission is granted");
            return true;
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        activity.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
            //resume tasks needing this permission
        }
    }

    public void sankeBar(String msg,View view) {
        Snackbar snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_SHORT);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        snackbar.show();
    }

}
