import org.junit.Test;
import static org.junit.Assert.*;

import model.cards.Deck;

public class DeckTest {
    @Test
    public void testGetCard() {
        int numberOfCardsInDeck = 52;

        Deck deck = new Deck();
        assertEquals(numberOfCardsInDeck, deck.size());
    }
}
