package pl.javastart.wydatex;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import pl.javastart.wydatex.database.ExpenseRepository;

public class ExpenseActivity extends Activity {

    private static final String PREF_LAST_CATEGORY = "pref.last.category";

    private EditText titleEditText;
    private EditText priceEditText;
    private Spinner categorySpinner;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        titleEditText = (EditText) findViewById(R.id.expense_name);
        priceEditText = (EditText) findViewById(R.id.expensePrice);
        categorySpinner = (Spinner) findViewById(R.id.expense_category);

        categorySpinner.setAdapter(new CategoryAdapter());

        if (shouldCareAboutLastCategory()) {
            loadLastCategory(categorySpinner);
        }
        loadDefaultValues();
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

    private void addNewExpense() {
        String title = titleEditText.getText().toString();
        double price = Double.parseDouble(priceEditText.getText().toString());
        ExpenseCategory category = (ExpenseCategory) categorySpinner.getSelectedItem();
        Expense expense = new Expense(title, price, category);
        ExpenseRepository.addExpense(this, expense);

        if (shouldCareAboutLastCategory()) {
            saveLastCategory(category);
        }

        finish();
    }

    private void saveLastCategory(ExpenseCategory expenseCategory) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREF_LAST_CATEGORY, expenseCategory.name());
        editor.commit();
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
                convertView = getLayoutInflater().inflate(android.R.layout.simple_spinner_item, null);
            }

            TextView textView = (TextView) convertView.findViewById(android.R.id.text1);
            textView.setText(getItem(position).getName());

            return convertView;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.expense_menu, menu);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                addNewExpense();
                return true;
            default:
                return super.onMenuItemSelected(featureId, item);
        }
    }
}
