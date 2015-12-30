package discounty.com.helpers;

import android.net.Uri;
import android.text.TextUtils;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;


public class SQLiteUriMatcher {

    public static final int NO_MATCH = -1;

    public static final int MATCH_ALL = 1;

    public static final int MATCH_ID = 2;

    private final Set<String> mAuthorities = new CopyOnWriteArraySet<>();

    public void addAuthority(String authority) {
        mAuthorities.add(authority);
    }

    public int match(Uri uri) {
        if (mAuthorities.contains(uri.getAuthority())) {
            final List<String> pathSegments = uri.getPathSegments();
            final int pathSegmentsSize = pathSegments.size();
            if (pathSegmentsSize == 1) {
                return MATCH_ALL;
            } else if (pathSegmentsSize == 2 && TextUtils.isDigitsOnly(pathSegments.get(1))) {
                return MATCH_ID;
            }
        }
        return NO_MATCH;
    }
}
