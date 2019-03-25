package pl.javastart.wydatex.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface LocationDao {

    @Query("SELECT * FROM location")
    List<Location> findAll();

    @Insert
    void insert(Location location);

    @Query("SELECT * FROM location WHERE id = :id")
    Location findById(long id);

    @Update
    void update(Location location);

    @Delete
    void delete(Location location);
}