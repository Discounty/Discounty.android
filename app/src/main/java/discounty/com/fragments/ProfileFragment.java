package discounty.com.fragments;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.activeandroid.query.Select;
import com.afollestad.materialdialogs.MaterialDialog;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;
import discounty.com.DiscountyApp;
import discounty.com.R;
import discounty.com.activities.LoginActivity;
import discounty.com.api.ServiceGenerator;
import discounty.com.authenticator.AccountGeneral;
import discounty.com.bus.BusProvider;
import discounty.com.data.models.Customer;
import discounty.com.bus.events.NameChangeEvent;
import discounty.com.helpers.BitmapHelper;
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
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    @Bind(R.id.avatarImg)
    ImageView avatarImg;

    @Bind(R.id.nameTxt)
    TextView nameTxt;

    @Bind(R.id.emailTxt)
    TextView emailTxt;

    @Bind(R.id.country_update)
    TextView countryTxtUpdate;

    @Bind(R.id.city_update)
    TextView cityTxtUpdate;

    @Bind(R.id.phone_number_update)
    TextView phoneNumberTxtUpdate;

    @Bind(R.id.first_name_update)
    TextView firstNameTxtUpdate;

    @Bind(R.id.last_name_update)
    TextView lastNameTxtUpdate;

    @Bind(R.id.country_update_edit)
    EditText countryEditUpdate;

    @Bind(R.id.city_update_edit)
    EditText cityEditUpdate;

    @Bind(R.id.phone_number_update_edit)
    EditText phoneNumberEditUpdate;

    @Bind(R.id.first_name_update_edit)
    EditText firstNameEditUpdate;

    @Bind(R.id.last_name_update_edit)
    EditText lastNameEditUpdate;

    @Bind(R.id.fab_update_info)
    FloatingActionButton fabUpdateInfo;

    @Bind(R.id.fab_update_photo)
    FloatingActionButton fabUpdatePhoto;

    @Bind(R.id.edit_actions_fab)
    FloatingActionsMenu fabMenu;

    @Bind(R.id.fab_save_edited_fields)
    FloatingActionButton fabSave;

    @Bind(R.id.edit_fields_view_switcher)
    ViewSwitcher editFieldsViewSwitcher;

    @Bind(R.id.fab_edit_switcher_view)
    ViewSwitcher fabEditViewSwitcher;

    @Bind(R.id.readonly_fields_layout)
    RelativeLayout readonlyLayout;

    @Bind(R.id.editable_fields_layout)
    RelativeLayout editableLayout;

    @Bind(R.id.coupons_count_txt)
    TextView couponsCountTxt;

    @Bind(R.id.discount_cards_count_txt)
    TextView discountCardsCountTxt;

    private AccountManager accountManager;

    private Customer customer;

    private Bus bus;

    final DiscountyService discountyService = ServiceGenerator.createService(DiscountyService.class);

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        customer = null;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        ButterKnife.bind(this, view);

        bus = BusProvider.getInstance();
        bus.register(this);

        accountManager = AccountManager.get(getContext());

        customer = new Select().from(Customer.class).executeSingle();

        nameTxt.setText(customer.firstName + " " + customer.lastName);
        emailTxt.setText(customer.email);
        avatarImg.setImageBitmap(BitmapHelper.base64ToBitmap(customer.avatarSmall));

//        fabMenu.bringToFront();

        fabMenu.setOnClickListener(v -> Log.d("FAB_MENU", "CLICKED"));

        getActivity().findViewById(R.id.fab).setVisibility(View.INVISIBLE);

        firstNameTxtUpdate.setText(customer.firstName);
        lastNameTxtUpdate.setText(customer.lastName);
        countryTxtUpdate.setText(customer.country);
        cityTxtUpdate.setText(customer.city);
        phoneNumberTxtUpdate.setText(customer.phoneNumber);


        Animation inAnim = new AlphaAnimation(0, 1);
        inAnim.setDuration(300);
        Animation outAnim = new AlphaAnimation(1, 0);
        outAnim.setDuration(300);

        editFieldsViewSwitcher.setInAnimation(inAnim);
        editFieldsViewSwitcher.setOutAnimation(outAnim);
        fabEditViewSwitcher.setInAnimation(inAnim);
        fabEditViewSwitcher.setOutAnimation(outAnim);

        fabUpdateInfo.setOnClickListener(v -> {
            fabMenu.collapse();
            setTextFieldsToUpdateState();
        });
        fabUpdatePhoto.setOnClickListener((v) -> {
            fabMenu.collapse();
            createChangePhotoDialog(v);
        });

        fabSave.setOnClickListener(v -> {
            saveEditedFields();
            fabEditViewSwitcher.setDisplayedChild(0);
            editFieldsViewSwitcher.setDisplayedChild(0);
        });


        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                nameTxt.setText(firstNameTxtUpdate.getText().toString().trim() + " " +
                        lastNameTxtUpdate.getText().toString().trim());
                nameTxt.setText(firstNameEditUpdate.getText().toString().trim() + " " +
                        lastNameEditUpdate.getText().toString().trim());
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                nameTxt.setText(firstNameTxtUpdate.getText().toString().trim() + " " +
                        lastNameTxtUpdate.getText().toString().trim());
                nameTxt.setText(firstNameEditUpdate.getText().toString().trim() + " " +
                        lastNameEditUpdate.getText().toString().trim());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                nameTxt.setText(firstNameTxtUpdate.getText().toString().trim() + " " +
                        lastNameTxtUpdate.getText().toString().trim());
                nameTxt.setText(firstNameEditUpdate.getText().toString().trim() + " " +
                        lastNameEditUpdate.getText().toString().trim());
            }
        };

        firstNameEditUpdate.addTextChangedListener(watcher);
        lastNameEditUpdate.addTextChangedListener(watcher);
        firstNameTxtUpdate.addTextChangedListener(watcher);
        lastNameTxtUpdate.addTextChangedListener(watcher);

        initCardsAndCouponsTextViews();

        view.setOnTouchListener((view1, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                if (fabMenu.isExpanded()) {
                    Rect rect = new Rect();
                    fabMenu.getGlobalVisibleRect(rect);

                    if (!rect.contains((int) motionEvent.getRawX(), (int) motionEvent.getRawY())) {
                        fabMenu.collapse();
                    }
                }
            }
            return true;
        });


        return view;
    }

    public void setTextFieldsToUpdateState() {
        editFieldsViewSwitcher.setDisplayedChild(1);
        fabEditViewSwitcher.setDisplayedChild(1);
        firstNameEditUpdate.setText(firstNameTxtUpdate.getText());
        firstNameEditUpdate.setCursorVisible(true);
        firstNameEditUpdate.requestFocus();
        lastNameEditUpdate.setText(lastNameTxtUpdate.getText());
        lastNameEditUpdate.setCursorVisible(true);
        cityEditUpdate.setText(cityTxtUpdate.getText());
        cityEditUpdate.setCursorVisible(true);
        countryEditUpdate.setText(countryTxtUpdate.getText());
        countryEditUpdate.setCursorVisible(true);
        phoneNumberEditUpdate.setText(phoneNumberTxtUpdate.getText());
        phoneNumberEditUpdate.setCursorVisible(true);
    }


    private void saveEditedFields() {
        Log.d("SAVE PROFILE", "INITIATE PROFILE SAVING");

        if (NetworkHelper.isNetworkConnected(getContext())) {
            Observable.create(new Observable.OnSubscribe<Boolean>() {
                @Override
                public void call(Subscriber<? super Boolean> subscriber) {

                   Boolean success = false;

                   Account account = accountManager.getAccountsByType(AccountGeneral.ACCOUNT_TYPE)[0];

                   try {

                       final String firstName = firstNameEditUpdate.getText().toString();
                       final String lastName = lastNameEditUpdate.getText().toString();
                       final String country = countryEditUpdate.getText().toString();
                       final String city = cityEditUpdate.getText().toString();
                       final String phoneNumber = phoneNumberEditUpdate.getText().toString();

                       Log.d("REFRESH TOKEN", accountManager.getUserData(account, LoginActivity.KEY_REFRESH_TOKEN));

                       AccessToken token = discountyService.refreshAccessToken(DiscountyService.REFRESH_GRANT_TYPE,
                               accountManager.getUserData(account, LoginActivity.KEY_REFRESH_TOKEN)).toBlocking().first();

                       if (token != null) {
                           Log.d("Profile Save", "ACCESS TOKEN\n" + token.getAccessToken());

                           accountManager.setUserData(account, LoginActivity.KEY_REFRESH_TOKEN,
                                   token.getRefreshToken());
                           accountManager.setAuthToken(account, AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS,
                                   token.getAccessToken());

                           Log.d("Profile Save", "SET NEW TOKEN TO ACCOUNT");

                           try {
                               Log.d("Profile Save", "START UPDATING CUSTOMER");

                               discounty.com.models.Customer newCustomer = discountyService.updateCustomer(token.getAccessToken(),
                                       firstName, lastName, country, city, phoneNumber, null).toBlocking().first();


                               if (newCustomer != null) {

                                   Log.d("Profile Save", "CUSTOMER UPDATE SUCCESS\n" + newCustomer.toString());

                                   customer.firstName = newCustomer.getFirstName();
                                   customer.lastName = newCustomer.getLastName();
                                   customer.country = newCustomer.getCountry();
                                   customer.city = newCustomer.getCity();
                                   customer.phoneNumber = newCustomer.getPhoneNumber();

                                   customer.save();

                                   Log.d("Profile Save", "CUSTOMER HAS BEEN SAVED IN THE DB\n" + customer.toString());

                                   success = true;
                               }

                           } catch (Exception e) {

                               Log.d("Profile Save", "ERROR WHEN UPDATING CUSTOMER");

                               e.printStackTrace();
                               success = false;
                           }
                       }

                   } catch (Exception e) {
                       Log.d("Profile Save", "ERROR WHEN REFRESHING TOKEN");

                       e.printStackTrace();
                       success = false;
                   }

                    Log.d("Profile Save", "FINISH UPDATING CUSTOMER");

                    subscriber.onCompleted();
                    subscriber.onNext(success);
                }
            })
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<Boolean>() {
                @Override
                public void onCompleted() {
                    Log.d("Profile Save", "CUSTOMER UPDATE COMPLETED");
                }

                @Override
                public void onError(Throwable e) {
                    Log.d("Profile Save", "CUSTOMER UPDATE ERROR (onError)");

                    e.printStackTrace();
                }

                @Override
                public void onNext(Boolean aBoolean) {
                    if (aBoolean != null && aBoolean) {
                        Snackbar.make(getView(), "Profile updated successfully", Snackbar.LENGTH_LONG).show();

                        Log.d("POST BUS INFO", customer.firstName + " " + customer.lastName);
                        bus.post(new NameChangeEvent(customer.firstName, customer.lastName));
                    } else {
                        Snackbar.make(getView(), "Problems with the Internet detected", Snackbar.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    @Subscribe
    public void updateTextAndEditViews(NameChangeEvent event) {
        Log.d("BUS in Profile", "NAME CHANGE EVENT FIRED");
        firstNameEditUpdate.setText(event.getFirstName());
        firstNameTxtUpdate.setText(event.getFirstName());
        lastNameEditUpdate.setText(event.getLastName());
        lastNameTxtUpdate.setText(event.getLastName());
    }

    public void createChangePhotoDialog(View v) {

        final Spannable message = new SpannableString("If you want to change a profile photo, you need to change your Gravatar.\n" +
                "What's gravatar?\n\n\nhttps://gravatar.com");
        Linkify.addLinks(message, Linkify.ALL);

        MaterialDialog dialog = new MaterialDialog.Builder(getContext())
                .title("Change profile photo")
                .content(message)
                .positiveText("Ok")
                .build();
        dialog.show();
    }

    private void initCardsAndCouponsTextViews() {
        Spannable discountCardsTitle = new SpannableString("Discount cards" + "\n");
        Spannable couponsTitle = new SpannableString("Coupons" + "\n");

        if (Build.VERSION.SDK_INT < 23) {
            discountCardsTitle.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.jet)), 0,
                    discountCardsTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            couponsTitle.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.jet)), 0,
                    couponsTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            discountCardsTitle.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.jet, null)), 0,
                    discountCardsTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            couponsTitle.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.jet, null)), 0,
                    couponsTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        discountCardsCountTxt.setText(discountCardsTitle, TextView.BufferType.SPANNABLE);
        couponsCountTxt.setText(couponsTitle, TextView.BufferType.SPANNABLE);

        Spannable discountCardsNumber = new SpannableString(String.valueOf(customer.discountCards().size()));
        // TODO Add coupons to customer
        Spannable couponsNumber = new SpannableString(String.valueOf(0));

        if (Build.VERSION.SDK_INT < 23) {
            discountCardsNumber.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.monsoon)), 0,
                    discountCardsNumber.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            couponsNumber.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.monsoon)), 0,
                    couponsNumber.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            discountCardsNumber.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.monsoon, null)), 0,
                    discountCardsNumber.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            couponsNumber.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.monsoon, null)), 0,
                    couponsNumber.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        discountCardsCountTxt.append(discountCardsNumber);
        couponsCountTxt.append(couponsNumber);
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
        getActivity().findViewById(R.id.fab).setVisibility(View.VISIBLE);
        fabMenu.setVisibility(View.INVISIBLE);
        mListener = null;
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
