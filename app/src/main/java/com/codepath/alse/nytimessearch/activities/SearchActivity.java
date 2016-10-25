package com.codepath.alse.nytimessearch.activities;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.alse.nytimessearch.Adapter.ArticleRecyclerViewAdapter;
import com.codepath.alse.nytimessearch.BuildConfig;
import com.codepath.alse.nytimessearch.Model.Article;
import com.codepath.alse.nytimessearch.Model.ArticleResponse;
import com.codepath.alse.nytimessearch.Model.Doc;
import com.codepath.alse.nytimessearch.Model.Filter;
import com.codepath.alse.nytimessearch.R;
import com.codepath.alse.nytimessearch.databinding.ActivitySearchBinding;
import com.codepath.alse.nytimessearch.fragments.FilterDialogFragment;
import com.codepath.alse.nytimessearch.utils.ApiClient;
import com.codepath.alse.nytimessearch.utils.ApiInterface;
import com.codepath.alse.nytimessearch.utils.EndlessScrollRecyclerViewListener;
import com.codepath.alse.nytimessearch.utils.ItemClickSupport;
import com.codepath.alse.nytimessearch.utils.NetworkingCalls;
import com.codepath.alse.nytimessearch.utils.SpacesItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;

public class SearchActivity extends AppCompatActivity implements FilterDialogFragment.SaveFilterListener {

    RecyclerView resultRecyclerView;
    TextView emptyView;
    ArrayList<Article> articles;
    ArticleRecyclerViewAdapter articleRecyclerViewAdapter;
    Filter filter = new Filter();
    String queryString;
    EndlessScrollRecyclerViewListener scrollRecyclerViewListener;
   // ActivitySearchBinding binding;
    ActivitySearchBinding binding;
    static final String NO_DATA = "no_data";
    static final String NO_QUERY = "no_query";
    int rec_padding = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

     //   binding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_search);
        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);

        resultRecyclerView = binding.searchRel.searchRview;
        emptyView = binding.searchRel.recyclerviewEmptyView;
        articles = new ArrayList<>();
        articleRecyclerViewAdapter = new ArticleRecyclerViewAdapter(this, articles, emptyView);
        resultRecyclerView.setAdapter(articleRecyclerViewAdapter);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        resultRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        scrollRecyclerViewListener = new EndlessScrollRecyclerViewListener(staggeredGridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemCount, RecyclerView view) {
                loadMoreDataFromApi(page);

            }
        };
        resultRecyclerView.addOnScrollListener(scrollRecyclerViewListener);
        SpacesItemDecoration itemDecoration = new SpacesItemDecoration(rec_padding);
        resultRecyclerView.addItemDecoration(itemDecoration);
        if (queryString == null) {
            updateEmptyView(NO_QUERY);
        }
        Context mContext = this;

        ItemClickSupport.addTo(resultRecyclerView).setOnItemClickListener(
                (recyclerView, position, v) -> {

                    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                    Activity activity = (Activity) mContext;
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_share);
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TEXT, articles.get(position).getWeb_url());
                    int requestCode = 100;

                    PendingIntent pendingIntent = PendingIntent.getActivity(mContext,
                            requestCode,
                            intent,
                            PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setActionButton(bitmap, "Share Link", pendingIntent, true);
                    CustomTabsIntent customTabsIntent = builder.build();
                    customTabsIntent.launchUrl(activity, Uri.parse(articles.get(position).getWeb_url()));
                }
        );
        //On Orientation change restore the values and make the network call
        if (savedInstanceState != null) {
            queryString = savedInstanceState.getString("query");
            filter = Parcels.unwrap(savedInstanceState.getParcelable("Filter"));
            makeNetworkCall(queryString, 0);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Store the data for orientation change
        outState.putParcelable("Filter", Parcels.wrap(filter));
        outState.putString("query", queryString);
    }

    //Method for loading more data while scrolling
    public void loadMoreDataFromApi(int page) {
        makeNetworkCall(queryString, page);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                onArticleSearch(query);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_filter) {
            createDialogFragment();
        }

        return super.onOptionsItemSelected(item);
    }
//Method to create dialog fragment
    public void createDialogFragment() {
        FragmentManager m = getSupportFragmentManager();
        Bundle args = new Bundle();
        args.putParcelable(FilterDialogFragment.EXTRA_FILTER, Parcels.wrap(filter));
        FilterDialogFragment fD = FilterDialogFragment.newInstance(this, args);
        fD.show(m, "dialog");
    }

    //Search method when clicked on Search icon
    public void onArticleSearch(String query) {
        queryString = query;
        articles.clear();
        articleRecyclerViewAdapter.notifyDataSetChanged();
        scrollRecyclerViewListener.resetState();
        resultRecyclerView.scrollToPosition(0);
        boolean isInternet = NetworkingCalls.isNetworkAvailable(this);
        if (!isInternet) {
            Toast.makeText(this, "No Internet", Toast.LENGTH_LONG).show();
            updateEmptyView("df");
        } else {
            makeNetworkCall(query, 0);
        }
    }

    public void makeNetworkCall(String query, final int page) {
        String url = "http://api.nytimes.com/svc/search/v2/articlesearch.json";
        String apiKey = BuildConfig.NYTIMES_API_KEY;
        OkHttpClient httpClient = new OkHttpClient();
        HttpUrl.Builder urlbuilder = HttpUrl.parse(url).newBuilder();
        urlbuilder.addQueryParameter("api-key", apiKey);
        urlbuilder.addQueryParameter("page", String.valueOf(page));
        if (filter.getDate() != null) {
            urlbuilder.addQueryParameter("begin_date", filter.getDate());
        }
        if (!TextUtils.isEmpty(query)) {
            urlbuilder.addQueryParameter("q", query);
        }
        String news = getNewsString();
        if (news != null) {
            urlbuilder.addQueryParameter("fq", news);
        }
        if (filter.getSortOrder() != null) {
            urlbuilder.addQueryParameter("sort", filter.getSortOrder());
        }
        String apiUrl = urlbuilder.build().toString();
        Log.v("Url", apiUrl);
        Request request = new Request.Builder().url(apiUrl).build();

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                Log.v("responseData", responseData);
                try {
                    JSONObject responseJson = new JSONObject(responseData);
                    Log.v("response" + page, responseJson.toString());
                    if (responseJson.has("response")) {
                        JSONArray responseArray = responseJson.getJSONObject("response").getJSONArray("docs");
                        //   if(page > 0){
                        final int curSize = articleRecyclerViewAdapter.getItemCount();
                        final ArrayList<Article> newItems = Article.processJSONArray(responseArray);
                        articles.addAll(newItems);

                        runOnUiThread(() -> {
                            articleRecyclerViewAdapter.notifyItemRangeInserted(curSize, newItems.size());
                        });
                    } else {
                        if (responseJson.has("message")) {
                            String message = responseJson.getString("message");
                            if (message.equals("API rate limit exceeded")) {
                                runOnUiThread(() -> {
                                    Handler handler = new Handler();
// Define the code block to be executed
                                    Runnable runnableCode = new Runnable() {
                                        @Override
                                        public void run() {
                                            // Do something here on the main thread
                                            Log.d("Handlers", "Called on main thread");
                                            makeNetworkCall(queryString, page);
                                            // Repeat this the same runnable code block again another 2 seconds

                                        }
                                    };
// Start the initial runnable task by posting through the handler
                                    handler.postDelayed(runnableCode, 2000);
                                });

                            }
                        }
                    }


                } catch (JSONException e) {

                    Log.d("Response", "No data for the requested topic");
                    queryString = null;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateEmptyView(NO_DATA);
                        }
                    });
                    e.printStackTrace();
                }
                ;
            }
        });

    }

    public void networkCalls(String query, final int page) {
        String apiKey = BuildConfig.NYTIMES_API_KEY;

        Retrofit retrofit = ApiClient.getClient();
        ApiInterface apiService = retrofit.create(ApiInterface.class);
        retrofit2.Call<ArticleResponse> call = apiService.getArticleList(page, apiKey, query);
        call.enqueue(new retrofit2.Callback<ArticleResponse>() {
            @Override
            public void onResponse(retrofit2.Call<ArticleResponse> call, retrofit2.Response<ArticleResponse> response) {
                ArticleResponse articleResponse = response.body();
                List<Doc> articles = response.body().getDocs();
                for (int i = 0; i < articles.size(); i++) {
                    Log.v("articles", articles.get(0).getWebUrl());
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ArticleResponse> call, Throwable t) {

            }
        });
    }
//Listener method when clicked on save button in dialog fragment
    @Override
    public void onSaveFilter(Filter f) {
        filter = f;
        if (!NetworkingCalls.isNetworkAvailable(this)) {
            Toast.makeText(this, "No Internet", Toast.LENGTH_LONG).show();
        } else {
            articles.clear();
            //  articleAdapter.notifyDataSetChanged();
            articleRecyclerViewAdapter.notifyDataSetChanged();
            scrollRecyclerViewListener.resetState();
            makeNetworkCall(queryString, 0);
        }
    }
//Method to format news desk string
    public String getNewsString() {
        String news;
        if (!(filter.isArts() || filter.isFashion() || filter.isSports())) {
            return null;
        } else {
            news = "news_desk:(";
            if (filter.isArts()) {
                news = news + "\"Arts\"";
            }
            if (filter.isFashion()) {
                news = news + "," + "\"Fashion\"";
            }
            if (filter.isSports()) {
                news = news + "," + "\"Sports\"";
            }
            news = news + ")";

        }
        return news;
    }

    //Updating empty textview according to network status
    private void updateEmptyView(String status) {
        if (articleRecyclerViewAdapter.getItemCount() == 0) {
            TextView tv = (TextView) findViewById(R.id.recyclerview_emptyView);
            String emptyString = getString(R.string.empty_string);
            if (tv != null) {
                switch (status) {
                    case NO_DATA:
                        emptyString = "No Article present for the requested topic";
                        break;
                    case NO_QUERY:
                        emptyString = "Search to get interested articles";
                        break;
                    default:
                        if (!NetworkingCalls.isNetworkAvailable(this)) {
                            emptyString = "No Internet";
                        }
                        break;
                }
                tv.setText(emptyString);
            }
        }
    }
}
