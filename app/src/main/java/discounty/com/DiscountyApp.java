package discounty.com;


import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.app.Application;

public class DiscountyApp extends Application {

    public static final String AUTHORITY = "com.discounty";

    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);
    }
}
