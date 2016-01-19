package discounty.com.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import discounty.com.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreateDiscountCardFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CreateDiscountCardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateDiscountCardFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String BARCODE_PARAM = "barcode_param";
    public static final String BARCODE_FORMAT_PARAM = "barcode_format_param";

    private String barcode;
    private String barcodeFormat;

    private OnFragmentInteractionListener mListener;

    @Bind(R.id.discount_card_barcode)
    TextView txtBarcode;

    @Bind(R.id.discount_card_barcode_format)
    TextView txtBarcodeFormat;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_discount_card, container, false);

        ButterKnife.bind(this, view);


        txtBarcode.setText(barcode);
        txtBarcodeFormat.setText(barcodeFormat);

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
