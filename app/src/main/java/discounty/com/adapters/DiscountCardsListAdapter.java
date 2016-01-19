package discounty.com.adapters;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import discounty.com.R;
import discounty.com.activities.DiscountCardDetailActivity;
import discounty.com.fragments.DiscountCardDetailFragment;
import discounty.com.data.models.DiscountCard;
import discounty.com.helpers.Colorize;

public class DiscountCardsListAdapter extends RecyclerView.Adapter<DiscountCardsListAdapter.ViewHolder> {


    private List<DiscountCard> records;

    public DiscountCardsListAdapter(List<DiscountCard> records) {
        this.records = records;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.discount_card_list_item, viewGroup, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        DiscountCard card = records.get(i);
        viewHolder.cardTitleTxt.setText(card.name);
        viewHolder.discountCard = card;

        String colorHex = Colorize.get();

        viewHolder.cardImg.setImageDrawable(TextDrawable.builder()
                .buildRound(card.name.substring(0, 1).toUpperCase(),
                        Color.parseColor(colorHex)));

        String desc = card.description;
        if (desc.length() > 40) {
            desc = desc.substring(0, 40) + "...";
        }
        viewHolder.cardDescriptionTxt.setText(desc);

        Log.d("ADAPTER_COLOR", " ====> " + colorHex);

        viewHolder.view.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, DiscountCardDetailActivity.class);
            intent.putExtra(DiscountCardDetailFragment.ARG_ITEM_ID, card.getId().longValue());
            intent.putExtra(DiscountCardDetailFragment.ARG_COLOR, colorHex);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.discount_card_item_image)
        ImageView cardImg;

        @Bind(R.id.card_title)
        TextView cardTitleTxt;

        @Bind(R.id.card_description)
        TextView cardDescriptionTxt;

        public DiscountCard discountCard;

        public View view;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            view = itemView;
        }
    }
}
