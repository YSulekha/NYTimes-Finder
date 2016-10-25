package com.codepath.alse.nytimessearch.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

//Utility class for Networking Calls
public class NetworkingCalls {
    public static boolean isNetworkAvailable(Context context){
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

 /*   public void makeNetworkCall(String query, final int page, Filter filter) {
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
                                            makeNetworkCall(query, page,filter);
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

    }*/

}
