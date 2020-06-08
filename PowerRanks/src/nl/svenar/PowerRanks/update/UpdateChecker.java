package nl.svenar.PowerRanks.update;

import java.util.Map;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import nl.svenar.PowerRanks.PowerRanks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class UpdateChecker {
	private PowerRanks plugin;
	private String BaseURL = "https://svenar.nl/powerranks/";
	private String UpdateCheckURL = BaseURL + "downloads/version.json";

	public UpdateChecker(PowerRanks plugin) {
		this.plugin = plugin;
		if (this.plugin.getConfigBool("updates.enable_update_checking"))
			check();
	}

	public void check() {
		Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
			PowerRanks.log.info("Checking for updates...");
			try {
				InputStream inputStream = new URL(UpdateCheckURL).openStream();

				String text = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n"));
				String current_server_type = getServerType();
				if (current_server_type.length() > 0) {
					JsonElement reader = new JsonParser().parse(text);
					JsonObject object = reader.getAsJsonObject().get("version").getAsJsonObject();
					for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
						String server_type = entry.getKey();
						String powerranks_version = entry.getValue().getAsJsonObject().get("powerranks").toString().replaceAll("\"", "");
						String server_version = entry.getValue().getAsJsonObject().get("minecraft").toString().replaceAll("\"", "");
						String download_path = entry.getValue().getAsJsonObject().get("download").toString().replaceAll("\"", "");

						if (server_type.toLowerCase().contains(current_server_type.toLowerCase()))
							checkUpdate(server_type, powerranks_version, server_version, download_path);
					}
				} else {
					PowerRanks.log.warning("Cannot check for updates! Unsupported server found: " + Bukkit.getServer().getVersion());
				}
			} catch (IOException exception) {
				PowerRanks.log.info("No new version available");
			}
		});
	}

	private void checkUpdate(String server_type, String powerranks_version, String server_version, String download_path) {
		boolean update_available = convertStringVersion(powerranks_version.replaceAll("[a-zA-Z ]", "")) > convertStringVersion(PowerRanks.pdf.getVersion().replaceAll("[a-zA-Z ]", ""));
		newVersionAvailable(update_available, server_type, powerranks_version.replaceAll("[a-zA-Z ]", ""), server_version, download_path);
	}

	private void newVersionAvailable(boolean update_available, String server_type, String powerranks_version, String server_version, String download_path) {
		if (update_available) {
			PowerRanks.log.warning("------------------------------------");
			PowerRanks.log.warning("A new " + PowerRanks.pdf.getName() + " version is available for " + server_type + "!");
			PowerRanks.log.warning("Current version: " + PowerRanks.pdf.getVersion());
			PowerRanks.log.warning("New version: " + powerranks_version);
			PowerRanks.log.warning("Download the new version from: " + BaseURL);
			PowerRanks.log.warning("Direct download link: " + BaseURL + download_path);
			PowerRanks.log.warning("------------------------------------");
		} else {
			PowerRanks.log.info("No new version available");
		}
	}

	// git-Bukkit-8160e29 (MC: 1.15.2)
	// git-Spigot-800b93f-8160e29 (MC: 1.15.2)
	// git-Paper-284 (MC: 1.15.2)

	private String getServerType() {
		String type = "";
		type = Bukkit.getServer().getVersion().toLowerCase().contains("bukkit") ? "bukkit" : type;
		type = Bukkit.getServer().getVersion().toLowerCase().contains("spigot") ? "bukkit" : type;
		type = Bukkit.getServer().getVersion().toLowerCase().contains("paper") ? "bukkit" : type;
		return type;
	}

	private int getServerVersion() {
		int version = 0;
		String str_version = Bukkit.getServer().getVersion().toLowerCase().substring(Bukkit.getServer().getVersion().toLowerCase().indexOf("(") + 1, Bukkit.getServer().getVersion().toLowerCase().indexOf(")")).replace("mc: ", "");
		version = convertStringVersion(str_version);
		return version;
	}

	private int convertStringVersion(String str_version) {
		int version = 0;
		String[] str_version_split = str_version.split("\\.");
		try {
			if (str_version_split.length == 1)
				version = Integer.parseInt(str_version_split[0]) * 1000;
			if (str_version_split.length == 2)
				version = Integer.parseInt(str_version_split[0]) * 1000 + Integer.parseInt(str_version_split[1]) * 100;
			if (str_version_split.length == 3)
				version = Integer.parseInt(str_version_split[0]) * 1000 + Integer.parseInt(str_version_split[1]) * 100 + Integer.parseInt(str_version_split[2]) * 10;
			if (str_version_split.length == 4)
				version = Integer.parseInt(str_version_split[0]) * 1000 + Integer.parseInt(str_version_split[1]) * 100 + Integer.parseInt(str_version_split[2]) * 10 + Integer.parseInt(str_version_split[3]);
		} catch (Exception e) {
		}
		return version;
	}
}