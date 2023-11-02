package nl.svenar.powerranks.nukkit.commands.webeditor;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

import cn.nukkit.utils.TextFormat;

import cn.nukkit.command.CommandSender;
import cn.nukkit.Player;
import cn.nukkit.scheduler.NukkitRunnable;

import nl.svenar.powerranks.common.http.DatabinClient;
import nl.svenar.powerranks.common.storage.PowerStorageManager;
import nl.svenar.powerranks.common.storage.provided.JSONStorageManager;
import nl.svenar.powerranks.common.utils.PRCache;
import nl.svenar.powerranks.common.utils.PRUtil;
import nl.svenar.powerranks.nukkit.PowerRanks;
import nl.svenar.powerranks.nukkit.commands.PowerCommand;
import nl.svenar.powerranks.nukkit.util.Util;

public class CmdWebeditor extends PowerCommand {

	private String tellraw_url = "tellraw %player% [\"\",{\"text\":\"Web editor is ready \",\"color\":\"dark_green\"},{\"text\":\"[\",\"color\":\"black\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"%url%\"}},{\"text\":\"click to open\",\"color\":\"dark_green\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"%url%\"}},{\"text\":\"]\",\"color\":\"black\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"%url%\"}}]";

	private String powerranks_webeditor_url = "https://editor.powerranks.nl/?id=";

	public CmdWebeditor(PowerRanks plugin, String command_name, COMMANDEXECUTOR ce) {
		super(plugin, command_name, ce);
		this.setCommandPermission("powerranks.cmd." + command_name.toLowerCase());
	}

	@Override
	public boolean onCommand(CommandSender sender, String commandLabel, String commandName, String[] args) {
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
								plugin.getLanguageManager().getFormattedUsageMessage(commandLabel, commandName,
										"commands." + commandName.toLowerCase() + ".arguments", sender instanceof Player));
					}
					break;
				default:
					sender.sendMessage(
							plugin.getLanguageManager().getFormattedUsageMessage(commandLabel, commandName,
									"commands." + commandName.toLowerCase() + ".arguments", sender instanceof Player));
					break;
			}

		} else {
			sender.sendMessage(
					plugin.getLanguageManager().getFormattedUsageMessage(commandLabel, commandName,
							"commands." + commandName.toLowerCase() + ".arguments", sender instanceof Player));
		}

		return false;
	}

	private void startWebeditor(CommandSender sender, String commandName) {
		sender.sendMessage(
				plugin.getLanguageManager().getFormattedMessage(
						"commands." + commandName.toLowerCase() + ".preparing-session"));

		JSONStorageManager jsonmanager = new JSONStorageManager(Util.DATA_DIR, "dummyRanks.json",
				"dummyPlayers.json");
		PowerStorageManager powermanager = plugin.getStorageManager();

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
		outputJSON += plugin.getUsertagStorage().toJSON("usertags", false);
		outputJSON += "}";

		jsonmanager.removeAllData();

		// PowerRanks.getInstance().getLogger().info("JSON: ");
		// PowerRanks.getInstance().getLogger().info(outputJSON);

		DatabinClient client = new DatabinClient("https://databin.svenar.nl", "Databinclient/1.0");

		client.postJSON(outputJSON);

		int uploadSize = outputJSON.length() / 1024;
		int updateInterval = 5;
		int timeout = 5;

		new NukkitRunnable() {
			int waitTime = 0;

			@Override
			public void run() {
				if (client.hasResponse()) {
					String key = client.getResponse().get("key");

					if (key.length() > 0 && !key.startsWith("[FAILED]")) {
						sender.sendMessage(TextFormat.DARK_AQUA + "===----------" + TextFormat.DARK_BLUE
								+ plugin.getDescription().getName() + TextFormat.DARK_AQUA + "----------===");
						// sender.sendMessage(TextFormat.DARK_GREEN + getIdentifier() + TextFormat.GREEN +
						// " v" + getVersion());
						if (sender instanceof Player) {
							plugin.getServer().dispatchCommand((CommandSender) plugin.getServer().getConsoleSender(),
									tellraw_url.replaceAll("%player%", sender.getName())
											.replaceAll("%url%", powerranks_webeditor_url + key).replaceAll("\n", ""));
						} else {
							sender.sendMessage(TextFormat.DARK_GREEN + "Web editor is ready " + TextFormat.BLACK + "["
									+ TextFormat.GREEN + powerranks_webeditor_url + key + TextFormat.BLACK + "]");
						}
						sender.sendMessage(TextFormat.DARK_GREEN + "Editor ID: " + TextFormat.GREEN + key);
						sender.sendMessage(TextFormat.DARK_GREEN + "Uploaded: " + TextFormat.GREEN + uploadSize + "KB");
						sender.sendMessage(TextFormat.DARK_AQUA + "===------------------------------===");
					}

					this.cancel();
				}

				if (waitTime / (20 / updateInterval) > timeout) {
					this.cancel();

					sender.sendMessage(
							plugin.getLanguageManager().getFormattedMessage(
									"commands." + commandName.toLowerCase() + ".timed-out"));
				}

				waitTime++;
			}
		}.runTaskTimer(plugin, 0, updateInterval);
	}

	private void loadWebeditor(CommandSender sender, String key, String commandName) {
		sender.sendMessage(
				plugin.getLanguageManager().getFormattedMessage(
						"commands." + commandName.toLowerCase() + ".downloading-data"));

		DatabinClient client = new DatabinClient("https://databin.svenar.nl", "Databinclient/1.0");

		client.getJSON(key);

		int updateInterval = 5;
		int timeout = 5;

		new NukkitRunnable() {
			int waitTime = 0;

			@Override
			public void run() {
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
							plugin.getLanguageManager().getFormattedMessage(
									"commands." + commandName.toLowerCase() + ".timed-out"));
				}

				waitTime++;
			}
		}.runTaskTimer(plugin, 0, updateInterval);
	}

	public void handleWebeditorDownload(CommandSender sender, Map<String, Object> jsonData, String commandName) {

		LinkedTreeMap<?, ?> serverData = (LinkedTreeMap<?, ?>) jsonData.get("serverdata");

		if (Objects.isNull(serverData) || !serverData.containsKey("powerranksVersion")) {
			plugin.getLogger().warning(serverData.toString());
			sender.sendMessage(
					plugin.getLanguageManager().getFormattedMessage(
							"commands." + commandName.toLowerCase() + ".downloaded-invalid-data"));
			return;
		}

		if (!((String) serverData.get("powerranksVersion")).equals(plugin.getDescription().getVersion())) {
			sender.sendMessage(PRUtil.powerFormatter(
					plugin.getLanguageManager().getFormattedMessage(
							"commands." + commandName.toLowerCase() + ".incompatible-version"),
					ImmutableMap.<String, String>builder()
							.put("player", sender.getName())
							.put("version", plugin.getDescription().getVersion())
							.put("downloaded_version", (String) serverData.get("powerranksVersion"))
							.build(),
					'[', ']'));
			return;
		}

		LinkedTreeMap<?, ?> rankData = (LinkedTreeMap<?, ?>) jsonData.get("rankdata");
		LinkedTreeMap<?, ?> playerData = (LinkedTreeMap<?, ?>) jsonData.get("playerdata");
		LinkedTreeMap<?, ?> usertags = (LinkedTreeMap<?, ?>) jsonData.get("usertags");

		JSONStorageManager jsonmanager = new JSONStorageManager(Util.DATA_DIR, "dummyRanks.json",
				"dummyPlayers.json");

		if (!(rankData instanceof LinkedTreeMap<?, ?> && playerData instanceof LinkedTreeMap<?, ?>)) {
			sender.sendMessage(
					plugin.getLanguageManager().getFormattedMessage(
							"commands." + commandName.toLowerCase() + ".failed-downloaded"));
			return;
		}

		PRCache.setRanks(jsonmanager.getRanksFromJSON(rankData));
		PRCache.setPlayers(jsonmanager.getPlayersFromJSON(playerData));

		plugin.getStorageLoader().saveData(plugin.getStorageManager());

		jsonmanager.removeAllData();

		plugin.getUsertagStorage().fromJSON("usertags", usertags);

		sender.sendMessage(
				plugin.getLanguageManager().getFormattedMessage(
						"commands." + commandName.toLowerCase() + ".success-downloaded"));

		sender.sendMessage(PRUtil.powerFormatter(
				plugin.getLanguageManager().getFormattedMessage(
						"commands." + commandName.toLowerCase() + ".download-stats"),
				ImmutableMap.<String, String>builder()
						.put("player", sender.getName())
						.put("rank_count", String.valueOf(PRCache.getRanks().size()))
						.put("player_count", String.valueOf(PRCache.getPlayers().size()))
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
		output += "\"" + plugin.getDescription().getVersion() + "\"";
		output += ",\"serverPermissions\":";
		output += "[" + String.join(",", server_permissions) + "]";
		output += "}";

		return output;
	}
}
