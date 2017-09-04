package View;

import javafx.collections.FXCollections;

import javafx.event.EventHandler;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import javafx.stage.Stage;

import model.cards.Card;

import model.players.Bank;
import model.players.Player;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class GameView {

    private static GameView instance = null;

    private Group root;
    private List<Node> userNodes;
    private List<Node> bankNodes;

    private ComboBox betComboBox;

    private Stage primaryStage;

    public Double getSelectedBet() {
        return (Double) betComboBox.getValue();
    }

    public GameView() {
        userNodes = new ArrayList<Node>();
        bankNodes = new ArrayList<Node>();
    }

    public static GameView currentView() {
        if(instance == null) {
            /*  Allocating shared instance.    */
            instance = new GameView();
        }
        return instance;
    }

    private void addInUserSubview(Node node) {
        root.getChildren().add(node);

        userNodes.add(node);
    }

    public void clear() {
        clearBankSubview();
        clearUserSubview();
    }

    private void clearBankSubview() {
        if(bankNodes.size() > 0) {
            root.getChildren().removeAll(bankNodes);
            bankNodes = new ArrayList<Node>();
        }
    }

    private void addInBankSubview(Node node) {
        root.getChildren().add(node);

        bankNodes.add(node);
    }

    private void clearUserSubview() {
        if(userNodes.size() > 0) {
            root.getChildren().removeAll(userNodes);
            userNodes = new ArrayList<Node>();
        }
    }

    public void initWithStage(Stage primaryStage) {
        primaryStage.setTitle("21");

        root = new Group();

        Scene scene = new Scene(root, LayoutUtils.WINDOW_WIDTH, LayoutUtils.WINDOW_HEIGHT, Color.DARKGREEN);

        this.primaryStage = primaryStage;

        Text bankText = new Text(LayoutUtils.WINDOW_WIDTH/2 - LayoutUtils.MARGIN_30, LayoutUtils.MARGIN_40, "Bank");
        bankText.setFill(Color.WHITE);
        bankText.setFont(Font.font("Verdana", 25));

        root.getChildren().add(bankText);


        this.primaryStage.setScene(scene);


        this.primaryStage.show();
    }


    /*  Probably a common method for presenting bank/player cards might be created. */
    /*  Not that time avaiable, though. */
    public void presentBank(Bank bank) {
        placeCards(bank.getCards(), LayoutUtils.MARGIN_10, LayoutUtils.BANK_START_Y, false);
    }


    public void presentPlayer(Player player) {
        presentCardsForPlayer(player);
    }

    public void presentPlayer(Player player, EventHandler betHandler) {
        presentCardsForPlayer(player);
        addBetOptions(player, betHandler);
    }

    public void presentPlayer(Player player, EventHandler hitHandler, EventHandler standHandler, EventHandler splitHandler, boolean split) {
        presentCardsForPlayer(player);
        addPlayOptions(player, hitHandler, standHandler, splitHandler, split);
    }


    private void placeCards(List<Card> cards, double originX, double originY, boolean user) {
        for(int index = 0; index < cards.size(); index++) {
            Card card = cards.get(index);
            try {
                Image image = new Image(getClass().getResource(card.pathToImage()).toURI().toString());

                ImageView imageView = new ImageView(image);
                imageView.setFitHeight(LayoutUtils.CARD_HEIGHT);
                imageView.setFitWidth(LayoutUtils.CARD_WIDTH);
                imageView.setX(((LayoutUtils.CARD_WIDTH/2) * index) + originX);
                imageView.setY(originY);
                if(user)
                    addInUserSubview(imageView);
                else
                    addInBankSubview(imageView);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }


    private void presentCardsForPlayer(Player player) {
        clearUserSubview();
        Text nameText = new Text(LayoutUtils.MARGIN_10, LayoutUtils.WINDOW_HEIGHT - LayoutUtils.CARD_HEIGHT - LayoutUtils.MARGIN_50, player.getName() + " -   " + player.getCurrentBalance() + "$");
        nameText.setFill(Color.WHITE);
        nameText.setFont(Font.font("Verdana", 25));

        addInUserSubview(nameText);

        placeCards(player.getCards(), LayoutUtils.MARGIN_10, LayoutUtils.WINDOW_HEIGHT - LayoutUtils.CARD_HEIGHT - LayoutUtils.MARGIN_10, true);
        if(player.hasSplit()) {
            placeCards(player.getSplitCards(), LayoutUtils.MARGIN_10 + LayoutUtils.WINDOW_WIDTH/2 , LayoutUtils.WINDOW_HEIGHT - LayoutUtils.CARD_HEIGHT - LayoutUtils.MARGIN_10, true);
        }
    }


    private void addBetOptions(Player player, EventHandler terminatonHandler) {
        /*  We should probably add option to leave?!    */

        ArrayList<Double> options = new ArrayList<Double>();

        for(double index = LayoutUtils.MINIMUM_BET; index <= player.getCurrentBalance(); index += LayoutUtils.MINIMUM_BET) {
            options.add(index);
        }


        Text currencyText = new Text(LayoutUtils.WINDOW_WIDTH - LayoutUtils.MARGIN_50, LayoutUtils.WINDOW_HEIGHT - LayoutUtils.CARD_HEIGHT - LayoutUtils.MARGIN_30,"$");
        currencyText.setFill(Color.WHITE);
        currencyText.setFont(Font.font("Verdana", 25));

        addInUserSubview(currencyText);

        Double currentBalance = player.getCurrentBalance();
        if(!options.contains(currentBalance)) {
            options.add(currentBalance);
        }

        betComboBox = new ComboBox(FXCollections.observableArrayList(options));
        betComboBox.setLayoutX(LayoutUtils.WINDOW_WIDTH - LayoutUtils.COMBO_BOX_MARGIN);
        betComboBox.setLayoutY(LayoutUtils.WINDOW_HEIGHT - LayoutUtils.CARD_HEIGHT - LayoutUtils.COMBO_BOX_MARGIN/3);
        betComboBox.getSelectionModel().selectFirst();
        addInUserSubview(betComboBox);

        Button betButton = new Button("Bet");
        betButton.setLayoutX(LayoutUtils.WINDOW_WIDTH - LayoutUtils.COMBO_BOX_MARGIN); // betComboBox.getLayoutX();
        betButton.setLayoutY(LayoutUtils.WINDOW_HEIGHT - LayoutUtils.CARD_HEIGHT - LayoutUtils.MARGIN_20);
        betButton.addEventHandler(MouseEvent.MOUSE_CLICKED, terminatonHandler);
        addInUserSubview(betButton);
    }


    private void addPlayOptions(Player player, EventHandler hitHandler, EventHandler standHandler, EventHandler splitHandler, boolean split) {
        Text betText = new Text(LayoutUtils.WINDOW_WIDTH - LayoutUtils.BET_TEXT_MARGIN, LayoutUtils.WINDOW_HEIGHT/2,"Bet: " + player.getCurrentBet() + " $");
        betText.setFill(Color.WHITE);
        betText.setFont(Font.font("Verdana", 20));
        addInUserSubview(betText);

        if(player.hasSplit()) {
            Text betSplitText = new Text(LayoutUtils.WINDOW_WIDTH - LayoutUtils.BET_TEXT_MARGIN, LayoutUtils.WINDOW_HEIGHT/2 + LayoutUtils.MARGIN_30,"Split bet: " + player.getCurrentBet() + " $");
            betSplitText.setFill(Color.WHITE);
            betSplitText.setFont(Font.font("Verdana", 20));
            addInUserSubview(betSplitText);
        }

        Button hitButton = new Button("Hit");
        hitButton.setLayoutY(betText.getY() + LayoutUtils.HIT_BUTTON_Y_MARGIN);
        hitButton.setLayoutX(betText.getX());
        hitButton.addEventHandler(MouseEvent.MOUSE_CLICKED, hitHandler);

        addInUserSubview(hitButton);

        Button standButton = new Button("Stand");
        standButton.setLayoutX(hitButton.getLayoutX());
        standButton.setLayoutY(hitButton.getLayoutY() + LayoutUtils.HIT_BUTTON_Y_MARGIN/2);
        standButton.addEventHandler(MouseEvent.MOUSE_CLICKED, standHandler);


        addInUserSubview(standButton);

        if(player.canSplit() && !player.hasSplit()) {
            Button splitButton = new Button("Split");
            splitButton.setLayoutX(standButton.getLayoutX());
            splitButton.setLayoutY(standButton.getLayoutY() + LayoutUtils.HIT_BUTTON_Y_MARGIN/2);
            splitButton.addEventHandler(MouseEvent.MOUSE_CLICKED, splitHandler);

            addInUserSubview(splitButton);
        }

        if(player.hasSplit()) {
            if((split && player.getSplitCards().size() == 1) || (!split && player.getCards().size() == 1)) {
                standButton.setDisable(true);
            }

            try {
                Image image = new Image(getClass().getResource("/arrow.png").toURI().toString());
                ImageView imageView = new ImageView(image);

                double margin = split ? LayoutUtils.MARGIN_10 + LayoutUtils.WINDOW_WIDTH/2 : LayoutUtils.MARGIN_10;

                imageView.setX(margin + (LayoutUtils.CARD_WIDTH/2) + LayoutUtils.MARGIN_5);
                imageView.setY(LayoutUtils.WINDOW_HEIGHT - LayoutUtils.CARD_HEIGHT - LayoutUtils.MARGIN_40);
                imageView.setFitHeight(LayoutUtils.MARGIN_30);
                imageView.setFitWidth(LayoutUtils.MARGIN_30);

                addInUserSubview(imageView);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }


    public void showBustMessage(Player player, boolean split) {

        Alert alert = getAlert(Alert.AlertType.ERROR);
        alert.setHeaderText("Busted");
        if(split)
            alert.setContentText("You have been busted, sorry! You have just lost " + player.getCurrentBet() + ". But, wait.. You can continue with your split.");
        else
            alert.setContentText("You have been busted, sorry! You lost " + player.getCurrentBet() + " $");

        System.out.println("Player " + player.getName() + " got busted. Lost " + player.getCurrentBet() + ", new balance: " + player.getCurrentBalance());
        alert.showAndWait();
    }

    public void showWinMessage(Player player, double winning) {
        Alert alert = getAlert(Alert.AlertType.INFORMATION);

        alert.setHeaderText("Win");

        alert.setContentText("You won " + winning + " $");
        System.out.println("Player " + player.getName() + " won. Won " + winning + ", new balance: " + player.getCurrentBalance());

        alert.showAndWait();
    }

    public void showLoseMessage(Player player, double lost) {
        Alert alert = getAlert(Alert.AlertType.ERROR);
        alert.setHeaderText("Lose");
        alert.setContentText("You lost " + lost + " $");

        System.out.println("Player " + player.getName() + " lose the round. Lost " + lost + ", new balance: " + player.getCurrentBalance());

        alert.showAndWait();
    }

    public void displayMessage(String message) {
        Alert alert = getAlert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Message from the game");
        alert.setContentText(message);

        alert.showAndWait();
    }


    private Alert getAlert(Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle("21 Game");
        alert.initOwner(primaryStage);

        return alert;
    }


    public void showPlayButton(EventHandler playHandler) {
        Button playButton = new Button("PLAY AGAIN");
        playButton.setLayoutX(LayoutUtils.WINDOW_WIDTH/2 - LayoutUtils.MARGIN_30);
        playButton.setLayoutY(LayoutUtils.WINDOW_HEIGHT/2 - LayoutUtils.MARGIN_15);

        playButton.addEventHandler(MouseEvent.MOUSE_CLICKED, playHandler);

        addInBankSubview(playButton);
    }
}