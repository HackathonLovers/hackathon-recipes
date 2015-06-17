package com.h4ckademy.lextrend.lextrendcamera;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadWebpageTask extends AsyncTask<String, Void, Bitmap> {

    private DownloadWebPageTaskCallback callback;

    public DownloadWebpageTask(DownloadWebPageTaskCallback callback) {
        this.callback = callback;
    }

    @Override
    protected Bitmap doInBackground(String... url) {

        // params comes from the execute() call: params[0] is the url.
        try {
            return downloadUrl(url[0]);
        } catch (IOException e) {
            Log.i("Error", "Unable to retrieve web page. URL may be invalid.");
            return null;
        }
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if( bitmap != null )
            callback.onFinish( bitmap );
        else
            callback.onError( "NULL Bitmap" );
    }

    // Given a URL, establishes an HttpUrlConnection and retrieves
    // the web page content as a InputStream, which it returns as
    // a Bitmap.
    private Bitmap downloadUrl(String myurl) throws IOException {
        InputStream is = null;

        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.i("Response", "The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a Bitmap
            return BitmapFactory.decodeStream(is);

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        }
        finally {
            if (is != null) {
                is.close();
            }
        }
    }

    public interface DownloadWebPageTaskCallback{
        public void onFinish(Bitmap b);
        public void onError(String errorMsg);
    }
}


