package model.cards;

public class Card {

    private Suit suit;

    private int value;

    static int LOWER_BOUND_VALUE = 1;
    static int MAXIMUM_BOUND_VALUE = 13;

    private int points;

    public Card(Suit suit, int value) {
        if(value < LOWER_BOUND_VALUE && value > MAXIMUM_BOUND_VALUE) {
            throw new RuntimeException("Value exceeds the bound");
        }
        this.value = value;
        this.suit = suit;

        switch (value) {
            case 1:  points = 11; break;
            case 11: points = 1; break;
            case 12: points = 2; break;
            case 13: points = 3; break;
            default: points = value;
        }
    }


    public Suit getSuit() {
        return suit;
    }

    public String getValue() {
        switch (value) {
            case 11: return "J";
            case 12: return "Q";
            case 13: return "K";
            default:return String.valueOf(value);
        }
    }


    public String pathToImage() {
        return "/card_images/" + suit.toString().toLowerCase() +  "/" + value + ".png";
    }


    public int getPoints() {
        return points;
    }


    public void setPoints(int points) {
        this.points = points;
    }


    public boolean isAce() {
        return value == 1;
    }
}
