package nl.svenar.powerranks.nukkit.commands.core;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.ImmutableMap;

import nl.svenar.powerranks.nukkit.PowerRanks;
import nl.svenar.powerranks.nukkit.commands.PowerCommand;
import nl.svenar.powerranks.common.utils.PRUtil;


import cn.nukkit.command.CommandSender;
import cn.nukkit.Player;

public class cmd_config extends PowerCommand {

    public cmd_config(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
        super(plugin, command_name, ce);
        this.setCommandPermission("powerranks.cmd." + command_name.toLowerCase());
    }

    @Override
    public boolean onCommand(CommandSender sender, String commandLabel, String commandName, String[] args) {
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("removeworldtag")) {
                String world_tag_regex = "[ ]{0,1}([&][a-fA-F0-9k-oK-OrR]){0,1}((&){0,1}[#][a-fA-F0-9]{6}){0,1}[ ]{0,1}[\\[]world[\\]][ ]{0,1}([&][a-fA-F0-9k-oK-OrR]){0,1}((&){0,1}[#][a-fA-F0-9]{6}){0,1}[ ]{0,1}";
                Pattern world_tag_pattern = Pattern.compile(world_tag_regex);
                Matcher world_tag_matcher_chat = world_tag_pattern
                        .matcher(plugin.getConfigManager().getString("chat.format", "").toLowerCase());
                Matcher world_tag_matcher_tab = world_tag_pattern.matcher(
                    plugin.getConfigManager().getString("tablist_modification.format", "").toLowerCase());

                while (world_tag_matcher_chat.find()) {
                    int start = world_tag_matcher_chat.start();
                    int end = world_tag_matcher_chat.end();
                    plugin.getConfigManager().setString("chat.format",
                    plugin.getConfigManager().getString("chat.format", "").replace(
                        plugin.getConfigManager().getString("chat.format", "").substring(start, end),
                                    ""));
                }

                while (world_tag_matcher_tab.find()) {
                    int start = world_tag_matcher_tab.start();
                    int end = world_tag_matcher_tab.end();
                    plugin.getConfigManager()
                            .setString("tablist_modification.format",
                            plugin.getConfigManager().getString("tablist_modification.format", "")
                                            .replace(plugin.getConfigManager()
                                                    .getString("tablist_modification.format", "").substring(start, end),
                                                    ""));
                }

                sender.sendMessage(PRUtil.powerFormatter(
                        plugin.getLanguageManager().getFormattedMessage(
                                "commands." + commandName.toLowerCase() + ".removed-world-tag"),
                        ImmutableMap.<String, String>builder()
                                .put("player", sender.getName())
                                .build(),
                        '[', ']'));
            } else {
                sender.sendMessage(
                        plugin.getLanguageManager().getFormattedUsageMessage(commandLabel, commandName,
                                "commands." + commandName.toLowerCase() + ".arguments", sender instanceof Player));
            }
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("enable") || args[0].equalsIgnoreCase("disable")) {
                boolean enable = args[0].equalsIgnoreCase("enable");
                if (args[1].equalsIgnoreCase("chat_formatting")) {

                    sender.sendMessage(PRUtil.powerFormatter(
                            plugin.getLanguageManager().getFormattedMessage(
                                    "commands." + commandName.toLowerCase() + ".state-changed"),
                            ImmutableMap.<String, String>builder()
                                    .put("player", sender.getName())
                                    .put("config_target", "Chat formatting")
                                    .put("old_state",
                                            String.valueOf(plugin.getConfigManager().getBool("chat.enabled", true)))
                                    .put("new_state", String.valueOf(enable))
                                    .build(),
                            '[', ']'));

                            plugin.getConfigManager().setBool("chat.enabled", enable);

                } else if (args[1].equalsIgnoreCase("tablist_formatting")) {
                    sender.sendMessage(PRUtil.powerFormatter(
                            plugin.getLanguageManager().getFormattedMessage(
                                    "commands." + commandName.toLowerCase() + ".state-changed"),
                            ImmutableMap.<String, String>builder()
                                    .put("player", sender.getName())
                                    .put("config_target", "Tablist formatting")
                                    .put("old_state",
                                            String.valueOf(plugin.getConfigManager()
                                                    .getBool("tablist_modification.enabled", true)))
                                    .put("new_state", String.valueOf(enable))
                                    .build(),
                            '[', ']'));

                            plugin.getConfigManager().setBool("tablist_modification.enabled", enable);

                } else if (args[1].equalsIgnoreCase("casesensitive_permissions")) {
                    sender.sendMessage(PRUtil.powerFormatter(
                            plugin.getLanguageManager().getFormattedMessage(
                                    "commands." + commandName.toLowerCase() + ".state-changed"),
                            ImmutableMap.<String, String>builder()
                                    .put("player", sender.getName())
                                    .put("config_target", "Case Sensitive Permissions")
                                    .put("old_state",
                                            String.valueOf(plugin.getConfigManager()
                                                    .getBool("general.case-sensitive-permissions", true)))
                                    .put("new_state", String.valueOf(enable))
                                    .build(),
                            '[', ']'));

                            plugin.getConfigManager().setBool("general.case-sensitive-permissions", enable);

                } else if (args[1].equalsIgnoreCase("op")) {
                    sender.sendMessage(PRUtil.powerFormatter(
                            plugin.getLanguageManager().getFormattedMessage(
                                    "commands." + commandName.toLowerCase() + ".state-changed"),
                            ImmutableMap.<String, String>builder()
                                    .put("player", sender.getName())
                                    .put("config_target", "Op command")
                                    .put("old_state",
                                            String.valueOf(!plugin.getConfigManager()
                                                    .getBool("general.disable-op", true)))
                                    .put("new_state", String.valueOf(enable))
                                    .build(),
                            '[', ']'));

                            plugin.getConfigManager().setBool("general.disable-op", !enable);

                } else if (args[1].equalsIgnoreCase("bungeecord")) {
                    sender.sendMessage(PRUtil.powerFormatter(
                            plugin.getLanguageManager().getFormattedMessage(
                                    "commands." + commandName.toLowerCase() + ".state-changed"),
                            ImmutableMap.<String, String>builder()
                                    .put("player", sender.getName())
                                    .put("config_target", "Bungeecord integration")
                                    .put("old_state",
                                            String.valueOf(plugin.getConfigManager()
                                                    .getBool("bungeecord.enabled", false)))
                                    .put("new_state", String.valueOf(enable))
                                    .build(),
                            '[', ']'));

                            plugin.getConfigManager().setBool("bungeecord.enabled", enable);

                } else {
                    sender.sendMessage(
                            plugin.getLanguageManager().getFormattedUsageMessage(commandLabel, commandName,
                                    "commands." + commandName.toLowerCase() + ".arguments", sender instanceof Player));
                }
            } else {
                sender.sendMessage(
                        plugin.getLanguageManager().getFormattedUsageMessage(commandLabel, commandName,
                                "commands." + commandName.toLowerCase() + ".arguments", sender instanceof Player));
            }
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("set")) {

                if (args[1].equalsIgnoreCase("playtime_update_interval")) {
                    try {
                        int time = Integer.parseInt(args[2]);

                        sender.sendMessage(PRUtil.powerFormatter(
                                plugin.getLanguageManager().getFormattedMessage(
                                        "commands." + commandName.toLowerCase() + ".state-changed"),
                                ImmutableMap.<String, String>builder()
                                        .put("player", sender.getName())
                                        .put("config_target", "Player playtime update interval")
                                        .put("old_state",
                                                String.valueOf(plugin.getConfigManager()
                                                        .getInt("general.playtime-update-interval", 60)))
                                        .put("new_state", String.valueOf(time))
                                        .build(),
                                '[', ']'));

                                plugin.getConfigManager().setInt("general.playtime-update-interval", time);
                    } catch (Exception e) {
                        sender.sendMessage(
                                plugin.getLanguageManager().getFormattedMessage(
                                        "commands." + commandName.toLowerCase() + ".numbers-only"));
                    }
                }

            } else if (args[1].equalsIgnoreCase("language")) {
                sender.sendMessage(PRUtil.powerFormatter(
                        plugin.getLanguageManager().getFormattedMessage(
                                "commands." + commandName.toLowerCase() + ".state-changed"),
                        ImmutableMap.<String, String>builder()
                                .put("player", sender.getName())
                                .put("config_target", "PowerRanks language (lang.yml)")
                                .put("old_state",
                                        String.valueOf(plugin.getConfigManager()
                                                .getString("general.language", "en")))
                                .put("new_state", args[2])
                                .build(),
                        '[', ']'));

                        plugin.getConfigManager().setString("general.language", args[2]);

            } else if (args[1].equalsIgnoreCase("bungeecord_server_name")) {
                sender.sendMessage(PRUtil.powerFormatter(
                        plugin.getLanguageManager().getFormattedMessage(
                                "commands." + commandName.toLowerCase() + ".state-changed"),
                        ImmutableMap.<String, String>builder()
                                .put("player", sender.getName())
                                .put("config_target", "PowerRanks Bungeecord server name")
                                .put("old_state",
                                        String.valueOf(plugin.getConfigManager()
                                                .getString("bungeecors.server-name", "Global")))
                                .put("new_state", args[2])
                                .build(),
                        '[', ']'));

                        plugin.getConfigManager().setString("bungeecors.server-name", args[2]);

            } else if (args[1].equalsIgnoreCase("autosave_files_interval")) {
                try {
                    int time = Integer.parseInt(args[2]);

                    sender.sendMessage(PRUtil.powerFormatter(
                            plugin.getLanguageManager().getFormattedMessage(
                                    "commands." + commandName.toLowerCase() + ".state-changed"),
                            ImmutableMap.<String, String>builder()
                                    .put("player", sender.getName())
                                    .put("config_target", "Data autosave interval")
                                    .put("old_state",
                                            String.valueOf(plugin.getConfigManager()
                                                    .getInt("general.autosave-files-interval", 600)))
                                    .put("new_state", String.valueOf(time))
                                    .build(),
                            '[', ']'));

                            plugin.getConfigManager().setInt("general.autosave-files-interval", time);
                } catch (Exception e) {
                    sender.sendMessage(
                            plugin.getLanguageManager().getFormattedMessage(
                                    "commands." + commandName.toLowerCase() + ".numbers-only"));
                }
            }
        } else {
            sender.sendMessage(
                    plugin.getLanguageManager().getFormattedUsageMessage(commandLabel, commandName,
                            "commands." + commandName.toLowerCase() + ".arguments", sender instanceof Player));
        }

        return false;
    }

    public ArrayList<String> tabCompleteEvent(CommandSender sender, String[] args) {
        ArrayList<String> tabcomplete = new ArrayList<String>();

        if (args.length == 1) {
            tabcomplete.add("removeworldtag");
            tabcomplete.add("enable");
            tabcomplete.add("disable");
            tabcomplete.add("set");
        }

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("enable") || args[0].equalsIgnoreCase("disable")) {
                tabcomplete.add("chat_formatting");
                tabcomplete.add("tablist_formatting");
                tabcomplete.add("casesensitive_permissions");
                tabcomplete.add("op");
                tabcomplete.add("bungeecord");
            }
        }

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("set")) {
                tabcomplete.add("language");
                tabcomplete.add("autosave_files_interval");
                tabcomplete.add("playtime_update_interval");
                tabcomplete.add("bungeecord_server_name");
            }
        }

        if (args.length == 3) {
            if (args[1].equalsIgnoreCase("language")) {
                for (String key : plugin.getLanguageManager().getInstance().getKeys("lang")) {
                    tabcomplete.add(key);
                }
            }
        }

        if (args.length == 3) {
            if (args[1].equalsIgnoreCase("autosave_files_interval")) {
                tabcomplete.add("60");
                tabcomplete.add("300");
                tabcomplete.add("600");
                tabcomplete.add("1800");
                tabcomplete.add("3600");
            }
        }

        if (args.length == 3) {
            if (args[1].equalsIgnoreCase("playtime_update_interval")) {
                tabcomplete.add("1");
                tabcomplete.add("5");
                tabcomplete.add("10");
                tabcomplete.add("60");
            }
        }

        return tabcomplete;
    }
}
