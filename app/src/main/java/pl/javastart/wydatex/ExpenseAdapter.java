package pl.javastart.wydatex;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import pl.javastart.wydatex.database.Expense;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder> {

    private int mBackground;
    private List<Expense> expenses;

    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final ImageView imageView;
        public final TextView nameTextView;
        public final TextView categoryTextView;
        public final TextView priceTextView;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            imageView = (ImageView) view.findViewById(R.id.avatar);
            nameTextView = (TextView) view.findViewById(R.id.title);
            categoryTextView = (TextView) view.findViewById(R.id.category);
            priceTextView = (TextView) view.findViewById(R.id.price);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + nameTextView.getText();
        }
    }

    public ExpenseAdapter(Context context, List<Expense> items) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, typedValue, true);
        mBackground = typedValue.resourceId;
        expenses = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_expense, parent, false);
        view.setBackgroundResource(mBackground);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Expense expense = expenses.get(position);

        holder.nameTextView.setText(expense.getName());
        holder.categoryTextView.setText(expense.getCategory().getName());
        holder.priceTextView.setText(String.format("%.2f", expense.getPrice()) + "zł");

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, ExpenseActivity.class);
                intent.putExtra(ExpenseActivity.EXTRA_ID, expense.getId());

                context.startActivity(intent);
            }
        });

        if (expense.getPhotoPath() != null) {
            Glide.with(holder.imageView.getContext())
                    .load(expense.getPhotoPath())
                    .fitCenter()
                    .into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return expenses.size();
    }
}


