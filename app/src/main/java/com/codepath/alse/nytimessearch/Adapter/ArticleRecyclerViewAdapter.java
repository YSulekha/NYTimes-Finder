package com.codepath.alse.nytimessearch.Adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.codepath.alse.nytimessearch.ArticleActivity;
import com.codepath.alse.nytimessearch.BR;
import com.codepath.alse.nytimessearch.Model.Article;
import com.codepath.alse.nytimessearch.R;
import com.codepath.alse.nytimessearch.databinding.GridItemBinding;
import com.codepath.alse.nytimessearch.databinding.RecycerItemBinding;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

/**
 * Created by aharyadi on 10/24/16.
 */

public class ArticleRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Article> articleList;
    private static final int  TITLE = 0;
    private static final int IMAGE = 1;

    public ArticleRecyclerViewAdapter(Context context, List<Article> list){
        this.mContext = context;
        this.articleList = list;
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
    /*    holder.binding.gridItemHeadline.setText(article.getHeadline());
       // holder.headline.setText(article.getHeadline());
        //  if(article.getThumbnail()!=null)
        Picasso.with(mContext).load(article.getThumbnail()).placeholder(R.color.colorPrimary).
                into(holder.binding.gridItemImage);*/

    }
    @BindingAdapter("bind:imageUrl")
    public static void loadImage(ImageView imageView, String url){
        Picasso.with(imageView.getContext()).load(url).placeholder(R.color.colorPrimary).
                into(imageView);
    }
    public void configureTitleViewHolder(ViewHolderTitle holder, int position){
        Article article = articleList.get(position);
        holder.binding.setVariable(BR.article,article);
        holder.binding.executePendingBindings();
      //  holder.headline.setText(article.getHeadline());
        //holder.overview.setText(article.getOverview());
     //   holder.binding.recyclerItemTitle.setText(article.getHeadline());
       // holder.binding.recyclerItemTitle.setText(article.getOverview());

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
        return articleList.size();
    }

    class ViewHolderImage extends RecyclerView.ViewHolder implements View.OnClickListener,Target {
        GridItemBinding binding;
    /*    @BindView(R.id.grid_item_image)
        DynamicHeightImageView thumbnail;
        @BindView(R.id.grid_item_headline)
        TextView headline;*/

        public ViewHolderImage(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
         //   ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if(position != RecyclerView.NO_POSITION){
                Article article = articleList.get(position);
                Intent intent = new Intent(mContext,ArticleActivity.class);
                intent.putExtra("Article",article.getWeb_url());
                mContext.startActivity(intent);
            }
        }

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            float ratio = (float)bitmap.getHeight()/(float)bitmap.getWidth();
            binding.gridItemImage.setHeightRatio(ratio);
            binding.gridItemImage.setImageBitmap(bitmap);
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    }
   public class ViewHolderTitle extends RecyclerView.ViewHolder implements View.OnClickListener{
        private RecycerItemBinding binding;
  /*      @BindView(R.id.recycler_item_title)
        TextView overview;
        @BindView(R.id.recycler_item_overview)
        TextView headline;*/

        public ViewHolderTitle(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if(position != RecyclerView.NO_POSITION){
                Article article = articleList.get(position);
                Intent intent = new Intent(mContext,ArticleActivity.class);
                intent.putExtra("Article",article.getWeb_url());
                mContext.startActivity(intent);
            }
        }


    }
}
