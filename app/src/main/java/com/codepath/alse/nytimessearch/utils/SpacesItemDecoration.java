package com.codepath.alse.nytimessearch.utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by aharyadi on 10/24/16.
 */

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    public int mSpace;

    public SpacesItemDecoration(int space){
        this.mSpace = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = mSpace;
        outRect.right = mSpace;
        outRect.bottom = mSpace;
        if(parent.getChildAdapterPosition(view)==0){
            outRect.top=mSpace;
        }
    }
}