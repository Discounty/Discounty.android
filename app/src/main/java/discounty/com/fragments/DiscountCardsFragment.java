package discounty.com.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.activeandroid.query.Select;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.animators.FadeInAnimator;
import com.marshalchen.ultimaterecyclerview.divideritemdecoration.HorizontalDividerItemDecoration;
import com.marshalchen.ultimaterecyclerview.itemTouchHelper.SimpleItemTouchHelperCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import discounty.com.R;
import discounty.com.adapters.DiscountCardsListAdapter;
import discounty.com.data.models.Customer;
import discounty.com.data.models.DiscountCard;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DiscountCardsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DiscountCardsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DiscountCardsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    private static final String ARG_PARAM2 = "param2";

    @Bind(R.id.cards_recycler_view)
    UltimateRecyclerView cardsRecyclerView;

    // TODO: Rename and change types of parameters
    private String mParam1;

    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private RecyclerView.LayoutManager layoutManager;

    public DiscountCardsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DiscountCardsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DiscountCardsFragment newInstance(String param1, String param2) {
        DiscountCardsFragment fragment = new DiscountCardsFragment();
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

//        LayoutInflater inflater =

//        initDiscountCardsRecyclerView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_discount_cards, container, false);

        ButterKnife.bind(this, view);

        initDiscountCardsRecyclerView();

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

    private void initDiscountCardsRecyclerView() {
        cardsRecyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());
        cardsRecyclerView.setLayoutManager(layoutManager);

        Customer customer = new Select().from(Customer.class).executeSingle();
        List<DiscountCard> records = customer.discountCards();
        records.addAll(records);
        records.addAll(records);

        DiscountCardsListAdapter adapter = new DiscountCardsListAdapter(records);

        cardsRecyclerView.setAdapter(adapter);
        cardsRecyclerView.setItemAnimator(cardsRecyclerView.getItemAnimator());

        cardsRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext()).build());

        cardsRecyclerView.setItemAnimator(new FadeInAnimator());
        cardsRecyclerView.getItemAnimator().setRemoveDuration(1000);

        cardsRecyclerView.setDefaultOnRefreshListener(() -> new Handler().postDelayed(() -> {
            // insert things to adapter
            cardsRecyclerView.setRefreshing(false);
            layoutManager.scrollToPosition(0);
        }, 1000));



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
