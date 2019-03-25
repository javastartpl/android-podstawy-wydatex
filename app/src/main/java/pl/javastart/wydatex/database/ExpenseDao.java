package pl.javastart.wydatex.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface ExpenseDao {

    @Query("SELECT * FROM expense")
    List<Expense> findAll();

    @Insert
    void insert(Expense expense);

    @Query("SELECT * FROM expense WHERE id = :id")
    Expense findById(long id);

    @Update
    void update(Expense expense);

    @Delete
    void delete(Expense expense);
}