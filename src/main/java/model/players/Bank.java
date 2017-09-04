package model.players;

import model.cards.Card;
import model.game.Game;

import java.util.ArrayList;
import java.util.List;

public class Bank {

    /*  How many times we can actually split?!   */
    /*  Here is not clear, I will assume it is possible to split at most 1 time.    */
    /*  A list of list can be used to split multiple times. */
//    protected List<List<Card>> cards;
//
//    protected List<Integer> points;

    protected List<Card> cards;
    protected int points;

    public Bank() {
        clear();
    }


    public void addCard(Card card) {

        cards.add(card);


        points += card.getPoints();
        boolean foundAce;

        while (points > Game.GAME_21) {
            foundAce = false;
            for(Card currentCard: cards) {
                if(currentCard.isAce() && currentCard.getPoints() == 11) {
                    foundAce = true;
                    currentCard.setPoints(1);
                    /*  points -= 10 actually.  Just to be clear.   */
                    points-= 11;
                    points++;
                    break;
                }
            }
            if(!foundAce) {
                /*  All aces are set to 1.  */
                break;
            }
        }
    }

    public int getCurrentPoints() {
        return points;
    }

    public List<Card> getCards() {
        return cards;
    }


    public void clear() {
        points = 0;
        cards = new ArrayList<Card>();
    }
}
