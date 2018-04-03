package displaycomic.android.com.displaycomic;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import java.io.InputStream;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.List;



/**
 * Created by varsha on 7/29/2017.
 */

public class RssService extends IntentService {

    private static final String RSS_LINK = "https://loadingartist.com/feed/";
    public static final String ITEMS = "items";
    public static final String ACTION_RSS_PARSED = "displaycomic.android.com.displaycomic.ACTION_RSS_PARSED";
    public RssService() {
        super("RssService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(Constants.TAG, "Service started");
        List<RssItem> rssItems = null;
        try {
            RssParser parser = new RssParser();
            rssItems = parser.parse(getInputStream(RSS_LINK));
        } catch (XmlPullParserException | IOException e) {
            Log.w(e.getMessage(), e);
        }

        // Send result
        Intent resultIntent = new Intent(ACTION_RSS_PARSED);
        resultIntent.putExtra(ITEMS, (Serializable) rssItems);
        LocalBroadcastManager.getInstance(this).sendBroadcast(resultIntent);

    }

    public InputStream getInputStream(String link) {
        try {
            URL url = new URL(link);
            return url.openConnection().getInputStream();
        } catch (IOException e) {
            Log.w(Constants.TAG, "Exception while retrieving the input stream", e);
            return null;
        }
    }
}
