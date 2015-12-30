package discounty.com.sync;


import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.annotation.TargetApi;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import discounty.com.activities.LoginActivity;
import discounty.com.authenticator.AccountGeneral;
import discounty.com.data.models.DiscountCard;

public class SyncAdapter extends AbstractThreadedSyncAdapter {

    ContentResolver contentResolver;

    Context context;

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        this.context = context;
        contentResolver = context.getContentResolver();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        contentResolver = context.getContentResolver();
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority,
                              ContentProviderClient provider, SyncResult syncResult) {
        try {
            AccountManager manager = AccountManager.get(context);
            String authToken = manager.blockingGetAuthToken(account, AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS, true);

            String userData = manager.getUserData(account, LoginActivity.PARAM_USER_PASS);
            String password = manager.getPassword(account);

            Log.i("onPerformSync", "authToken:  " + authToken);
            Log.i("onPerformSync", "pass:  " + password);
            Log.i("onPerformSync", "username:  " + account.name);

            // Simulate network
            try {
                Thread.sleep(TimeUnit.SECONDS.toMillis(3));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            List<DiscountCard> cardsNeedingSync = new Select().from(DiscountCard.class)
                    .where("needsSync == 1")
                    .execute();

            ActiveAndroid.beginTransaction();
            try {
                for (DiscountCard card : cardsNeedingSync) {
                    card.needsSync = false;
                    card.save();
                }
                ActiveAndroid.setTransactionSuccessful();
            } finally {
                ActiveAndroid.endTransaction();
            }

        } catch (OperationCanceledException | IOException | AuthenticatorException e) {
            e.printStackTrace();
        }
    }
}
