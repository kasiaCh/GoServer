package Game;

import GameSummary.AreaMarker;
import Listeners.IPlayerMadeGameDecisionListener;
import Rules.Ko;
import Rules.OccupiedField;
import Rules.Rule;
import Rules.Suicide;

import java.io.IOException;
import java.util.ArrayList;

public class Room implements IPlayerMadeGameDecisionListener {
    /**
     * Game board.
     */
    private Board board;
    /**
     * Player which creates room.
     */
    private IPlayer initiator;
    /**
     * Player which joins the room.
     */
    private IPlayer joiner;
    /**
     * Index of the room.
     */
    private int index;

    /**
     * Object which marks area of the player.
     */
    private AreaMarker areaMarker;

    public Room(){}

    public Room(IPlayer initiator){
        this.initiator = initiator;
        board = new Board();
        areaMarker = new AreaMarker(board);
        initiator.setAreaMarker(areaMarker);
    }

    public IPlayer getInitiator() {
        return initiator;
    }

    public IPlayer getJoiner() { return joiner; }

    public void setJoiner(IPlayer joiner){
        this.joiner = joiner;
        joiner.setAreaMarker(areaMarker);
    }

    /**
     * Method which is called when player made turn. Turn is analyzed, then, depending on the type of the turn, specific
     * messages are send to the player and his opponent (updated board, info about captured stones, ask to make turn etc
     * are send.
     * @param player player who made turn
     * @param turn turn made by the player
     */
    @Override
    public void playerMadeTurn(IPlayer player, Turn turn){

        Rule rule = board.analyzeTurn(turn);

        if (rule instanceof OccupiedField){
            player.sendInfoIllegalMoveOccupiedField();
            try {
                player.makeGameDecision(this);
            } catch (IOException e) {
                player.getOpponent().sendInfo("OPPONENT_GAVE_UP");
                player.deleteRoom();
                player.disconnectPlayer();

            }
        }
        else if (rule instanceof Suicide){
            player.sendInfoIllegalMoveSuicide();
            try {
                player.makeGameDecision(this);
            } catch (IOException e) {
                player.getOpponent().sendInfo("OPPONENT_GAVE_UP");
                player.deleteRoom();
                player.disconnectPlayer();
            }
        }
        else if (rule instanceof Ko){
            player.sendInfoIllegalMoveKO();
            try {
                player.makeGameDecision(this);
            } catch (IOException e) {
                player.getOpponent().sendInfo("OPPONENT_GAVE_UP");
                player.deleteRoom();
                player.disconnectPlayer();
            }
        }
        else {

            ArrayList<String> updatedBoard = boardToString(board.getBoard());

            player.sendInfoLegalMove();
            player.sendUpdatedBoard(updatedBoard);
            player.getOpponent().sendUpdatedBoard(updatedBoard);
            player.sendInfoCapturedStones(Integer.toString(board.getCapturedForWhite()),
                    Integer.toString(board.getCapturedForBlack()));
            player.getOpponent().sendInfoCapturedStones(Integer.toString(board.getCapturedForWhite()),
                    Integer.toString(board.getCapturedForBlack()));

            try {
                player.getOpponent().makeGameDecision(this);
            } catch (IOException e) {
                player.getOpponent().sendInfo("OPPONENT_GAVE_UP");
                player.deleteRoom();
                player.disconnectPlayer();
            }

        }
    }

    public void setIndex(int index) {
        this.index = index+1;
    }

    public int getIndex() {
        return index;
    }

    /**
     * Parse the array which represents board with PlayerColors to ArrayList of String
     * (prepares it to sending to the clients)
     * @param board board to parse
     * @return ArrayList of Strings
     */
    private ArrayList<String> boardToString(PlayerColor[][] board){
        ArrayList<String> updatedBoard = new ArrayList<>();

        for (int i = 0; i < 19; i++){
            for (int j = 0; j < 19; j++){
                updatedBoard.add(board[i][j].toString());
            }
        }

        return updatedBoard;
    }

    /**
     * Updates board when one of the players marked area
     * @param response response with info about marked area
     */
    public void updateBoard(String response){
        PlayerColor [][] updatedBoard = new PlayerColor[19][19];

        String response1 = response.replace("MARK_AREA [","");
        String board[] = response1.replace("]","").split(", ");

        for(int i = 0; i < 19; i++){
            for(int j = 0; j < 19; j++){
                if(board[(i*18)+i+j].equals("BLACK"))
                    updatedBoard[i][j] = PlayerColor.BLACK;
                else if(board[(i*18)+i+j].equals("WHITE"))
                    updatedBoard[i][j] = PlayerColor.WHITE;
                else if(board[(i*18)+i+j].equals("FREE"))
                    updatedBoard[i][j] = PlayerColor.FREE;
            }
        }

        this.board.setBoard(updatedBoard);

    }
}