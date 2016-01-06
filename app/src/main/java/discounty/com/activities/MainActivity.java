package discounty.com.activities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import discounty.com.R;
import discounty.com.authenticator.AccountGeneral;
import discounty.com.data.models.Customer;
import discounty.com.fragments.DiscountCardsFragment;
import discounty.com.fragments.ProfileFragment;
import discounty.com.helpers.BitmapHelper;
import fr.tkeunebr.gravatar.Gravatar;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        DiscountCardsFragment.OnFragmentInteractionListener,
        ProfileFragment.OnFragmentInteractionListener {

    private static final String STATE_DIALOG = "state_dialog";

    private static final String STATE_INVALIDATE = "state_invalidate";

//    @Bind(R.id.cards_recycler_view)

    @Bind(R.id.drawer_layout)
    DrawerLayout drawer;

    @Bind(R.id.nav_view)
    NavigationView navigationView;

//    @Bind(R.id.nav_header_main_layout)

    private String TAG = this.getClass().getSimpleName();

    private AccountManager accountManager;

    private AlertDialog alertDialog;

    private boolean invalidate;

    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        accountManager = AccountManager.get(this);

        if (accountManager.getAccountsByType("com.discounty").length == 0) {
            addNewAccount(accountManager);
        }

        navigationView.getHeaderView(0).getBackground().setColorFilter(Color.rgb(123, 123, 123), PorterDuff.Mode.MULTIPLY);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Welcome to Discounty!", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());


        navigationView.getHeaderView(0).setOnClickListener(view -> {
            setFragment(new ProfileFragment());
            setTitle("Profile");
            drawer.closeDrawer(GravityCompat.START);
            fab.setVisibility(View.INVISIBLE);
        });

//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

//        initDiscountCardsRecyclerView();


//        findViewById(R.id.button).setOnClickListener(view -> {
//            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
////                intent.putExtra("com.google.zxing.client.android.SCAN.SCAN_MODE", "QR_CODE_MODE");
//            startActivityForResult(intent, 0);
//        });


//        String gravatarUrl = Gravatar.init().with(accountManager.getAccountsByType(AccountGeneral.ACCOUNT_TYPE)[0].name)
//                .size(BitmapHelper.dpToPx(108, getApplicationContext())).build();
//        Bitmap avatar = null;
//        try {
//            avatar = Picasso.with(getApplicationContext()).load(gravatarUrl).get();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        final CircleImageView imgAvatar = (CircleImageView) navigationView.getHeaderView(0).findViewById(R.id.img_avatar);
        Account account = accountManager.getAccountsByType(AccountGeneral.ACCOUNT_TYPE)[0];
        ((TextView)navigationView.getHeaderView(0).findViewById(R.id.nav_header_txt_subheader)).setText(account.name);


        Log.d("EMAIL URL", account.name);

        try {
            Observable<Bitmap[]> observable = Observable.create(new Observable.OnSubscribe<Bitmap[]>() {

                @Override
                public void call(Subscriber<? super Bitmap[]> subscriber) {
                    try {
                        // Set customer's name
                        Customer customer = new Select().from(Customer.class).executeSingle();
                        ((TextView)navigationView.getHeaderView(0).findViewById(R.id.nav_header_txt_header))
                                .setText(customer.firstName + ' ' + customer.lastName);


                        String gravatarUrlSmall = Gravatar.init().with(accountManager.getAccountsByType(AccountGeneral.ACCOUNT_TYPE)[0].name)
                                .size(BitmapHelper.dpToPx(85, getApplicationContext())).build();

                        DisplayMetrics metrics = new DisplayMetrics();
                        getWindowManager().getDefaultDisplay().getMetrics(metrics);
                        String gravatarUrlBig = Gravatar.init().with(accountManager.getAccountsByType(AccountGeneral.ACCOUNT_TYPE)[0].name)
                                .size(metrics.widthPixels).build();

                        Bitmap smallAvatar = Picasso.with(getApplicationContext()).load(gravatarUrlSmall).get();

                        Bitmap bigAvatar = Picasso.with(getApplicationContext()).load(gravatarUrlBig).get();

                        try {
                            customer.avatarSmall = BitmapHelper.bitmapToBase64(smallAvatar.copy(smallAvatar.getConfig(), true));
                            customer.avatarBig = BitmapHelper.bitmapToBase64(bigAvatar.copy(bigAvatar.getConfig(), true));
                            customer.save();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        // result[0] - small avatar for nav drawer,
                        // result[1] - big avatar for profile fragment.
                        Bitmap[] result = new Bitmap[]{smallAvatar, bigAvatar};

                        subscriber.onNext(result);
                        subscriber.onCompleted();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            Subscriber<Bitmap[]> subscriber = new Subscriber<Bitmap[]>() {
                @Override
                public void onCompleted() {
                    Log.d("BITMAP SUBSCRIBER", "SUCCESS");
                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                }

                @Override
                public void onNext(Bitmap[] bitmaps) {
                    imgAvatar.setImageBitmap(BitmapHelper.getCircleBitmap(bitmaps[0]));

//                    try {
//                        Customer customer = new Select().from(Customer.class).executeSingle();
//                        customer.avatarSmall = BitmapHelper.bitmapToBase64(bitmaps[0].copy(bitmaps[0].getConfig(), true));
//                        customer.avatarBig = BitmapHelper.bitmapToBase64(bitmaps[1].copy(bitmaps[1].getConfig(), true));
//                        customer.save();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
                }
            };

            observable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (savedInstanceState == null) {
            setFragment(new DiscountCardsFragment());
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

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }


    public void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_layout_main_activity, fragment)
                .commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

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
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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

        Fragment fragment = null;
        Class fragmentClass;

        switch (id) {
            case R.id.nav_discount_cards:
                fragmentClass = DiscountCardsFragment.class;
                break;

            case R.id.nav_coupons:
                fragmentClass = DiscountCardsFragment.class;
                break;

            case R.id.nav_ads:
                fragmentClass = DiscountCardsFragment.class;
                break;

            case R.id.nav_notifications:
                fragmentClass = DiscountCardsFragment.class;
                break;

            case R.id.nav_send:
                fragmentClass = DiscountCardsFragment.class;
                break;

            case R.id.nav_settings:
                fragmentClass = DiscountCardsFragment.class;
                break;

            default:
                fragmentClass = DiscountCardsFragment.class;
                break;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }

//        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction().replace(R.id.frame_layout_main_activity, fragment).commit();

        setFragment(fragment);

//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        item.setChecked(true);
        setTitle(item.getTitle());
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

//    private void initDiscountCardsRecyclerView() {
//        cardsRecyclerView.setHasFixedSize(true);
//
//        layoutManager = new LinearLayoutManager(this);
//        cardsRecyclerView.setLayoutManager(layoutManager);
//
//        List<DiscountCard> records = new ArrayList<>();
//        records.add(null);
//        records.add(null);
//        records.add(null);
//        records.add(null);
//        records.add(null);
//        records.add(null);
//        records.add(null);
//        records.add(null);
//        records.add(null);
//
//        DiscountCardsListAdapter adapter = new DiscountCardsListAdapter(records);
//
//        cardsRecyclerView.setAdapter(adapter);
//        cardsRecyclerView.setItemAnimator(cardsRecyclerView.getItemAnimator());
//    }
}
