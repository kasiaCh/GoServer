package Rules;

import Game.Board;
import Game.PlayerColor;
import Game.Turn;


public class Suicide implements Rule {

    private Board board;

    public Suicide(Board board){
        this.board = board;
    }

    @Override
    public boolean check(PlayerColor[][] board, Turn turn) {
        int x = turn.getX();
        int y = turn.getY();
        PlayerColor color = turn.getPlayerColor();

        board[x][y] = color;
        this.board.checkMove(x,y,color);
        if (this.board.isAnyToRemove()){
            return false;
        }

        board[x][y] = PlayerColor.FREE;
        return checkFullArea(x,y,color,board);

    }

    private boolean checkFullArea(int x, int y, PlayerColor playerColor, PlayerColor[][] board) {
        int[][] testTab = new int[19][19];
        boolean isSuicideField = true;

        return ifAreaIsFull(x, y, 1, playerColor, testTab, board, isSuicideField);
    }

    private boolean ifAreaIsFull(int x, int y, int number, PlayerColor playerColor, int[][] testTab, PlayerColor[][] board, boolean isSuicideField) {

        if (x < 0 || x >= 19 || y < 0 || y >= 19) {
            return true;
        }

        if (testTab[x][y] <= number && testTab[x][y] != 0) {
            return true;
        }

        if ( isSuicideField || board[x][y] == playerColor) testTab[x][y] = number;
        else {
            if (board[x][y].equals(PlayerColor.FREE)) return false;
            else {
                return true;
            }
        }

        isSuicideField = false;

        if (!ifAreaIsFull(       x, y - 1, number + 1, playerColor, testTab, board, isSuicideField)) return false;
        if (!ifAreaIsFull(x + 1,        y, number + 1, playerColor, testTab, board, isSuicideField)) return false;
        if (!ifAreaIsFull(       x, y + 1, number + 1, playerColor, testTab, board, isSuicideField)) return false;
        if (!ifAreaIsFull(x - 1,        y, number + 1, playerColor, testTab, board, isSuicideField)) return false;

        return true;
    }
}
