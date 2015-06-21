package pl.javastart.wydatex;

public class Expense {

    private String name;
    private double price;
    private ExpenseCategory category;

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
