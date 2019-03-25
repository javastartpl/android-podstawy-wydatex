package pl.javastart.wydatex.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import pl.javastart.wydatex.expense.ExpenseCategory;

@Entity
public class Expense {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    private String name;

    private double price;

    private String photoPath;

    @TypeConverters(ExpenseCategoryConverter.class)
    private ExpenseCategory category;

    private Long locationId;

    @SuppressWarnings("unused")
    public Expense() {
    }

    @Ignore
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

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }
}
