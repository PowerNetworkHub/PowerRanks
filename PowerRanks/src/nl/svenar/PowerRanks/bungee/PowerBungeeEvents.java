package nl.svenar.PowerRanks.bungee;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Util;
import nl.svenar.PowerRanks.api.PowerRanksAPI;

public class PowerBungeeEvents {

	private PowerRanks plugin;
	private BungeeMessageHandler bungee;
	private PowerBungeeDataManager dataManager;
	private PowerRanksAPI prAPI;
	private String serverID = "";
	private Long start_time = 0L;
	private int ping_time_in_seconds = 5;
	private int server_ping_timeout_count = 4;
	private int server_cache_update_checking_interval = 10;

	private HashMap<String, Integer> bungee_servers_list = new HashMap<String, Integer>();
	private HashMap<String, Long> bungee_servers_start_time = new HashMap<String, Long>();

	private ArrayList<String[]> queue = new ArrayList<String[]>();

	public PowerBungeeEvents(PowerRanks plugin) {
		this.plugin = plugin;
		this.bungee = this.plugin.bungee_message_handler;
		this.serverID = (100 + Math.round(Math.random() * 900)) + "-" + (100 + Math.round(Math.random() * 900)) + "-" + (100 + Math.round(Math.random() * 900));
		this.start_time = System.currentTimeMillis();
		this.prAPI = this.plugin.loadAPI();

		PowerRanks.log.info("PowerRanks BungeeCord server ID: " + this.serverID);

		dataManager = new PowerBungeeDataManager();

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

		scheduler.scheduleSyncRepeatingTask(plugin, new Runnable() {

			@Override
			public void run() {
				sendRankHashes();
			}

		}, 0L, 20L * server_cache_update_checking_interval);
	}

	/*
	 * Send data
	 */

	public void eventPing() {
		if (this.bungee == null)
			return;

		String[] data = { "ping", this.serverID, String.valueOf(this.start_time) };
		queue.add(data);
	}

	public void eventJoin(Player player) {
		eventPing();
	}

	public void eventLeave(Player player) {
		eventPing();
	}

	private void sendRankHashes() {
		if (this.bungee == null)
			return;

//		String[] data = {"ping", this.serverID, String.valueOf(this.start_time)};
		ArrayList<String> rawData = new ArrayList<String>();
		rawData.add("rankhashes");
		rawData.add(serverID);
		for (Entry<String, String> entry : dataManager.createRankHashes().entrySet()) {
			rawData.add(entry.getKey() + "@" + entry.getValue());
		}
		queue.add(rawData.toArray(new String[0]));

	}

	/*
	 * Send rank (as master) to a specified server
	 */
	private void sendRank(String targetServerID, String rankName) {
		String rank_data_to_send = dataManager.serializeRank(rankName, true);
		PowerRanks.log.info(">>> " + rank_data_to_send);
		String[] rank_data_to_send_split = Util.splitStringEvery(rank_data_to_send, 32);
		PowerRanks.log.info(">>>> " + String.join("", rank_data_to_send_split));

		ArrayList<String> rawData = new ArrayList<String>();
		rawData.add("rankdata");
		rawData.add(serverID);
		rawData.add(targetServerID);
		for (String line : rank_data_to_send_split) {
			rawData.add(line);
		}
		queue.add(rawData.toArray(new String[0]));
	}

	/*
	 * Rank data received from master > Save to local rank
	 */
	private void updateRank(String received_rank_data) {
		dataManager.saveRankDataToRank(prAPI, dataManager.deserializeRank(received_rank_data));
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
//		PowerRanks.log.info("<<< Received: " + String.join(", ", data));
		if (data.size() > 1) {
			String topic = data.get(0);
			String senderid = data.get(1);

			if (topic.equalsIgnoreCase("ping")) {
				Long startTime = Long.parseLong(data.get(2));
				if (!senderid.equals(serverID)) {
					bungee_servers_list.put(senderid, 0);
					bungee_servers_start_time.put(senderid, startTime);
				}

			} else if (topic.equalsIgnoreCase("rankhashes")) {
				if (!senderid.equals(serverID)) {
					HashMap<String, String> remote_rankhashed = new HashMap<String, String>();
					HashMap<String, String> local_rankhashed = dataManager.createRankHashes();
					ArrayList<String> ranks_to_send = new ArrayList<String>();

					PowerRanks.log.info("Sender serverID: " + senderid);
					PowerRanks.log.info("Sender is master?: " + (isMaster(senderid) ? "yes" : "no"));

					for (int i = 2; i < data.size(); i++) {
						String[] rankhash = data.get(i).split("@");
						remote_rankhashed.put(rankhash[0], rankhash[1]);
					}

					for (Entry<String, String> entry : remote_rankhashed.entrySet()) {
						if (!local_rankhashed.containsKey(entry.getKey())) {
							PowerRanks.log.info("Rank " + entry.getKey() + " not found");
						} else {
							PowerRanks.log.info("Rank " + entry.getKey() + " " + (entry.getValue().equals(local_rankhashed.get(entry.getKey())) ? "is the same" : "is different") + " (" + entry.getValue() + ", " + local_rankhashed.get(entry.getKey()) + ")");
						}

						if (local_rankhashed.containsKey(entry.getKey()) && !entry.getValue().equals(local_rankhashed.get(entry.getKey()))) {
							ranks_to_send.add(entry.getKey());
						}
					}

					for (Entry<String, String> entry : local_rankhashed.entrySet()) {
						if (!remote_rankhashed.containsKey(entry.getKey())) {
							PowerRanks.log.info("Rank " + entry.getKey() + " local only");
							ranks_to_send.add(entry.getKey());
						}
					}

					if (isMaster()) {
						for (String rank_to_send : ranks_to_send) {
							 PowerRanks.log.info("SEND: " + rank_to_send);
							sendRank(senderid, rank_to_send);
						}
					}

					PowerRanks.log.info("");
				}
			} else if (topic.equalsIgnoreCase("rankdata")) {
				String targetid = data.get(2);
				String received_rank_data = "";

				if (isMaster(senderid) && targetid.equals(getServerID())) {
					for (int i = 3; i < data.size(); i++) {
						received_rank_data += data.get(i).replaceFirst("!", "");
					}

					updateRank(received_rank_data);

//					PowerRanks.log.info("RECV: " + received_rank_data);
					PowerRanks.log.info("RECV: " + dataManager.deserializeRank(received_rank_data).get("name"));
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
		return isMaster(getServerID());
	}

	public boolean isMaster(String serverID) {
		return getMasterID().equals(serverID);
	}

	public String getServerID() {
		return serverID;
	}

	public Long getStartTime() {
		return start_time;
	}
}
