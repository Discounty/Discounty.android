package discounty.com;


import com.activeandroid.ActiveAndroid;
import com.activeandroid.app.Application;

public class DiscountyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);
    }
}
