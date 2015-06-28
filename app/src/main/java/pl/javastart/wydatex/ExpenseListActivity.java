package pl.javastart.wydatex;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import pl.javastart.wydatex.database.ExpenseRepository;

public class ExpenseListActivity extends Activity {

    private ListView expenseListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_list);

        expenseListView = (ListView) findViewById(R.id.listView);
        expenseListView.setAdapter(new ExpenseListAdapter());
        expenseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ExpenseListActivity.this, ExpenseActivity.class);
                intent.putExtra(ExpenseActivity.ID, id);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        expenseListView.invalidateViews();
    }

    private class ExpenseListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return ExpenseRepository.getAllExpenses(ExpenseListActivity.this).size();
        }

        @Override
        public Expense getItem(int position) {
            return ExpenseRepository.getAllExpenses(ExpenseListActivity.this).get(position);
        }

        @Override
        public long getItemId(int position) {
            return getItem(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_item_expense, null);
            }

            TextView title = (TextView) convertView.findViewById(R.id.title);
            TextView price = (TextView) convertView.findViewById(R.id.price);
            TextView category = (TextView) convertView.findViewById(R.id.category);
            Expense item = getItem(position);
            title.setText(item.getName());

            price.setText(String.format("%.2f", item.getPrice()) + "zł");
            category.setText(item.getCategory().getName());

            return convertView;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_new_expense:
                Intent intent = new Intent(getApplicationContext(), ExpenseActivity.class);
                startActivity(intent);
                return true;
            case R.id.preferences:
                intent = new Intent(getApplicationContext(), PreferencesActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onMenuItemSelected(featureId, item);
        }
    }
}
