package com.codepath.alse.nytimessearch.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.alse.nytimessearch.Model.Article;
import com.codepath.alse.nytimessearch.R;
import com.squareup.picasso.Picasso;

import java.util.List;


public class ArticleArrayAdapter extends ArrayAdapter<Article> {
    public ArticleArrayAdapter(Context context, List<Article> articles) {
        super(context, android.R.layout.simple_list_item_1,articles);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ArticleItemViewHolder articleItemViewHolder;
        Article article = getItem(position);
        Context context = getContext();
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.grid_item,parent,false);
             articleItemViewHolder = new ArticleItemViewHolder(convertView);
            convertView.setTag(articleItemViewHolder);
        }
        else{
            articleItemViewHolder = (ArticleItemViewHolder) convertView.getTag();
        }
        articleItemViewHolder.headline.setText(article.getHeadline());
        Picasso.with(context).load(article.getThumbnail()).placeholder(R.color.colorPrimary).
                into(articleItemViewHolder.thumbnail);
        return convertView;
    }

    class ArticleItemViewHolder{
      //  @BindView(R.id.grid_item_image)
        ImageView thumbnail;
        //@BindView(R.id.grid_item_headline)
        TextView headline;

        public ArticleItemViewHolder(View v){
            //ButterKnife.bind(this,v);
        }

    }
}
