package discounty.com.fragments;

import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.text.TextUtils;

import discounty.com.DiscountyApp;
import discounty.com.R;


public class SyncSettings extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String KEY_AUTO_SYNC = "com.discounty.KEY_AUTO_SYNC";

    private static final String KEY_AUTO_SYNC_INTERVAL = "com.discounty.KEY_AUTO_SYNC_INTERVAL";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.sync_prefs);
        final ListPreference interval = (ListPreference) getPreferenceManager()
                .findPreference(KEY_AUTO_SYNC_INTERVAL);
        interval.setSummary(interval.getEntry());
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
        if (TextUtils.equals(KEY_AUTO_SYNC, key)) {
            if (prefs.getBoolean(key, false)) {
                final long interval = Long.parseLong(prefs.getString(
                        KEY_AUTO_SYNC_INTERVAL,
                        getString(R.string.auto_sync_interval_default)
                ));
//                ContentResolver.addPeriodicSync(DiscountyApp.account, DiscountyApp.AUTHORITY, Bundle.EMPTY, interval);
            } else {
//                ContentResolver.removePeriodicSync(DiscountyApp.account, DiscountyApp.AUTHORITY, new Bundle());
            }
        } else if (TextUtils.equals(KEY_AUTO_SYNC_INTERVAL, key)) {
            final ListPreference interval = (ListPreference) getPreferenceManager().findPreference(key);
            interval.setSummary(interval.getEntry());
//            ContentResolver.addPeriodicSync(
//                    DiscountyApp.account, DiscountyApp.AUTHORITY,
//                    Bundle.EMPTY, Long.parseLong(interval.getValue())
//            );
        }
    }
}
