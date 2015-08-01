package pl.javastart.wydatex;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import pl.javastart.wydatex.database.ExpenseRepository;

public class ExpenseListFragment extends Fragment {

    private ListView expenseListView;
    private ExpenseListAdapter expenseListAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expense_list, container, false);

        expenseListView = (ListView) view.findViewById(R.id.listView);
        expenseListAdapter = new ExpenseListAdapter();
        expenseListView.setAdapter(expenseListAdapter);
        expenseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ExpenseActivity.class);
                intent.putExtra(ExpenseActivity.ID, id);
                startActivity(intent);
            }
        });
        expenseListView.setEmptyView(view.findViewById(R.id.no_expenses));

        setHasOptionsMenu(true);

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        expenseListAdapter.notifyDataSetInvalidated();
    }

    private class ExpenseListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return ExpenseRepository.getAllExpenses(getActivity()).size();
        }

        @Override
        public Expense getItem(int position) {
            return ExpenseRepository.getAllExpenses(getActivity()).get(position);
        }

        @Override
        public long getItemId(int position) {
            return getItem(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_expense, null);
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_new_expense:
                Intent intent = new Intent(getActivity().getApplicationContext(), ExpenseActivity.class);
                startActivity(intent);
                return true;
            case R.id.preferences:
                // TODO
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
