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

    private final TypedValue mTypedValue = new TypedValue();
    private int mBackground;
    private List<Expense> expenses;

    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public Long id;
        public String name;
        public String category;
        public String price;
        public String photoPath;

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

    public Expense getValueAt(int position) {
        return expenses.get(position);
    }

    public ExpenseAdapter(Context context, List<Expense> items) {
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        mBackground = mTypedValue.resourceId;
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
        Expense expense = expenses.get(position);
        holder.id = expense.getId();
        holder.name = expense.getName();
        holder.category = expense.getCategory().getName();

        holder.price = String.format("%.2f", expense.getPrice()) + "zł";
        holder.photoPath = expense.getPhotoPath();


        holder.nameTextView.setText(holder.name);
        holder.categoryTextView.setText(holder.category);
        holder.priceTextView.setText(holder.price);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, ExpenseActivity.class);
                intent.putExtra(ExpenseActivity.EXTRA_ID, holder.id);

                context.startActivity(intent);
            }
        });

        if (holder.photoPath != null) {
            Glide.with(holder.imageView.getContext())
                    .load(holder.photoPath)
                    .fitCenter()
                    .into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return expenses.size();
    }
}


