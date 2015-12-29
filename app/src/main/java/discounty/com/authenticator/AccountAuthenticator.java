package discounty.com.authenticator;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import discounty.com.R;
import discounty.com.activities.LoginActivity;
import discounty.com.api.ServiceGenerator;
import discounty.com.interfaces.DiscountyService;

/**
 * This class is responsible for account
 * authentication handling.
 */
public class AccountAuthenticator extends AbstractAccountAuthenticator implements LoaderManager.LoaderCallbacks<String> {

    final DiscountyService discountyService = ServiceGenerator.createService(DiscountyService.class);
    private final Context context;
    private String TAG = getClass().getSimpleName();

    public AccountAuthenticator(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public Bundle editProperties(AccountAuthenticatorResponse accountAuthenticatorResponse, String s) {
        return null;
    }

    @Override
    public Bundle addAccount(AccountAuthenticatorResponse accountAuthenticatorResponse, String accountType,
                             String authTokenType, String[] requiredFeatures, Bundle options) throws NetworkErrorException {
        final Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra(LoginActivity.ARG_ACCOUNT_TYPE, accountType);
        intent.putExtra(LoginActivity.ARG_AUTH_TYPE, authTokenType);
        intent.putExtra(LoginActivity.ARG_IS_ADDING_ACCOUNT, true);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, accountAuthenticatorResponse);

        final Bundle bundle = new Bundle();
        if (options != null) {
            bundle.putAll(options);
        }
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);
        return bundle;
    }

    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse accountAuthenticatorResponse, Account account, Bundle bundle) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse accountAuthenticatorResponse, Account account,
                               String authTokenType, Bundle options) throws NetworkErrorException {
        if (!authTokenType.equals(AccountGeneral.AUTHTOKEN_TYPE_READ_ONLY) &&
                !authTokenType.equals(AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS)) {
            final Bundle result = new Bundle();
            result.putString(AccountManager.KEY_ERROR_MESSAGE, "Invalid authTokenType");
            return result;
        }

        final AccountManager manager = AccountManager.get(context.getApplicationContext());

        String authToken = manager.peekAuthToken(account, authTokenType);

        Log.d("Discounty", TAG + " > peekAuthToken returned - " + authToken);

        if (TextUtils.isEmpty(authToken)) {
            final String password = manager.getPassword(account);
            if (password != null) {
                try {
                    Log.d("Discounty", TAG + " > re-authenticating with existing password");
                    authToken = discountyService.getAccessToken(DiscountyService.ACCESS_GRANT_TYPE,
                            account.name, password).toBlocking().first().getAccessToken();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (!TextUtils.isEmpty(authToken)) {
            final Bundle result = new Bundle();
            result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
            result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
            result.putString(AccountManager.KEY_AUTHTOKEN, authToken);
            return result;
        }

        final Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, accountAuthenticatorResponse);
        intent.putExtra(LoginActivity.ARG_ACCOUNT_TYPE, account.type);
        intent.putExtra(LoginActivity.ARG_AUTH_TYPE, authTokenType);
        final Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);
        return bundle;
    }

    @Override
    public String getAuthTokenLabel(String authTokenType) {
        if (AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS.equals(authTokenType)) {
            return AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS_LABEL;
        } else if (AccountGeneral.AUTHTOKEN_TYPE_READ_ONLY.equals(authTokenType)) {
            return AccountGeneral.AUTHTOKE_TYPE_READ_ONLY_LABEL;
        } else {
            return authTokenType + " (Label)";
        }
    }

    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse accountAuthenticatorResponse, Account account, String s, Bundle bundle) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse accountAuthenticatorResponse, Account account, String[] features) throws NetworkErrorException {
        final Bundle result = new Bundle();
        result.putBoolean(AccountManager.KEY_BOOLEAN_RESULT, false);
        return result;
    }

    @Override
    public Loader<String> onCreateLoader(int i, Bundle bundle) {
//        if (id == R.id.auth_token_loader) {
//            return new AuthTokenLoader(
//                    getActivity().getApplicationContext(),
//                    mLogin.getText().toString(),
//                    mPassword.getText().toString()
//            );
//        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String s) {

    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }
}
