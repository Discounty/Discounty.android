package discounty.com.activities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import discounty.com.R;
import discounty.com.authenticator.AccountGeneral;
import discounty.com.helpers.BitmapHelper;
import fr.tkeunebr.gravatar.Gravatar;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String STATE_DIALOG = "state_dialog";

    private static final String STATE_INVALIDATE = "state_invalidate";

    private String TAG = this.getClass().getSimpleName();

    private AccountManager accountManager;

    private AlertDialog alertDialog;

    private boolean invalidate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        accountManager = AccountManager.get(this);

        if (accountManager.getAccountsByType("com.discounty").length == 0) {
            addNewAccount(accountManager);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Welcome to Discounty!", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        findViewById(R.id.button).setOnClickListener(view -> {
            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
//                intent.putExtra("com.google.zxing.client.android.SCAN.SCAN_MODE", "QR_CODE_MODE");
            startActivityForResult(intent, 0);
        });


//        String gravatarUrl = Gravatar.init().with(accountManager.getAccountsByType(AccountGeneral.ACCOUNT_TYPE)[0].name)
//                .size(BitmapHelper.dpToPx(108, getApplicationContext())).build();
//        Bitmap avatar = null;
//        try {
//            avatar = Picasso.with(getApplicationContext()).load(gravatarUrl).get();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        final CircleImageView imgAvatar = (CircleImageView) findViewById(R.id.img_avatar);
        Log.d("EMAIL URL", accountManager.getAccountsByType(AccountGeneral.ACCOUNT_TYPE)[0].name);
//        imgAvatar.setImageBitmap(BitmapHelper.getCircleBitmap(avatar));

        try {
            Observable<Bitmap> observable = Observable.create(new Observable.OnSubscribe<Bitmap>() {

                @Override
                public void call(Subscriber<? super Bitmap> subscriber) {
                    try {
                        String gravatarUrl = Gravatar.init().with(accountManager.getAccountsByType(AccountGeneral.ACCOUNT_TYPE)[0].name)
                                .size(BitmapHelper.dpToPx(90, getApplicationContext())).build();
                        Log.wtf("GRAVATAR", gravatarUrl);
                        subscriber.onNext(Picasso.with(getApplicationContext()).load(gravatarUrl).get());
                        subscriber.onCompleted();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            Subscriber<Bitmap> subscriber = new Subscriber<Bitmap>() {
                @Override
                public void onCompleted() {
                   Log.d("BITMAP SUBSCRIBER", "SUCCESS");
                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                }

                @Override
                public void onNext(Bitmap bitmap) {
                    imgAvatar.setImageBitmap(BitmapHelper.getCircleBitmap(bitmap));
                }
            };

            observable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        } catch (Exception e) {
            e.printStackTrace();
        }


//        DiscountyService discountyService = ServiceGenerator.createService(DiscountyService.class);
//
//        try {
//            discountyService.getTestGetHello()
//                    .subscribeOn(Schedulers.newThread())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Subscriber<String>() {
//                        @Override
//                        public void onCompleted() {
//                            Log.d("onCompleted()", "REQUEST COMPLETED");
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//                            Log.d("onError()", e.getMessage());
//                        }
//
//                        @Override
//                        public void onNext(String s) {
//                            Log.d("onNext()", s);
//                        }
//                    });
//        } catch(Exception e) {
//            e.printStackTrace();
//        }
    }

    private void addNewAccount(AccountManager manager) {
        manager.addAccount(AccountGeneral.ACCOUNT_TYPE, AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS, null, null, this,
                accountManagerFuture -> {
                    try {
                        accountManagerFuture.getResult();
                    } catch (android.accounts.OperationCanceledException | IOException | AuthenticatorException e) {
                        e.printStackTrace();
                        MainActivity.this.finish();
                    }
                }, null);
    }

    public void OnScan(View v) {
        Intent data = new Intent("com.google.zxing.client.android.SCAN");
//        data.putExtra("com.google.zxing.client.android.SCAN.SCAN_MODE", "QR_CODE_MODE");
        startActivityForResult(data, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (resultCode == RESULT_OK) {
                Log.i("App", "RESULT_OK");
            } else if (resultCode == RESULT_CANCELED)
                Log.i("App", "RESULT_CANCELED");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        switch (id) {
            case R.id.action_settings:
                Toast.makeText(getApplicationContext(), "Settings pressed", Toast.LENGTH_LONG).show();
                return true;
            case R.id.action_logout:
                performLogout();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

//        return super.onOptionsItemSelected(item);
    }

    private void performLogout() {
        try {
            Account[] accounts = accountManager.getAccountsByType(AccountGeneral.ACCOUNT_TYPE);
            if (accounts.length != 0) {
                Log.d("ACCOUNT", accounts[0].toString());
                Log.d("ACCOUNT TOKEN TYPE", accounts[0].type);
                Log.d("ACCOUNT TOKEN NAME", accounts[0].name);

                accountManager.clearPassword(accounts[0]);
                accountManager.invalidateAuthToken(AccountGeneral.ACCOUNT_TYPE,
                        accountManager.getAuthToken(accounts[0], AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS, null, true,
                                accountManagerFuture -> {
                                    try {
                                        Log.d("invalidateAuthToken", accountManagerFuture.getResult().toString());
                                    } catch (android.accounts.OperationCanceledException | AuthenticatorException | IOException e) {
                                        e.printStackTrace();
                                    }
                                }, null).toString());

                if (Build.VERSION.SDK_INT < 22) { // use deprecated method
                    accountManager.removeAccount(accounts[0], null, null);
                } else {
                    accountManager.removeAccountExplicitly(accounts[0]);
                }
//                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//                intent.putExtra(LoginActivity.ARG_ACCOUNT_TYPE, AccountGeneral.ACCOUNT_TYPE);
//                startActivity(intent);
//                finish();
                addNewAccount(accountManager);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_ads) {

        } else if (id == R.id.nav_notifications) {

        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
