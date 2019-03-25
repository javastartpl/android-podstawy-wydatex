package pl.javastart.wydatex.database;

import android.arch.persistence.room.TypeConverter;

import pl.javastart.wydatex.expense.ExpenseCategory;

public class ExpenseCategoryConverter {

    @TypeConverter
    public static ExpenseCategory toCategory(String name) {
        return ExpenseCategory.valueOf(name);
    }

    @TypeConverter
    public static String toString(ExpenseCategory category) {
        return category.name();
    }
}