package discounty.com.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.activeandroid.query.Select;
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

    @Bind(R.id.fab_update_info)
    FloatingActionButton fabUpdateInfo;

    @Bind(R.id.edit_actions_fab)
    FloatingActionsMenu fabMenu;

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


        fabUpdateInfo.setOnClickListener(v -> setTextFieldsToUpdateState());

        firstNameTxtUpdate.setOnClickListener(v -> setTextViewEditable((TextView) v, InputType.TYPE_TEXT_VARIATION_PERSON_NAME));
        lastNameTxtUpdate.setOnClickListener(v -> setTextViewEditable((TextView) v, InputType.TYPE_TEXT_VARIATION_PERSON_NAME));
        countryTxtUpdate.setOnClickListener(v -> setTextViewEditable((TextView) v, InputType.TYPE_CLASS_TEXT));
        cityTxtUpdate.setOnClickListener(v -> setTextViewEditable((TextView) v, InputType.TYPE_CLASS_TEXT));
        phoneNumberTxtUpdate.setOnClickListener(v -> setTextViewEditable((TextView) v, InputType.TYPE_CLASS_PHONE));

        firstNameTxtUpdate.setOnTouchListener((v, motionEvent) -> {
            setTextViewEditable((TextView) v, InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
            return true;
        });
        lastNameTxtUpdate.setOnTouchListener((v, motionEvent) -> {
            setTextViewEditable((TextView) v, InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
            return true;
        });
        countryTxtUpdate.setOnTouchListener((v, motionEvent) -> {
            setTextViewEditable((TextView) v, InputType.TYPE_CLASS_TEXT);
            return true;
        });
        cityTxtUpdate.setOnTouchListener((v, motionEvent) -> {
            setTextViewEditable((TextView) v, InputType.TYPE_CLASS_TEXT);
            return true;
        });
        phoneNumberTxtUpdate.setOnTouchListener((v, motionEvent) -> {
            setTextViewEditable((TextView) v, InputType.TYPE_CLASS_PHONE);
            return true;
        });


        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                nameTxt.setText(firstNameTxtUpdate.getText() + " " + lastNameTxtUpdate.getText());
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                nameTxt.setText(firstNameTxtUpdate.getText() + " " + lastNameTxtUpdate.getText());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                nameTxt.setText(firstNameTxtUpdate.getText() + " " + lastNameTxtUpdate.getText());
            }
        };

        firstNameTxtUpdate.addTextChangedListener(watcher);
        lastNameTxtUpdate.addTextChangedListener(watcher);

        return view;
    }

    public void setTextFieldsToUpdateState(TextView... fields) {
        for (TextView view : fields) {
            setTextViewEditable(view, null);
        }
        firstNameTxtUpdate.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        lastNameTxtUpdate.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        phoneNumberTxtUpdate.setInputType(InputType.TYPE_CLASS_PHONE);
        countryTxtUpdate.setInputType(InputType.TYPE_CLASS_TEXT);
        cityTxtUpdate.setInputType(InputType.TYPE_CLASS_TEXT);
    }

    public void setTextViewEditable(TextView textView, Integer typeClass) {
        textView.setCursorVisible(true);
        textView.setFocusableInTouchMode(true);
        if (typeClass != null) {
        textView.setInputType(typeClass);
        }
        textView.requestFocus();
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
