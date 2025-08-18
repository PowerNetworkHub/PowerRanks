package nl.svenar.powerranks.test.tests;

import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.bukkit.entity.Player;

import org.mockbukkit.mockbukkit.ServerMock;
import nl.svenar.powerranks.bukkit.cache.CacheManager;
import nl.svenar.powerranks.common.structure.PRPlayer;
import nl.svenar.powerranks.test.util.Assert;
import nl.svenar.powerranks.test.util.Mock;
import nl.svenar.powerranks.test.util.TestDebugger;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@TestInstance(Lifecycle.PER_CLASS)
public class TestRankRenameIntegrity {

    private final int numPlayers = 20;
    private ServerMock server;

    @BeforeAll
    public void setUp() {
        Mock.init();
        server = Mock.getServerMock();
        server.setPlayers(numPlayers);
    }

    @AfterAll
    public void tearDown() {
        Mock.unmock();
    }

    @Test
    public void A_renameChild_keepsInheritanceLinks() {
        TestDebugger.log(this, "");
        TestDebugger.log(this, "[A_renameChild_keepsInheritanceLinks] Start");

        Player admin = Mock.getPlayer(0);
        Player target = Mock.getPlayer(15);
        PRPlayer prTarget = CacheManager.getPlayer(target.getUniqueId().toString());

        admin.setOp(true);
        target.setOp(false);

        server.execute("pr", admin, "createrank", "ParentR");
        server.execute("pr", admin, "createrank", "ChildR");
        server.execute("pr", admin, "addinheritance", "ParentR", "ChildR"); // ParentR inherits from ChildR
        server.execute("pr", admin, "addperm", "ChildR", "test.rename.child");
        server.execute("pr", admin, "setrank", target.getName(), "ParentR");

        prTarget.updatePermissionsFromRanks();
        Assert.assertTrue("Has target player permission 'test.rename.child'.", prTarget.isPermissionAllowed("test.rename.child", true));

        // Rename child and ensure inheritance still effective
        server.execute("pr", admin, "renamerank", "ChildR", "ChildR_New");
        prTarget.updatePermissionsFromRanks();

        // Add a new perm to the renamed child; parent should still see it via inheritance
        server.execute("pr", admin, "addperm", "ChildR_New", "test.rename.child.new");
        prTarget.updatePermissionsFromRanks();
        Assert.assertTrue("Has target player permission 'test.rename.child.new'.", prTarget.isPermissionAllowed("test.rename.child.new", true));

        TestDebugger.log(this, "[A_renameChild_keepsInheritanceLinks] OK");
    }

    @Test
    public void B_renameAssignedRank_updatesPlayerReference() {
        TestDebugger.log(this, "");
        TestDebugger.log(this, "[B_renameAssignedRank_updatesPlayerReference] Start");

        Player admin = Mock.getPlayer(0);
        Player target = Mock.getPlayer(16);
        PRPlayer prTarget = CacheManager.getPlayer(target.getUniqueId().toString());

        admin.setOp(true);
        target.setOp(false);

        server.execute("pr", admin, "createrank", "ToBeRenamed");
        server.execute("pr", admin, "setrank", target.getName(), "ToBeRenamed");
        prTarget.updatePermissionsFromRanks();
        assertTrue(prTarget.getRanks().stream().anyMatch(r -> r.getName().equals("ToBeRenamed")));

        server.execute("pr", admin, "renamerank", "ToBeRenamed", "RenamedFinal");
        prTarget.updatePermissionsFromRanks();
        assertFalse(prTarget.getRanks().stream().anyMatch(r -> r.getName().equals("ToBeRenamed")));
        assertTrue(prTarget.getRanks().stream().anyMatch(r -> r.getName().equals("RenamedFinal")));

        TestDebugger.log(this, "[B_renameAssignedRank_updatesPlayerReference] OK");
    }
}
