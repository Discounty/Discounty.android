package discounty.com;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.app.Application;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

public class DiscountyApp extends Application {

    public static final String AUTHORITY = "com.discounty";

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        ActiveAndroid.initialize(this);
    }
}
