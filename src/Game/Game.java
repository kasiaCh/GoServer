package Game;

import Listeners.IGameView;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

/**
 * Class which coordinates the game. Observes initial actions of human.
 */
public class Game implements IGameView {
    /**
     * List of rooms.
     */
    private ArrayList<Room> rooms;

    public Game() {
        rooms = new ArrayList<Room>();

    }

    /**
     * Engages start of the game for each client. Makes socket on a specific port, accepts clients, starts their threads,
     * then calls methods that take care of starting game.
     * @throws IOException
     */
    public void startGame() throws IOException {

        InetAddress addr = InetAddress.getByName("192.168.1.190");
        ServerSocket listener = new ServerSocket(8901);

        try {
            while (true) {
                Human player1 = new Human(listener.accept());
                player1.attachView(this);
                player1.start();
            }

        } finally {
            listener.close();
        }
    }


    /**
     * Method which is called when new room has been created. New room is added to the ArrayList of rooms.
     * @param room New room
     */
    @Override
    public synchronized void onCreateNewRoom(Room room)  {
        boolean ifNull = false;
        for(Room r : rooms){
            if(r==null){
                ifNull = true;
                int index = rooms.indexOf(r);
                rooms.add(index, room);
                room.setIndex(index);
                break;
            }

        }
        if(!ifNull) {
            rooms.add(room);
            room.setIndex(rooms.indexOf(room));
        }
    }

    /**
     * Method which is called when player joined to room. Sets specified parameters to the player and room.
     * @param numberOfRoom number of room player joined.
     * @param human human who joined the room
     * @return chosen room.
     * @throws IOException
     */
    @Override
    public synchronized Room onJoinHumanToRoom(int numberOfRoom, Human human) throws IOException {
        Room chosenRoom = rooms.get(numberOfRoom);
        chosenRoom.setJoiner(human);
        human.setIndexOfRoom(chosenRoom.getIndex());
        human.setPlayerColor(PlayerColor.BLACK);
        human.setOpponent(chosenRoom.getInitiator());
        chosenRoom.getInitiator().setOpponent(human);
        return chosenRoom;
    }

    /**
     * Parse the list of indexes to String.
     * @return parsed list of indexes.
     */
    @Override
    public synchronized List<String> getListOfIndexesToString(){
        List<String> stringListOfIndexes = new ArrayList<>();
        for (Room room : rooms){
            if(room!=null) {
                stringListOfIndexes.add(Integer.toString(room.getIndex()));
            }
        }

        return stringListOfIndexes;
    }

    /**
     * Parse the list of initiators logins to String.
     * @return parsed list of initiators' logins.
     */
    @Override
    public synchronized List<String> getListOfInitiatorsLoginsToString(){
        List<String> stringListOfPlayer1Logins = new ArrayList<>();
        for (Room room : rooms){
            if(room!=null)
                stringListOfPlayer1Logins.add(room.getInitiator().getLogin().toString());
        }

        return stringListOfPlayer1Logins;
    }

    /**
     * Parse the list of joiners logins to String.
     * @return parsed list of joiners' logins.
     */
    @Override
    public synchronized List<String> getListOfJoinersLoginsToString(){
        List<String> stringListOfPlayer2Logins = new ArrayList<>();
        for (Room room : rooms){
            if(room!=null) {
                if (room.getJoiner() != null)
                    stringListOfPlayer2Logins.add(room.getJoiner().getLogin().toString());
                else
                    stringListOfPlayer2Logins.add(" ");
            }
        }

        return stringListOfPlayer2Logins;
    }

    /**
     * Method which is called when room is deleted. Removes the room from the ArrayList of rooms.
     * @param indexOfRoom index of deleted room
     */
    @Override
    public void onDeleteRoom(int indexOfRoom) {
        rooms.remove(indexOfRoom-1);
        rooms.add(indexOfRoom-1, null);

    }
}