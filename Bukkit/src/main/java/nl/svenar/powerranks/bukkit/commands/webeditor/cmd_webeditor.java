package nl.svenar.powerranks.bukkit.commands.webeditor;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import nl.svenar.powerranks.common.http.DatabinClient;
import nl.svenar.powerranks.common.storage.PowerStorageManager;
import nl.svenar.powerranks.common.storage.provided.JSONStorageManager;
import nl.svenar.powerranks.bukkit.PowerRanks;
import nl.svenar.powerranks.bukkit.cache.CacheManager;
import nl.svenar.powerranks.bukkit.commands.PowerCommand;
import nl.svenar.powerranks.bukkit.data.PowerRanksVerbose;
import nl.svenar.powerranks.bukkit.util.Util;

public class cmd_webeditor extends PowerCommand {

	private String tellraw_url = "tellraw %player% [\"\",{\"text\":\"Web editor is ready \",\"color\":\"dark_green\"},{\"text\":\"[\",\"color\":\"black\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"%url%\"}},{\"text\":\"click to open\",\"color\":\"dark_green\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"%url%\"}},{\"text\":\"]\",\"color\":\"black\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"%url%\"}}]";

	private String powerranks_webeditor_url = "https://editor.powerranks.nl/?id=";

	public cmd_webeditor(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
		this.setCommandPermission("powerranks.cmd." + command_name.toLowerCase());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String commandName,
			String[] args) {
		if (args.length == 0) {
			// No args
		} else if (args.length == 1 || args.length == 2) {
			final String webeditorCommand = args[0].toLowerCase();

			switch (webeditorCommand) {
				case "start":
					startWebeditor(sender, commandName);
					break;
				case "load":
					if (args.length == 2) {
						loadWebeditor(sender, args[1], commandName);
					} else {
						sender.sendMessage(
								PowerRanks.getLanguageManager().getFormattedUsageMessage(commandLabel, commandName,
										"commands." + commandName.toLowerCase() + ".arguments", sender instanceof Player));
					}
					break;
				default:
					sender.sendMessage(
							PowerRanks.getLanguageManager().getFormattedUsageMessage(commandLabel, commandName,
									"commands." + commandName.toLowerCase() + ".arguments", sender instanceof Player));
					break;
			}

		} else {
			sender.sendMessage(
					PowerRanks.getLanguageManager().getFormattedUsageMessage(commandLabel, commandName,
							"commands." + commandName.toLowerCase() + ".arguments", sender instanceof Player));
		}

		return false;
	}

	private void startWebeditor(CommandSender sender, String commandName) {
		sender.sendMessage(
				PowerRanks.getLanguageManager().getFormattedMessage(
						"commands." + commandName.toLowerCase() + ".preparing-session"));

		JSONStorageManager jsonmanager = new JSONStorageManager(PowerRanks.fileLoc, "dummyRanks.json",
				"dummyPlayers.json");
		PowerStorageManager powermanager = CacheManager.getStorageManager();

		jsonmanager.setRanks(powermanager.getRanks());
		jsonmanager.setPlayers(powermanager.getPlayers());

		String outputJSON = "";

		outputJSON += "{";
		outputJSON += "\"serverdata\":";
		outputJSON += getServerDataAsJSON();
		outputJSON += ",";
		outputJSON += "\"rankdata\":";
		outputJSON += jsonmanager.getRanksAsJSON(false);
		outputJSON += ",";
		outputJSON += "\"playerdata\":";
		outputJSON += jsonmanager.getPlayersAsJSON(false);
		outputJSON += ",";
		outputJSON += "\"usertags\":";
		outputJSON += PowerRanks.getUsertagManager().toJSON("usertags", false);
		outputJSON += ",";
		outputJSON += "\"tablist\":";
		outputJSON += PowerRanks.getTablistConfigManager().toJSON(null, false);
		outputJSON += "}";

		jsonmanager.removeAllData();

		// PowerRanks.getInstance().getLogger().info("JSON: ");
		// PowerRanks.getInstance().getLogger().info(outputJSON);

		DatabinClient client = new DatabinClient("https://databin.svenar.nl", "Databinclient/1.0");

		client.postJSON(outputJSON);

		int uploadSize = outputJSON.length() / 1024;
		int updateInterval = 5;
		int timeout = 5;

		new BukkitRunnable() {
			int waitTime = 0;

			@Override
			public void run() {
				PowerRanksVerbose.log("task", "Running task uploading webeditor data");

				if (client.hasResponse()) {
					String key = client.getResponse().get("key");

					if (key.length() > 0 && !key.startsWith("[FAILED]")) {
						sender.sendMessage(ChatColor.DARK_AQUA + "===----------" + ChatColor.DARK_BLUE
								+ PowerRanks.pdf.getName() + ChatColor.DARK_AQUA + "----------===");
						// sender.sendMessage(ChatColor.DARK_GREEN + getIdentifier() + ChatColor.GREEN +
						// " v" + getVersion());
						if (sender instanceof Player) {
							Bukkit.getServer().dispatchCommand((CommandSender) Bukkit.getServer().getConsoleSender(),
									tellraw_url.replaceAll("%player%", sender.getName())
											.replaceAll("%url%", powerranks_webeditor_url + key).replaceAll("\n", ""));
						} else {
							sender.sendMessage(ChatColor.DARK_GREEN + "Web editor is ready " + ChatColor.BLACK + "["
									+ ChatColor.GREEN + powerranks_webeditor_url + key + ChatColor.BLACK + "]");
						}
						sender.sendMessage(ChatColor.DARK_GREEN + "Editor ID: " + ChatColor.GREEN + key);
						sender.sendMessage(ChatColor.DARK_GREEN + "Uploaded: " + ChatColor.GREEN + uploadSize + "KB");
						sender.sendMessage(ChatColor.DARK_AQUA + "===------------------------------===");
					}

					this.cancel();
				}

				if (waitTime / (20 / updateInterval) > timeout) {
					this.cancel();

					sender.sendMessage(
							PowerRanks.getLanguageManager().getFormattedMessage(
									"commands." + commandName.toLowerCase() + ".timed-out"));
				}

				waitTime++;
			}
		}.runTaskTimer(PowerRanks.getInstance(), 0, updateInterval);
	}

	private void loadWebeditor(CommandSender sender, String key, String commandName) {
		sender.sendMessage(
				PowerRanks.getLanguageManager().getFormattedMessage(
						"commands." + commandName.toLowerCase() + ".downloading-data"));

		DatabinClient client = new DatabinClient("https://databin.svenar.nl", "Databinclient/1.0");

		client.getJSON(key);

		int updateInterval = 5;
		int timeout = 5;

		new BukkitRunnable() {
			int waitTime = 0;

			@Override
			public void run() {
				PowerRanksVerbose.log("task", "Running task downloading webeditor data");

				if (client.hasResponse()) {
					this.cancel();

					String rawJSON = client.getRawResponse();
					Gson gson = new Gson();
					Type mapType = new TypeToken<Map<String, Object>>() {
					}.getType();
					Map<String, Object> jsonData = gson.fromJson(rawJSON, mapType);
					handleWebeditorDownload(sender, jsonData, commandName);
				}

				if (waitTime / (20 / updateInterval) > timeout) {
					this.cancel();

					sender.sendMessage(
							PowerRanks.getLanguageManager().getFormattedMessage(
									"commands." + commandName.toLowerCase() + ".timed-out"));
				}

				waitTime++;
			}
		}.runTaskTimer(PowerRanks.getInstance(), 0, updateInterval);
	}

	public void handleWebeditorDownload(CommandSender sender, Map<String, Object> jsonData, String commandName) {

		LinkedTreeMap<?, ?> serverData = (LinkedTreeMap<?, ?>) jsonData.get("serverdata");

		if (Objects.isNull(serverData) || !serverData.containsKey("powerranksVersion")) {
			PowerRanks.getInstance().getLogger().warning(serverData.toString());
			sender.sendMessage(
					PowerRanks.getLanguageManager().getFormattedMessage(
							"commands." + commandName.toLowerCase() + ".downloaded-invalid-data"));
			return;
		}

		if (!((String) serverData.get("powerranksVersion")).equals(PowerRanks.getVersion())) {
			sender.sendMessage(Util.powerFormatter(
					PowerRanks.getLanguageManager().getFormattedMessage(
							"commands." + commandName.toLowerCase() + ".incompatible-version"),
					ImmutableMap.<String, String>builder()
							.put("player", sender.getName())
							.put("version", PowerRanks.getVersion())
							.put("downloaded_version", (String) serverData.get("powerranksVersion"))
							.build(),
					'[', ']'));
			return;
		}

		LinkedTreeMap<?, ?> rankData = (LinkedTreeMap<?, ?>) jsonData.get("rankdata");
		LinkedTreeMap<?, ?> playerData = (LinkedTreeMap<?, ?>) jsonData.get("playerdata");
		LinkedTreeMap<?, ?> usertags = (LinkedTreeMap<?, ?>) jsonData.get("usertags");
		LinkedTreeMap<?, ?> tablist = (LinkedTreeMap<?, ?>) jsonData.get("tablist");

		JSONStorageManager jsonmanager = new JSONStorageManager(PowerRanks.fileLoc, "dummyRanks.json",
				"dummyPlayers.json");

		if (!(rankData instanceof LinkedTreeMap<?, ?> && playerData instanceof LinkedTreeMap<?, ?>)) {
			sender.sendMessage(
					PowerRanks.getLanguageManager().getFormattedMessage(
							"commands." + commandName.toLowerCase() + ".failed-downloaded"));
			return;
		}

		CacheManager.setRanks(jsonmanager.getRanksFromJSON(rankData));
		CacheManager.setPlayers(jsonmanager.getPlayersFromJSON(playerData));

		CacheManager.save();

		jsonmanager.removeAllData();

		PowerRanks.getUsertagManager().fromJSON("usertags", usertags);
		PowerRanks.getTablistConfigManager().fromJSON(null, tablist);

		sender.sendMessage(
				PowerRanks.getLanguageManager().getFormattedMessage(
						"commands." + commandName.toLowerCase() + ".success-downloaded"));

		sender.sendMessage(Util.powerFormatter(
				PowerRanks.getLanguageManager().getFormattedMessage(
						"commands." + commandName.toLowerCase() + ".download-stats"),
				ImmutableMap.<String, String>builder()
						.put("player", sender.getName())
						.put("rank_count", String.valueOf(CacheManager.getRanks().size()))
						.put("player_count", String.valueOf(CacheManager.getPlayers().size()))
						.build(),
				'[', ']'));
	}

	public ArrayList<String> tabCompleteEvent(CommandSender sender, String[] args) {
		ArrayList<String> tabcomplete = new ArrayList<String>();

		if (args.length == 1) {
			tabcomplete.add("start");
			tabcomplete.add("load");
		}

		return tabcomplete;
	}

	private String getServerDataAsJSON() {
		String output = "";

		List<String> server_permissions = new ArrayList<String>();

		for (String perm : plugin.getPermissionRegistry().getPermissions()) {
			server_permissions.add("\"" + perm + "\"");
		}

		output += "{";
		output += "\"powerranksVersion\":";
		output += "\"" + PowerRanks.getVersion() + "\"";
		output += ",\"serverPermissions\":";
		output += "[" + String.join(",", server_permissions) + "]";
		output += "}";

		return output;
	}
}
