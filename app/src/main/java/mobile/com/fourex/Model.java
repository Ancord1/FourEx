package mobile.com.fourex;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Model implements Contract.IModel, ISearch {
    private LocationProvider locationProvider;
    private Contract.IPresenter presenter;
    private List<Venue> venues;
    private Context context;

    public Model(Context con) {
        this.context = con;
        locationProvider = new LocationProvider(con);
    }

    @Override
    public void search(String input) {
        if (isNetworkAvailable()) {
            if (locationProvider.getLocation() != null) {
                double lat = locationProvider.getLatitude();
                double lon = locationProvider.getLongitude();
                new SearchHelper(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, input, lat, lon);
            } else {
                presenter.makeDialog(context.getString(R.string.no_location_title), context.getString(R.string.no_location_msg));
            }
        } else presenter.makeDialog(context.getString(R.string.no_inet_title), "");
    }

    @Override
    public void onSearchComplete(JSONObject result) {
        if (result == null)
            presenter.makeDialog(context.getString(R.string.no_data_title), context.getString(R.string.no_data_msg));
        else {
            processResponse(result);
            presenter.onSuccess(venues);
        }
    }

    @Override
    public void searchInProgress(boolean isSearching) {
        presenter.showSearchingProgress(isSearching);
    }

    @Override
    public void setCallback(Contract.IPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void stopLocationUpdates() {
        locationProvider.stopLocationAcquire();
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectionManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectionManager == null) {
            return false;
        }
        NetworkInfo networkInfo = connectionManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private void processResponse(JSONObject response) {
        venues = new ArrayList<>();
        try {
            if (response.has("response")) {
                JSONObject jObject = response.getJSONObject("response");
                JSONArray jsonArray = jObject.getJSONArray("venues");
                for (int i = 0; i < jsonArray.length(); i++) {
                    String name, address, distance;

                    if (jsonArray.getJSONObject(i).has("name") && !jsonArray.getJSONObject(i).isNull("name")) {
                        name = jsonArray.getJSONObject(i).getString("name");
                        if (jsonArray.getJSONObject(i).has("location")) {
                            if (!jsonArray.getJSONObject(i).getJSONObject("location").isNull("address")) {
                                address = jsonArray.getJSONObject(i).getJSONObject("location").getString("address");
                            } else address = "N/A";

                            if (!jsonArray.getJSONObject(i).getJSONObject("location").isNull("distance")) {
                                distance = Integer.toString(jsonArray.getJSONObject(i).getJSONObject("location").getInt("distance"));
                            } else distance = "N/A";

                            Venue venue = new Venue(name, address, distance);
                            venues.add(venue);
                        }
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
