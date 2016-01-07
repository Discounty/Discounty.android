package discounty.com;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.app.Application;
import com.squareup.otto.Bus;

import java.util.ArrayList;
import java.util.List;

public class DiscountyApp extends Application {

    public static final String AUTHORITY = "com.discounty";

    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);
    }

    public static void clearAllDbTables() {
        SQLiteDatabase db = ActiveAndroid.getDatabase();
        List<String> tables = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM sqlite_master WHERE type='table';", null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            String tableName = cursor.getString(1);
            if (!tableName.equals("android_metadata") &&
                    !tableName.equals("sqlite_sequence")) {
                tables.add(tableName);
            }
            cursor.moveToNext();
        }
        cursor.close();
        for (String tableName : tables) {
            db.execSQL("DELETE FROM " + tableName);
        }
    }
}
