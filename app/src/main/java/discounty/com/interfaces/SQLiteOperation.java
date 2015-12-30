package discounty.com.interfaces;


public interface SQLiteOperation {
    int INSERT = 1;

    int UPDATE = 2;

    int DELETE = 3;

    String KEY_LAST_ID = "com.discounty.data.KEY_LAST_ID";

    String KEY_AFFECTED_ROWS = "com.discounty.data.KEY_AFFECTED_ROWS";
}
