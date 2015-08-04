package pl.javastart.wydatex.database;

import android.content.Context;

import com.j256.ormlite.dao.RuntimeExceptionDao;

import pl.javastart.wydatex.location.MapsActivity;

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
}
