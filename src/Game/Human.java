package Game;

import GameSummary.AreaMarker;
import Listeners.IGameView;
import Listeners.IPlayerMadeGameDecisionListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Class which represents Human in Server.
 * Each Human is a single thread and implements interface IPlayer.
 */
public class Human extends Thread implements IPlayer {
    /**
     * Socket of Human.
     */
    private final Socket socket;

    /**
     * Output stream.
     */
    private PrintWriter out;

    /**
     * Input stream.
     */
    private BufferedReader in;

    /**
     * Login.
     */
    private Login login;

    /**
     * Index of room Human plays in.
     */
    private int indexOfRoom;

    /**
     * Color of player's stones.
     */
    private PlayerColor playerColor;

    /**
     * Opponent of the player.
     */
    private IPlayer opponent;

    /**
     * Statement if one of players passed.
     */
    private boolean ifOpponentPassed = false;

    /**
     * Object which marks area of the player.
     */
    private AreaMarker areaMarker;

    IGameView view;


    public Human(Socket socket) {
        this.socket = socket;
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void attachView(IGameView view) {
        this.view = view;
    }


    @Override
    public void run() {
        super.run();
        try {
            logIn();
            chooseOpponent();
        } catch (IOException e) {
            disconnectPlayer();
        }
    }

    /**
     * Sends to the particular client information about possibility of making turn,
     * then receives information about turn making by this client and delegates it
     * to the one of listeners of human
     *
     * @param listener of making turns by human
     */
    @Override
    public void makeGameDecision(IPlayerMadeGameDecisionListener listener) throws IOException {

        try {
            out.println("MAKE_TURN");

            String response = in.readLine();

            if (response.startsWith("TURN")) {

                String[] move = response.split(" ");
                Turn turn = new Turn(Integer.parseInt(move[1]), Integer.parseInt(move[2]), playerColor);
                listener.playerMadeTurn(this, turn);

            } else if (response.equals("PASS")) {

                if (ifOpponentPassed) {

                    sendInfoWaitForOpponentToMarkDeadStones();
                    opponent.sendInfoMarkDeadStones();
                    opponent.sumUp(listener);
                    setIfOpponentPassed(false);
                    opponent.setIfOpponentPassed(false);
                    return;


                } else {
                    opponent.sendInfoOpponentPassed();
                    if(! (opponent instanceof Bot)) {
                        try {
                            opponent.makeGameDecision(listener);
                        } catch (IOException e) {
                            opponent.sendInfo("OPPONENT_GAVE_UP");
                            deleteRoom();
                            disconnectPlayer();
                        }
                    } else {
                        sumUp(listener);
                    }
                }
            }

        } catch (IOException e) {
            opponent.sendInfo("OPPONENT_GAVE_UP");
            deleteRoom();
            disconnectPlayer();
        }

    }

    /**
     * Read  messages from the players when the final part of the game starts and sends specified message.
     * @param listener
     * @throws IOException
     */
    public void sumUp(IPlayerMadeGameDecisionListener listener) throws IOException{
        String response = in.readLine();
        if(response.startsWith("MARKED_AS_DEAD: ") && opponent instanceof Bot){
            sendInfo("BOT_ACCEPTED_DEAD_STONES");
            for(int i = 0; i < 19; i++){
                for(int j = 0; j < 19; j++){
                    ArrayList<String> singleMarkedArea = new ArrayList<>();
                    singleMarkedArea = areaMarker.serwerMarkArea(i,j);
                    sendInfo("SINGLE_MARKED_AREA: " + singleMarkedArea);
                }
            }
            sendInfo("SERWER_MARKED_AREA");
        } else {
            opponent.sendInfo(response);
        }
        if(!response.startsWith("MARK_AREA ") && !response.equals("RESUME"))
            opponent.sumUp(listener);
        else if(response.startsWith("MARK_AREA")){
            listener.updateBoard(response);
            opponent.markArea(listener);
        }
        else if(response.equals("RESUME")){
            opponent.makeGameDecision(listener);
            setIfOpponentPassed(false);
            opponent.setIfOpponentPassed(false);
            return;
        }
    }

    /**
     * Read message from the players when start the part of "marking area"  and sends specified messages.
     * @param listener
     * @throws IOException
     */
    public void markArea(IPlayerMadeGameDecisionListener listener) throws IOException{
        String response = in.readLine();

        if(response.startsWith("FINAL_MARKED_AS_AREA: ")) {
            opponent.sendInfo(response);
            opponent.markArea(listener);
        }
        while (response.startsWith("AREA: ")){
            String [] coordinates = response.split(" ");
            ArrayList<String> stringMarkedArea = areaMarker.markArea(Integer.parseInt(coordinates[1]),Integer.parseInt(coordinates[2]));
            out.println("MARKED_AREA: " + stringMarkedArea);
            response = in.readLine();
            if(response.startsWith("FINAL_MARKED_AS_AREA: ")) {
                opponent.sendInfo(response);
                opponent.markArea(listener);
            }
        }
        if(response.equals("AREA_ACCEPTED")){
            opponent.sendInfo(response);
            markArea(listener);

        } else if (response.equals("AREA_NOT_ACCEPTED")){
            opponent.sendInfo(response);
            opponent.markArea(listener);
        }
        else if(response.equals("RESUME")){
            opponent.sendInfo(response);
            opponent.makeGameDecision(listener);
        }
    }



    /**
     * Receives login from the particular client and delegates it to the one of listeners of Human.
     */
    private void logIn() throws IOException {

        String response = in.readLine();
        login = new Login(response);

    }

    /**
     * Receives response from the particular client about type of opponent and delegates it to
     * the one of listeners of Human.
     */
    public void chooseOpponent() throws IOException {

        String response = in.readLine();

        if (response.equals("BOT")) {
            Bot bot = new Bot();
            Room room = new Room(bot);
            view.onCreateNewRoom(room);
            view.onJoinHumanToRoom(room.getIndex()-1, this);
            bot.sendMyLogin();
            makeGameDecision(room);

        } else if (response.equals("HUMAN")) {
            sendLists(view.getListOfIndexesToString());
            sendLists(view.getListOfInitiatorsLoginsToString());
            sendLists(view.getListOfJoinersLoginsToString());//sends the list of rooms to the particular human to show him it
            decideIfNewRoom();
        }

    }

    /**
     * Receives response from the particular client about whether he wants to play in new room or create new one
     * and delegates it to the one of listeners of Human.
     */
    public void decideIfNewRoom() throws IOException {

        String response = in.readLine();
        if (response.equals("NEW")) {
            Room room = new Room(this);
            view.onCreateNewRoom(room);
            setIndexOfRoom(room.getIndex());
            setPlayerColor(PlayerColor.WHITE);

        } else if (response.equals("EXISTING")) {
            chooseRoom();
        } else if(response.equals("REFRESH")){
            sendLists(view.getListOfIndexesToString());
            sendLists(view.getListOfInitiatorsLoginsToString());
            sendLists(view.getListOfJoinersLoginsToString());//sends the list of rooms to the particular human to show him it
            decideIfNewRoom();
        }

    }

    /**
     * Receives response from the particular client which involves information about existing room he chose
     * and delegates it to the one of listeners of Human.
     */
    public void chooseRoom() throws IOException {
        Room room = null;
        String response = in.readLine();
        if(response.equals("REFRESH")){
            sendLists(view.getListOfIndexesToString());
            sendLists(view.getListOfInitiatorsLoginsToString());
            sendLists(view.getListOfJoinersLoginsToString());//sends the list of rooms to the particular human to show him it
            chooseRoom();
        }
        else {
            int numberOfRoom = Integer.parseInt(response);
            try {
                room = view.onJoinHumanToRoom(numberOfRoom, this);
            } catch (IOException e) {
                opponent.sendInfo("OPPONENT_GAVE_UP");
                deleteRoom();
                disconnectPlayer();
            }

            opponent.sendMyLogin();
            try {
                makeGameDecision(room);
            } catch (IOException e) {
                opponent.sendInfo("OPPONENT_GAVE_UP");
                deleteRoom();
                disconnectPlayer();
            }
        }
    }

    /**
     * Sets the login.
     *
     * @param login
     */
    public void setLogin(Login login) {
        this.login = login;
    }

    /**
     * Gets the login.
     *
     * @return login.
     */
    public Login getLogin() {
        return login;
    }

    /**
     * Sets the index of room Human plays in.
     *
     * @param index of room.
     */
    public void setIndexOfRoom(int index) {
        indexOfRoom = index;
    }

    /**
     * Gets the index of room Human plays in.
     *
     * @return index of room.
     */
    public int getIndexOfRoom() {
        return indexOfRoom;
    }

    /**
     * Sets color of player's stones.
     *
     * @param playerColor - color of player's stones.
     */
    public void setPlayerColor(PlayerColor playerColor) {
        this.playerColor = playerColor;
    }

    /**
     * Gets color of player's stones.
     *
     * @return color of player's stones.
     */
    public PlayerColor getPlayerColor() {
        return playerColor;
    }

    /**
     * Sets opponent of the player.
     *
     * @param opponent - opponent of the player.
     */
    public void setOpponent(IPlayer opponent) {
        this.opponent = opponent;
    }

    /**
     * Gets opponent of the player.
     *
     * @return opponent of the player.
     */
    public IPlayer getOpponent() {
        return opponent;
    }

    public void setIfOpponentPassed(boolean ifPassed){
        ifOpponentPassed = ifPassed;
    }

    /**
     * Sets areaMarker.
     * @param areaMarker
     */
    public void setAreaMarker(AreaMarker areaMarker) { this.areaMarker = areaMarker; }

    /**
     * Sends login.
     */
    public void sendMyLogin(){

        out.println("LOGIN: " + opponent.getLogin().toString());
    }

    /**
     * Sends list of rooms to the particular client
     *
     * @param rooms - list of rooms
     */
    public void sendLists(List<String> rooms) {
        out.println(rooms);
    }

    /**
     * Sends table of updated fields on the board.
     *
     * @param updatedBoard - updated board.
     */
    public void sendUpdatedBoard(ArrayList<String> updatedBoard) {
        out.println("UPDATED_BOARD " + updatedBoard);
    }

    /**
     * Sends message to player when his opponent has just passed.
     */
    public void sendInfoOpponentPassed() {
        ifOpponentPassed = true;
        out.println("OPPONENT_PASSED");
    }


    /**
     * Sends info that player made legal move.
     */
    public void sendInfoLegalMove() {
        opponent.setIfOpponentPassed(false);
        out.println("LEGAL_MOVE"); }

    /**
     * Sends info that player made illegal move - KO.
     */
    public void sendInfoIllegalMoveKO() { out.println("KO"); }

    /**
     * Sends info that player made illegal move - Suicide.
     */
    public void sendInfoIllegalMoveSuicide() { out.println("SUICIDE"); }

    /**
     * Sends info that player made illegal move - Occupied Field.
     */
    public void sendInfoIllegalMoveOccupiedField() { out.println("OCCUPIED_FIELD"); }

    /**
     * Sends info about captures of players.
     * @param capturedForWhite
     * @param capturedForBlack
     */
    public void sendInfoCapturedStones(String capturedForWhite, String capturedForBlack){
        out.println("CAPTURED " + capturedForWhite + "," + capturedForBlack);
    }

    /**
     * Sends info to the player to wait for opponent to mark dead stones.
     */
    private void sendInfoWaitForOpponentToMarkDeadStones(){
        out.println("WAIT_FOR_MARKING_DEAD");
    }

    /**
     * Sends info to the player to mark dead stones.
     */
    public void sendInfoMarkDeadStones(){
        out.println("MARK_DEAD");
    }

    /**
     * Send info to the client.
     * @param info
     */
    public void sendInfo(String info){
        out.println(info);
    }

    /**
     * Closes the socket.
     */
    public void disconnectPlayer() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteRoom(){
        view.onDeleteRoom(indexOfRoom);
    }
}