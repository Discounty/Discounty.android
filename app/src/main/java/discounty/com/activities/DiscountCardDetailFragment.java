package discounty.com.activities;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.activeandroid.query.Select;

import butterknife.Bind;
import butterknife.ButterKnife;
import discounty.com.R;
import discounty.com.activities.dummy.DummyContent;
import discounty.com.data.models.Customer;
import discounty.com.data.models.DiscountCard;
import discounty.com.helpers.Colorize;

/**
 * A fragment representing a single DiscountCard detail screen.
 */
public class DiscountCardDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    public static final String ARG_COLOR = "item_color";

    private DiscountCard discountCard;

    private String toolbarColorHex;

    private int colorId;

//    @Bind(R.id.discountcard_detail)
    TextView txtDescription;

//    @Bind(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;

//    @Bind(R.id.detail_toolbar)
    Toolbar toolbar;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DiscountCardDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {

            Log.d("DISCOUNT CARD ID", "DISCOUNT CARD ID ====> " + getArguments().getLong(ARG_ITEM_ID));

            if (getArguments().containsKey(ARG_COLOR)) {
                toolbarColorHex = getArguments().getString(ARG_COLOR);
            } else {
                if (Build.VERSION.SDK_INT < 23) { // use deprecated method
                    toolbarColorHex = "#" + Integer.toHexString(getResources()
                            .getColor(R.color.colorAccent));
                } else {
                    toolbarColorHex = "#" + Integer.toHexString(getResources()
                            .getColor(R.color.colorAccent, null));
                }
            }

            discountCard =
                    new Select()
                    .from(DiscountCard.class)
                    .where("_ID = ?", getArguments().getLong(ARG_ITEM_ID))
                    .executeSingle();


            Activity activity = this.getActivity();
            toolbarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (toolbarLayout != null) {
                toolbarLayout.setTitle(discountCard.name);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.discountcard_detail, container, false);

        txtDescription = (TextView)rootView.findViewById(R.id.discountcard_detail);
//        toolbarLayout = (CollapsingToolbarLayout)getActivity().findViewById(R.id.toolbar_layout);
        toolbar = (Toolbar)getActivity().findViewById(R.id.detail_toolbar);

        if (discountCard != null) {
            txtDescription.setText(discountCard.description);
        }

        Log.d("TOOL_BAR_COLOR", " ====> " + toolbarColorHex);

        if (toolbarColorHex != null) {
            toolbar.setBackgroundColor(Color.parseColor(toolbarColorHex));
            toolbar.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(toolbarColorHex)));
            toolbarLayout.setBackgroundColor(Color.parseColor(toolbarColorHex));
            toolbarLayout.setContentScrimColor(Color.parseColor(toolbarColorHex));

            Window window = getActivity().getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Colorize.darken(Color.parseColor(toolbarColorHex), 0.5f));
//            window.setStatusBarColor(Colorize.modifyAlphaChannel(Color.parseColor(toolbarColorHex), 0.2f));
        }

        return rootView;
    }
}
