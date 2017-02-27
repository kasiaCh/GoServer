package GameSummary;

import Game.Board;
import Game.PlayerColor;

import java.util.ArrayList;


public class AreaMarker {
    private Board board;
    private PlayerColor[][] boardTab;
    private ArrayList<PlayerColor> whoseArea;
    private ArrayList<String> markedAreaColor;
    private int[][] markedArea;

    public AreaMarker (Board board){
        this.board = board;
    }

    public ArrayList<String> markArea(int x, int y){
        boardTab = board.getBoard();
        markedArea = new int[19][19];
        whoseArea = new ArrayList<>();
        flood_fill(x,y,1);

        return markedAreaToString();
    }

    private void flood_fill(int x, int y, int number){
        if (x < 0 || x >= 19 || y < 0 || y >= 19) return;

        if (boardTab[x][y].equals(PlayerColor.BLACK) || boardTab[x][y].equals(PlayerColor.WHITE)) {
            whoseArea.add(boardTab[x][y]);
            return;
        }

        if (markedArea[x][y] <= number && markedArea[x][y] != 0) return;

        if (boardTab[x][y].equals(PlayerColor.FREE))
        {
            markedArea[x][y] = number;

            flood_fill(x-1,     y, number + 1);
            flood_fill(x+1,     y, number + 1);
            flood_fill(     x,y+1, number + 1);
            flood_fill(     x,y-1, number + 1);
        }
    }

    public ArrayList<String> serwerMarkArea(int i, int j){
        boardTab = board.getBoard();
        markedArea = new int[19][19];
        whoseArea = new ArrayList<>();

        if (boardTab[i][j].equals(PlayerColor.FREE)) {
            flood_fill(i, j, 1);
            if (whoseArea.indexOf(PlayerColor.BLACK) != -1 && whoseArea.indexOf(PlayerColor.WHITE) != -1){
                markedAreaColor(PlayerColor.FREE);
            }
            else if (whoseArea.indexOf(PlayerColor.BLACK) != -1 && whoseArea.indexOf(PlayerColor.WHITE) == -1){
                markedAreaColor(PlayerColor.BLACK);
            } else if (whoseArea.indexOf(PlayerColor.BLACK) == -1 && whoseArea.indexOf(PlayerColor.WHITE) != -1){
                markedAreaColor(PlayerColor.WHITE);
            }
        }

        return markedAreaColor;
    }

    private ArrayList<String> markedAreaToString(){
        ArrayList<String> markedAreaToString = new ArrayList<>();
        for (int i = 0; i < 19; i++){
            for(int j = 0; j < 19; j++){
                markedAreaToString.add(Integer.toString(markedArea[i][j]));
            }
        }
        return markedAreaToString;
    }

    private void markedAreaColor(PlayerColor color){
        markedAreaColor = new ArrayList<>();
        for (int i = 0; i < 19; i++){
            for(int j = 0; j < 19; j++){
                if(markedArea[i][j] != 0){
                    markedAreaColor.add(((i*18)+i+j),color.toString());
                }
                else if(markedArea[i][j] == 0){
                    markedAreaColor.add(((i*18)+i+j),PlayerColor.FREE.toString());
                }
            }
        }
    }
}
