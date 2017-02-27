package GameTests;

import Game.Bot;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class BotTest {

    Bot bot;

    @Before
    public void setUp() throws Exception {
        bot = new Bot();
    }

    @Test
    public void getLogin() throws Exception {
        String login = bot.getLogin().toString();
        Assert.assertNotNull(login);
        Assert.assertEquals("BOT",login);
    }
}