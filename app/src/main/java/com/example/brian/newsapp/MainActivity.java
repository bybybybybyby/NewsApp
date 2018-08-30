package com.example.brian.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Article>> {

    public static final String LOG_TAG = MainActivity.class.getName();

    private static final String GUARDIAN_REQUEST_URL =
               "https://content.guardianapis.com/search?";

    private ArticleAdapter mAdapter;

    private static final int ARTICLE_LOADER_ID = 1;

    // TextView that is displayed when the list is empty
      private TextView mEmptyStateTextView;

    private View loadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView articleListView = findViewById(R.id.list);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_textview);
        articleListView.setEmptyView(mEmptyStateTextView);

        // Create a new adapter that takes an empty list of articles as input
        mAdapter = new ArticleAdapter(this, new ArrayList<Article>());

        // Set the adapter on the {@link ListView} so the list can be populate in the user interface
        articleListView.setAdapter(mAdapter);

        // Find View for the loading indicator
        loadingIndicator = findViewById(R.id.loading_spinner);

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to LoaderManger, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();
            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(ARTICLE_LOADER_ID, null, this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            loadingIndicator.setVisibility(View.INVISIBLE);
            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }

        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with the selected Article.
        articleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current article that was clicked on
                Article currentArticle = mAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri articleUri = Uri.parse(currentArticle.getUrl());

                // Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, articleUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });
    }

    @Override
    //onCreateLoader instantiates and returns a new Loader for the given ID
    public Loader<List<Article>> onCreateLoader(int i, Bundle bundle) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        // getString retrieves a String value from the preferences.  The second parameter is the
        // default value for this preference.
        String section = sharedPrefs.getString(
                getString(R.string.settings_section_key),
                getString(R.string.settings_section_default));

        String date = sharedPrefs.getString(
                getString(R.string.settings_before_date_key),
                getString(R.string.settings_before_date_default));

        // parse breaks apart the URI string that is passed into its parameter.
        Uri baseUri = Uri.parse(GUARDIAN_REQUEST_URL);

        //buildUpon prepares the baseUri that we just parsed so we can add query parameters to it.
        Uri.Builder uriBuilder = baseUri.buildUpon();

        //Append query parameter and its value.
        if(!section.equals("all")) {
            uriBuilder.appendQueryParameter("section", section);
        }
        uriBuilder.appendQueryParameter("show-tags", "contributor");
        uriBuilder.appendQueryParameter("order-by", "newest");
        if(!TextUtils.isEmpty(date)){
            uriBuilder.appendQueryParameter("to-date", date);
        }
        uriBuilder.appendQueryParameter("q", "bitcoin");
        uriBuilder.appendQueryParameter("api-key", "63df1762-ccb4-4774-b499-7bee73041bf8");

        // Return the completed uri
        Log.v("!!URIBUILDER URL", uriBuilder.toString());
        return new ArticleLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> articles) {
        loadingIndicator.setVisibility(View.VISIBLE);
        mAdapter.clear();

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            mEmptyStateTextView.setText(R.string.no_articles);

            //Removes loading indicator now that onLoadFinished is called
            loadingIndicator.setVisibility(View.INVISIBLE);

            // Clear the adapter of previous article data
            mAdapter.clear();

            // If there is a valid list of {@link Article}s, then add them to the adapter's
            // data set. This will trigger the ListView to update.
            if (articles != null && !articles.isEmpty()) {
                mAdapter.addAll(articles);
            }
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            loadingIndicator.setVisibility(View.INVISIBLE);
            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the Options enu we specified in XML
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
