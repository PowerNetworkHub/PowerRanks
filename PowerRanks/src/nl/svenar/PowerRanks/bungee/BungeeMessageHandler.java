package nl.svenar.PowerRanks.bungee;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import nl.svenar.PowerRanks.PowerRanks;

public class BungeeMessageHandler implements PluginMessageListener {

	private PowerRanks plugin;
	private String channel_name = "BungeeCord";
	private String subchannel_name = "PowerRanks";

	public BungeeMessageHandler(PowerRanks plugin) {
		this.plugin = plugin;
		this.plugin.getServer().getMessenger().registerOutgoingPluginChannel(this.plugin, channel_name);
		this.plugin.getServer().getMessenger().registerIncomingPluginChannel(this.plugin, channel_name, this);
	}

	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		if (!channel.equals(channel_name)) {
			return;
		}
		ByteArrayDataInput in = ByteStreams.newDataInput(message);
		String subchannel = in.readUTF();
		ArrayList<String> data_in = new ArrayList<String>();
		if (subchannel.equals(subchannel_name)) {
			int index = 0;
			while (index < 64) {
				try {
					String read = in.readUTF();
					if (read.contains("\n")) {
						for (String line : read.split("\n")) {
							data_in.add(line.replaceAll("[^a-zA-Z0-9-!@#$%^&*_=+:;,]", ""));
						}
					} else {
						data_in.add(read.replaceAll("[^a-zA-Z0-9-!@#$%^&*_=+:;,]", ""));
					}
				} catch (Exception e) {
					break;
				}
				index++;
			}
			this.plugin.power_bungee_events.onDataReceive(data_in);
		}

	}

	public void change_server(Player player, String server) {
		ByteArrayDataOutput output = ByteStreams.newDataOutput();
		output.writeUTF("Connect");
		output.writeUTF(server);

		player.sendPluginMessage(plugin, channel_name, output.toByteArray());
	}

	public void sendRaw(Player player, String[] args) throws IOException {
		ByteArrayDataOutput output = ByteStreams.newDataOutput();
		ByteArrayDataOutput output_self = ByteStreams.newDataOutput();

		ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
		DataOutputStream msgout = new DataOutputStream(msgbytes);

		output.writeUTF("Forward"); // So BungeeCord knows to forward it
		output.writeUTF("ALL");
		output.writeUTF(subchannel_name);

		for (String data : args) {
			msgout.writeUTF(data + "\n");
		}

		byte[] bytes = msgbytes.toByteArray();
		output.writeShort(bytes.length);
		output.write(bytes);

		output_self.writeUTF(subchannel_name);
		output_self.writeShort(bytes.length);
		output_self.write(bytes);
		
		PowerRanks.log.info(">>> Sending: " + String.join(", ", args));

		if (player != null) {
			player.sendPluginMessage(plugin, channel_name, output.toByteArray()); // Send to other servers
		} else {
//			Bukkit.getServer().sendPluginMessage(plugin, channel_name, output.toByteArray()); // Send to other servers
//			PowerRanks.log.info(plugin.getServer().getOnlinePlayers().size() + "");
			player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
			if (player != null) {
				player.sendPluginMessage(plugin, channel_name, output.toByteArray()); // Send to other servers
			}
		}
//		onPluginMessageReceived(channel_name, player, output_self.toByteArray()); // Also trigger own handler
	}
}
