package nl.svenar.powerranks.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import nl.svenar.powerranks.PowerRanks;
import nl.svenar.powerranks.data.PRPlayer;
import nl.svenar.powerranks.handlers.BaseDataHandler;

public class LeaveEvent implements Listener {

    public LeaveEvent(PowerRanks plugin) {
    }
    
    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        onLeave(player);
    }

    public static void onLeave(Player player) {
        PRPlayer prPlayer = BaseDataHandler.getPlayer(player.getUniqueId());
        if (prPlayer == null) {
            prPlayer = new PRPlayer();
            prPlayer.setName(player.getName());
            prPlayer.setUuid(player.getUniqueId());
            BaseDataHandler.addPlayer(prPlayer);
        } else {
            prPlayer.setPlayer(null);
        }
    }
}
