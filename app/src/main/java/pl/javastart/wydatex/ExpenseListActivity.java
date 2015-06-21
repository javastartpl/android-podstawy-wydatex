package pl.javastart.wydatex;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class ExpenseListActivity extends Activity {

    private ListView expenseListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_expense_list);

        expenseListView = (ListView) findViewById(R.id.listView);
        expenseListView.setAdapter(new ExpenseListAdapter());

		Button newExpenseButton = (Button) findViewById(R.id.add_expense);
		newExpenseButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				Intent intent = new Intent(getApplicationContext(), ExpenseActivity.class);
				startActivity(intent);
			}
		});
	}

    @Override
    protected void onStart() {
        super.onStart();
        expenseListView.invalidateViews();
    }

    public void settingsButton(View view) {
        Intent intent = new Intent(getApplicationContext(), PreferencesActivity.class);
        startActivity(intent);
    }

    private class ExpenseListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return ExpenseDatabase.getExpenses().size();
        }

        @Override
        public Expense getItem(int position) {
            return ExpenseDatabase.getExpenses().get(position);
        }

        @Override
        public long getItemId(int position) {
            return  position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
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
}
