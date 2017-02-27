package Game;

public class Turn {

    private int x;
    private int y;
    private PlayerColor playerColor;

    public Turn (int x, int y, PlayerColor playerColor){
        this.x = x;
        this.y = y;
        this.playerColor = playerColor;
    }

    public PlayerColor getPlayerColor(){
        return playerColor;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }
}
