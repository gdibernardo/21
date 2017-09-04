package model.players;


import model.cards.Card;

import java.util.ArrayList;
import java.util.List;

public class Player extends Bank implements Comparable<Player> {

    public int compareTo(Player other) {
        return this.id.compareTo(other.getId());
    }

    private String name;

    private double balance;

    private boolean didSplit;

    /*  How many times we can actually split?!   */
    /*  Here is not clear, I will assume it is possible to split at most 1 time.    */
    protected int splitPoints;
    protected List<Card> splitCards;

    private Integer id;

    public Integer getId() {
        return id;
    }

    public Player(int id, String name, double balance) {
        super();

        this.id = id;
        this.name = name;
        this.balance = balance;

        currentBet = 0;
    }


    private double currentBet;

    public String getName() {
        return name;
    }


    public double getCurrentBalance() {
        return balance;
    }


    public void bet(double bet) {
        currentBet = bet;
        balance -= bet;
    }


    public double getCurrentBet() {
        return currentBet;
    }


    public void win() {
        this.balance += currentBet * 2;
    }

    public void lose() {
        // this.balance -= currentBet;
    }

    public boolean hasSplit() {
        return didSplit;
    }

    public void split() {

        bet(currentBet);

        didSplit = true;
        Card card = cards.remove(1);

        points -= card.getPoints();
        splitPoints = card.getPoints();
        splitCards = new ArrayList<Card>();
        splitCards.add(card);
    }


    public List<Card> getSplitCards() {
        return splitCards;
    }


    @Override
    public void clear() {
        super.clear();

        didSplit = false;
        splitPoints = 0;
        splitCards = null;
    }

    /*  Shall we check if player can double the current bet?!  */
    public boolean canSplit() {
        return ((cards.size() == 2) && (cards.get(0).getValue().equals(cards.get(1).getValue())) && (balance >= currentBet));
    }

    public void addCardToSplit(Card card) {
        splitCards.add(card);
        splitPoints += card.getPoints();
    }


    public int getCurrentSplitPoints() {
        return splitPoints;
    }
}

