package com.codepath.alse.nytimessearch.Adapter;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.alse.nytimessearch.BR;
import com.codepath.alse.nytimessearch.Model.Article;
import com.codepath.alse.nytimessearch.R;
import com.codepath.alse.nytimessearch.databinding.GridItemBinding;
import com.codepath.alse.nytimessearch.databinding.RecycerItemBinding;

import java.util.List;


public class ArticleRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    static private Context mContext;
    private List<Article> articleList;
    private static final int  TITLE = 0;
    private static final int IMAGE = 1;
    private TextView emptyView;


    public ArticleRecyclerViewAdapter(Context context, List<Article> list, TextView view){
        this.mContext = context;
        this.articleList = list;
        this.emptyView = view;
    }

    public Context getContext(){
        return mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        switch (viewType){
            case IMAGE:
                View articleView = inflater.inflate(R.layout.grid_item,parent,false);
                viewHolder = new ViewHolderImage(articleView);
                break;
            case TITLE:
                View titleView = inflater.inflate(R.layout.recycer_item,parent,false);
                viewHolder = new ViewHolderTitle(titleView);
                break;

        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)){
            case IMAGE:
                ViewHolderImage viewHolderImage = (ViewHolderImage)holder;
                configureImageViewHolder(viewHolderImage,position);
                break;
            case TITLE:
                ViewHolderTitle viewHolderTitle = (ViewHolderTitle)holder;
                configureTitleViewHolder(viewHolderTitle,position);
                break;
        }
    }

    public void configureImageViewHolder(ViewHolderImage holder, int position){
        Article article = articleList.get(position);
        holder.binding.setVariable(BR.article,article);
        holder.binding.executePendingBindings();
    }
    @BindingAdapter("bind:imageUrl")
    public static void loadImage(ImageView imageView, String url){
    //    Picasso.with(imageView.getContext()).load(url).placeholder(R.color.colorPrimary).
         //      // into(imageView);
        Glide.with(mContext).load(url).placeholder(R.color.colorPrimary).into(imageView);
    }
    public void configureTitleViewHolder(ViewHolderTitle holder, int position){
        Article article = articleList.get(position);
        holder.binding.setVariable(BR.article,article);
        holder.binding.executePendingBindings();

    }
    @Override
    public int getItemViewType(int position) {
        if(articleList.get(position).getThumbnail()==null){
            return TITLE;
        }
        else{
            return IMAGE;
        }
    }


    @Override
    public int getItemCount() {
        emptyView.setVisibility(articleList.size() == 0 ? View.VISIBLE : View.INVISIBLE);
        return articleList.size();
    }
    public void shareIntent(String url){

        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();

        Activity activity = (Activity)mContext;

        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_action_share);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, url);
        int requestCode = 100;

        PendingIntent pendingIntent = PendingIntent.getActivity(mContext,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setActionButton(bitmap, "Share Link", pendingIntent, true);
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(activity, Uri.parse(url));
    }


    class ViewHolderImage extends RecyclerView.ViewHolder{
        GridItemBinding binding;

        public ViewHolderImage(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);

        }


    }
   public class ViewHolderTitle extends RecyclerView.ViewHolder{
        private RecycerItemBinding binding;
       Activity activity;

        public ViewHolderTitle(View itemView) {
            super(itemView);

            binding = DataBindingUtil.bind(itemView);
        }

    }
}
