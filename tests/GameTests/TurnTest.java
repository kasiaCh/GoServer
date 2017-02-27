package GameTests;

import Game.PlayerColor;
import Game.Turn;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class TurnTest {

    Turn turn;
    int x;
    int y;
    PlayerColor playerColor;

    @Before
    public void setUp() throws Exception {
        x = 1;
        y = 2;
        playerColor = PlayerColor.BLACK;
        turn = new Turn(x,y,playerColor);
    }

    @Test
    public void getPlayerColor() throws Exception {
        Assert.assertNotNull(turn.getPlayerColor());
        Assert.assertEquals(PlayerColor.BLACK,turn.getPlayerColor());
    }

    @Test
    public void getX() throws Exception {
        Assert.assertNotNull(turn.getX());
        Assert.assertEquals(1,turn.getX());
    }

    @Test
    public void getY() throws Exception {
        Assert.assertNotNull(turn.getY());
        Assert.assertEquals(2,turn.getY());
    }

}