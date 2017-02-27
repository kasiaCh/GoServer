package GameTests;

import Game.Board;
import Game.PlayerColor;
import Game.Turn;
import Rules.Ko;
import Rules.OccupiedField;
import Rules.Suicide;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;



public class BoardTest {
    Board board;
    PlayerColor[][] fields;

    @Before
    public void setUp() throws Exception {
        board = new Board();
        fields = board.getBoard();
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                fields[i][j] = PlayerColor.FREE;
            }
        }

        fields[3][4] = PlayerColor.BLACK;
        fields[2][5] = PlayerColor.BLACK;
        fields[3][6] = PlayerColor.BLACK;
        fields[4][4] = PlayerColor.WHITE;
        fields[5][5] = PlayerColor.WHITE;
        fields[4][6] = PlayerColor.WHITE; // ko

        fields[10][10] = PlayerColor.BLACK;
        fields[9][11] = PlayerColor.BLACK;
        fields[10][12] = PlayerColor.BLACK;
        fields[11][11] = PlayerColor.BLACK; // suicide

        fields[14][14] = PlayerColor.WHITE;
        fields[13][15] = PlayerColor.WHITE;
        fields[14][16] = PlayerColor.WHITE;
        fields[15][15] = PlayerColor.WHITE; // suicide

        fields[5][6] = PlayerColor.BLACK;
        fields[6][6] = PlayerColor.BLACK;
        fields[6][5] = PlayerColor.WHITE;
        fields[7][6] = PlayerColor.WHITE;
        fields[6][7] = PlayerColor.WHITE; // delete stones


    }

    @Test
    public void testIfOccupied() throws Exception {
        // occupied field provoked
        Turn turn = new Turn(3,4, PlayerColor.WHITE);
        Assert.assertEquals(true, board.analyzeTurn(turn)instanceof OccupiedField);
    }

    @Test
    public void testIfKo(){
        // ko provoked
        Turn turn1 = new Turn(3,5, PlayerColor.WHITE);
        Turn turn2 = new Turn(4,5, PlayerColor.BLACK);

        board.analyzeTurn(turn1);
        Assert.assertTrue(fields[turn1.getX()][turn1.getY()].equals(PlayerColor.WHITE));
        board.analyzeTurn(turn2);
        Assert.assertTrue(fields[turn2.getX()][turn2.getY()].equals(PlayerColor.BLACK));
        board.analyzeTurn(turn1);
        Assert.assertTrue(fields[turn1.getX()][turn1.getY()].equals(PlayerColor.WHITE));
        Assert.assertEquals(true, board.analyzeTurn(turn2) instanceof Ko);
        Assert.assertFalse(fields[turn2.getX()][turn2.getY()].equals(PlayerColor.BLACK));
    }
    @Test
    public void testIfSuicide(){
        // suicide provoked
        Turn turn3 = new Turn(10,11, PlayerColor.WHITE);
        Turn turn4 = new Turn(14, 15, PlayerColor.BLACK);
        Assert.assertEquals(true, board.analyzeTurn(turn3) instanceof Suicide);
        Assert.assertEquals(true, board.analyzeTurn(turn4) instanceof Suicide);
    }

    @Test
    public void testDeleteStones(){
        Turn turn = new Turn(5,7, PlayerColor.WHITE);
        board.analyzeTurn(turn);
        Assert.assertEquals(true, fields[5][6].equals(PlayerColor.FREE));
        Assert.assertEquals(true, fields[6][6].equals(PlayerColor.FREE));
    }


}