package nl.svenar.powerranks.test.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import nl.svenar.powerranks.api.PowerRanksAPI;
import nl.svenar.powerranks.common.structure.PRPermission;
import nl.svenar.powerranks.common.structure.PRPlayer;
import nl.svenar.powerranks.common.structure.PRRank;
import nl.svenar.powerranks.common.utils.PRCache;
import nl.svenar.powerranks.test.util.TestDebugger;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestPlayersAPI {

    private PRRank rank1;
    
    private PRRank rank2;

    private PRPlayer player1;

    private PowerRanksAPI api;

    @Before
    public void setupCache() {
        TestDebugger.log(this, "===== Setting up PRCache =====");
        PRCache.reset();

        rank1 = PRCache.createRank("rank1");
        rank1.setWeight(10);
        rank1.setPrefix("prefix1");
        rank1.setSuffix("suffix1");

        rank2 = PRCache.createRank("rank2");
        rank2.setWeight(20);
        rank2.setPrefix("prefix2");
        rank2.setSuffix("suffix2");

        player1 = PRCache.createPlayer("player1", UUID.randomUUID());

        api = new PowerRanksAPI();
    }

    @After
    public void cleanupCache() {
        TestDebugger.log(this, "===== Cleaning up PRCache =====");
        PRCache.reset();
    }

    @Test
    public void aTestRanks() {
        TestDebugger.log(this, "[aTestRanks] Setup...");

        assertEquals(0, api.getPlayersAPI().getRanks(player1).size());

        api.getPlayersAPI().addRank(player1, rank1);
        api.getPlayersAPI().addRank(player1, rank1);
        api.getPlayersAPI().addRank(player1, rank2);
        assertEquals(2, api.getPlayersAPI().getRanks(player1).size());

        api.getPlayersAPI().removeRank(player1, rank1);
        api.getPlayersAPI().removeRank(player1, rank1);
        assertEquals(1, api.getPlayersAPI().getRanks(player1).size());

        TestDebugger.log(this, "[aTestRanks] OK");
    }

    @Test
    public void bTestPermissions() {
        TestDebugger.log(this, "[bTestPermissions] Setup...");

        assertEquals(0, api.getPlayersAPI().getPermissions(player1).size());

        api.getPlayersAPI().addPermission(player1, new PRPermission("test.permission.1", true));
        api.getPlayersAPI().addPermission(player1, new PRPermission("test.permission.2", false));
        assertEquals(2, api.getPlayersAPI().getPermissions(player1).size());

        assertTrue(api.getPlayersAPI().hasPermission(player1, "test.permission.1"));
        assertTrue(api.getPlayersAPI().hasPermission(player1, "test.permission.2"));
        assertTrue(api.getPlayersAPI().isPermissionAllowed(player1, "test.permission.1"));
        assertTrue(!api.getPlayersAPI().isPermissionAllowed(player1, "test.permission.2"));

        api.getPlayersAPI().removePermission(player1, new PRPermission("test.permission.1", true));
        api.getPlayersAPI().removePermission(player1, new PRPermission("test.permission.1", true));
        assertEquals(1, api.getPlayersAPI().getPermissions(player1).size());

        assertTrue(!api.getPlayersAPI().hasPermission(player1, "test.permission.1"));
        assertTrue(api.getPlayersAPI().hasPermission(player1, "test.permission.2"));
        assertTrue(!api.getPlayersAPI().isPermissionAllowed(player1, "test.permission.1"));
        assertTrue(!api.getPlayersAPI().isPermissionAllowed(player1, "test.permission.2"));


        TestDebugger.log(this, "[bTestPermissions] OK");
    }

    @Test
    public void cTestPlaytime() {
        TestDebugger.log(this, "[cTestPlaytime] Setup...");

        assertEquals(0, api.getPlayersAPI().getPlaytime(player1));

        api.getPlayersAPI().setPlaytime(player1, 100);

        assertEquals(100, api.getPlayersAPI().getPlaytime(player1));

        TestDebugger.log(this, "[cTestPlaytime] OK");
    }

    @Test
    public void dTestUsertags() {
        TestDebugger.log(this, "[dTestUsertags] Setup...");

        assertEquals(0, api.getPlayersAPI().getUsertags(player1).size());

        api.getPlayersAPI().addUsertag(player1, "usertag1");
        api.getPlayersAPI().addUsertag(player1, "usertag2");
        assertEquals(2, api.getPlayersAPI().getUsertags(player1).size());

        api.getPlayersAPI().removeUsertag(player1, "usertag1");
        api.getPlayersAPI().removeUsertag(player1, "usertag1");
        assertEquals(1, api.getPlayersAPI().getUsertags(player1).size());

        TestDebugger.log(this, "[dTestUsertags] OK");
    }

}
