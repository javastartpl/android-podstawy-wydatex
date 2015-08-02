package pl.javastart.wydatex;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import pl.javastart.wydatex.database.DatabaseHelper;
import pl.javastart.wydatex.database.ExpenseRepository;

public class ExpenseActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap googleMap;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setRotateGesturesEnabled(false);
        googleMap.getUiSettings().setZoomControlsEnabled(false);
        googleMap.getUiSettings().setZoomControlsEnabled(false);
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51.9976972, 19.2078602), 5));
    }

    private enum State {NEW, EDIT}

    private static final String PREF_LAST_CATEGORY = "pref.last.category";

    public static final int INVALID_ID = -1;
    public static final String ID = "id";

    private State state;
    private EditText titleEditText;
    private EditText priceEditText;
    private Spinner categorySpinner;

    private Expense expense;

    private SharedPreferences sharedPreferences;

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

        categorySpinner.setAdapter(new CategoryAdapter());

        long id = INVALID_ID;
        if (getIntent().getExtras() != null) {
            id = getIntent().getExtras().getLong(ID, INVALID_ID);
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
        }

        if (state == State.NEW) {
            if (shouldCareAboutLastCategory()) {
                loadLastCategory(categorySpinner);
            }
            loadDefaultValues();
        }

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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

    private class CategoryAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return ExpenseCategory.values().length;
        }

        @Override
        public ExpenseCategory getItem(int position) {
            return ExpenseCategory.values()[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(android.R.layout.simple_dropdown_item_1line, null);
            }

            TextView textView = (TextView) convertView.findViewById(android.R.id.text1);
            textView.setText(getItem(position).getName());

            return convertView;
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
                    addNewExpense();
                } else {
                    saveChanges();
                }
                finish();
                return true;
            case R.id.delete:
                DatabaseHelper.getInstance(this).getExpenseDao().delete(expense);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addNewExpense() {
        String title = titleEditText.getText().toString();
        double price = Double.parseDouble(priceEditText.getText().toString());
        ExpenseCategory category = (ExpenseCategory) categorySpinner.getSelectedItem();
        Expense expense = new Expense(title, price, category);
        ExpenseRepository.addExpense(this, expense);

        if (state == State.NEW && shouldCareAboutLastCategory()) {
            saveLastCategory(category);
        }
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
}
