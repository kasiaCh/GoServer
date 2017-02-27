package GameSummaryTests;

import Game.Board;
import Game.PlayerColor;
import GameSummary.AreaMarker;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;


public class AreaMarkerTest {
    AreaMarker areaMarker;
    Board board;
    PlayerColor[][] fields;

    @Before
    public void setUp() throws Exception {
        board = new Board();
        areaMarker = new AreaMarker(board);

        fields = board.getBoard();
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                fields[i][j] = PlayerColor.FREE;
            }
        }

        /* Area for black: (0,0) */
        fields[1][0] = PlayerColor.BLACK;
        fields[0][1] = PlayerColor.BLACK;

        /* Area for white: (4,0),(5,0) */
        fields[3][0] = PlayerColor.WHITE;
        fields[3][1] = PlayerColor.WHITE;
        fields[4][1] = PlayerColor.WHITE;
        fields[5][1] = PlayerColor.WHITE;
        fields[6][1] = PlayerColor.WHITE;
        fields[6][0] = PlayerColor.WHITE;

        /* Area for black: (2,3),(3,3),(2,4) */
        fields[2][2] = PlayerColor.BLACK;
        fields[1][3] = PlayerColor.BLACK;
        fields[1][4] = PlayerColor.BLACK;
        fields[2][5] = PlayerColor.BLACK;
        fields[3][4] = PlayerColor.BLACK;
        fields[4][3] = PlayerColor.BLACK;
        fields[3][2] = PlayerColor.BLACK;

        /* Area for white: (18,17),(18,18),(17,18) */
        fields[18][16] = PlayerColor.WHITE;
        fields[17][17] = PlayerColor.WHITE;
        fields[16][18] = PlayerColor.WHITE;
    }

    @Test
    public void markArea() throws Exception {

        ArrayList<String> testArray1 = new ArrayList<>();
        ArrayList<String> testArray2 = new ArrayList<>();
        ArrayList<String> testArray3 = new ArrayList<>();
        ArrayList<String> testArray4 = new ArrayList<>();

        ArrayList<String> markedArea1 = areaMarker.markArea(0,0);
        ArrayList<String> markedArea2 = areaMarker.markArea(4,0);
        ArrayList<String> markedArea3 = areaMarker.markArea(2,3);
        ArrayList<String> markedArea4 = areaMarker.markArea(17,18);

        for (int i = 0; i < 361; i++){
            testArray1.add("0");
            testArray2.add("0");
            testArray3.add("0");
            testArray4.add("0");
        }

        testArray1.add(0,"1");

        testArray2.add(76,"1");
        testArray2.add(95,"2");

        testArray3.add(41,"1");
        testArray3.add(42,"2");
        testArray3.add(60,"2");

        testArray4.add(341,"1");
        testArray4.add(359,"3");
        testArray4.add(360,"2");

        Assert.assertNotNull(markedArea1);
        Assert.assertNotNull(markedArea2);
        Assert.assertNotNull(markedArea3);
        Assert.assertNotNull(markedArea4);

        /* Wzór: Tab[i][j] = String[(i*18)+i+j]; */

        Assert.assertFalse(markedArea1.get(0).equals("0"));

        Assert.assertFalse(markedArea2.get(76).equals("0"));
        Assert.assertFalse(markedArea2.get(95).equals("0"));

        Assert.assertFalse(markedArea3.get(41).equals("0"));
        Assert.assertFalse(markedArea3.get(60).equals("0"));
        Assert.assertFalse(markedArea3.get(42).equals("0"));

        Assert.assertFalse(markedArea4.get(360).equals("0"));
        Assert.assertFalse(markedArea4.get(359).equals("0"));
        Assert.assertFalse(markedArea4.get(341).equals("0"));

        for (int i = 0; i < 361; i++) {
            Assert.assertEquals(testArray1.get(i), markedArea1.get(i));
            Assert.assertEquals(testArray2.get(i), markedArea2.get(i));
            Assert.assertEquals(testArray3.get(i), markedArea3.get(i));
            Assert.assertEquals(testArray4.get(i), markedArea4.get(i));
        }
    }

    @Test
    public void serwerMarkArea() throws Exception {

        ArrayList<String> testArray1 = new ArrayList<>();
        ArrayList<String> testArray2 = new ArrayList<>();
        ArrayList<String> testArray3 = new ArrayList<>();
        ArrayList<String> testArray4 = new ArrayList<>();

        for (int i = 0; i < 361; i++){
            testArray1.add("FREE");
            testArray2.add("FREE");
            testArray3.add("FREE");
            testArray4.add("FREE");
        }

        testArray1.add(0,"BLACK");

        testArray2.add(76,"WHITE");
        testArray2.add(95,"WHITE");

        testArray3.add(41,"BLACK");
        testArray3.add(42,"BLACK");
        testArray3.add(60,"BLACK");

        testArray4.add(341,"WHITE");
        testArray4.add(359,"WHITE");
        testArray4.add(360,"WHITE");

        ArrayList<String> serwerMarkedArea = null;

        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                if(fields[i][j].equals(PlayerColor.FREE))
                    serwerMarkedArea = areaMarker.serwerMarkArea(i,j);
                if (i == 0 && j == 0) {
                    for (int k = 0; k < 361; k++) {
                        Assert.assertEquals(testArray1.get(k), serwerMarkedArea.get(k));
                    }
                } else if ((i == 4 && j == 0) || (i == 5 && j == 0)){
                    for (int k = 0; k < 361; k++) {
                        Assert.assertEquals(testArray2.get(k), serwerMarkedArea.get(k));
                    }
                } else if ((i == 2 && j == 3) || (i == 3 && j == 3) || (i == 2 && j == 4)){
                    for (int k = 0; k < 361; k++) {
                        Assert.assertEquals(testArray3.get(k), serwerMarkedArea.get(k));
                    }
                } else if ((i == 18 && j == 17) || (i == 18 && j == 18) || (i == 17 && j == 18)){
                    for (int k = 0; k < 361; k++) {
                        Assert.assertEquals(testArray4.get(k), serwerMarkedArea.get(k));
                    }
                }
            }
        }
    }

}
