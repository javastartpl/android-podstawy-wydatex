package pl.javastart.wydatex.location;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import pl.javastart.wydatex.R;
import pl.javastart.wydatex.database.Location;
import pl.javastart.wydatex.database.LocationRepository;

public class MapsActivity extends AppCompatActivity {

    public static final String EXTRA_LOCATION_ID = "extra.location.id";

    private Location location;

    private GoogleMap googleMap;

    private EditText locationName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        locationName = (EditText) findViewById(R.id.location_name);

        setUpMapIfNeeded();

        if(getIntent().getExtras() != null) {
            long locationId = getIntent().getExtras().getLong(EXTRA_LOCATION_ID);
            location = LocationRepository.findById(this, locationId);
        } else {
            location = new Location();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (googleMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (googleMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #googleMap} is not null.
     */
    private void setUpMap() {
        if(location != null) {
            googleMap.addMarker(new MarkerOptions().position(new LatLng(location.getLat(), location.getLng())).title(location.getName()));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_map_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.save) {
            saveCurrentLocation();
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveCurrentLocation() {
        double lat = googleMap.getCameraPosition().target.latitude;
        double lng = googleMap.getCameraPosition().target.longitude;
        location.setLat(lat);
        location.setLng(lng);
        location.setName(locationName.getText().toString());
        LocationRepository.insertOrUpdate(this, location);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
