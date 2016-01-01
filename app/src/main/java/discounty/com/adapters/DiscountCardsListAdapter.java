package discounty.com.adapters;


import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.github.lzyzsd.randomcolor.RandomColor;

import java.util.List;

import discounty.com.R;
import discounty.com.data.models.DiscountCard;

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
//        viewHolder.name.setText(card.name);
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private Drawable drawable;

        public ViewHolder(View itemView) {
            super(itemView);


            // Set round circle icons
            RandomColor randomColor = new RandomColor();
            ((ImageView) itemView.findViewById(R.id.discount_card_item_image))
                    .setImageDrawable(TextDrawable.builder().buildRound("A", randomColor.randomColor()));
        }
    }
}
