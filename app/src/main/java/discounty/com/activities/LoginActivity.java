package discounty.com.activities;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Delete;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.Bind;
import butterknife.ButterKnife;
import discounty.com.R;
import discounty.com.api.ServiceGenerator;
import discounty.com.authenticator.AccountGeneral;
import discounty.com.data.models.Barcode;
import discounty.com.data.models.BarcodeType;
import discounty.com.data.models.Coupon;
import discounty.com.data.models.DiscountCard;
import discounty.com.data.models.Feedback;
import discounty.com.data.models.Shop;
import discounty.com.interfaces.DiscountyService;
import discounty.com.models.AccessToken;
import discounty.com.models.CardBarcode;
import discounty.com.models.CardBarcodeType;
import discounty.com.models.Customer;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AccountAuthenticatorActivity
        implements LoaderCallbacks<Cursor>, Validator.ValidationListener {

    public final static String ARG_ACCOUNT_TYPE = "ACCOUNT_TYPE";

    public final static String ARG_AUTH_TYPE = "AUTH_TYPE";

    public final static String ARG_ACCOUNT_NAME = "ACCOUNT_NAME";

    public final static String ARG_IS_ADDING_ACCOUNT = "AS_ADDING_ACCOUNT";

    public final static String KEY_ERROR_MESSAGE = "ERR_MSG";

    public static final String PARAM_USER_PASS = "USER_PASS";

    public static final String KEY_REFRESH_TOKEN = "REFRESH_TOKEN";
    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;
    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world",
            "yakovenko.denis.a@gmail.com:password", "a@:aaaaa"
    };

    final DiscountyService discountyService = ServiceGenerator.createService(DiscountyService.class);

    private final int REQ_SIGNUP = 1;

    private final String TAG = this.getClass().getSimpleName();
    // UI references.
    @NotEmpty
    @Email
    @Bind(R.id.input_email)
    EditText mEmailInput;
    @NotEmpty
    @Bind(R.id.input_password)
    EditText mPasswordInput;
    @Bind(R.id.login_progress)
    View mProgressView;
    @Bind(R.id.login_form)
    View mLoginFormView;
    @Bind(R.id.btn_login)
    Button btnLogin;
    private Validator validator;
    private AccountManager accountManager;

    private String authTokenType;

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        validator = new Validator(this);
        validator.setValidationListener(this);

        accountManager = AccountManager.get(getBaseContext());

        String accountName = getIntent().getStringExtra(ARG_ACCOUNT_NAME);
        authTokenType = getIntent().getStringExtra(ARG_AUTH_TYPE);

        if (authTokenType != null) {
            authTokenType = AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS;
        }

        if (accountName != null) {
            mEmailInput.setText(accountName);
        }

        // Set up the login form.
//        mEmailInput = (EditText) findViewById(R.id.input_email);
        populateAutoComplete();

        mPasswordInput = (EditText) findViewById(R.id.input_password);
//        mPasswordInput.setOnEditorActionListener((textView, id, keyEvent) -> {
//            if (id == R.id.login || id == EditorInfo.IME_NULL) {
//                attemptLogin();
//                return true;
//            }
//            return false;
//        });

//        Button mEmailSignInButton = (Button) findViewById(R.id.btn_login);
//        mEmailSignInButton.setOnClickListener(view -> submit());

        btnLogin.setOnClickListener(v -> validator.validate(true));

        TextView mSignup = (TextView) findViewById(R.id.btn_signup);
        mSignup.setOnClickListener(v -> {
            try {
                Intent signup = new Intent(LoginActivity.this, SignUpActivity.class);
                signup.putExtras(getIntent().getExtras());
                startActivityForResult(signup, REQ_SIGNUP);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

//        mLoginFormView = findViewById(R.id.login_form);
//        mProgressView = findViewById(R.id.login_progress);


    }


    public void onTokenReceived(Account account, String password, String token) {
        final AccountManager am = AccountManager.get(this);
        final Bundle result = new Bundle();
        if (am.addAccountExplicitly(account, password, new Bundle())) {
            result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
            result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
            result.putString(AccountManager.KEY_AUTHTOKEN, token);
            am.setAuthToken(account, account.type, token);
        } else {
            result.putString(AccountManager.KEY_ERROR_MESSAGE, "Account already exists");
        }
        setAccountAuthenticatorResult(result);
        setResult(RESULT_OK);
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_SIGNUP && resultCode == RESULT_OK) {
            finishLogin(data);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void submit() {
        final String username = mEmailInput.getText().toString();
        final String password = mPasswordInput.getText().toString();

        final String accountType = getIntent().getStringExtra(ARG_ACCOUNT_TYPE);

        new AsyncTask<String, Void, Intent>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                showProgress(true);
            }

            @Override
            protected Intent doInBackground(String... params) {

                Log.d("Discounty", TAG + " > Started authenticating");
                String authtoken, refreshtoken;
                Bundle data = new Bundle();

                try {
//                    if (NetworkHelper.isInternetAvailable(getBaseContext())) {

                    AccessToken accessToken = discountyService.getAccessToken(DiscountyService.ACCESS_GRANT_TYPE,
                            username, password).toBlocking().first();
                    authtoken = accessToken.getAccessToken();
                    refreshtoken = accessToken.getRefreshToken();

                    data.putString(AccountManager.KEY_ACCOUNT_NAME, username);
                    data.putString(AccountManager.KEY_ACCOUNT_TYPE, accountType);
                    data.putString(AccountManager.KEY_AUTHTOKEN, authtoken);
                    data.putString(PARAM_USER_PASS, password);
                    data.putString(KEY_REFRESH_TOKEN, refreshtoken);
//                    }
                } catch (Exception e) {
                    data.putString(KEY_ERROR_MESSAGE, e.getMessage());
                }

                final Intent result = new Intent();
                result.putExtras(data);
                return result;
            }

            @Override
            protected void onPostExecute(Intent intent) {
                showProgress(false);
                if (intent.hasExtra(KEY_ERROR_MESSAGE)) {
                    Toast.makeText(getBaseContext(), intent.getStringExtra(KEY_ERROR_MESSAGE),
                            Toast.LENGTH_LONG).show();
                } else {
                    finishLogin(intent);
                }
            }
        }.execute();
    }

    private void finishLogin(Intent intent) {
        String accountName = intent.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
        String accountPassword = intent.getStringExtra(PARAM_USER_PASS);

        final Account account = new Account(accountName, intent.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE));

        if (getIntent().getBooleanExtra(ARG_IS_ADDING_ACCOUNT, false)) {
            String authtoken = intent.getStringExtra(AccountManager.KEY_AUTHTOKEN);
            String authtokenType = authTokenType;
            String refreshToken = intent.getStringExtra(KEY_REFRESH_TOKEN);
            Bundle userData = new Bundle();
            userData.putString(KEY_REFRESH_TOKEN, refreshToken);

            accountManager.addAccountExplicitly(account, accountPassword, userData);
            accountManager.setAuthToken(account, authtokenType, authtoken);



            try {

                Observable.create(new Observable.OnSubscribe<Customer>() {
                    @Override
                    public void call(Subscriber<? super Customer> subscriber) {
                        try {
                            Customer customer = discountyService.getFullCustomerInfo(authtoken).toBlocking().first();

                            subscriber.onNext(customer);

                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d("CUSTOMER FAILURE", null);
                        }
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Customer>() {
                    @Override
                    public void onCompleted() {
                        Log.d("CUSTOMER onCompleted", "SUCCESS");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Customer customer) {

                        Log.d("CUSTOMER SUCCESS", "START SAVING CUSTOMER");

                        saveCustomerToDb(customer);

                        Log.d("CUSTOMER SUCCESS", "CUSTOMER HAS BEEN SAVED");
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }



        } else {
            accountManager.setPassword(account, accountPassword);
        }
        setAccountAuthenticatorResult(intent.getExtras());
        setResult(RESULT_OK, intent);
        finish();
    }

    private void saveCustomerToDb(Customer customer) {
        try {
            // Clear the DB
            new Delete().from(Barcode.class).where("_id > ?", -1).execute();
            new Delete().from(BarcodeType.class).where("_id > ?", -1).execute();
            new Delete().from(Coupon.class).where("_id > ?", -1).execute();
            new Delete().from(discounty.com.data.models.Customer.class).where("_id > ?", -1).execute();
            new Delete().from(Feedback.class).where("_id > ?", -1).execute();
            new Delete().from(Shop.class).where("_id > ?", -1).execute();

            Log.d("saveCustomerToDb()", "FINISH CLEARING THE DB");

            discounty.com.data.models.Customer customerAA = new discounty.com.data.models.Customer();

//            List<discounty.com.models.DiscountCard> discountCards = new ArrayList<>();

            // Customer general info
            customerAA.serverId = customer.getId();
            customerAA.firstName = customer.getFirstName();
            customerAA.lastName = customer.getLastName();
            customerAA.email = customer.getEmail();
            customerAA.needsSync = false;
            customerAA.phoneNumber = customer.getPhoneNumber();
            customerAA.city = customer.getCity();
            customerAA.country = customer.getCountry();
            customerAA.createdAt = convertDateStringToLong(customer.getCreatedAt());
            customerAA.updatedAt = convertDateStringToLong(customer.getUpdatedAt());
            customerAA.save();

            ActiveAndroid.beginTransaction();
            try {
                for (discounty.com.models.DiscountCard card : customer.getDiscountCards()) {

                    Barcode barcodeAA = new Barcode();
                    BarcodeType barcodeTypeAA = new BarcodeType();
                    CardBarcode barcode = card.getBarcode();
                    CardBarcodeType barcodeType = barcode.getBarcodeType();

                    // BarcodeType
                    // TODO save only unique types
                    barcodeTypeAA.barcodeType = barcodeType.getBarcodeType();
                    barcodeTypeAA.needsSync = false;
                    barcodeTypeAA.serverId = barcodeType.getId();
                    barcodeTypeAA.createdAt = convertDateStringToLong(barcodeType.getCreatedAt());
                    barcodeTypeAA.updatedAt = convertDateStringToLong(barcodeType.getUpdatedAt());
                    barcodeTypeAA.save();

                    // Barcode
                    barcodeAA.barcode = barcode.getBarcode();
                    barcodeAA.needsSync = false;
                    barcodeAA.discountPercentage = Double.parseDouble(barcode.getDiscountPercentage());
                    barcodeAA.extraInfo = barcode.getExtraInfo();
                    barcodeAA.createdAt = convertDateStringToLong(barcode.getCreatedAt());
                    barcodeAA.updatedAt = convertDateStringToLong(barcode.getUpdatedAt());
                    barcodeAA.serverId = barcode.getId();
                    barcodeAA.barcodeType = barcodeTypeAA;
                    barcodeAA.customer = customerAA;
                    barcodeAA.save();

                    // DiscountCard
                    new DiscountCard(card.getName(), card.getDescription(), barcodeAA,
                            convertDateStringToLong(card.getCreatedAt()),
                            convertDateStringToLong(card.getUpdatedAt()),
                            card.getId(), customerAA, false)
                            .save();
                }
                ActiveAndroid.setTransactionSuccessful();
            } finally {
                ActiveAndroid.endTransaction();
                customerAA.save();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Long convertDateStringToLong(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            return format.parse(date).getTime();
        } catch (ParseException e) {
            return null;
        }
    }

    private String convertDateLongToString(Long date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        Date dateObj = new Date(date);
        return format.format(dateObj);
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailInput, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, v -> {
                        requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailInput.setError(null);
        mPasswordInput.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailInput.getText().toString();
        String password = mPasswordInput.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordInput.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordInput;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailInput.setError(getString(R.string.error_field_required));
            focusView = mEmailInput;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailInput.setError(getString(R.string.error_invalid_email));
            focusView = mEmailInput;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

//        mEmailInput.setAdapter(adapter);
    }

    @Override
    public void onValidationSucceeded() {
        submit();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Snackbar.make(view, message, Snackbar.LENGTH_LONG).setAction(message, null).show();
            }
        }
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            } else {
                mPasswordInput.setError(getString(R.string.error_incorrect_password));
                mPasswordInput.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

