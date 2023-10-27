package nl.svenar.powerranks.nukkit.events;

import java.util.Date;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import nl.svenar.powerranks.common.utils.PRCache;
import nl.svenar.powerranks.common.utils.PRUtil;
import nl.svenar.powerranks.nukkit.PowerRanks;
import nl.svenar.powerranks.nukkit.permissible.PermissibleInjector;

public class OnJoin implements Listener {
    
    private PowerRanks plugin;

    public OnJoin(PowerRanks plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        
        if (PRCache.getPlayer(player.getUniqueId().toString()) == null) {
            PRCache.createPlayer(player.getName(), player.getUniqueId());
        }

        long time = new Date().getTime();
		PRUtil.addPlayerPlaytimeCache(player.getUniqueId(), time);

        PermissibleInjector.inject(plugin, player);
    }
}
