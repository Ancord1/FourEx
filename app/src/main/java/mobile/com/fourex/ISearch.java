package mobile.com.fourex;

import org.json.JSONObject;

public interface ISearch {
    void onSearchComplete(JSONObject result);
    void searchInProgress(boolean isSearching);
}
