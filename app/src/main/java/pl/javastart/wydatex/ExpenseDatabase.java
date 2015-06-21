package pl.javastart.wydatex;

import java.util.ArrayList;
import java.util.List;

public class ExpenseDatabase {

    private static List<Expense> expenses = new ArrayList<Expense>();

    public static List<Expense> getExpenses() {
        return expenses;
    }

    static  {
        expenses.add(new Expense("Jajka" , 4.2, ExpenseCategory.FOOD));
        expenses.add(new Expense("Kino" , 32, ExpenseCategory.ENTERTAINMENT));
        expenses.add(new Expense("Szampon" , 9.99, ExpenseCategory.HYGIENE));
    }

    public static void addExpense(Expense expense) {
        expenses.add(expense);
    }
}
