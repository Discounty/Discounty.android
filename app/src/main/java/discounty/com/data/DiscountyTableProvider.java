package discounty.com.data;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;

import discounty.com.interfaces.SQLiteOperation;

public abstract class DiscountyTableProvider implements SQLiteOperation {

    private final String mName;

    public DiscountyTableProvider(String name) {
        mName = name;
    }

    public abstract Uri getBaseUri();

    public Cursor query(SQLiteDatabase db, String[] columns, String where, String[] whereArgs, String orderBy) {
        return db.query(mName, columns, where, whereArgs, null, null, orderBy);
    }

    public long insert(SQLiteDatabase db, ContentValues values) {
        return db.insert(mName, BaseColumns._ID, values);
    }

    public int delete(SQLiteDatabase db, String where, String[] whereArgs) {
        return db.delete(mName, where, whereArgs);
    }

    public int update(SQLiteDatabase db, ContentValues values, String where, String[] whereArgs) {
        return db.update(mName, values, where, whereArgs);
    }

    public void onContentChanged(Context context, int operation, Bundle extras) {

    }

    public abstract void onCreate(SQLiteDatabase db);

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("drop table if exists " + mName + ";");
        onCreate(db);
    }
}
