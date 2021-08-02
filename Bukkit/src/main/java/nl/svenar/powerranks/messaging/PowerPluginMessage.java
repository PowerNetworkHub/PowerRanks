package nl.svenar.powerranks.messaging;

import java.util.ArrayList;

import org.bukkit.scheduler.BukkitRunnable;

import nl.svenar.powerranks.PowerRanks;
import nl.svenar.powerranks.utils.ErrorManager;

public class PowerPluginMessage {

    private static PowerRanks plugin;
    public static String bungeeServername = "";

    public static ArrayList<String> MESSAGE_HEADER_GET_SERVER = new ArrayList<>();
    public static ArrayList<String> MESSAGE_HEADER_FORWARD_ALL = new ArrayList<>();

    public enum POWERRANKS_MESSAGE_TYPE {
        HEARTHBEAT("hearthbeat");

        private String name;

        private POWERRANKS_MESSAGE_TYPE(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name + "-" + bungeeServername;
        }
    }

    public static void setup(PowerRanks plugin) {
        PowerPluginMessage.plugin = plugin;

        MESSAGE_HEADER_GET_SERVER.add("GetServer");

        MESSAGE_HEADER_FORWARD_ALL.add("Forward");
        MESSAGE_HEADER_FORWARD_ALL.add("ALL");

        PowerRanks.getPluginMessageHandler().sendOutgoingMessage(MESSAGE_HEADER_GET_SERVER, "", true);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (bungeeServername.length() > 0) {
                    PowerRanks.getPluginMessageHandler().sendOutgoingMessage(MESSAGE_HEADER_FORWARD_ALL, POWERRANKS_MESSAGE_TYPE.HEARTHBEAT.getName(), false);
                }
            }
        }.runTaskTimer(plugin, 1L, 100L);
    }

    public static void handleIncommingMessage(String subchannel, String msg) {
        if (subchannel.equals(PluginMessageHandler.MESSAGE_SUBCHANNEL)) {

        } else if (subchannel.equals("GetServer")) {
            bungeeServername = msg;
        }
        ErrorManager.logWarning("handleIncommingMessage\n\n" + subchannel + "\n" + msg);
    }
    
}