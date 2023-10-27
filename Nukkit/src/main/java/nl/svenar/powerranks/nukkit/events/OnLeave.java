package nl.svenar.powerranks.nukkit.events;

import java.util.Date;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerQuitEvent;
import nl.svenar.powerranks.common.structure.PRPlayer;
import nl.svenar.powerranks.common.utils.PRCache;
import nl.svenar.powerranks.common.utils.PRUtil;
import nl.svenar.powerranks.nukkit.PowerRanks;

public class OnLeave implements Listener {
    

    public OnLeave(PowerRanks plugin) {
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        
        if (PRCache.getPlayer(player.getUniqueId().toString()) == null) {
            PRCache.createPlayer(player.getName(), player.getUniqueId());
        }

        PRPlayer prPlayer = PRCache.getPlayer(player.getUniqueId().toString());

        long leave_time = new Date().getTime();
		long join_time = leave_time;
		try {
			join_time = PRUtil.getPlayerPlaytimeCache(player.getUniqueId());
		} catch (Exception e1) {
		}

		prPlayer.setPlaytime(prPlayer.getPlaytime() + (leave_time - join_time) / 1000);
    }
}
