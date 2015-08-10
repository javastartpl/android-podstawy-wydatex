package pl.javastart.wydatex.expense;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.util.UUID;

import pl.javastart.wydatex.R;
import pl.javastart.wydatex.database.DatabaseHelper;
import pl.javastart.wydatex.database.Expense;
import pl.javastart.wydatex.database.ExpenseRepository;
import pl.javastart.wydatex.database.Location;
import pl.javastart.wydatex.location.LocationActivity;

public class ExpenseActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap googleMap;

    private enum State {NEW, EDIT}

    private static final String PREF_LAST_CATEGORY = "pref.last.category";
    private static final int REQUEST_TAKE_PHOTO = 999;

    public static final int INVALID_ID = -1;
    public static final String EXTRA_ID = "id";

    private State state;
    private EditText titleEditText;
    private EditText priceEditText;
    private Spinner categorySpinner;

    private ImageButton photoImageButton;
    private ImageView photoView;
    private TextView locationNameTextView;

    private String photoPath;

    private Expense expense;

    private SharedPreferences sharedPreferences;

    private MapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        titleEditText = (EditText) findViewById(R.id.expense_name);
        priceEditText = (EditText) findViewById(R.id.expensePrice);
        categorySpinner = (Spinner) findViewById(R.id.expense_category);

        categorySpinner.setAdapter(new CategoryAdapter(this));

        long id = INVALID_ID;
        if (getIntent().getExtras() != null) {
            id = getIntent().getExtras().getLong(EXTRA_ID, INVALID_ID);
        }

        if (id != INVALID_ID) {
            // id zostało przekazane
            state = State.EDIT;
            expense = DatabaseHelper.getInstance(this).getExpenseDao().queryForId(id);
            titleEditText.setText(expense.getName());
            priceEditText.setText(Double.toString(expense.getPrice()));
            categorySpinner.setSelection(ExpenseCategory.getId(expense.getCategory().getName()));
        } else {
            // id NIE zostało przekazane
            state = State.NEW;
            expense = new Expense();
        }

        if (state == State.NEW) {
            if (shouldCareAboutLastCategory()) {
                loadLastCategory(categorySpinner);
            }
            loadDefaultValues();
        }

        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);


        photoImageButton = (ImageButton) findViewById(R.id.backdropButton);
        photoView = (ImageView) findViewById(R.id.backdropImage);
        locationNameTextView = (TextView) findViewById(R.id.location_name);

        updatePhoto();
    }

    @Override
    protected void onResume() {
        super.onResume();

        findViewById(R.id.map_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLocationActivity();
            }
        });

        mapFragment.getMapAsync(this);
        updateMap(googleMap);
    }

    private void openLocationActivity() {
        Intent intent = new Intent(ExpenseActivity.this, LocationActivity.class);

        if(expense.getId() == null) {
            ExpenseCategory category = (ExpenseCategory) categorySpinner.getSelectedItem();
            expense.setCategory(category);
            ExpenseRepository.createOrUpdate(this, expense);
        }

        intent.putExtra(LocationActivity.EXTRA_EXPENSE_ID, expense.getId());
        if (expense.getLocation() != null) {
            intent.putExtra(LocationActivity.EXTRA_LOCATION_ID, expense.getLocation().getId());
        }
        startActivity(intent);
    }


    private void updatePhoto() {
        if (expense != null && expense.getPhotoPath() != null) {
            photoImageButton.setVisibility(View.GONE);
            photoView.setVisibility(View.VISIBLE);
            String photoPath = expense.getPhotoPath();
            Glide.with(this).load(photoPath).fitCenter().into(photoView);
        } else {
            photoImageButton.setVisibility(View.VISIBLE);
            photoView.setVisibility(View.GONE);
            photoImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dispatchTakePictureIntent();
                }
            });
        }
    }

    private boolean shouldCareAboutLastCategory() {
        return sharedPreferences.getBoolean("pref_save_category", false);
    }

    private void loadLastCategory(Spinner categorySpinner) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String lastCategoryName = prefs.getString(PREF_LAST_CATEGORY, "");
        if (!lastCategoryName.isEmpty()) {
            int id = ExpenseCategory.getId(lastCategoryName);
            categorySpinner.setSelection(id);
        }
    }

    private void loadDefaultValues() {
        boolean defaultValues = sharedPreferences.getBoolean("pref_default_values", false);
        if (defaultValues) {
            String defaultName = sharedPreferences.getString("pref_default_name", "");
            String defaultPrice = sharedPreferences.getString("pref_default_price", "9.99");

            titleEditText.setText(defaultName);
            priceEditText.setText(defaultPrice);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (state == State.NEW) {
            getMenuInflater().inflate(R.menu.add_expense_menu, menu);
        }
        if (state == State.EDIT) {
            getMenuInflater().inflate(R.menu.menu_expense_edit, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                if (state == State.NEW) {
                    boolean success = addNewExpense();
                    if(success) {
                        finish();
                    }
                } else {
                    saveChanges();
                    finish();
                }
                return true;
            case R.id.delete:
                if(expense.getPhotoPath() != null) {
                    File file = new File(expense.getPhotoPath());
                    file.delete();
                }

                DatabaseHelper.getInstance(this).getExpenseDao().delete(expense);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * @return true on success, false otherwise
     */
    private boolean addNewExpense() {

        if(titleEditText.getText().toString().trim().length() == 0) {
            Toast.makeText(this, "Podaj nazwę wydatku!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(priceEditText.getText().toString().trim().length() == 0) {
            Toast.makeText(this, "Podaj cenę wydatku!", Toast.LENGTH_SHORT).show();
            return false;
        }

        expense.setName(titleEditText.getText().toString());

        double price = Double.parseDouble(priceEditText.getText().toString());
        expense.setPrice(price);

        ExpenseCategory category = (ExpenseCategory) categorySpinner.getSelectedItem();
        expense.setCategory(category);

        ExpenseRepository.createOrUpdate(this, expense);

        if (state == State.NEW && shouldCareAboutLastCategory()) {
            saveLastCategory(category);
        }

        return true;
    }

    private void saveLastCategory(ExpenseCategory expenseCategory) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREF_LAST_CATEGORY, expenseCategory.name());
        editor.apply();
    }


    private void saveChanges() {
        expense.setName(titleEditText.getText().toString());
        expense.setPrice(Double.parseDouble(priceEditText.getText().toString()));
        expense.setCategory((ExpenseCategory) categorySpinner.getSelectedItem());
        DatabaseHelper.getInstance(this).getExpenseDao().update(expense);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        updateMap(googleMap);
    }

    private void updateMap(GoogleMap googleMap) {
        if(googleMap == null) {
            return;
        }
        googleMap.clear();
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setRotateGesturesEnabled(false);
        googleMap.getUiSettings().setZoomControlsEnabled(false);
        googleMap.getUiSettings().setZoomControlsEnabled(false);
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);

        Location location = expense.getLocation();
        if(location != null) {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            float zoom = location.getZoom();
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
            MarkerOptions marker = new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude()));
            googleMap.addMarker(marker);
            locationNameTextView.setText(location.getName());
        } else {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51.9976972, 19.2078602), 5));
        }

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                openLocationActivity();
            }
        });
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            photoFile = createImageFile();
            photoPath = photoFile.getAbsolutePath();

            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK && requestCode == REQUEST_TAKE_PHOTO) {
            expense.setPhotoPath(photoPath);
            updatePhoto();
        } else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "Anulowano", Toast.LENGTH_SHORT).show();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private File createImageFile() {

        String randomName = UUID.randomUUID().toString();

        File storageDir = new File(getExternalFilesDir(null), "wydatex_photos");
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }

        File image = null;
        image = new File(storageDir, randomName);

        return image;
    }
}
