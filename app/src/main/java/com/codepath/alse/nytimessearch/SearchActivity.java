package com.codepath.alse.nytimessearch;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import com.codepath.alse.nytimessearch.Adapter.ArticleArrayAdapter;
import com.codepath.alse.nytimessearch.Model.Article;
import com.codepath.alse.nytimessearch.Model.Filter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SearchActivity extends AppCompatActivity implements FilterDialogFragment.SaveFilterListener{

    @BindView(R.id.search_btn)Button searchButton;
    @BindView(R.id.search_etext)EditText searchText;
    @BindView(R.id.search_gview) GridView resultGridView;
    ArrayList<Article> articles;
    ArticleArrayAdapter articleArrayAdapter;
    Filter filter = new Filter();
    int pageN=0;
    String queryString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        articles = new ArrayList<>();
        articleArrayAdapter = new ArticleArrayAdapter(this,articles);
        resultGridView.setAdapter(articleArrayAdapter);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        resultGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(SearchActivity.this,ArticleActivity.class);
                intent.putExtra("Article",articles.get(i).getWeb_url());
                startActivity(intent);
            }
        });
        resultGridView.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemCount) {
                Log.v("LoadMore", String.valueOf(page));
                loadMoreDataFromApi(page);
                return true;
            }
        });
    }

    public void loadMoreDataFromApi(int page){

        makeNetworkCall(queryString,page);

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

        if(id==R.id.action_filter){
            createDialogFragment();
        }

        return super.onOptionsItemSelected(item);
    }

    public void createDialogFragment(){
        FragmentManager m = getSupportFragmentManager();
        Bundle args = new Bundle();
        args.putParcelable("Filter", Parcels.wrap(filter));
        FilterDialogFragment fD = FilterDialogFragment.newInstance(args);
        fD.show(m,"dd");
    }

    //onClick method of Search button
    public void onArticleSearch(String query) {

    //    String text = searchText.getText().toString();
        queryString = query;
        articles.clear();
        articleArrayAdapter.notifyDataSetChanged();
        makeNetworkCall(query,0);
    }

    public void makeNetworkCall(String query, final int page){
        String url = "http://api.nytimes.com/svc/search/v2/articlesearch.json";
        String apiKey = BuildConfig.NYTIMES_API_KEY;
        OkHttpClient httpClient = new OkHttpClient();
        HttpUrl.Builder urlbuilder = HttpUrl.parse(url).newBuilder();
        urlbuilder.addQueryParameter("api-key",apiKey);
        urlbuilder.addQueryParameter("page",String.valueOf(page));
        if(filter.getDate()!=null){
            urlbuilder.addQueryParameter("begin_date",filter.getDate());
        }
        if(!TextUtils.isEmpty(query)) {
            urlbuilder.addQueryParameter("q", query);
        }
        String news = getNewsString();
        if(news != null){
            urlbuilder.addQueryParameter("fq",news);
        }
        if(filter.getSortOrder()!=null){
            urlbuilder.addQueryParameter("sort",filter.getSortOrder());
        }
        String apiUrl = urlbuilder.build().toString();
        Log.v("Url",apiUrl);
        Request request = new Request.Builder().url(apiUrl).build();

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                pageN = page;
                try {

                    JSONObject responseJson = new JSONObject(responseData);
                    Log.v("response",responseJson.toString());
                    JSONArray responseArray = responseJson.getJSONObject("response").getJSONArray("docs");
                 //   articles.clear();
                    articles.addAll(Article.processJSONArray(responseArray));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            articleArrayAdapter.notifyDataSetChanged();
                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                };
            }
        });

    }

    @Override
    public void onSaveFilter(Filter f) {
        Log.v("InsideOnSaveFiltre","dfsdf");
        filter = f;
        String text = searchText.getText().toString();
        Log.v("InsideOnSaveFiltre",text);
        makeNetworkCall(queryString,0);
    }

    public String getNewsString(){
        String news;
        if(!(filter.isArts() || filter.isFashion() || filter.isSports())){
            return null;
        }
        else{
            news = "news_desk:(";
            if(filter.isArts()){
                news = news+"\"Arts\"";
            }
            if(filter.isFashion()){
                news = news+","+"\"Fashion\"";
            }
            if(filter.isSports()){
                news = news+","+"\"Sports\"";
            }
            news = news+")";

        }
        return news;
    }
}
