package discounty.com.bus.events;


import java.util.List;

import discounty.com.data.models.DiscountCard;

public class DiscountCardsListUpdateEvent {

    private List<DiscountCard> discountCards;

    public List<DiscountCard> getDiscountCards() {
        return discountCards;
    }

    public void setDiscountCards(List<DiscountCard> discountCards) {
        this.discountCards = discountCards;
    }

    public DiscountCardsListUpdateEvent(List<DiscountCard> discountCards) {
        this.discountCards = discountCards;
    }
}
