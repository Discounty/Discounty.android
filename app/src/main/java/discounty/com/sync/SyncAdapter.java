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
import discounty.com.api.ServiceGenerator;
import discounty.com.authenticator.AccountGeneral;
import discounty.com.data.models.DiscountCard;
import discounty.com.helpers.NetworkHelper;
import discounty.com.interfaces.DiscountyService;
import discounty.com.models.Customer;

public class SyncAdapter extends AbstractThreadedSyncAdapter {

    ContentResolver contentResolver;

    final DiscountyService discountyService = ServiceGenerator.createService(DiscountyService.class);

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


            try {
                if (NetworkHelper.isNetworkConnected(context)) {
                    Customer customer = getCustomerFullInfo(authToken);
                } else {
                    Log.d("ATTEMPT SYNC ERROR", "NO NETWORK CONNECTION");
                }

            } catch (Exception e) {
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

    private Customer getCustomerFullInfo(String authToken) {
        try {
            Customer customer = discountyService.getFullCustomerInfo(authToken).toBlocking().first();

            Log.d("CUSTOMER SUCCESS", customer.toString());

            return customer;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("CUSTOMER FAILURE", null);
            return null;
        }
    }
}
