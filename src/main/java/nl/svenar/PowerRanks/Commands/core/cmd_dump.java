package nl.svenar.PowerRanks.Commands.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Cache.CacheManager;
import nl.svenar.PowerRanks.Commands.PowerCommand;
import nl.svenar.PowerRanks.Data.PowerRanksVerbose;
import nl.svenar.common.http.DatabinClient;
import nl.svenar.common.storage.PowerStorageManager;
import nl.svenar.common.storage.provided.YAMLStorageManager;

public class cmd_dump extends PowerCommand {

	private String databin_url = "https://databin.svenar.nl/";
	private String logs_powerranks_url = "https://logs.powerranks.nl/dump/?id=";
	private String tellraw_url = "tellraw %player% [\"\",{\"text\":\"Log dump is ready \",\"color\":\"dark_green\"},{\"text\":\"[\",\"color\":\"black\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"%url%\"}},{\"text\":\"click to open\",\"color\":\"dark_green\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"%url%\"}},{\"text\":\"]\",\"color\":\"black\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"%url%\"}}]";

	public cmd_dump(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
		this.setCommandPermission("powerranks.cmd." + command_name.toLowerCase());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String commandName, String[] args) {
		uploadLog(sender, commandName);

		return false;
	}

	public ArrayList<String> tabCompleteEvent(CommandSender sender, String[] args) {
		ArrayList<String> tabcomplete = new ArrayList<String>();
		return tabcomplete;
	}

	private void uploadLog(CommandSender sender, String commandName) {
		DatabinClient client = new DatabinClient("https://databin.svenar.nl", "Databinclient/1.0");

		ArrayList<String> serverLog = new ArrayList<String>();

		final String serverPath = plugin.getServer().getWorldContainer().getAbsolutePath();
		final File serverLogFile = new File(serverPath.substring(0, serverPath.length() - 1), "logs/latest.log");

		if (serverLogFile.exists()) {
			try (BufferedReader br = new BufferedReader(new FileReader(serverLogFile))) {
				String line;
				while ((line = br.readLine()) != null) {
					serverLog.add(removeIP(line).replaceAll("\"", "'"));
				}
			} catch (IOException e) {
				serverLog.add("Error reading server log file.");
			}
		} else {
			serverLog.add("Server log not found.");
		}

		String outputJSONLog = "";
		outputJSONLog += "{";
		outputJSONLog += "\"type\":\"dump\",";
		outputJSONLog += "\"version\":{";
		outputJSONLog += "\"powerranks\":\"" + PowerRanks.pdf.getVersion() + "\",";
		outputJSONLog += "\"server\":\"" + Bukkit.getVersion() + " | " + Bukkit.getServer().getBukkitVersion() + "\"";
		outputJSONLog += "},";
		
		outputJSONLog += "\"plugins\":[";
		Plugin[] plugins = Bukkit.getPluginManager().getPlugins();
		for (Plugin plugin : plugins) {
			outputJSONLog += "\"" + plugin.getName() + "(" + plugin.getDescription().getVersion() + ")\",";
		}
		outputJSONLog = outputJSONLog.substring(0, outputJSONLog.length() - 1);

		outputJSONLog += "],";
		outputJSONLog += "\"serverlog\":[";
		if (serverLog.size() > 0) {
			for (String line : serverLog) {
				outputJSONLog += "\"" + line + "\",";
			}
			outputJSONLog = outputJSONLog.substring(0, outputJSONLog.length() - 1);
		}
		outputJSONLog += "],";

		YAMLStorageManager yamlmanager = new YAMLStorageManager(PowerRanks.fileLoc, "dummyRanks.yml", "dummyPlayers.yml");
		PowerStorageManager powermanager = CacheManager.getStorageManager();

		yamlmanager.setRanks(powermanager.getRanks());
		yamlmanager.setPlayers(powermanager.getPlayers());

		yamlmanager.saveAll();

		File ranksFile = new File(PowerRanks.fileLoc, "dummyRanks.yml");
		File playersFile = new File(PowerRanks.fileLoc, "dummyPlayers.yml");
		File configFile = new File(PowerRanks.fileLoc, "config.yml");
		File usertagsFile = new File(PowerRanks.fileLoc, "usertags.yml");

		ArrayList<String> ranksYaml = new ArrayList<String>();
		ArrayList<String> playersYaml = new ArrayList<String>();
		ArrayList<String> configYaml = new ArrayList<String>();
		ArrayList<String> usertagsYaml = new ArrayList<String>();

        PowerRanks.getConfigManager().save();
        PowerRanks.getUsertagManager().save();

		if (ranksFile.exists()) {
			try (BufferedReader br = new BufferedReader(new FileReader(ranksFile))) {
				String line;
				while ((line = br.readLine()) != null) {
					ranksYaml.add(line.replaceAll("\"", "'"));
				}
			} catch (IOException e) {
				ranksYaml.add("Error reading ranks file.");
			}
		} else {
			ranksYaml.add("Ranks file does not exist.");
		}

		if (playersFile.exists()) {
			try (BufferedReader br = new BufferedReader(new FileReader(playersFile))) {
				String line;
				while ((line = br.readLine()) != null) {
					playersYaml.add(line.replaceAll("\"", "'"));
				}
			} catch (IOException e) {
				playersYaml.add("Error reading players file.");
			}
		} else {
			playersYaml.add("Players file does not exist.");
		}

		if (configFile.exists()) {
			try (BufferedReader br = new BufferedReader(new FileReader(configFile))) {
				String line;
				while ((line = br.readLine()) != null) {
					configYaml.add(line.replaceAll("\"", "'"));
				}
			} catch (IOException e) {
				configYaml.add("Error reading config file.");
			}
		} else {
			configYaml.add("Config file does not exist.");
		}

        if (usertagsFile.exists()) {
			try (BufferedReader br = new BufferedReader(new FileReader(usertagsFile))) {
				String line;
				while ((line = br.readLine()) != null) {
					usertagsYaml.add(line.replaceAll("\"", "'"));
				}
			} catch (IOException e) {
				usertagsYaml.add("Error reading usertags file.");
			}
		} else {
			usertagsYaml.add("Usertags file does not exist.");
		}

		outputJSONLog += "\"powerranks\":{";
		outputJSONLog += "\"ranks\": [";
		if (ranksYaml.size() > 0) {
			for (String line : ranksYaml) {
				outputJSONLog += "\"" + line + "\",";
			}
			outputJSONLog = outputJSONLog.substring(0, outputJSONLog.length() - 1);
		}
		outputJSONLog += "],";
		outputJSONLog += "\"players\": [";
		if (playersYaml.size() > 0) {
			for (String line : playersYaml) {
				outputJSONLog += "\"" + line + "\",";
			}
			outputJSONLog = outputJSONLog.substring(0, outputJSONLog.length() - 1);
		}
		outputJSONLog += "],";
		outputJSONLog += "\"config\": [";
		if (configYaml.size() > 0) {
			for (String line : configYaml) {
				outputJSONLog += "\"" + line + "\",";
			}
			outputJSONLog = outputJSONLog.substring(0, outputJSONLog.length() - 1);
		}
		outputJSONLog += "],";
		outputJSONLog += "\"usertags\": [";
		if (usertagsYaml.size() > 0) {
			for (String line : usertagsYaml) {
				outputJSONLog += "\"" + line + "\",";
			}
			outputJSONLog = outputJSONLog.substring(0, outputJSONLog.length() - 1);
		}
		outputJSONLog += "]";
		outputJSONLog += "}";
		outputJSONLog += "}";

        yamlmanager.removeAllData();

		client.postJSON(outputJSONLog);

		int uploadSize = outputJSONLog.length() / 1024;
		int updateInterval = 5;
		int timeout = 5;

		new BukkitRunnable() {
			int waitTime = 0;

			@Override
			public void run() {
				PowerRanksVerbose.log("task", "Running task uploading dump data");

				if (client.hasResponse()) {
					String key = client.getResponse().get("key");

					if (key.length() > 0 && !key.startsWith("[FAILED]")) {
						sender.sendMessage(ChatColor.DARK_AQUA + "===----------" + ChatColor.DARK_BLUE
								+ PowerRanks.pdf.getName() + ChatColor.DARK_AQUA + "----------===");
						if (sender instanceof Player) {
							Bukkit.getServer().dispatchCommand((CommandSender) Bukkit.getServer().getConsoleSender(),
									tellraw_url.replaceAll("%player%", sender.getName())
											.replaceAll("%url%", databin_url + key).replaceAll("\n", ""));
						} else {
							sender.sendMessage(ChatColor.DARK_GREEN + "Data upload is ready " + ChatColor.BLACK + "["
									+ ChatColor.GREEN + logs_powerranks_url + key + ChatColor.BLACK + "]");
						}
						sender.sendMessage(ChatColor.DARK_GREEN + "ID: " + ChatColor.GREEN + key);
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

	private String removeIP(String line) {
		String output = line;

		String ipExp = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}";
    	Pattern ipPattern = Pattern.compile(ipExp);

		Matcher matcher = ipPattern.matcher(output);
        output = matcher.replaceAll("<<IP_HIDDEN>>");

		return output;
	}
}
