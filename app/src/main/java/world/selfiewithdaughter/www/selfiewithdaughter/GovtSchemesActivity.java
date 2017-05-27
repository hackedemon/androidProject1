package world.selfiewithdaughter.www.selfiewithdaughter;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class GovtSchemesActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Schemes>> {

    /** URL for data */
    private static final String REQUEST_URL =
            "http://ratofy.xyz/api.php/";

    /** Adapter for the list of schemes */
    private SchemesAdapter mAdapter;

    /** TextView that is displayed when the list is empty */
    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_govt_schemes);

        // Find a reference to the {@link ListView} in the layout
        ListView schemeListView = (ListView) findViewById(R.id.list);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        schemeListView.setEmptyView(mEmptyStateTextView);

        // Create a new adapter that takes an empty list of schemes as input
        mAdapter = new SchemesAdapter(this, new ArrayList<Schemes>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        schemeListView.setAdapter(mAdapter);

        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected scheme.
        schemeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current item that was clicked on
                Schemes currentItem = mAdapter.getItem(position);

                // Create a new intent to view the details
                Intent websiteIntent = new Intent(GovtSchemesActivity.this, SchemeDetail.class);
                websiteIntent.putExtra("HEADER", currentItem.getHeader());
                websiteIntent.putExtra("DESCRIPTION", currentItem.getDescription());

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(1, null, this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }

        final Button button = (Button) findViewById(R.id.adminButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Send the intent to launch a new activity for Admin Login
                startActivity(new Intent(GovtSchemesActivity.this, LoginActivity.class));
            }
        });
    }

    @Override
    public Loader<List<Schemes>> onCreateLoader(int i, Bundle bundle) {
        // Create a new loader for the given URL
        return new SchemesLoader(this, REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Schemes>> loader, List<Schemes> schemes) {
        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display "No schemes found."
        mEmptyStateTextView.setText(R.string.no_schemes);

        // Clear the adapter of previous earthquake data
        mAdapter.clear();

        // If there is a valid list of {@link Schemes}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (schemes != null && !schemes.isEmpty()) {
            mAdapter.addAll(schemes);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Schemes>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }
}