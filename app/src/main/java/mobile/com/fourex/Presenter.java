package mobile.com.fourex;

import java.util.List;

public class Presenter implements Contract.IPresenter{
    private Contract.IView view;
    private Contract.IModel model;

    Presenter (Contract.IView view, Contract.IModel model){
        this.model = model;
        this.view = view;
        this.model.setCallback(this);
    }
    @Override
    public void onSearch(String input) {
        model.search(input);
    }

    @Override
    public void onSuccess(List<Venue> venues) {
        view.showVenues(venues);
    }

    @Override
    public void makeDialog(String title, String message) {
        view.showDialog(title, message);
    }

    @Override
    public void showSearchingProgress(boolean isSearching) {
        view.showProgressBar(isSearching);
    }

    @Override
    public void onDestroy(){
        view = null;
        model.stopLocationUpdates();
    }

    public Contract.IView getView(){
        return this.view;
    }
}
