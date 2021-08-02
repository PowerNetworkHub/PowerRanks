package nl.svenar.powerranks.messaging;

import java.util.ArrayList;
import java.util.Collection;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.google.common.collect.Iterables;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;

import nl.svenar.powerranks.PowerRanks;

public class PluginMessageHandler implements PluginMessageListener {

    private static final String MESSAGE_CHANNEL = "BungeeCord";
    public static final String MESSAGE_SUBCHANNEL = "PowerRanks";    

    private PowerRanks plugin;

    public PluginMessageHandler(PowerRanks plugin) {
        this.plugin = plugin;
    }

    public void setup() {
        this.plugin.getServer().getMessenger().registerOutgoingPluginChannel(this.plugin, MESSAGE_CHANNEL);
        this.plugin.getServer().getMessenger().registerIncomingPluginChannel(this.plugin, MESSAGE_CHANNEL, this);

        PowerPluginMessage.setup(this.plugin);
    }

    public void close() {
        this.plugin.getServer().getMessenger().unregisterOutgoingPluginChannel(this.plugin);
        this.plugin.getServer().getMessenger().unregisterIncomingPluginChannel(this.plugin);
    }

    public void sendOutgoingMessage(ArrayList<String> messageHeader, String message, boolean retryOnFail) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        // out.writeUTF("Forward"); // So BungeeCord knows to forward it
        // out.writeUTF("ALL");
        for (String header : messageHeader) {
            out.writeUTF(header);
        }
        out.writeUTF(MESSAGE_SUBCHANNEL);
        if (message.length() > 0) {
            out.writeUTF(message);
        }
        byte[] data = out.toByteArray();

        System.out.println("OUT: " + MESSAGE_CHANNEL + " - " + "Forward" + " > " + "ALL" + " > " + MESSAGE_SUBCHANNEL + " > " + message);

        if (retryOnFail) {
            // Loop (every 100 ticks | 5s) until it can be send (at least 1 player online in
            // the server)
            new BukkitRunnable() {
                @Override
                public void run() {
                    Collection<? extends Player> players = plugin.getServer().getOnlinePlayers();
                    Player player = Iterables.getFirst(players, null);
                    if (player == null) {
                        return;
                    }

                    player.sendPluginMessage(plugin, MESSAGE_CHANNEL, data);
                    cancel();
                }
            }.runTaskTimer(this.plugin, 1L, 100L);
        } else {
            Collection<? extends Player> players = plugin.getServer().getOnlinePlayers();
            Player player = Iterables.getFirst(players, null);
            if (player == null) {
                return;
            }

            player.sendPluginMessage(plugin, MESSAGE_CHANNEL, data);
        }
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals(MESSAGE_CHANNEL)) {
            return;
        }

        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();
        String msg = in.readUTF();

        // if (!subchannel.equals(MESSAGE_SUBCHANNEL)) {
        //     return;
        // }

        // System.out.println(channel + " - " + msg);

        PowerPluginMessage.handleIncommingMessage(subchannel, msg);
    }
}
