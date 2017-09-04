package model.cards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {

    private List<Card> cards;

    public Deck() {
        cards = new ArrayList<Card>();

        for(Suit suit : Suit.values()) {
            for(int value = Card.LOWER_BOUND_VALUE; value <= Card.MAXIMUM_BOUND_VALUE; value++) {
                Card card = new Card(suit, value);
                cards.add(card);
            }
        }
        
        Collections.shuffle(cards);
    }

    public Card getCard() {
        if(cards.size() > 0) {
            return cards.remove(0);
        }
        /*  Shall I throw some kind of runtime exception?!   */
        return null;
    }

    public int size() {
        return cards.size();
    }


}
