package nl.svenar.PowerRanks.Events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import net.md_5.bungee.api.ChatColor;
import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Cache.CachedConfig;

public class OnPreCommand implements Listener {
    
    private PowerRanks plugin;

    public OnPreCommand(PowerRanks plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        String[] args = (event.getMessage().startsWith("/") ? event.getMessage().replaceFirst("/", "") : event.getMessage()).split(" ");
        String command = args[0];
        if (CachedConfig.getBoolean("general.disable-op") && (command.equalsIgnoreCase("op") || command.equalsIgnoreCase("deop"))) {
            event.getPlayer().sendMessage(plugin.plp + ChatColor.RED + "This command is disabled");
            event.setCancelled(true);
        }
        // event.getPlayer().sendMessage("" + args.length);
        // event.getPlayer().sendMessage(args[0]);
    }
}
