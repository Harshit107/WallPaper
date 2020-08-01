package com.harshit.wallpaper.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.harshit.wallpaper.ItemList;
import com.harshit.wallpaper.R;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;

public class LoadImageRecyclearAdapter extends RecyclerView.Adapter<LoadImageRecyclearAdapter.LoadImageViewHolder> {

    private Context context;
    private ArrayList<ItemList> itemLists;

    public LoadImageRecyclearAdapter(Context context, ArrayList<ItemList> itemLists) {
        this.context = context;
        this.itemLists = itemLists;
    }

    @NonNull
    @Override
    public LoadImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.recycle_list,parent,false);
        return new LoadImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull LoadImageViewHolder holder,
                                 int position) {

      ItemList selectItem = itemLists.get(position);
      String imageUrl = selectItem.getImageUrl();
      String downloadImageUrl = selectItem.getDownloadImageUrl();
      int likes = selectItem.getLikes();
      int fav = selectItem.getFavourite();
      int downloads = selectItem.getDownloads();
      int views = selectItem.getViews();
      int id = selectItem.getmId();

        Log.d("value",String.valueOf(downloads));

      holder.download.setText(String.valueOf(downloads));
      holder.views.setText(String.valueOf(views));
      holder.fav.setText(String.valueOf(fav));
      holder.likes.setText(String.valueOf(likes));
     // holder.displayImage.setText(imageUrl);

         RequestOptions options = new RequestOptions()
                 .fitCenter()
                 .centerCrop()
                .placeholder(R.drawable.progress_animation)
                .error(android.R.drawable.stat_notify_error)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH)
                .dontAnimate()
                .dontTransform();

        Glide
                .with(context)

                .load(imageUrl)
                .apply(options)
                .into(holder.displayImage);


    }

    @Override
    public int getItemCount() {
        return itemLists.size();
    }


    //Inner Class For Views
    public class LoadImageViewHolder extends RecyclerView.ViewHolder{

        public TextView likes,download,views,fav;
        ImageView likeImage,displayImage;
        //Button download_bt;
        public LoadImageViewHolder(@NonNull View itemView) {
            super(itemView);

            likeImage = itemView.findViewById(R.id.like);
            likes = itemView.findViewById(R.id.like_tv);
            download = itemView.findViewById(R.id.download);
            views = itemView.findViewById(R.id.views);
            displayImage = itemView.findViewById(R.id.image);
            fav = itemView.findViewById(R.id.favorites);
//            download_bt = itemView.findViewById(R.id.download_bt);

        }
    }


}
