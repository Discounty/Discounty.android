package discounty.com.activities;

import android.accounts.AccountManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import discounty.com.R;
import discounty.com.api.ServiceGenerator;
import discounty.com.interfaces.DiscountyService;
import discounty.com.models.AccessToken;
import discounty.com.models.Customer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


public class SignUpActivity extends AppCompatActivity {

    @Bind(R.id.btn_login)
    TextView btnLogin;
    @Bind(R.id.btn_signup)
    Button btnSignup;
    @Bind(R.id.input_first_name)
    EditText inputFirstName;
    @Bind(R.id.input_last_name)
    EditText inputLastName;
    @Bind(R.id.input_email)
    EditText inputEmail;
    @Bind(R.id.input_password)
    EditText inputPassword;
    private String TAG = getClass().getSimpleName();
    private String accountType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        accountType = getIntent().getStringExtra(LoginActivity.ARG_ACCOUNT_TYPE);

        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        btnLogin.setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });

        btnSignup.setOnClickListener(v -> {
            createAccount();
        });
    }

    private void createAccount() {
        //TODO validation

        new AsyncTask<String, Void, Intent>() {

            final DiscountyService discountyService = ServiceGenerator.createService(DiscountyService.class);
            String firstName = inputFirstName.getText().toString().trim();
            String lastName = inputLastName.getText().toString().trim();
            String email = inputEmail.getText().toString().trim();
            String password = inputPassword.getText().toString();

            @Override
            protected Intent doInBackground(String... strings) {
                Log.d("Discounty", TAG + " > Started authenticating");

                String authtoken = null;

                Bundle data = new Bundle();

                try {
                    // TODO get auth token
//                    String authtoken;

                    try {
                        discountyService.signupCustomer(email, password, firstName, lastName)
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .toBlocking()
                                .subscribe(new Subscriber<Customer>() {
                                    @Override
                                    public void onCompleted() {
                                        Log.d("SignUp", "COMPLETED");
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        e.printStackTrace();
                                    }

                                    @Override
                                    public void onNext(Customer customer) {
                                        discountyService.getAccessToken(DiscountyService.ACCESS_GRANT_TYPE,
                                                email, password)
                                                .subscribeOn(Schedulers.newThread())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(new Subscriber<AccessToken>() {
                                                    @Override
                                                    public void onCompleted() {
                                                        Log.d("AccessToken", "COMPLETED");
                                                    }

                                                    @Override
                                                    public void onError(Throwable e) {
                                                        e.printStackTrace();
                                                    }

                                                    @Override
                                                    public void onNext(AccessToken token) {
                                                        data.putString(AccountManager.KEY_AUTHTOKEN, token.getAccessToken());
                                                    }
                                                });
                                    }
                                });
                    } catch (Exception e) {
                        Log.wtf("SignUpActivity", e.getMessage());
                        e.printStackTrace();
                    }

                    data.putString(AccountManager.KEY_ACCOUNT_NAME, email);
                    data.putString(AccountManager.KEY_ACCOUNT_TYPE, accountType);
//                    data.putString(AccountManager.KEY_AUTHTOKEN, authtoken);
                    data.putString(LoginActivity.PARAM_USER_PASS, password);
                } catch (Exception e) {
                    data.putString(LoginActivity.KEY_ERROR_MESSAGE, e.getMessage());
                }

                final Intent result = new Intent();
                result.putExtras(data);
                return result;
            }

            @Override
            protected void onPostExecute(Intent intent) {
                if (intent.hasExtra(LoginActivity.KEY_ERROR_MESSAGE)) {
                    Toast.makeText(getBaseContext(), intent.getStringExtra(LoginActivity.KEY_ERROR_MESSAGE),
                            Toast.LENGTH_LONG).show();
                } else {
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        }.execute();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }
}
