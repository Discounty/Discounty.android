package discounty.com.fragments;

import android.content.Context;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
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

import butterknife.Bind;
import butterknife.ButterKnife;
import discounty.com.R;
import discounty.com.data.models.Customer;
import discounty.com.helpers.BitmapHelper;

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

    private Customer customer;

    public ProfileFragment() {
        // Required empty public constructor
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

        customer = new Select().from(Customer.class).executeSingle();

        nameTxt.setText(customer.firstName + " " + customer.lastName);
        emailTxt.setText(customer.email);
        avatarImg.setImageBitmap(BitmapHelper.base64ToBitmap(customer.avatarBig));

        fabMenu.bringToFront();

        fabMenu.setOnClickListener(v -> Log.d("FAB_MENU", "CLICKED"));

        getActivity().findViewById(R.id.fab).setOnClickListener(v -> v.setVisibility(View.INVISIBLE));

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

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                nameTxt.setText(firstNameTxtUpdate.getText() + " " + lastNameTxtUpdate.getText());
                nameTxt.setText(firstNameEditUpdate.getText() + " " + lastNameEditUpdate.getText());
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                nameTxt.setText(firstNameTxtUpdate.getText() + " " + lastNameTxtUpdate.getText());
                nameTxt.setText(firstNameEditUpdate.getText() + " " + lastNameEditUpdate.getText());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                nameTxt.setText(firstNameTxtUpdate.getText() + " " + lastNameTxtUpdate.getText());
                nameTxt.setText(firstNameEditUpdate.getText() + " " + lastNameEditUpdate.getText());
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

                    if(!rect.contains((int)motionEvent.getRawX(), (int)motionEvent.getRawY())) {
                        fabMenu.collapse();
                    }
                }
            }
            return true;
        });

        fabSave.setOnClickListener(v -> {
            saveEditedFields();
            fabEditViewSwitcher.showNext();
        });

        return view;
    }

    public void setTextFieldsToUpdateState() {
        editFieldsViewSwitcher.showNext();
        fabEditViewSwitcher.showNext();
        if (editFieldsViewSwitcher.getCurrentView() == editableLayout){
            firstNameEditUpdate.setText(firstNameTxtUpdate.getText());
            firstNameEditUpdate.setCursorVisible(true);
            lastNameEditUpdate.setText(lastNameTxtUpdate.getText());
            lastNameEditUpdate.setCursorVisible(true);
            cityEditUpdate.setText(cityTxtUpdate.getText());
            cityEditUpdate.setCursorVisible(true);
            countryEditUpdate.setText(countryTxtUpdate.getText());
            countryEditUpdate.setCursorVisible(true);
            phoneNumberEditUpdate.setText(phoneNumberTxtUpdate.getText());
            phoneNumberEditUpdate.setCursorVisible(true);
        }
    }


    private void saveEditedFields() {
    }

    public void createChangePhotoDialog(View v) {
        new MaterialDialog.Builder(getContext())
                .title("Change profile photo")
                .content("If you want to change a profile photo, you need to change your Gravatar.\nWhat's gravatar?")
                .positiveText("Ok")
                .show();
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
