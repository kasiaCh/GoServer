package Game;

import GameSummary.AreaMarker;
import Listeners.IPlayerMadeGameDecisionListener;

import java.io.IOException;
import java.util.ArrayList;


public interface IPlayer {

    void makeGameDecision(IPlayerMadeGameDecisionListener listener) throws IOException;

    Login getLogin();

    IPlayer getOpponent();

    void setOpponent(IPlayer opponent);

    void sendMyLogin();

    void sendInfoOpponentPassed();

    void sendUpdatedBoard(ArrayList<String> updatedBoard);

    void sendInfoIllegalMoveKO();

    void sendInfoIllegalMoveSuicide();

    void sendInfoIllegalMoveOccupiedField();

    void sendInfoLegalMove();

    void sendInfoCapturedStones(String capturedForWhite, String capturedForBlack);

    void sendInfoMarkDeadStones() throws IOException;

    void setIfOpponentPassed(boolean ifPassed);

    void sendInfo(String info);

    void sumUp(IPlayerMadeGameDecisionListener listener) throws IOException;

    void markArea(IPlayerMadeGameDecisionListener listener) throws IOException;

    void setAreaMarker(AreaMarker areaMarker);

    void disconnectPlayer();

    void deleteRoom();

}

