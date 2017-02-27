package Rules;

import Game.PlayerColor;
import Game.Turn;


public interface Rule {

    public boolean check(PlayerColor[][] board, Turn turn);
}
