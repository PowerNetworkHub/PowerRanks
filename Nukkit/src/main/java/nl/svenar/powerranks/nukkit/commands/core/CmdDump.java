package nl.svenar.powerranks.nukkit.commands.core;

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

import cn.nukkit.utils.TextFormat;

import cn.nukkit.command.CommandSender;
import cn.nukkit.Player;
import cn.nukkit.scheduler.NukkitRunnable;

import nl.svenar.powerranks.common.http.DatabinClient;
import nl.svenar.powerranks.common.storage.PowerStorageManager;
import nl.svenar.powerranks.common.storage.provided.JSONStorageManager;
import nl.svenar.powerranks.common.utils.AsyncReadFile;
import nl.svenar.powerranks.nukkit.PowerRanks;
import nl.svenar.powerranks.nukkit.commands.PowerCommand;
import nl.svenar.powerranks.nukkit.util.Util;

public class CmdDump extends PowerCommand {

    private String databin_url = "https://databin.svenar.nl";

    private String logs_powerranks_url = "https://logs.powerranks.nl/dump/?id=";

    ArrayList<String> serverLog = new ArrayList<String>();
    
    Map<String, Object> coreData = new HashMap<String, Object>();

    public CmdDump(PowerRanks plugin, String command_name, COMMANDEXECUTOR ce) {
        super(plugin, command_name, ce);
        this.setCommandPermission("powerranks.cmd." + command_name.toLowerCase());
    }

    @Override
    public boolean onCommand(CommandSender sender, String commandLabel, String commandName, String[] args) {
        if (sender instanceof Player) {
            sender.sendMessage(
                    TextFormat.DARK_AQUA + "--------" + TextFormat.DARK_BLUE + plugin.getDescription().getName() + TextFormat.DARK_AQUA + "--------");
            sender.sendMessage(TextFormat.GREEN + "Dump has started collecting data!");
            sender.sendMessage(TextFormat.GREEN + "Check the server console for more information.");
            sender.sendMessage(TextFormat.DARK_AQUA + "--------------------------");
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

        final String serverPath = plugin.getServer().getDataPath();
        final File serverLogFile = new File(serverPath.substring(0, serverPath.length() - 1), "logs/latest.log");

        AsyncReadFile arf = new AsyncReadFile();
        arf.setFile(serverLogFile.getPath());
        long totalLength = arf.getFileLength();
        arf.read();

        new NukkitRunnable() {
            Instant startTime = Instant.now();
            int progressTimerIndex = Integer.MAX_VALUE;

            @Override
            public void run() {
                if (!arf.isReady()) {
                    if (progressTimerIndex > 20 * 10) {
                        progressTimerIndex = 0;
                        int currentLength = arf.getData().length();
                        plugin.getLogger().info("Reading server log... (" + ((100 * currentLength) / totalLength) + "%)");
                    }
                    progressTimerIndex++;
                    return;
                } else {
                    plugin.getLogger().info("Server log:");

                    plugin.getLogger().info("- Removing sensitive information from server log...");
                    for (String line : arf.getData().split("\n")) {
                        serverLog.add(removeIP(line).replaceAll("\"", "'"));
                    }
                    plugin.getLogger()
                            .info("- Reading took " + Duration.between(startTime, Instant.now()).toMillis() + "ms");
                    plugin.getLogger()
                            .info("- Read " + arf.getData().split("\n").length + " lines (" + arf.getData().length() + " characters)!");
                    this.cancel();
                    plugin.getLogger().info("");
                }
            }
        }.runTaskTimer(plugin, 0, 1);
    }

    private void readCoreData() {
        coreData = new HashMap<>();

        new NukkitRunnable() {
            Instant startTime = Instant.now();

            @Override
            public void run() {
                plugin.getLogger().info("Plugin data:");

                JSONStorageManager jsonmanager = new JSONStorageManager(Util.DATA_DIR, "dummyRanks.json", "dummyPlayers.json");
                PowerStorageManager powermanager = plugin.getStorageManager();

                jsonmanager.setRanks(powermanager.getRanks());
                jsonmanager.setPlayers(powermanager.getPlayers());

                // jsonmanager.getRanksAsJSON(false);
                // jsonmanager.getPlayersAsJSON(false);
                // PowerRanks.getUsertagManager().toJSON("usertags", false);

                coreData.put("rankdata", jsonmanager.getRanksAsMap());
                coreData.put("playerdata", jsonmanager.getPlayersAsMap());
                coreData.put("config", plugin.getConfigManager().getRawData());
                coreData.put("usertags", plugin.getUsertagStorage().getRawData().get("usertags"));

                plugin.getLogger().info("- Reading took " + Duration.between(startTime, Instant.now()).toMillis() + "ms");
                plugin.getLogger().info("- Read " + jsonmanager.getRanks().size() + " ranks and " + jsonmanager.getPlayers().size() + " players!");
                plugin.getLogger().info("- Adding configuration & usertags");

                jsonmanager.removeAllData();
                plugin.getLogger().info("");

                this.cancel();

            }
        }.runTaskTimer(plugin, 0, 1);
    }

    private void prepareLog(CommandSender sender, String commandName) {
        plugin.getLogger().info("");
        plugin.getLogger().info("=== -------------------------------- ===");
        plugin.getLogger().info("     PowerRanks is collecting data!     ");
        plugin.getLogger().info("=== -------------------------------- ===");

        readServerLog();
        readCoreData();

        new NukkitRunnable() {
            Instant startTime = Instant.now();
            int progressIndex = 0;
            Map<String, Object> elements = new HashMap<>();
            boolean serverLogDone = false;
            boolean coreDataDone = false;

            @Override
            @SuppressWarnings("unchecked")
            public void run() {
                if (serverLog.size() > 0) {
                    plugin.getLogger().info("Server log found!");

                    elements.put("type", "dump");
                    elements.put("version", new HashMap<>());
                    ((Map<String, Object>) elements.get("version")).put("powerranks", plugin.getDescription().getVersion());
                    ((Map<String, Object>) elements.get("version")).put("server",
                    plugin.getServer().getVersion() + " | " + plugin.getServer().getNukkitVersion());

                    // TODO
                    // String pluginData = "";
                    // Plugin[] plugins = plugin.getPluginManager().getPlugins();
                    // for (Plugin plugin : plugins) {
                    //     pluginData += plugin.getName() + "(" + plugin.getDescription().getVersion() + "),";
                    // }
                    // pluginData = pluginData.substring(0, pluginData.length() - 1);
                    // elements.put("plugins", pluginData);

                    elements.put("serverlog", Arrays.asList(serverLog));

                    serverLog = new ArrayList<>();
                    serverLogDone = true;
                }

                if (coreData.keySet().size() > 0) {
                    plugin.getLogger().info("Core data found!");

                    elements.put("coredata", coreData);
                    coreData = new HashMap<>();
                    coreDataDone = true;
                }

                if (serverLogDone && coreDataDone) {
                    plugin.getLogger().info(
                            "Data collected successfully in " + Duration.between(startTime, Instant.now()).toMillis() + "ms, uploading...");
                    startTime = Instant.now();

                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        String json = objectMapper.writeValueAsString(elements);
                        uploadDump(json);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }

                    plugin.getLogger()
                            .info("Done! Uploading took " + Duration.between(startTime, Instant.now()).toMillis() + "ms");

                    this.cancel();
                } else {
                    progressIndex += 1;
                    if (progressIndex > 1200) {
                        plugin.getLogger().info("Failed to dump data!");
                        plugin.getLogger()
                                .info("Timed out after " + Duration.between(startTime, Instant.now()).toMillis() + "ms!");
                        this.cancel();
                    }
                }
            }
        }.runTaskTimer(plugin, 0, 1);
    }

    private void uploadDump(String outputJSON) {
        DatabinClient client = new DatabinClient(databin_url, "Databinclient/1.0");

        int uploadSize = outputJSON.length() / 1024;

        plugin.getLogger().info("Uploading " + uploadSize + "KB, please wait...");

        client.postJSON(outputJSON);
        int updateInterval = 5;
        int timeout = 5;

        new NukkitRunnable() {
            int waitTime = 0;

            @Override
            public void run() {
                if (client.hasResponse()) {
                    Map<String, String> response = client.getResponse();
                    if (response.keySet().contains("key")) {
                        String key = response.get("key");

                        if (key.length() > 0 && !key.startsWith("[FAILED]")) {
                            plugin.getLogger().info("");
                            plugin.getLogger().info("===----------" + plugin.getDescription().getName() + "----------===");
                            plugin.getLogger().info("Data upload is ready " + "[" + logs_powerranks_url + key + "]");
                            plugin.getLogger().info("ID: " + key);
                            plugin.getLogger().info("Uploaded: " + uploadSize + "KB");
                            plugin.getLogger().info("===------------------------------===");
                        } else {
                            plugin.getLogger().info("Uploading dump failed, received server error!");
                        }
                    }
                    

                    this.cancel();
                }

                if (waitTime / (20 / updateInterval) > timeout) {

                    plugin.getLogger().info("Uploading dump timed-out!");
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 0, updateInterval);
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
