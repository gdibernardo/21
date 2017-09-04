import org.junit.Test;
import static org.junit.Assert.*;

import model.game.Game;
import model.players.Player;


public class GameTest {


    @Test
    public void testRemovePlayer() {
        int testNumberOfPlayers = 5;

        Game game = new Game(testNumberOfPlayers);
        int expectedValue = testNumberOfPlayers - 1;

        game.initialize();
        Player player = game.nextPlayer();

        game.removePlayer(player);
        assertEquals(expectedValue, game.numberOfActivePlayers());
    }


    @Test
    public void testinsertWaitingPlayersBackToGame() {

        int numberOfPlayers = 2;
        Game game = new Game(numberOfPlayers);

        Player player = game.nextPlayer();
        player.bet(player.getCurrentBalance());
        /*  Lose everything.    */
        player.lose();

        game.removePlayer(player);

        game.insertWaitingPlayersBackToGame();

        assertEquals(1, game.getNumberOfPlayers());

        player = game.nextPlayer();
        player.bet(player.getCurrentBalance());
        /*  Lose everything.    */
        player.lose();

        game.insertWaitingPlayersBackToGame();

        assertEquals(0, game.getNumberOfPlayers());
    }
}
