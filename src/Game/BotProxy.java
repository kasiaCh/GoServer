package Game;

import GameSummary.AreaMarker;
import Listeners.IPlayerMadeGameDecisionListener;

import java.io.IOException;
import java.util.ArrayList;


abstract public class BotProxy implements IPlayer {


    @Override
    public void makeGameDecision(IPlayerMadeGameDecisionListener listener) throws IOException {}

    @Override
    public Login getLogin() {
        return null;
    }

    @Override
    public IPlayer getOpponent() {
        return null;
    }

    @Override
    public void sendMyLogin() {}

    @Override
    public void sendInfoOpponentPassed() {}

    @Override
    public void sendUpdatedBoard(ArrayList<String> updatedBoard) {}

    @Override
    public void sendInfoIllegalMoveKO() {}

    @Override
    public void sendInfoIllegalMoveSuicide() {}

    @Override
    public void sendInfoIllegalMoveOccupiedField() {}

    @Override
    public void sendInfoLegalMove() {}

    @Override
    public void sendInfoCapturedStones(String capturedForWhite, String capturedForBlack) {}

    @Override
    public void sendInfoMarkDeadStones() throws IOException {}

    @Override
    public void setIfOpponentPassed(boolean ifPassed) {}

    @Override
    public void sendInfo(String info) {}

    @Override
    public void sumUp(IPlayerMadeGameDecisionListener listener) throws IOException {}

    @Override
    public void markArea(IPlayerMadeGameDecisionListener listener) throws IOException {}

    @Override
    public void setAreaMarker(AreaMarker areaMarker) {}

    @Override
    public void disconnectPlayer() {}

    @Override
    public void deleteRoom() {}

    @Override
    public void setOpponent(IPlayer opponent) {}
}
