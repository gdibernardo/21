import View.GameView;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.stage.Stage;

import model.game.Game;

import model.cards.Card;
import model.players.Bank;
import model.players.Player;

import javafx.application.Application;

import java.util.ArrayList;
import java.util.List;


public class GameApp extends Application {

    private static final int DEFAULT_NUMBER_OF_PLAYERS = 2;

    private Game game;

    private List<Node> userNodes = new ArrayList<Node>();

    private int numberOfUserThatPlayed;
    private int numberOfUserThatBet;

    private int numberOfGamePlayers;


    public void initGame() {
        game = new Game(numberOfGamePlayers);
    }

    private void defaultState() {
        numberOfUserThatPlayed = 0;
        numberOfUserThatBet = 0;
    }

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {

        List<String> params = getParameters().getRaw();

        int numberOfPlayer = DEFAULT_NUMBER_OF_PLAYERS;

        if (params.size() > 0) {
            /*  Get number of players from input.   */
            numberOfPlayer = Integer.parseInt(params.get(0));
        }

        /*  Init game view. */
        GameView.currentView().initWithStage(primaryStage);

        numberOfGamePlayers = numberOfPlayer;

        /*  Initializing game state.    */
        initGame();
        /*  Starting the game.  */
        startGame();
    }


    void startGame() {
        /*  Give initial card(s).  */
        defaultState();
        game.insertWaitingPlayersBackToGame();
        game.initialize();

        GameView.currentView().clear();

        if(game.getNumberOfPlayers() <= 0) {
            GameView.currentView().displayMessage("No enough players to play!");
            GameView.currentView().showPlayButton(new EventHandler() {
                public void handle(Event event) {
                    initGame();
                    startGame();
                }
            });
            return;
        }

        for (int index = 0; index < game.getNumberOfPlayers(); index++) {
            Player player = game.nextPlayer();
            Card card = game.getCard();

            player.addCard(card);
        }

        betRound();
    }


    private void betRound() {

        if (numberOfUserThatBet == game.getNumberOfPlayers()) {
            gameRound();
        } else {

            final Player player = game.nextPlayer();
            numberOfUserThatBet++;

            GameView.currentView().presentPlayer(player, new EventHandler<Event>() {
                public void handle(Event event) {
                    player.bet(GameView.currentView().getSelectedBet());
                    System.out.println("Bet round: " + player.getName() + " bets " + player.getCurrentBet() + ", balance: " + player.getCurrentBalance());
                    betRound();
                }
            });
        }
    }


    private void gameRound() {

        if(numberOfUserThatPlayed == game.getNumberOfPlayers()) {
            if(game.numberOfActivePlayers() > 0) {
                /*  Bank should play now.   */
                playBank();
            }
            else {
                /*  Starting the game again.    */
                startGame();
            }
            return;
        }

        Player currentPlayer = game.nextPlayer();
        currentPlayer.addCard(game.getCard());
        numberOfUserThatPlayed++;
        play(currentPlayer, false);
    }


    private void play(final Player player, final boolean split) {
        GameView.currentView().presentPlayer(player, new EventHandler() {
            public void handle(Event event) {
                Card card = game.getCard();
                if(split) {
                    player.addCardToSplit(card);
                } else {
                    player.addCard(card);
                }
                play(player, split);
            }
        }, new EventHandler() {
            public void handle(Event event) {
                if(player.hasSplit() && !split) {
                    play(player, true);
                } else {
                    gameRound();
                }
            }
        }, new EventHandler() {
            public void handle(Event event) {
                player.split();
                System.out.println("Player " + player.getName() + " split. Bet: " + player.getCurrentBet() + ", new balance: " + player.getCurrentBalance());
                play(player, split);
            }
        }, split);


        if(player.getCurrentPoints() > Game.GAME_21) {
            if(player.hasSplit()) {
                if(!split) {
                    player.lose();
                    GameView.currentView().showBustMessage(player, true);
                    /*  Player loses money for the first game.  */
                    play(player, true);

                } else {
                    if(player.getCurrentSplitPoints() > Game.GAME_21) {
                        busted(player);
                        gameRound();
                    }
                }
            } else {
                busted(player);
                gameRound();
            }
        } else {
            if(player.hasSplit() && player.getCurrentSplitPoints() > Game.GAME_21) {
                player.lose();
                gameRound();
                GameView.currentView().showBustMessage(player, false);
            }
        }
    }


    private void busted(Player player) {
        player.lose();
        game.removePlayer(player);
        GameView.currentView().showBustMessage(player, false);
    }


    private void playBank() {
        System.out.println("Now bank play...");
        Bank bank = game.getBank();
        while (bank.getCurrentPoints() <= Game.MINIMUM_POINTS_FOR_BANK) {
            bank.addCard(game.getCard());
        }

        GameView.currentView().presentBank(bank);
        resultRound();
    }


    private void resultRound() {

        Bank bank = game.getBank();

        if(bank.getCurrentPoints() > Game.GAME_21) {

            System.out.println("Bank " + bank.getCurrentPoints() + ". Busted.");

            for(Player player : game.getPlayers()) {
                GameView.currentView().presentPlayer(player);
                double winning = 0;
                if(player.hasSplit()) {
                    if(player.getCurrentSplitPoints() <= Game.GAME_21 && player.getCurrentPoints() <= Game.GAME_21) {
                        player.win();
                        winning += player.getCurrentBet();
                    }
                }
                player.win();
                winning += player.getCurrentBet();
                GameView.currentView().showWinMessage(player, winning);
            }
        } else {
            for (Player player : game.getPlayers()) {

                GameView.currentView().presentPlayer(player);
                double winning = 0;
                double lost = 0;
                System.out.println("Bank " + bank.getCurrentPoints() + "    -   Player " + player.getName() + " " + player.getCurrentPoints());
                if(player.hasSplit()) {
                    System.out.println("Bank " + bank.getCurrentPoints() + "    -   Player " + player.getName() + " " + player.getCurrentSplitPoints());

                    if(player.getCurrentPoints() <= Game.GAME_21) {
                        if(player.getCurrentPoints() > bank.getCurrentPoints()) {
                            player.win();
                            winning += player.getCurrentBet();
                        } else {
                            player.lose();
                            lost += player.getCurrentBet();
                        }
                    }
                    if(player.getCurrentSplitPoints() <= Game.GAME_21) {
                        if(player.getCurrentSplitPoints() > bank.getCurrentPoints()) {
                            player.win();
                            winning += player.getCurrentBet();
                        } else {
                            player.lose();
                            lost += player.getCurrentBet();
                        }
                    }

                    if(lost != 0) {
                        GameView.currentView().showLoseMessage(player, lost);
                    }
                    if(winning != 0) {
                        GameView.currentView().showWinMessage(player, winning);
                    }

                } else {
                    if(player.getCurrentPoints() > bank.getCurrentPoints()) {
                       player.win();
                       GameView.currentView().showWinMessage(player, player.getCurrentBet());
                    } else {
                        player.lose();
                        GameView.currentView().showLoseMessage(player, player.getCurrentBet());
                    }
                }
            }
        }
        startGame();
    }
}
