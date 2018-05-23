package mobile.com.fourex;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

public class SearchHelper extends AsyncTask<Object, Void, JSONObject> {
    private static final String TAG = "SearchHelper";
    private ISearch iSearch;
    private String today;


    public SearchHelper(ISearch onFinish){
        this.iSearch = onFinish;
    }

    @Override
    protected void onPreExecute(){
        Date date = new Date();
        today =  new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(date);
        iSearch.searchInProgress(true);
        Log.e(TAG, today);
    }

    @Override
    protected JSONObject doInBackground(Object... args) {
        HttpURLConnection connection = null;
        String query = (String) args[0];
        double lat = (double) args[1];
        double lon = (double) args[2];
        try {
            URL url = new URL(URLProvider.webPath + URLProvider.endPoint
                    + "?client_id=" + URLProvider.clientID + "&client_secret=" + URLProvider.clientSecret
                    + "&v=" + today + "&ll=" + lat + "," + lon + "&query=" + query);

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            if ((connection.getResponseCode() == HttpsURLConnection.HTTP_OK)) {
                InputStream in = new BufferedInputStream(connection.getInputStream());
                String response = inputStreamToString(in);
                JSONTokener token = new JSONTokener(response);
                return new JSONObject(token);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        } finally {
            if (connection != null){
                connection.disconnect();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(JSONObject jObject){
        super.onPostExecute(jObject);
        iSearch.searchInProgress(false);
        iSearch.onSearchComplete(jObject);
    }

    private String inputStreamToString(InputStream in) {
        String line;
        StringBuilder builder = new StringBuilder();
        InputStreamReader isr = new InputStreamReader(in);
        BufferedReader reader = new BufferedReader(isr);
        try {
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return builder.toString();
    }
}