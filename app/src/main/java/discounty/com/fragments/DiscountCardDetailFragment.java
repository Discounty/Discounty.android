package discounty.com.fragments;

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
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import discounty.com.R;
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

    TextView txtDescription;

    TextView txtName;

    CollapsingToolbarLayout toolbarLayout;

    Toolbar toolbar;

    FloatingActionMenu fabEditMenu;

    FloatingActionButton fabDeleteCard;

    FloatingActionButton fabEditCard;

    FloatingActionButton fabEditBarcode;

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

        txtDescription = (TextView)rootView.findViewById(R.id.discount_card_description);
        txtName = (TextView)rootView.findViewById(R.id.discount_card_name);
        toolbar = (Toolbar)getActivity().findViewById(R.id.detail_toolbar);
        fabEditMenu = (FloatingActionMenu)getActivity().findViewById(R.id.edit_discount_card_fab_menu);
        fabDeleteCard = (FloatingActionButton)fabEditMenu.findViewById(R.id.fab_delete_discount_card);
        fabEditCard = (FloatingActionButton)fabEditMenu.findViewById(R.id.fab_edit_discount_card_info);
        fabEditBarcode = (FloatingActionButton)fabEditMenu.findViewById(R.id.fab_edit_discount_card_barcode);

        if (discountCard != null) {
            txtDescription.setText(discountCard.description);
            txtName.setText(discountCard.name);
        }

        Log.d("TOOL_BAR_COLOR", " ====> " + toolbarColorHex);

        if (toolbarColorHex != null) {

            int parsedColor = Color.parseColor(toolbarColorHex);

            fabEditMenu.setMenuButtonColorNormal(parsedColor);
            fabEditMenu.setMenuButtonColorPressed(parsedColor);

            fabDeleteCard.setColorPressed(parsedColor);
            fabDeleteCard.setColorNormal(parsedColor);

            fabEditCard.setColorPressed(parsedColor);
            fabEditCard.setColorNormal(parsedColor);

            fabEditBarcode.setColorPressed(parsedColor);
            fabEditBarcode.setColorNormal(parsedColor);

            toolbar.setBackgroundColor(parsedColor);
            toolbar.setBackgroundTintList(ColorStateList.valueOf(parsedColor));
            toolbarLayout.setBackgroundColor(parsedColor);
            toolbarLayout.setContentScrimColor(parsedColor);

            Window window = getActivity().getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Colorize.darken(parsedColor, 0.5f));
        }

        return rootView;
    }
}
