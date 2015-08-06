package pl.javastart.wydatex.database;

import android.content.Context;

import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.util.List;

import pl.javastart.wydatex.location.MapsActivity;

public class ExpenseRepository {

    private static RuntimeExceptionDao<Expense, Long> dao(Context context) {
        return DatabaseHelper.getInstance(context).getExpenseDao();
    }

    public static List<Expense> getAllExpenses(Context context) {
        return dao(context).queryForAll();
    }

    public static void addExpense(Context context, Expense expense) {
        dao(context).create(expense);
    }

    public static Expense findById(Context context, Long id) {
        return dao(context).queryForId(id);
    }

    public static void update(Context context, Expense expense) {
        dao(context).update(expense);
    }
}
