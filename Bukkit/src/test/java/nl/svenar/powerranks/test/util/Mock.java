package nl.svenar.powerranks.test.util;

import static org.junit.Assert.assertNotNull;

import org.bukkit.entity.Player;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.UnimplementedOperationException;
import nl.svenar.powerranks.bukkit.PowerRanks;

public class Mock {
    
    private static ServerMock serverMock;
    
    private static PowerRanks plugin;

    public static ServerMock getServerMock() {
        return serverMock;
    }

    public static void init() {
        if (serverMock != null) {
            return;
        }
        
        serverMock = MockBukkit.mock();
        plugin = MockBukkit.load(PowerRanks.class);
    }

    public static void unmock() {
        if (serverMock == null) {
            return;
        }
        
        // MockBukkit.unmock();
        // serverMock = null;
        // plugin = null;
    }

    public static Player getPlayer(int num) {
        if (serverMock == null) {
            return null;
        }

        Player player = null;
        try {
            player = serverMock.getPlayer(num);
        } catch (UnimplementedOperationException e) {
            e.printStackTrace();
        }
        assertNotNull(player);
        return player;
    }

    public static PowerRanks getPlugin() {
        return plugin;
    }
}
