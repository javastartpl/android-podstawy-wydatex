package pl.javastart.wydatex.database;

import android.content.Context;

import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.util.List;

public class ExpenseRepository {

    public static List<Expense> getAllExpenses(Context context) {
        RuntimeExceptionDao<Expense, Long> dao = DatabaseHelper.getInstance(context).getExpenseDao();
        return dao.queryForAll();
    }

    public static void addExpense(Context context, Expense expense) {
        RuntimeExceptionDao<Expense, Long> dao = DatabaseHelper.getInstance(context).getExpenseDao();
        dao.create(expense);
    }
}
