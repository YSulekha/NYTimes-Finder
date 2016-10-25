package com.codepath.alse.nytimessearch.utils;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;

/**
 * Created by aharyadi on 10/24/16.
 */

public abstract class EndlessScrollRecyclerViewListener extends RecyclerView.OnScrollListener {
    private int visibleThreshold = 5;
    private int currentPage = 0;
    private int previousTotalItemCount = 0;
    private boolean loading = true;
    private int startingPageIndex = 0;

    RecyclerView.LayoutManager  mLayoutManager;

    public EndlessScrollRecyclerViewListener(LinearLayoutManager layoutManager){
        this.mLayoutManager = layoutManager;
    }

    public EndlessScrollRecyclerViewListener(GridLayoutManager layoutManager){
        this.mLayoutManager = layoutManager;
        visibleThreshold = visibleThreshold * layoutManager.getSpanCount();
    }

    public EndlessScrollRecyclerViewListener(StaggeredGridLayoutManager layoutManager){
        this.mLayoutManager = layoutManager;
        visibleThreshold = visibleThreshold * layoutManager.getSpanCount();
    }

    public int getLastVisibleItem(int[] lastVisibleItemPositions) {
        int maxSize = 0;
        for (int i = 0; i < lastVisibleItemPositions.length; i++) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i];
            }
            else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i];
            }
        }
        return maxSize;
    }


    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        int lastVisibleItemPosition = 0;
        int totalItemCount = mLayoutManager.getItemCount();

        if(mLayoutManager instanceof StaggeredGridLayoutManager){
            int[] lastVisibeItempositions = ((StaggeredGridLayoutManager)mLayoutManager).findLastVisibleItemPositions(null);
            lastVisibleItemPosition = getLastVisibleItem(lastVisibeItempositions);
        }
        else if(mLayoutManager instanceof GridLayoutManager){
            lastVisibleItemPosition = ((GridLayoutManager)mLayoutManager).findLastVisibleItemPosition();
        }
        else if(mLayoutManager instanceof LinearLayoutManager){
            lastVisibleItemPosition = ((LinearLayoutManager)mLayoutManager).findLastVisibleItemPosition();
        }

        if(loading && totalItemCount >previousTotalItemCount){
            loading = false;
            previousTotalItemCount = totalItemCount;
        }

        if(!loading && visibleThreshold+lastVisibleItemPosition > totalItemCount){
            currentPage++;
            onLoadMore(currentPage,totalItemCount,recyclerView);
            loading=true;

        }
    }
    public void resetState(){
        Log.v("startingPageIndex",String.valueOf(this.startingPageIndex));
        this.currentPage =this.startingPageIndex;
        this.previousTotalItemCount=0;
        this.loading = true;

    }
    public abstract void onLoadMore(int page,int totalItemCount, RecyclerView  view);
}
