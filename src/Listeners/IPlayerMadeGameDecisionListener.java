package Listeners;

import Game.IPlayer;
import Game.Turn;


public interface IPlayerMadeGameDecisionListener {

    void playerMadeTurn(IPlayer player, Turn turn);

    void updateBoard(String response);


}
