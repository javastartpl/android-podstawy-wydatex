package pl.javastart.wydatex.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Expense.class, Location.class}, version = 1)
public abstract class WydatexDatabase extends RoomDatabase {

    public abstract LocationDao getLocationDao();
    public abstract ExpenseDao getExpenseDao();

    private static volatile WydatexDatabase INSTANCE;

    public static WydatexDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (WydatexDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            WydatexDatabase.class, "wydatex-db")
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}