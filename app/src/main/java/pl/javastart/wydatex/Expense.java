package pl.javastart.wydatex;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "expense")
public class Expense {

    @DatabaseField(generatedId = true)
    private Long id;

    @DatabaseField
    private String name;

    @DatabaseField
    private double price;

    @DatabaseField
    private ExpenseCategory category;


    @SuppressWarnings("unused")
    public Expense() {
    }

    public Expense(String name, double price, ExpenseCategory category) {
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public ExpenseCategory getCategory() {
        return category;
    }

    public void setCategory(ExpenseCategory category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
