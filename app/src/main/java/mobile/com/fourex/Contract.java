package mobile.com.fourex;

import java.util.List;

public interface Contract {

    interface IView{
        void showVenues(List<Venue> venues);
        void showDialog(String title, String message);
        void showProgressBar(boolean isSearching);
    }

    interface IPresenter{
        void onSearch(String input);
        void onSuccess(List<Venue> venues);
        void makeDialog(String title, String message);
        void showSearchingProgress(boolean isSearching);
        void onDestroy();
    }

    interface IModel {
        void search(String input);
        void setCallback(Contract.IPresenter p);
        void stopLocationUpdates();
    }
}
