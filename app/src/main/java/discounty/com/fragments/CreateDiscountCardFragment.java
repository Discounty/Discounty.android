package discounty.com.fragments;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.activeandroid.query.Select;
import com.jakewharton.rxbinding.view.RxView;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.squareup.otto.Bus;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import discounty.com.R;
import discounty.com.activities.LoginActivity;
import discounty.com.api.ServiceGenerator;
import discounty.com.authenticator.AccountGeneral;
import discounty.com.bus.BusProvider;
import discounty.com.bus.events.DiscountCardsListUpdateEvent;
import discounty.com.data.models.Barcode;
import discounty.com.data.models.BarcodeType;
import discounty.com.data.models.Customer;
import discounty.com.data.models.DiscountCard;
import discounty.com.helpers.NetworkHelper;
import discounty.com.interfaces.DiscountyService;
import discounty.com.models.AccessToken;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreateDiscountCardFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class CreateDiscountCardFragment extends Fragment implements Validator.ValidationListener {

    public static final String BARCODE_PARAM = "barcode_param";
    public static final String BARCODE_FORMAT_PARAM = "barcode_format_param";

    private String barcode;
    private String barcodeFormat;

    private OnFragmentInteractionListener mListener;

    private Bus bus;

    @NotEmpty
    @Bind(R.id.discount_card_barcode)
    EditText txtEditBarcode;

    @NotEmpty
    @Bind(R.id.input_discount_card_name)
    EditText txtEditDiscountCardName;

    @NotEmpty
    @Bind(R.id.input_discount_card_description)
    EditText txtEditDiscountCardDesc;

    @Bind(R.id.input_discount_percentage)
    EditText txtEditDiscountPercentage;

    @Bind(R.id.input_extra_info)
    EditText txtEditExtraInfo;

    @Bind(R.id.input_shop_name)
    EditText txtEditShopName;

    @Bind(R.id.fab_save_discount_card)
    FloatingActionButton fabSaveDiscountCard;

    private Validator validator;

    public CreateDiscountCardFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null && args.containsKey(BARCODE_PARAM) && args.containsKey(BARCODE_FORMAT_PARAM)) {
            barcode = args.getString(BARCODE_PARAM);
            barcodeFormat = args.getString(BARCODE_FORMAT_PARAM);
        }

        getActivity().findViewById(R.id.fab).setVisibility(View.INVISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_discount_card, container, false);

        ButterKnife.bind(this, view);

        validator = new Validator(this);
        validator.setValidationListener(this);

        bus = BusProvider.getInstance();
        bus.register(this);


        txtEditBarcode.setText(barcode);

        fabSaveDiscountCard.bringToFront();


        RxView.clicks(fabSaveDiscountCard)
                .debounce(2000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Void>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Void aVoid) {
                        validator.validate();
                    }
                });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroy() {
        getActivity().findViewById(R.id.fab).setVisibility(View.VISIBLE);
        super.onDestroy();
    }

    private DiscountCard saveNewCardInDB(Customer customer, String barcodeFormat, String barcodeStr,
                                 String cardName, String cardDesc, Double discountPercantage,
                                 String extraInfo) {

        // BarcodeType
        BarcodeType barcodeType = new Select()
                .from(BarcodeType.class)
                .where("BarcodeType = ?", barcodeFormat)
                .executeSingle();

        if (barcodeType == null) {
            barcodeType = new BarcodeType(barcodeFormat, null, new Date().getTime(),
                    new Date().getTime(), true);
        } else {
            barcodeType.updatedAt = new Date().getTime();
        }
        barcodeType.save();

        // Barcode
        Barcode barcode = new Barcode();
        barcode.customer = customer;
        barcode.barcodeType = barcodeType;
        barcode.barcode = barcodeStr;
        barcode.needsSync = true;
        barcode.discountPercentage = discountPercantage;
        barcode.extraInfo = extraInfo;
        barcode.serverId = null;
        barcode.createdAt = new Date().getTime();
        barcode.updatedAt = new Date().getTime();
        barcode.save();

        // DiscountCard
        DiscountCard card = new DiscountCard();
        card.name = cardName;
        card.description = cardDesc;
        card.customer = customer;
        card.barcode = barcode;
        card.needsSync = true;
        card.serverId = null;
        card.createdAt = new Date().getTime();
        card.updatedAt = new Date().getTime();
        // TODO add shop relationship
        card.save();

        customer.updatedAt = new Date().getTime();
        customer.save();

        // Emit RecyclerView update event
        bus.post(new DiscountCardsListUpdateEvent(customer.discountCards()));

        return card;
    }

    private void prepareDataAndSaveNewCard() {
        Customer customer = new Select().from(Customer.class).executeSingle();
        String cardName = txtEditDiscountCardName.getText().toString();
        String cardDesc = txtEditDiscountCardDesc.getText().toString();
        Double discountPercentage = Double.parseDouble(txtEditDiscountPercentage.getText().toString());
        String extraInfo = txtEditExtraInfo.getText().toString();

        DiscountCard card = saveNewCardInDB(customer, barcodeFormat, barcode, cardName,
                cardDesc, discountPercentage, extraInfo);

        getFragmentManager().executePendingTransactions();
        final FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_main_activity, new DiscountCardsFragment());
        fragmentTransaction.commit();

        AccountManager accountManager = AccountManager.get(getContext());
        persistNewDiscountCardToServerAsync(accountManager, card, txtEditShopName.getText().toString());
    }

    private void persistNewDiscountCardToServerAsync(AccountManager accountManager, DiscountCard card, String shopName) {
        Log.d("SAVE DISCOUNT CARD", "INITIATE DISCOUNT CARD SAVING");

        final DiscountyService discountyService = ServiceGenerator.createService(DiscountyService.class);

        if (NetworkHelper.isNetworkConnected(getContext())) {

            Observable.create(new Observable.OnSubscribe<Boolean>() {
                @Override
                public void call(Subscriber<? super Boolean> subscriber) {

                    Boolean success = false;

                    final Account account = accountManager.getAccountsByType(AccountGeneral.ACCOUNT_TYPE)[0];

                    try {

                        final String name = card.name;
                        final String description = card.description;
                        final String barcodeType = card.barcode.barcodeType.barcodeType;
                        final String barcode = card.barcode.barcode;
                        final Double discountPercentage = card.barcode.discountPercentage;
                        final String extraInfo = card.barcode.extraInfo;

                        Log.d("REFRESH TOKEN", accountManager.getUserData(account, LoginActivity.KEY_REFRESH_TOKEN));

                        AccessToken token = discountyService.refreshAccessToken(DiscountyService.REFRESH_GRANT_TYPE,
                                accountManager.getUserData(account, LoginActivity.KEY_REFRESH_TOKEN)).toBlocking().first();


                        if (token != null) {
                            Log.d("DiscountCard Save", "ACCESS TOKEN\n" + token.getAccessToken());

                            accountManager.setUserData(account, LoginActivity.KEY_REFRESH_TOKEN,
                                    token.getRefreshToken());
                            accountManager.setAuthToken(account, AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS,
                                    token.getAccessToken());

                            Log.d("DiscountCard Save", "SET NEW TOKEN TO ACCOUNT");

                            try {
                                Log.d("DiscountCard Save", "START SAVING DISCOUNT CARD");

                                discounty.com.models.DiscountCard savedCard = discountyService.createDiscountCard(token.getAccessToken(),
                                        name, description, barcodeType, barcode, discountPercentage,
                                        extraInfo, shopName).toBlocking().first();

                                if (savedCard != null) {

                                    Log.d("DiscountCard Save", "DISCOUNT CARD SAVING SUCCESS\n" + savedCard.toString());

                                    success = true;
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.d("DiscountCard Save", "ERROR WHEN SAVING DISCOUNT CARD");
                                success = false;
                            }

                        }

                    } catch (Exception e) {
                        success = false;
                        e.printStackTrace();
                    }

                    Log.d("Profile Save", "FINISH SAVING DISCOUNT CARD");

                    subscriber.onCompleted();
                    subscriber.onNext(success);
                }
            })
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<Boolean>() {
                @Override
                public void onCompleted() {
                    Log.d("DiscountCard Save", "DiscountCard SUCCESS onCompleted()");
                }

                @Override
                public void onError(Throwable e) {
                    Log.d("DiscountCard Save", "ERROR onError(e)");
                    e.printStackTrace();
                }

                @Override
                public void onNext(Boolean aBoolean) {
                    if (aBoolean) {
                        Log.d("DiscountCard Save", "DiscountCard SUCCESS onNext()");

                    } else {
                        Snackbar.make(getView(), "Problems with the Internet detected", Snackbar.LENGTH_LONG).show();
                        Log.d("DiscountCard Save", "DiscountCard ERROR onNext()");
                    }
                }
            });
        }
    }

    @Override
    public void onValidationSucceeded() {
        prepareDataAndSaveNewCard();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(getContext());

            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Snackbar.make(view, message, Snackbar.LENGTH_LONG).setAction(message, null).show();
            }
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
