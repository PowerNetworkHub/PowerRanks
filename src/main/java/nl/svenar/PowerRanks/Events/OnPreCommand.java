package nl.svenar.PowerRanks.Events;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;

import nl.svenar.PowerRanks.PowerRanks;
// import nl.svenar.PowerRanks.Cache.CachedConfig;

public class OnPreCommand implements Listener {
    
    private PowerRanks plugin;

    public OnPreCommand(PowerRanks plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onServerCommand(ServerCommandEvent event) {
        if (this.handleCommand(event.getCommand())) {
            event.getSender().sendMessage(plugin.plp + ChatColor.RED + "This command is disabled");
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        String[] args = (event.getMessage().startsWith("/") ? event.getMessage().replaceFirst("/", "") : event.getMessage()).split(" ");
        String command = args[0];

        if (this.handleCommand(command)) {
            event.getPlayer().sendMessage(plugin.plp + ChatColor.RED + "This command is disabled");
            event.setCancelled(true);
        }
    }

    private boolean handleCommand(String command) {
        if (PowerRanks.getConfigManager().getBool("general.disable-op", false) && (command.equalsIgnoreCase("op") || command.equalsIgnoreCase("deop"))) {
            return true;
        }

        return false;
    }
}
