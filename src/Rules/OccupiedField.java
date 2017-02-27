package Rules;

import Game.PlayerColor;
import Game.Turn;


public class OccupiedField implements Rule {
    @Override
    public boolean check(PlayerColor[][] board, Turn turn) {
        return (board[turn.getX()][turn.getY()].equals(PlayerColor.WHITE) ||
                board[turn.getX()][turn.getY()].equals(PlayerColor.BLACK));
    }
}
