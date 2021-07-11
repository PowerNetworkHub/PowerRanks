package nl.svenar.powerranks.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class BukkitUtils {
    
    public static Player getPlayerByName(String target_player_name) {
		Player target_player = null;
		for (Player online_player : Bukkit.getOnlinePlayers()) {
			if (online_player.getName().equalsIgnoreCase(target_player_name)) {
				target_player = online_player;
				break;
			}
		}
		return target_player;
	}
}
