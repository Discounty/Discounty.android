package discounty.com.activities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.os.OperationCanceledException;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

import discounty.com.R;
import discounty.com.authenticator.AccountGeneral;

public class Main2Activity extends AppCompatActivity
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
        setContentView(R.layout.activity_main2);
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


        ((Button) findViewById(R.id.button)).setOnClickListener(view -> {
            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
//                intent.putExtra("com.google.zxing.client.android.SCAN.SCAN_MODE", "QR_CODE_MODE");
            startActivityForResult(intent, 0);
        });

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
        accountManager.addAccount("com.discounty", AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS, null, null, this,
                accountManagerFuture -> {
                    try {
                        accountManagerFuture.getResult();
                    } catch (android.accounts.OperationCanceledException | IOException | AuthenticatorException e) {
                        e.printStackTrace();
                        Main2Activity.this.finish();
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
        Account[] accounts = accountManager.getAccountsByType("com.discounty");
        if (accounts.length != 0) {
            Log.d("ACCOUNT", accounts[0].toString());
            accountManager.removeAccount(accounts[0], accountManagerFuture -> {
                try {
                    if (accountManagerFuture.getResult()) {
                        Log.d("ACCOUNT", "REMOVED");
                        finish();
                        startActivity(new Intent(Main2Activity.this, LoginActivity.class));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, null);
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

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
