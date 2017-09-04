import org.junit.Test;
import static org.junit.Assert.*;

import model.cards.Card;
import model.cards.Suit;
import model.players.Player;

public class PlayerTest {
    @Test
    public void testCanSplit() {
        Card firstCard = new Card(Suit.CLUBS, 2);
        Card secondCard = new Card(Suit.SPADES, 2);

        Player player = new Player(0, "Player-0", 500);
        player.addCard(firstCard);
        player.addCard(secondCard);

        player.bet(player.getCurrentBalance()/2 + 1);

        assertFalse(player.canSplit());

        player.win();

        player.bet(player.getCurrentBalance()/2 - 1);

        assertTrue(player.canSplit());

        player.clear();

        player.addCard(new Card(Suit.SPADES, 3));
        player.addCard(new Card(Suit.SPADES, 4));

        assertFalse(player.canSplit());
    }


    @Test
    public void testSplit() {
        Card firstCard = new Card(Suit.CLUBS, 2);
        Card secondCard = new Card(Suit.SPADES, 2);

        Player player = new Player(0, "Player-0", 500);
        player.addCard(firstCard);
        player.addCard(secondCard);

        player.bet(100);

        player.split();

        assertTrue(player.getCurrentPoints() == player.getCurrentSplitPoints());
        assertTrue(player.getCards().size() == 1 && player.getSplitCards().size() == 1);
    }
}
