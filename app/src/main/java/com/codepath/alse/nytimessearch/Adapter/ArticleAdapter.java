package com.codepath.alse.nytimessearch.Adapter;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codepath.alse.nytimessearch.activities.ArticleActivity;
import com.codepath.alse.nytimessearch.utils.DynamicHeightImageView;
import com.codepath.alse.nytimessearch.Model.Article;
import com.codepath.alse.nytimessearch.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;



public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {

    private Context mContext;
    private List<Article> articleList;

    public ArticleAdapter(Context context, List<Article> articles){
        mContext = context;
        articleList = articles;
    }

    public Context getContext(){
        return mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View articleView = inflater.inflate(R.layout.grid_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(articleView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Article article = articleList.get(position);
        holder.headline.setText(article.getHeadline());
        Picasso.with(mContext).load(article.getThumbnail()).placeholder(R.color.colorPrimary).
                into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,Target{
     //   @BindView(R.id.grid_item_image)
        DynamicHeightImageView thumbnail;
       // @BindView(R.id.grid_item_headline)
        TextView headline;

        public ViewHolder(View itemView) {
            super(itemView);
       //     ButterKnife.bind(this,itemView);
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
            thumbnail.setHeightRatio(ratio);
            thumbnail.setImageBitmap(bitmap);
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    }
}
