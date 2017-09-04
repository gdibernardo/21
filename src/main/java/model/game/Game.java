package model.game;

import model.cards.Card;
import model.cards.Deck;
import model.players.Bank;
import model.players.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game {

    public static int GAME_21 = 21;

    public static int MINIMUM_POINTS_FOR_BANK = 16;

    public static double DEFAULT_BALANCE = 1000.0;

    private static final int USERS_PER_DECK = 3;
    private static final String PLAYER_NAME_PREFIX = "PLAYER-";

    private Bank bank;
    private List<Player> players;

    private int turn;

    private List<Deck> decks;

    public static int INITIAL_ID = 0;

    private int numberOfPlayers;

    private ArrayList<Player> waitingPlayers;


    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    private int numberOfDecks;

    public Game(int numberOfPlayers) {

        if(numberOfPlayers <= 0) {
            /*  Raise an exception. */
            throw new RuntimeException("Invalid number of players. In order to initialize the game the number of players has to be greater than 0.");
        }

        bank = new Bank();

        this.numberOfPlayers = numberOfPlayers;
        players = new ArrayList<Player>();
        waitingPlayers = new ArrayList<Player>();

        for (int index = INITIAL_ID; index < numberOfPlayers; index++) {
            players.add(new Player(index,PLAYER_NAME_PREFIX + index, DEFAULT_BALANCE));
        }
    }

    public void initialize() {

        bank.clear();

        for(Player player : players) {
            player.clear();
        }

        numberOfDecks = (int) Math.ceil(numberOfPlayers / (double) USERS_PER_DECK);

        decks = new ArrayList<Deck>();

        for (int index = 0; index < numberOfDecks; index++) {
            decks.add(new Deck());
        }

        turn = -1;

        System.out.println("Initialized game with " + numberOfPlayers + " players and " + numberOfDecks + " decks.");
    }

    public Card getCard() {

        if(decks.size() <= 0) {
            throw new RuntimeException("No more card available in deck(s).");
        }

        Deck deck = decks.get(0);
        Card card = deck.getCard();
        if (deck.size() == 0) {
            /*  Removing deck.  */
            decks.remove(0);
        }

        return card;
    }


    public Bank getBank() {
        return bank;
    }

    public List<Player> getPlayers() {
        return players;
    }

    /*  We should probably check if is it possible to get the current player.   */
    public Player getCurrentPlayer() {
        return players.get(turn);
    }

    public Player nextPlayer() {
        /*  Some sanity checks are probably required.   */
        /*  Incrementing turn for the next round.   */
        turn =  (turn + 1) % players.size();
        Player player = players.get(turn);
        return player;
    }

    public int numberOfActivePlayers() {
        return players.size();
    }


    public void removePlayer(Player player) {
        /*  Some sanity checks are probably required.   */
        waitingPlayers.add(player);
        players.remove(player);
        /*  Probably this might be changed.    */
        turn--;
    }

    public void insertWaitingPlayersBackToGame() {

        players.addAll(waitingPlayers);

        players.removeIf(player -> player.getCurrentBalance() <= 0);

        if(players.size() > 0) {
            Collections.sort(players);
        }

        numberOfPlayers = players.size();

        waitingPlayers = new ArrayList<Player>();
    }
}

