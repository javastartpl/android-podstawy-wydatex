package pl.javastart.wydatex.database;

import android.app.Activity;
import android.content.Context;

import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.util.List;

public class LocationRepository {

    private static RuntimeExceptionDao<Location, Long> dao(Context context) {
        return DatabaseHelper.getInstance(context).getLocationDao();
    }

    public static void insert(Context context, Location location) {
        dao(context).create(location);
    }

    public static Location findById(Context context, Long id) {
        return dao(context).queryForId(id);
    }

    public static void insertOrUpdate(Context context, Location location) {
        dao(context).createOrUpdate(location);
    }

    public static List<Location> findAll(Context context) {
        return dao(context).queryForAll();
    }
}
