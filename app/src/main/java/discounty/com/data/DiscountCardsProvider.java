package discounty.com.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;


public class DiscountCardsProvider extends DiscountyTableProvider {
    public static final String TABLE_NAME = "discount_cards";

    public static final Uri URI = Uri.parse("content://com.discounty/" + TABLE_NAME);

    public DiscountCardsProvider() {
        super(TABLE_NAME);
    }

    public static long getId(Cursor c) {
        return c.getLong(c.getColumnIndex(Columns._ID));
    }

    public static String getName(Cursor c) {
        return c.getString(c.getColumnIndex(Columns.NAME));
    }

    public static String getDescription(Cursor c) {
        return c.getString(c.getColumnIndex(Columns.DESCRIPTION));
    }

    public static long getShopId(Cursor c) {
        return c.getLong(c.getColumnIndex(Columns.SHOP_ID));
    }

    public static long getCustomerId(Cursor c) {
        return c.getLong(c.getColumnIndex(Columns.CUSTOMER_ID));
    }

    public static String getUnregisteredShop(Cursor c) {
        return c.getString(c.getColumnIndex(Columns.UNREGISTERED_SHOP));
    }

    public static long getCreatedAt(Cursor c) {
        return c.getLong(c.getColumnIndex(Columns.CREATED_AT));
    }

    public static long getUpdatedAt(Cursor c) {
        return c.getLong(c.getColumnIndex(Columns.UPDATED_AT));
    }

    @Override
    public Uri getBaseUri() {
        return URI;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        db.execSQL("create table if not exists " + TABLE_NAME +
//                "(" + Columns._ID + " integer primary key on conflict replace, "
//                + Columns.NAME + " text, "
//                + Columns.DESCRIPTION + " text, "
//                + Columns.SHOP_ID + " integer, "
//                + Columns.CUSTOMER_ID + " integer, "
//                + Columns.UNREGISTERED_SHOP + " text, "
//                + Columns.CREATED_AT + " integer, "
//                + Columns.UPDATED_AT + " integer);");
//        db.execSQL("create index if not exists " +
//                TABLE_NAME + "_" + Columns.FEED_ID + "_index" +
//                " on " + TABLE_NAME + "(" + Columns.FEED_ID + ");");
    }

    public interface Columns extends BaseColumns {
        String NAME = "name";
        String DESCRIPTION = "description";
        String SHOP_ID = "shop_id";
        String CUSTOMER_ID = "customer_id";
        String UNREGISTERED_SHOP = "unregistered_shop";
        String CREATED_AT = "created_at";
        String UPDATED_AT = "updated_at";
    }
}
