package Game;

import Rules.Ko;
import Rules.OccupiedField;
import Rules.Rule;
import Rules.Suicide;


public class Board {
    private OccupiedField occupiedField;
    private Suicide suicide;
    private Ko ko;
    private PlayerColor[][] board;
    private int[][] stonesToRemove;
    private int[][] tmp;
    private int capturedForWhite;
    private int capturedForBlack;


    public Board() {
        board = new PlayerColor[19][19];
        initializeBoard(board);
        occupiedField = new OccupiedField();
        suicide = new Suicide(this);
        ko = new Ko();
        stonesToRemove = new int[19][19];
        tmp = new int[19][19];
    }

    public Rule analyzeTurn(Turn turn) {
        if (isOccupied(turn)) {
            return occupiedField;
        }
        else if (isKo(turn)) {
            return ko;
        }
        else if (isSuicide(turn)) {
            return suicide;
        }
        else {
            updateBoard(turn);
        }
        return null;
    }

    private void updateBoard(Turn turn) {
        int x = turn.getX();
        int y = turn.getY();
        PlayerColor playerColor = turn.getPlayerColor();
        board[x][y] = playerColor;
        checkMove(x, y, playerColor);
        countCaptured(turn.getPlayerColor());
        removeStones();
        clearTables();
    }

    public boolean checkMove(int x, int y, PlayerColor playerColor) {
        PlayerColor oppositeColor;
        if (playerColor.equals(PlayerColor.BLACK)) {
            oppositeColor = PlayerColor.WHITE;
        } else {
            oppositeColor = PlayerColor.BLACK;
        }

        checkFullArea(x - 1, y - 1, oppositeColor);
        checkFullArea(       x, y - 1, oppositeColor);
        checkFullArea(x + 1, y - 1, oppositeColor);
        checkFullArea(x + 1,        y, oppositeColor);
        checkFullArea(x + 1, y + 1, oppositeColor);
        checkFullArea(       x, y + 1, oppositeColor);
        checkFullArea(x - 1, y + 1, oppositeColor);
        checkFullArea(x - 1,        y, oppositeColor);

        return false;
    }

    private boolean checkFullArea(int x, int y, PlayerColor playerColor) {
        int[][] testTab = new int[19][19];

        if (x < 0 || x >= 19 || y < 0 || y >= 19) return false;

        if (board[x][y] != playerColor) return false;

        boolean value = ifAreaIsFull(x, y, 1, playerColor, testTab);

        if (value) {
            for (int i = 0; i < 19; i++) {
                for (int j = 0; j < 19; j++) {
                    stonesToRemove[i][j] = tmp[i][j];
                }
            }
        }

        return value;
    }

    boolean ifAreaIsFull(int x, int y, int number, PlayerColor playerColor, int[][] testTab) {


        if (x < 0 || x >= 19 || y < 0 || y >= 19) {
            return true;
        }

        if (testTab[x][y] <= number && testTab[x][y] != 0) {
            return true;
        }

        if (board[x][y] == playerColor) testTab[x][y] = number;
        else {
            if (board[x][y].equals(PlayerColor.FREE)) return false;
            else {
                return true;
            }
        }


        if (!ifAreaIsFull(       x, y - 1, number + 1, playerColor, testTab)) return false;
        if (!ifAreaIsFull(x + 1,        y, number + 1, playerColor, testTab)) return false;
        if (!ifAreaIsFull(       x, y + 1, number + 1, playerColor, testTab)) return false;
        if (!ifAreaIsFull(x - 1,        y, number + 1, playerColor, testTab)) return false;

        tmp[x][y] = 1;
        return true;
    }


    public PlayerColor[][] getBoard() {
        return board;
    }

    void setBoard(PlayerColor[][] board) {
        this.board = board;
    }

    private void initializeBoard(PlayerColor[][] board) {
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                board[i][j] = PlayerColor.FREE;
            }
        }
    }

    private boolean isOccupied(Turn turn) {

        return occupiedField.check(board, turn);
    }

    private boolean isSuicide(Turn turn) {
        return suicide.check(board, turn);
    }

    private boolean isKo(Turn turn) {

        return ko.check(board, turn);
    }

    private void removeStones() {
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                if (stonesToRemove[i][j] == 1) {
                    board[i][j] = PlayerColor.FREE;
                }
            }
        }
    }

    private void countCaptured(PlayerColor playerColor){
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                if (stonesToRemove[i][j] == 1) {
                    if(playerColor.equals(PlayerColor.WHITE))
                        capturedForWhite++;
                    else if(playerColor.equals(PlayerColor.BLACK))
                        capturedForBlack++;

                }
            }
        }

    }

    private void clearTables() {
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                tmp[i][j] = 0;
                stonesToRemove[i][j] = 0;
            }
        }

    }

    public boolean isAnyToRemove() {
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                if (stonesToRemove[i][j] == 1)
                    return true;
            }
        }
        return false;
    }

    int getCapturedForWhite(){
        return capturedForWhite;
    }

    int getCapturedForBlack(){
        return capturedForBlack;
    }
}
