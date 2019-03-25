package pl.javastart.wydatex.location;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import pl.javastart.wydatex.R;
import pl.javastart.wydatex.database.Expense;
import pl.javastart.wydatex.database.Location;
import pl.javastart.wydatex.database.WydatexDatabase;

public class LocationActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks {

    public static final String EXTRA_LOCATION_ID = "extra.location.id";
    public static final String EXTRA_EXPENSE_ID = "extra.expense.id";

    private Expense expense;
    private Location location;

    private GoogleMap googleMap;

    private EditText locationName;
    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        setupToolbar();

        locationName = findViewById(R.id.location_name);

        setUpMapIfNeeded(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

                if(getIntent().getExtras() == null) {
                    location = new Location();
                }else if(getIntent().getExtras().getLong(EXTRA_LOCATION_ID) != 0) {
                    long locationId = getIntent().getExtras().getLong(EXTRA_LOCATION_ID);
                    location = WydatexDatabase.getDatabase(LocationActivity.this).getLocationDao().findById(locationId);
                    locationName.setText(location.getName());
                    showLocationMarker();
                    navigateMapCameraTo(location);

                } else {
                    long expenseId = getIntent().getExtras().getLong(EXTRA_EXPENSE_ID);
                    expense = WydatexDatabase.getDatabase(LocationActivity.this).getExpenseDao().findById(expenseId);
                    location = new Location();
                    expense.setLocationId(location.getId());
                    buildGoogleApiClient();
                    googleApiClient.connect();
                }
            }
        });

    }

    private void navigateMapCameraTo(Location location) {
        LatLng pos = new LatLng(location.getLatitude(), location.getLongitude());

        CameraPosition cameraPosition = CameraPosition.builder()
                .target(pos)
                .zoom(14)
                .build();

        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

            }
        });
    }

    private void setUpMapIfNeeded(final OnMapReadyCallback onMapReadyCallback) {
        if (googleMap == null) {
            ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap map) {
                    googleMap = map;
                    onMapReadyCallback.onMapReady(map);
                }
            });
        } else {
            onMapReadyCallback.onMapReady(googleMap);
        }
    }

    private void showLocationMarker() {
        if(location != null) {
            googleMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title(location.getName()));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_location_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.save) {
            saveCurrentLocation();
            finish();
        }

        if(item.getItemId() == R.id.delete) {
            deleteLocation();
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteLocation() {
        if(expense != null && expense.getLocationId() != null) {
            expense.setLocationId(null);
            WydatexDatabase.getDatabase(this).getExpenseDao().update(expense);
        }

        WydatexDatabase.getDatabase(this).getLocationDao().delete(location);
    }

    private void saveCurrentLocation() {
        double lat = googleMap.getCameraPosition().target.latitude;
        double lng = googleMap.getCameraPosition().target.longitude;
        float zoom = googleMap.getCameraPosition().zoom;
        location.setLatitude(lat);
        location.setLongitude(lng);
        location.setZoom(zoom);
        location.setName(locationName.getText().toString());
        WydatexDatabase.getDatabase(this).getLocationDao().update(location);
        WydatexDatabase.getDatabase(this).getExpenseDao().update(expense);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    protected synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        android.location.Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(
                googleApiClient);

        LatLng pos = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());

        CameraPosition cameraPosition = CameraPosition.builder()
                .target(pos)
                .zoom(14)
                .build();

        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

}
