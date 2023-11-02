package nl.svenar.powerranks.test.tests;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import nl.svenar.powerranks.api.PowerRanksAPI;
import nl.svenar.powerranks.common.structure.PRPermission;
import nl.svenar.powerranks.common.structure.PRRank;
import nl.svenar.powerranks.common.utils.PRCache;
import nl.svenar.powerranks.test.util.TestDebugger;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestRanksAPI {

    private PRRank rank1;

    private PRRank rank2;

    private PRRank rank3;
    
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

        rank3 = PRCache.createRank("rank3");
        rank3.setWeight(30);
        rank3.setPrefix("prefix3");
        rank3.setSuffix("suffix3");

        api = new PowerRanksAPI();
    }

    @After
    public void cleanupCache() {
        TestDebugger.log(this, "===== Cleaning up PRCache =====");
        PRCache.reset();
    }

    @Test
    public void aTestGetRank() {
        TestDebugger.log(this, "[aTestGetRank] Setup...");

        assertEquals(rank1, api.getRanksAPI().get("rank1"));
        assertEquals(rank2, api.getRanksAPI().get("rank2"));
        assertEquals(rank3, api.getRanksAPI().get("rank3"));

        TestDebugger.log(this, "[aTestGetRank] OK");
    }

    @Test
    public void bTestPrefix() {
        TestDebugger.log(this, "[bTestPrefix] Setup...");

        assertEquals(rank1.getPrefix(), api.getRanksAPI().getPrefix(rank1));

        api.getRanksAPI().setPrefix(rank1, "newprefix1");
        assertEquals("newprefix1", api.getRanksAPI().getPrefix(rank1));

        TestDebugger.log(this, "[bTestPrefix] OK");
    }

    @Test
    public void cTestSuffix() {
        TestDebugger.log(this, "[cTestSuffix] Setup...");

        assertEquals(rank1.getSuffix(), api.getRanksAPI().getSuffix(rank1));

        api.getRanksAPI().setSuffix(rank1, "newprefix1");
        assertEquals("newprefix1", api.getRanksAPI().getSuffix(rank1));

        TestDebugger.log(this, "[cTestSuffix] OK");
    }

    @Test
    public void dTestPermissions() {
        TestDebugger.log(this, "[dTestPermissions] Setup...");

        assertEquals(0, api.getRanksAPI().getPermissions(rank1).size());

        PRPermission permission1 = new PRPermission("permission.1", true);
        PRPermission permission2 = new PRPermission("permission.2", false);
        api.getRanksAPI().addPermission(rank1, permission1);
        api.getRanksAPI().addPermission(rank1, permission2);
        assertEquals(2, api.getRanksAPI().getPermissions(rank1).size());

        api.getRanksAPI().removePermission(rank1, permission1.getName());
        assertEquals(1, api.getRanksAPI().getPermissions(rank1).size());

        TestDebugger.log(this, "[dTestPermissions] OK");
    }

    @Test
    public void eNameColor() {
        TestDebugger.log(this, "[eNameColor] Setup...");

        assertEquals(rank1.getNamecolor(), api.getRanksAPI().getNameColor(rank1));

        api.getRanksAPI().setNameColor(rank1, "&a");
        assertEquals("&a", api.getRanksAPI().getNameColor(rank1));

        TestDebugger.log(this, "[eNameColor] OK");
    }

    @Test
    public void fChatColor() {
        TestDebugger.log(this, "[fChatColor] Setup...");

        assertEquals(rank1.getNamecolor(), api.getRanksAPI().getChatColor(rank1));

        api.getRanksAPI().setChatColor(rank1, "&a");
        assertEquals("&a", api.getRanksAPI().getChatColor(rank1));

        TestDebugger.log(this, "[fChatColor] OK");
    }

    @Test
    public void gInheritances() {
        TestDebugger.log(this, "[gInheritances] Setup...");

        assertEquals(0, api.getRanksAPI().getInheritances(rank1).size());

        api.getRanksAPI().addInheritance(rank1, rank2);
        api.getRanksAPI().addInheritance(rank1, rank3);
        assertEquals(2, api.getRanksAPI().getInheritances(rank1).size());

        api.getRanksAPI().removeInheritance(rank1, rank2);
        assertEquals(1, api.getRanksAPI().getInheritances(rank1).size());

        TestDebugger.log(this, "[gInheritances] OK");

    }
}
