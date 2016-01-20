package discounty.com.fragments;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import discounty.com.R;
import discounty.com.data.models.DiscountCard;
import discounty.com.google.zxing.WriterException;
import discounty.com.helpers.BarcodeEncoder;
import discounty.com.helpers.BitmapHelper;
import discounty.com.helpers.Colorize;
import discounty.com.helpers.Operation;
import discounty.com.helpers.OperationHelper;

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

    private boolean isImageFitToScreen;

    TextView txtDescription;

    TextView txtName;

    CollapsingToolbarLayout toolbarLayout;

    Toolbar toolbar;

    FloatingActionMenu fabEditMenu;

    FloatingActionButton fabDeleteCard;

    FloatingActionButton fabEditCard;

    FloatingActionButton fabEditBarcode;

    ImageView imgBarcode;

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
        imgBarcode = (ImageView)rootView.findViewById(R.id.barcode_image_view);
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

            try {
                toolbar.setBackgroundColor(parsedColor);
                toolbar.setBackgroundTintList(ColorStateList.valueOf(parsedColor));
                toolbarLayout.setBackgroundColor(parsedColor);
                toolbarLayout.setContentScrimColor(parsedColor);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Window window = getActivity().getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Colorize.darken(parsedColor, 0.5f));
        }

        initDiscountCardBarcodeBitmap();

        return rootView;
    }

    private void initDiscountCardBarcodeBitmap() {
        BarcodeEncoder encoder = BarcodeEncoder.getInstance();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)getContext()).getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);

        int screenHeight = displayMetrics.heightPixels;
        int screenWidth = displayMetrics.widthPixels;

        int height = screenHeight / 4;
        int width = screenWidth - (int)(screenWidth * 0.5);

        int orientation = getActivity().getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            height = screenHeight;
            width = screenWidth;
        }

        encoder.setBitmap_Width_Height(width, height);
        try {
            String barcode = discountCard.barcode.barcode;
            String format = discountCard.barcode.barcodeType.barcodeType;
            Bitmap discountCardBarcodeBitmap = encoder.generateBarcode(barcode, format);

            imgBarcode.setImageBitmap(discountCardBarcodeBitmap);

            imgBarcode.setOnClickListener(v -> {
                if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }
            });

        } catch (WriterException e) {
            Log.d("BARCODE_ENCODE_FAILURE", e.getMessage());
            e.printStackTrace();
        }

    }
}
