package mobile.com.fourex;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@RunWith(MockitoJUnitRunner.class)
public class ExampleUnitTest {

    @Mock
    private Contract.IView view;
    @Mock
    private Contract.IModel model;
    @Mock
    private SearchHelper searchHelper;
    @Mock
    private LocationProvider provider;
    @Mock
    private Model mod;

    private Presenter presenter;

    @Before
    public void setUp() {
        presenter = new Presenter(view, model);
        mod = mock(Model.class);
    }


    @Test
    public void checkIfItemsArePassedToView() {
        List<Venue> items = Arrays
                .asList(new Venue("Mol", "Lat", "9")
                        , new Venue("Pub", "St", "5"));
        presenter.onSuccess(items);
        verify(view, times(1)).showVenues(items);
    }

    @Test
    public void checkOnDestroy() {
        presenter.onDestroy();
        verify(model, times(1)).stopLocationUpdates();
        assertNull(presenter.getView());
    }

    @Test
    public void checkGetLocationNotCalled() {
        when(mod.isNetworkAvailable()).thenReturn(false);
        verify(provider, times(0)).getLocation();
    }

    @Test
    public void checkSearchHelperNotInstantiated() {
        when(provider.getLocation()).thenReturn(null);
        verify(searchHelper, times(0)).onPreExecute();
    }

    @Test
    public void checkIfRightMessageIsDisplayed() {
        ArgumentCaptor<String> captor1 = forClass(String.class);
        List<String> s = Arrays.asList("B", "C");
        presenter.makeDialog("B", "C");
        verify(view, times(1)).showDialog(captor1.capture(), captor1.capture());
        assertThat(captor1.getAllValues(), is(s));
    }

    @Test
    public void checkAppNotCrushes( ){
        when(mod.isNetworkAvailable()).thenReturn(true);
        when(provider.getLocation()).thenReturn(null);
        verify(mod, times(0)).search("A");
    }

    @Test
    public void checkIfSearchInNull(){
        //check view doesnt call showVenues method
        List<Venue> items = Arrays
                .asList(new Venue("Mol", "Lat", "9")
                        , new Venue("Pub", "St", "5"));
        when(searchHelper.doInBackground()).thenReturn(null);
        verify(view, times(0)).showVenues(items);
    }
}