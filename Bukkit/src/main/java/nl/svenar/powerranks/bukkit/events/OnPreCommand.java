package nl.svenar.powerranks.bukkit.events;

import java.util.HashMap;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;

import nl.svenar.powerranks.bukkit.PowerRanks;
import nl.svenar.powerranks.common.utils.PRUtil;

public class OnPreCommand implements Listener {

    private PowerRanks plugin;

    public OnPreCommand(PowerRanks plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onServerCommand(ServerCommandEvent event) {
        if (this.handleCommand(event.getCommand())) {
            String langPrefix = plugin.getLanguageManager().getFormattedMessage("command-disabled");
            String langLine = plugin.getLanguageManager().getFormattedMessage("command-disabled");
            String output = PRUtil.powerFormatter(langPrefix + " " + langLine, new HashMap<>(), '[', ']');
            event.getSender().sendMessage(output);
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        if (this.handleCommand(removeDash(event.getMessage()))) {
            String langPrefix = plugin.getLanguageManager().getFormattedMessage("command-disabled");
            String langLine = plugin.getLanguageManager().getFormattedMessage("command-disabled");
            String output = PRUtil.powerFormatter(langPrefix + " " + langLine, new HashMap<>(), '[', ']');
            event.getPlayer().sendMessage(output);
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
