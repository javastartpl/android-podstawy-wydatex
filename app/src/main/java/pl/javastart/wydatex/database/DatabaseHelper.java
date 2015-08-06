package pl.javastart.wydatex.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "wydatex.db";
    private static final int DATABASE_VERSION = 1;

    private static DatabaseHelper instance;
    private RuntimeExceptionDao<Expense, Long> expenseDao;
    private RuntimeExceptionDao<Location, Long> locationDao;

    public DatabaseHelper(Context context, String databaseName, SQLiteDatabase.CursorFactory factory, int databaseVersion) {
        super(context, databaseName, factory, databaseVersion);
    }

    public static DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        recreateDatabase(connectionSource);
    }


    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        recreateDatabase(connectionSource);
    }

    private void recreateDatabase(ConnectionSource connectionSource) {
        try {
            TableUtils.dropTable(connectionSource, Expense.class, true);
            TableUtils.dropTable(connectionSource, Location.class, true);
            TableUtils.createTableIfNotExists(connectionSource, Expense.class);
            TableUtils.createTableIfNotExists(connectionSource, Location.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public RuntimeExceptionDao<Expense, Long> getExpenseDao() {
        if (expenseDao == null) {
            expenseDao = getRuntimeExceptionDao(Expense.class);
        }
        return expenseDao;
    }

    public RuntimeExceptionDao<Location, Long> getLocationDao() {
        if (locationDao == null) {
            locationDao = getRuntimeExceptionDao(Location.class);
        }
        return locationDao;
    }

}
