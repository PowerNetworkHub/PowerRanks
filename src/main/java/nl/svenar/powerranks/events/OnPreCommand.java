package nl.svenar.powerranks.events;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;

import nl.svenar.powerranks.PowerRanks;

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
        if (this.handleCommand(removeDash(event.getMessage()))) {
            event.getPlayer().sendMessage(plugin.plp + ChatColor.RED + "This command is disabled");
            event.setCancelled(true);
        }
    }

    private boolean handleCommand(String command) {
        if (PowerRanks.getConfigManager().getBool("general.disable-op", false)
                && (command.equalsIgnoreCase("op") || command.equalsIgnoreCase("deop"))) {
            return true;
        }

        return false;
    }

    private static String removeDash(String input) {
        if (input.charAt(0) == '/') {
            return input.substring(1);
        }
        return input;
    }
}
