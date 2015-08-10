package pl.javastart.wydatex.expense;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

class CategoryAdapter extends BaseAdapter {

    private ExpenseActivity expenseActivity;

    public CategoryAdapter(ExpenseActivity expenseActivity) {
        this.expenseActivity = expenseActivity;
    }

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
            convertView = expenseActivity.getLayoutInflater().inflate(android.R.layout.simple_dropdown_item_1line, null);
        }

        TextView textView = (TextView) convertView.findViewById(android.R.id.text1);
        textView.setText(getItem(position).getName());

        return convertView;
    }
}
