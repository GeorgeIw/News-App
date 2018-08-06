package com.example.android.newsapps1;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Articles>> {

    private TextView EmptyTextView;
    private ArticlesAdapter adapter;
    private static final String LOG_TAG = MainActivity.class.getName();
    private static final String GUARDIAN_LATEST_NEWS_URL = "https://content.guardianapis.com/search";
    private static final int LOADER_VALUE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //find the View with id:list and store it o ArticleListView variable
        ListView ArticleListView = findViewById(R.id.list);
        //find the view with id:empy_view and store it to EmptyTextView variable
        EmptyTextView = findViewById(R.id.empty_view);
        //set the EmptyTextView to ArticleListView
        ArticleListView.setEmptyView(EmptyTextView);
        //create a new adapter Object
        adapter = new ArticlesAdapter(this, new ArrayList<Articles>());
        //the the adapter Object to ArticleListView
        ArticleListView.setAdapter(adapter);
        //set the clickListener to every list item in the ListView
        //they should open the appropriate website URL when clicked
        ArticleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                //get the position of the list item
                Articles currentArticle = adapter.getItem(position);
                //make a Uri object from the URL
                Uri articleUri = Uri.parse(currentArticle.getUrl());
                //create a new Intent instance to open the articleUri
                Intent JumpToWebsite = new Intent(Intent.ACTION_VIEW, articleUri);
                //launch the activity
                startActivity(JumpToWebsite);
            }
        });
        //check the state of the network
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        //get details for the current active network
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        //if there is a network connection fetch the data
        if (networkInfo != null && networkInfo.isConnected()) {
            //get a reference of the LoaderManager
            LoaderManager loaderManager = getLoaderManager();
            //initialize the loader and pass the activity argument (this)
            loaderManager.initLoader(LOADER_VALUE, null, this);
            //else, display the empty text view as an error
        } else {
            //find the View with id:loading_indicator and store it in loadingProggressBar variable
            View loadingProggressBar = findViewById(R.id.loading_indicator);
            //set the visibility of the loadingProggressBar to "GONE"
            //the loading indicator is not visible anymore
            loadingProggressBar.setVisibility(View.GONE);
            //set the text of the EmptyTextView to R.string.no_internet_connection
            EmptyTextView.setText(R.string.no_internet_connection);
        }

    }

    @Override
    public android.content.Loader<List<Articles>> onCreateLoader(int i, Bundle bundle) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        //create a String that will be used in the Query
        String numOfArticles = sharedPreferences.getString(
                getString(R.string.number_of_articles_key),
                getString(R.string.number_of_articles_default_value));

        //create a String that will be used in the Query
        String orderBy = sharedPreferences.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default_value));

        //create a String that will be used in the Query
        String orderByUseDate = sharedPreferences.getString(
                getString(R.string.settings_order_by_use_date_key),
                getString(R.string.settings_order_by_use_date_default_value));

        //parse the Guardian Url to create the Query
        Uri originalUri = Uri.parse(GUARDIAN_LATEST_NEWS_URL);
        //create the Query based in the following Query parameters
        Uri.Builder uriBuilder = originalUri.buildUpon();
        uriBuilder.appendQueryParameter("use-date", orderByUseDate);
        uriBuilder.appendQueryParameter("order-by", orderBy);
        uriBuilder.appendQueryParameter("page-size", numOfArticles);
        uriBuilder.appendQueryParameter("show-tags", "contributor");
        uriBuilder.appendQueryParameter("api-key", "test");
        //create a new loader for the given URL
        //Log.i(LOG_TAG,uriBuilder.toString());
        return new ArticlesLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(android.content.Loader<List<Articles>> loader, List<Articles> articles) {
        //find the View with id:loading_indicator and store it to LoadingIndicator variable
        View LoadingIndicator = findViewById(R.id.loading_indicator);
        //se the visibility of the LoadingIndicator to "GONE"
        //and thus the LoadingIndicator is not visible anymore
        LoadingIndicator.setVisibility(View.GONE);
        //set the text on the EmptyTextView to R.string.empty_view to display the error
        EmptyTextView.setText(R.string.empty_view);
        //clear the adapter of previous data
        adapter.clear();
        //if there is a valid list of Article then add them to the adapter
        if (articles != null && !articles.isEmpty()) {
            adapter.addAll(articles);
        }
    }

    @Override
    public void onLoaderReset(android.content.Loader<List<Articles>> loader) {
        //clear the the current data that the loader submitted
        adapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate the layout with the menu with id:main_menu
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //get the id of an item
        int id = item.getItemId();
        //if the id == action_settings open an intent that will open the NewsSettingActivity
        //this activity will hold the data for the settings - preferences
        if (id == R.id.action_settings) {
            Intent newsSettingsIntent = new Intent(this, NewsSettingsActivity.class);
            startActivity(newsSettingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
