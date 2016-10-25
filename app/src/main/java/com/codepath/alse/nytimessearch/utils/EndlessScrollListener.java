package com.codepath.alse.nytimessearch.utils;

import android.util.Log;
import android.widget.AbsListView;

/**
 * Created by aharyadi on 10/22/16.
 */

public  abstract class EndlessScrollListener implements AbsListView.OnScrollListener {
    private int visibleThreshold = 5;
    private int currentPage = -1;
    private int previousTotalItemCount = 0;
    private boolean loading = true;
    private int startingPageIndex = 0;

    public EndlessScrollListener(){

    }
    public EndlessScrollListener(int vThreshold, int page){
        this.visibleThreshold = vThreshold;
        this.currentPage = page;
        this.startingPageIndex = page;
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

    }

    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        Log.v("LoadMoreEnd",String.valueOf(currentPage)+String.valueOf(previousTotalItemCount)+String.valueOf(startingPageIndex));
        Log.v("LoadMoreEndPrev",String.valueOf(previousTotalItemCount));
        Log.v("LoadMoreEndTotal",String.valueOf(totalItemCount));
        if(totalItemCount < previousTotalItemCount){
            currentPage = startingPageIndex;
            previousTotalItemCount = totalItemCount;
            if(totalItemCount == 0){
                this.loading = true;
            }
            Log.v("FirstIf","1");
        }
        if(loading && totalItemCount > previousTotalItemCount){
            loading = false;
            previousTotalItemCount = totalItemCount;
            currentPage = currentPage+1;
            Log.v("SecondIf",String.valueOf(currentPage));
        }
        if(!loading && firstVisibleItem+visibleThreshold+visibleItemCount>=totalItemCount){
            loading = true;
            Log.v("ThirdIf",String.valueOf(currentPage));
            loading = onLoadMore((currentPage+1),totalItemCount);

        }

    }
    public abstract boolean onLoadMore(int page,int totalItemCount);
}
