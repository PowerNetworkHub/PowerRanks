package nl.svenar.PowerRanks.bungee;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import nl.svenar.PowerRanks.PowerRanks;

public class PowerBungeeEvents {

	private PowerRanks plugin;
	private BungeeMessageHandler bungee;
	private String serverID = "";
	private Long start_time = 0L;
	private int ping_time_in_seconds = 5;
	private int server_ping_timeout_count = 4;
	
	private HashMap<String, Integer> bungee_servers_list = new HashMap<String, Integer>();
	private HashMap<String, Long> bungee_servers_start_time = new HashMap<String, Long>();

	private ArrayList<String[]> queue = new ArrayList<String[]>();

	public PowerBungeeEvents(PowerRanks plugin) {
		this.plugin = plugin;
		this.bungee = this.plugin.bungee_message_handler;
		this.serverID = (100 + Math.round(Math.random() * 900)) + "-" + (100 + Math.round(Math.random() * 900)) + "-" + (100 + Math.round(Math.random() * 900));
		this.start_time = System.currentTimeMillis();
		
		PowerRanks.log.info("PowerRanks BungeeCord server ID: " + this.serverID);

		BukkitScheduler scheduler = plugin.getServer().getScheduler();
		scheduler.scheduleSyncRepeatingTask(plugin, new Runnable() {

			@Override
			public void run() {
				
				HashMap<String, Integer> bungee_servers_list_new = new HashMap<String, Integer>();
				for (String key : bungee_servers_list.keySet()) {
					bungee_servers_list_new.put(key, bungee_servers_list.get(key) + 1);
					if (bungee_servers_list_new.get(key) + 1 > server_ping_timeout_count) {
						bungee_servers_list_new.remove(key);
						bungee_servers_start_time.remove(key);
					}
				}
				
				bungee_servers_list = bungee_servers_list_new;
				
				eventPing();
				
			}

		}, 0L, 20 * this.ping_time_in_seconds);

		scheduler.scheduleSyncRepeatingTask(plugin, new Runnable() {

			@Override
			public void run() {
				if (queue.size() > 0) {
					String[] data = queue.get(0);
					try {
						bungee.sendRaw(null, data);
						queue.remove(0);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		}, 0L, 5L);
	}

	/*
	 * Send data
	 */

	public void eventPing() {
		if (this.bungee == null)
			return;

		String[] data = {"ping", this.serverID, String.valueOf(this.start_time)};
		queue.add(data);
	}

	public void eventJoin(Player player) {
		eventPing();
	}

	public void eventLeave(Player player) {
		eventPing();
	}

//	public void eventPowerRanksRankChange(Player player, String newRank) {
//		if (this.bungee == null)
//			return;
//
//		String[] data = {"setrank", player.getUniqueId().toString(), newRank };
//		queue.add(data);
//	}

	/*
	 * Received data
	 */

	public void onDataReceive(ArrayList<String> data) {
//		PowerRanks.log.info("Data received!");
		PowerRanks.log.info("<<< Received: " + String.join(", ", data));
		if (data.size() > 1) {
			String topic = data.get(0);
			String senderid = data.get(1);
			
			if (topic.equalsIgnoreCase("ping")) {
				Long startTime = Long.parseLong(data.get(2));
				if (!senderid.equals(serverID)) {
					bungee_servers_list.put(senderid, 0);
					bungee_servers_start_time.put(senderid, startTime);
				}

			} else {
				int i = 0;
				for (String l : data) {
					PowerRanks.log.info("#" + i + ": " + l);
					i++;
				}
			}
		}

		
//		PowerRanks.log.info("-----");
	}

	/*
	 * TESTING
	 */

	public void sendRaw(String[] data) {
		queue.add(data);
	}

	public ArrayList<String> getConnectedServers() {
		ArrayList<String> output = new ArrayList<String>();
		for (String key : bungee_servers_list.keySet()) {
			output.add(key);
		}
		return output;
	}
	
	public String getMasterID() {
		String master = serverID;
		Long last_start_time = start_time;
		for (Entry<String, Long> entry : bungee_servers_start_time.entrySet()) {
			if (entry.getValue() < last_start_time) {
				last_start_time = entry.getValue();
				master = entry.getKey();
			}
		}
		return master;
	}
	
	public boolean isMaster() {
		return getMasterID().equals(getServerID());
	}

	public String getServerID() {
		return serverID;
	}

	public Long getStartTime() {
		return start_time;
	}
}
