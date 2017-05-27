package world.selfiewithdaughter.www.selfiewithdaughter;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Loads a list of schemes by using an AsyncTask to perform the
 * network request to the given URL.
 */
public class SchemesLoader extends AsyncTaskLoader<List<Schemes>> {

    /** Tag for log messages */
    private static final String LOG_TAG = SchemesLoader.class.getName();

    /** Query URL */
    private String mUrl;

    /**
     * Constructs a new {@link SchemesLoader}.
     *
     * @param context of the activity
     * @param url to load data from
     */
    public SchemesLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<Schemes> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of schemes.
        List<Schemes> schemes = QueryUtils.fetchSchemesData(mUrl);
        return schemes;
    }
}