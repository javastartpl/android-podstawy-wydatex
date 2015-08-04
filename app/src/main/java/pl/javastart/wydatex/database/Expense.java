package pl.javastart.wydatex.database;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import pl.javastart.wydatex.ExpenseCategory;

@DatabaseTable(tableName = "expense")
public class Expense {

    @DatabaseField(generatedId = true)
    private Long id;

    @DatabaseField
    private String name;

    @DatabaseField
    private double price;

    @DatabaseField
    private String photoPath;

    @DatabaseField
    private ExpenseCategory category;

    @DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    private Location location;

    @SuppressWarnings("unused")
    public Expense() {
    }

    public Expense(String name, double price, ExpenseCategory category, String photoPath) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.photoPath = photoPath;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }
}
