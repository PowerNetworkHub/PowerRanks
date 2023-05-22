package nl.svenar.powerranks.commands.core;

import java.io.File;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import nl.svenar.common.http.DatabinClient;
import nl.svenar.common.storage.PowerStorageManager;
import nl.svenar.common.storage.provided.JSONStorageManager;
import nl.svenar.common.utils.AsyncReadFile;
import nl.svenar.powerranks.PowerRanks;
import nl.svenar.powerranks.cache.CacheManager;
import nl.svenar.powerranks.commands.PowerCommand;
import nl.svenar.powerranks.data.PowerRanksVerbose;

public class CmdDump extends PowerCommand {

    private String databin_url = "https://databin.svenar.nl";
    private String logs_powerranks_url = "https://logs.powerranks.nl/dump/?id=";

    ArrayList<String> serverLog = new ArrayList<String>();
    Map<String, Object> coreData = new HashMap<String, Object>();

    public CmdDump(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
        super(plugin, command_name, ce);
        this.setCommandPermission("powerranks.cmd." + command_name.toLowerCase());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String commandName, String[] args) {
        if (sender instanceof Player) {
            sender.sendMessage(
                    ChatColor.DARK_AQUA + "--------" + ChatColor.DARK_BLUE + PowerRanks.pdf.getName() + ChatColor.DARK_AQUA + "--------");
            sender.sendMessage(ChatColor.GREEN + "Dump has started collecting data!");
            sender.sendMessage(ChatColor.GREEN + "Check the server console for more information.");
            sender.sendMessage(ChatColor.DARK_AQUA + "--------------------------");
        }
        prepareLog(sender, commandName);

        return false;
    }

    public ArrayList<String> tabCompleteEvent(CommandSender sender, String[] args) {
        ArrayList<String> tabcomplete = new ArrayList<String>();
        return tabcomplete;
    }

    private void readServerLog() {
        serverLog.clear();

        final String serverPath = plugin.getServer().getWorldContainer().getAbsolutePath();
        final File serverLogFile = new File(serverPath.substring(0, serverPath.length() - 1), "logs/latest.log");

        AsyncReadFile arf = new AsyncReadFile();
        arf.setFile(serverLogFile.getPath());
        long totalLength = arf.getFileLength();
        arf.read();

        new BukkitRunnable() {
            Instant startTime = Instant.now();
            int progressTimerIndex = Integer.MAX_VALUE;

            @Override
            public void run() {
                if (!arf.isReady()) {
                    if (progressTimerIndex > 20 * 10) {
                        progressTimerIndex = 0;
                        int currentLength = arf.getData().length();
                        PowerRanks.getInstance().getLogger().info("Reading server log... (" + ((100 * currentLength) / totalLength) + "%)");
                    }
                    progressTimerIndex++;
                    return;
                } else {
                    PowerRanks.getInstance().getLogger().info("Server log:");

                    PowerRanks.getInstance().getLogger().info("- Removing sensitive information from server log...");
                    for (String line : arf.getData().split("\n")) {
                        serverLog.add(removeIP(line).replaceAll("\"", "'"));
                    }
                    PowerRanks.getInstance().getLogger()
                            .info("- Reading took " + Duration.between(startTime, Instant.now()).toMillis() + "ms");
                    PowerRanks.getInstance().getLogger()
                            .info("- Read " + arf.getData().split("\n").length + " lines (" + arf.getData().length() + " characters)!");
                    this.cancel();
                    PowerRanks.getInstance().getLogger().info("");
                }
            }
        }.runTaskTimer(PowerRanks.getInstance(), 0, 1);
    }

    private void readCoreData() {
        coreData = new HashMap<>();

        new BukkitRunnable() {
            Instant startTime = Instant.now();

            @Override
            public void run() {
                PowerRanks.getInstance().getLogger().info("Plugin data:");

                JSONStorageManager jsonmanager = new JSONStorageManager(PowerRanks.fileLoc, "dummyRanks.json", "dummyPlayers.json");
                PowerStorageManager powermanager = CacheManager.getStorageManager();

                jsonmanager.setRanks(powermanager.getRanks());
                jsonmanager.setPlayers(powermanager.getPlayers());

                // jsonmanager.getRanksAsJSON(false);
                // jsonmanager.getPlayersAsJSON(false);
                // PowerRanks.getUsertagManager().toJSON("usertags", false);

                coreData.put("rankdata", jsonmanager.getRanksAsMap());
                coreData.put("playerdata", jsonmanager.getPlayersAsMap());
                coreData.put("config", PowerRanks.getConfigManager().getRawData());
                coreData.put("usertags", PowerRanks.getUsertagManager().getRawData().get("usertags"));

                PowerRanks.getInstance().getLogger().info("- Reading took " + Duration.between(startTime, Instant.now()).toMillis() + "ms");
                PowerRanks.getInstance().getLogger().info("- Read " + jsonmanager.getRanks().size() + " ranks and " + jsonmanager.getPlayers().size() + " players!");
                PowerRanks.getInstance().getLogger().info("- Adding configuration & usertags");

                jsonmanager.removeAllData();
                PowerRanks.getInstance().getLogger().info("");

                this.cancel();

            }
        }.runTaskTimer(PowerRanks.getInstance(), 0, 1);
    }

    private void prepareLog(CommandSender sender, String commandName) {
        PowerRanks.getInstance().getLogger().info("");
        PowerRanks.getInstance().getLogger().info("=== -------------------------------- ===");
        PowerRanks.getInstance().getLogger().info("     PowerRanks is collecting data!     ");
        PowerRanks.getInstance().getLogger().info("=== -------------------------------- ===");

        readServerLog();
        readCoreData();

        new BukkitRunnable() {
            Instant startTime = Instant.now();
            int progressIndex = 0;
            Map<String, Object> elements = new HashMap<>();
            boolean serverLogDone = false;
            boolean coreDataDone = false;

            @Override
            @SuppressWarnings("unchecked")
            public void run() {
                if (serverLog.size() > 0) {
                    PowerRanks.getInstance().getLogger().info("Server log found!");

                    elements.put("type", "dump");
                    elements.put("version", new HashMap<>());
                    ((Map<String, Object>) elements.get("version")).put("powerranks", PowerRanks.pdf.getVersion());
                    ((Map<String, Object>) elements.get("version")).put("server",
                            Bukkit.getVersion() + " | " + Bukkit.getServer().getBukkitVersion());

                    String pluginData = "";
                    Plugin[] plugins = Bukkit.getPluginManager().getPlugins();
                    for (Plugin plugin : plugins) {
                        pluginData += plugin.getName() + "(" + plugin.getDescription().getVersion() + "),";
                    }
                    pluginData = pluginData.substring(0, pluginData.length() - 1);
                    elements.put("plugins", pluginData);

                    elements.put("serverlog", Arrays.asList(serverLog));

                    serverLog = new ArrayList<>();
                    serverLogDone = true;
                }

                if (coreData.keySet().size() > 0) {
                    PowerRanks.getInstance().getLogger().info("Core data found!");

                    elements.put("coredata", coreData);
                    coreData = new HashMap<>();
                    coreDataDone = true;
                }

                if (serverLogDone && coreDataDone) {
                    PowerRanks.getInstance().getLogger().info(
                            "Data collected successfully in " + Duration.between(startTime, Instant.now()).toMillis() + "ms, uploading...");
                    startTime = Instant.now();

                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        String json = objectMapper.writeValueAsString(elements);
                        uploadDump(json);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }

                    PowerRanks.getInstance().getLogger()
                            .info("Done! Uploading took " + Duration.between(startTime, Instant.now()).toMillis() + "ms");

                    this.cancel();
                } else {
                    progressIndex += 1;
                    if (progressIndex > 1200) {
                        PowerRanks.getInstance().getLogger().info("Failed to dump data!");
                        PowerRanks.getInstance().getLogger()
                                .info("Timed out after " + Duration.between(startTime, Instant.now()).toMillis() + "ms!");
                        this.cancel();
                    }
                }
            }
        }.runTaskTimer(PowerRanks.getInstance(), 0, 1);
    }

    private void uploadDump(String outputJSON) {
        DatabinClient client = new DatabinClient(databin_url, "Databinclient/1.0");

        int uploadSize = outputJSON.length() / 1024;

        PowerRanks.getInstance().getLogger().info("Uploading " + uploadSize + "KB, please wait...");

        client.postJSON(outputJSON);
        int updateInterval = 5;
        int timeout = 5;

        new BukkitRunnable() {
            int waitTime = 0;

            @Override
            public void run() {
                PowerRanksVerbose.log("task", "Running task uploading dump data");

                if (client.hasResponse()) {
                    Map<String, String> response = client.getResponse();
                    if (response.keySet().contains("key")) {
                        String key = response.get("key");

                        if (key.length() > 0 && !key.startsWith("[FAILED]")) {
                            PowerRanks.getInstance().getLogger().info("");
                            PowerRanks.getInstance().getLogger().info("===----------" + PowerRanks.pdf.getName() + "----------===");
                            PowerRanks.getInstance().getLogger().info("Data upload is ready " + "[" + logs_powerranks_url + key + "]");
                            PowerRanks.getInstance().getLogger().info("ID: " + key);
                            PowerRanks.getInstance().getLogger().info("Uploaded: " + uploadSize + "KB");
                            PowerRanks.getInstance().getLogger().info("===------------------------------===");
                        } else {
                            PowerRanks.getInstance().getLogger().info("Uploading dump failed, received server error!");
                        }
                    }
                    

                    this.cancel();
                }

                if (waitTime / (20 / updateInterval) > timeout) {

                    PowerRanks.getInstance().getLogger().info("Uploading dump timed-out!");
                    this.cancel();
                }
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
