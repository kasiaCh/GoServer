package GameTests;

import Game.Game;
import Game.Human;
import Game.PlayerColor;
import Game.Room;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Iterator;


@RunWith(MockitoJUnitRunner.class)
public class GameTest {

    @Mock
    private ArrayList<Room> rooms = new ArrayList<Room>()
    {{
        add (new Room());
        add (new Room());
    }};

    @Mock
    private Room room;

    @Mock
    private Room room1;

    @Mock
    private Room room2;

    @Mock
    Human joiner;

    @Mock
    Human initiator;

    @Mock
    private Iterator<Room> iterator;


    @InjectMocks
    Game game = new Game();


    @Before
    public void setUp(){
        Mockito.when(iterator.hasNext()).thenReturn(true, true, false);
        Mockito.when(iterator.next()).thenReturn(room1)
                .thenReturn(room2);

        Mockito.when(rooms.iterator()).thenReturn(iterator);
    }

    @Test
    public void startGame() throws Exception {

    }

    @Test
    public void testOnCreateNewRoom() throws Exception {

        ArgumentCaptor<Room> captor = ArgumentCaptor.forClass(Room.class);
        game.onCreateNewRoom(room);
        Mockito.verify(rooms).add(captor.capture());
        Assert.assertEquals(room, captor.getValue());

    }

    @Test
    public void onJoinHumanToRoom() throws Exception {

        ArgumentCaptor<Integer> captor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<PlayerColor> captor1 = ArgumentCaptor.forClass(PlayerColor.class);
        ArgumentCaptor<Human> captor3 = ArgumentCaptor.forClass(Human.class);

        Mockito.when(rooms.get(2)).thenReturn(room);
        Mockito.when(room.getInitiator()).thenReturn(initiator);
        Mockito.when(room.getIndex()).thenReturn(2);
        game.onJoinHumanToRoom(2, joiner);

        Mockito.verify(joiner).setIndexOfRoom(captor.capture());
        Assert.assertEquals(Integer.valueOf(2), captor.getValue());
        Mockito.verify(joiner).setPlayerColor(captor1.capture());
        Assert.assertEquals(PlayerColor.BLACK, captor1.getValue());
        Mockito.verify(joiner).setOpponent(captor3.capture());
        Assert.assertEquals(initiator, captor3.getValue());

    }


    @Test
    public void onDeleteRoom() throws Exception {
        ArgumentCaptor<Integer> captor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Room> captor1 = ArgumentCaptor.forClass(Room.class);
        game.onDeleteRoom(2);
        Mockito.verify(rooms).remove((int)captor.capture());
        Assert.assertEquals(Integer.valueOf(1), captor.getValue());
        Mockito.verify(rooms).add((int)captor.capture(), captor1.capture());
        Assert.assertEquals(Integer.valueOf(1), captor.getValue());
        Assert.assertEquals(null, captor1.getValue());
    }

}