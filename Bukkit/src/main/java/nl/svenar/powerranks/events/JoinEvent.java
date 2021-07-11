package nl.svenar.powerranks.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import nl.svenar.powerranks.PowerRanks;
import nl.svenar.powerranks.data.PRPlayer;
import nl.svenar.powerranks.handlers.BaseDataHandler;

public class JoinEvent implements Listener {

    public JoinEvent(PowerRanks plugin) {
    }
    
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        onJoin(player);
    }

    public static void onJoin(Player player) {
        PRPlayer prPlayer = BaseDataHandler.getPlayer(player.getUniqueId());
        if (prPlayer == null) {
            prPlayer = new PRPlayer(player);
            BaseDataHandler.addPlayer(prPlayer);
        } else {
            prPlayer.setPlayer(player);
            prPlayer.setName(player.getName());
        }
        prPlayer.injectPermissible();
    }
}
