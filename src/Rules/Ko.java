package Rules;

import Game.Board;
import Game.PlayerColor;
import Game.Turn;

import java.util.Arrays;


public class Ko implements Rule {
    private PlayerColor[][] turnsOfBlack;
    private PlayerColor[][] turnsOfWhite;


    public Ko(){
        turnsOfBlack = new PlayerColor[19][19];
        turnsOfWhite = new PlayerColor[19][19];
    }

    @Override
    public boolean check(PlayerColor[][] board, Turn turn) {
        PlayerColor[][] tmp = new PlayerColor[19][19];

        for(int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                tmp[i][j] = board[i][j];
            }
        }

        tmp[turn.getX()][turn.getY()] = turn.getPlayerColor();

        if (turn.getPlayerColor().equals(PlayerColor.BLACK)) {
            boolean equals = Arrays.deepEquals(tmp, turnsOfBlack);
            if (!equals) {
                for(int i = 0; i < 19; i++){
                    for(int j = 0; j < 19; j++){
                        turnsOfBlack[i][j] = tmp[i][j];
                    }
                }
            }

            return equals;

        } else {
            boolean equals = Arrays.deepEquals(tmp, turnsOfWhite);
            if (equals) {
                for(int i = 0; i < 19; i++){
                    for(int j = 0; j < 19; j++){
                        turnsOfWhite[i][j] = tmp[i][j];
                    }
                }
            }

            return equals;

        }
    }

}