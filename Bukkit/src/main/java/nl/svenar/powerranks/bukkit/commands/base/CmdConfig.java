package nl.svenar.powerranks.bukkit.commands.base;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.command.CommandSender;

import com.google.common.collect.ImmutableMap;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;
import nl.svenar.powerranks.bukkit.PowerRanks;
import nl.svenar.powerranks.bukkit.commands.PowerBaseCommand;

@CommandAlias("%powerrankscommand")
@Description("Change PowerRanks configuration settings")
public class CmdConfig extends PowerBaseCommand {

    public CmdConfig(PowerRanks plugin) {
        super(plugin);
    }

    @Subcommand("config removeworldtag")
    @CommandPermission("powerranks.cmd.config")
    public void onConfigRemoveworldtag(CommandSender sender) {
        String world_tag_regex = "[ ]{0,1}([&][a-fA-F0-9k-oK-OrR]){0,1}((&){0,1}[#][a-fA-F0-9]{6}){0,1}[ ]{0,1}[\\[]world[\\]][ ]{0,1}([&][a-fA-F0-9k-oK-OrR]){0,1}((&){0,1}[#][a-fA-F0-9]{6}){0,1}[ ]{0,1}";
        Pattern world_tag_pattern = Pattern.compile(world_tag_regex);
        Matcher world_tag_matcher_chat = world_tag_pattern
                .matcher(PowerRanks.getConfigManager().getString("chat.format", "").toLowerCase());
        Matcher world_tag_matcher_tab = world_tag_pattern.matcher(
                PowerRanks.getConfigManager().getString("tablist_modification.format", "").toLowerCase());

        while (world_tag_matcher_chat.find()) {
            int start = world_tag_matcher_chat.start();
            int end = world_tag_matcher_chat.end();
            PowerRanks.getConfigManager().setString("chat.format",
                    PowerRanks.getConfigManager().getString("chat.format", "").replace(
                            PowerRanks.getConfigManager().getString("chat.format", "").substring(start, end),
                            ""));
        }

        while (world_tag_matcher_tab.find()) {
            int start = world_tag_matcher_tab.start();
            int end = world_tag_matcher_tab.end();
            PowerRanks.getConfigManager()
                    .setString("tablist_modification.format",
                            PowerRanks.getConfigManager().getString("tablist_modification.format", "")
                                    .replace(PowerRanks.getConfigManager()
                                            .getString("tablist_modification.format", "").substring(start, end),
                                            ""));
        }

        this.plugin.updateAllPlayersTABlist();

        sendMessage(sender, "config-removed-world-tag", ImmutableMap.of( //
                "player", sender.getName() //
        ), true);
    }

    @Subcommand("config enable|disable chat_formatting|tablist_formatting|casesensitive_permissions|op|bungeecord")
    @CommandPermission("powerranks.cmd.config")
    public void onConfigEnableDisable(CommandSender sender, String enableDisable, String targetOption) {
        boolean enable = enableDisable.equalsIgnoreCase("enable");

        switch (targetOption.toLowerCase()) {
            case "chat_formatting":
                sendMessage(sender, "config-state-changed", ImmutableMap.of( //
                        "player", sender.getName(), //
                        "config_target", "Chat formatting", //
                        "old_state", String.valueOf(PowerRanks.getConfigManager().getBool("chat.enabled", true)), //
                        "new_state", String.valueOf(enable) //
                ), true);
                PowerRanks.getConfigManager().setBool("chat.enabled", enable);
                break;
            case "tablist_formatting":
                sendMessage(sender, "config-state-changed", ImmutableMap.of( //
                        "player", sender.getName(), //
                        "config_target", "Tablist formatting", //
                        "old_state",
                        String.valueOf(PowerRanks.getConfigManager().getBool("tablist_modification.enabled", true)), //
                        "new_state", String.valueOf(enable) //
                ), true);
                PowerRanks.getConfigManager().setBool("tablist_modification.enabled", enable);
                break;
            case "casesensitive_permissions":
                sendMessage(sender, "config-state-changed", ImmutableMap.of( //
                        "player", sender.getName(), //
                        "config_target", "Case Sensitive Permissions", //
                        "old_state",
                        String.valueOf(
                                PowerRanks.getConfigManager().getBool("general.case-sensitive-permissions", true)), //
                        "new_state", String.valueOf(enable) //
                ), true);
                PowerRanks.getConfigManager().setBool("general.case-sensitive-permissions", enable);
                break;
            case "op":
                sendMessage(sender, "config-state-changed", ImmutableMap.of( //
                        "player", sender.getName(), //
                        "config_target", "Bungeecord Integration", //
                        "old_state", String.valueOf(!PowerRanks.getConfigManager().getBool("general.disable-op", true)), //
                        "new_state", String.valueOf(enable) //
                ), true);
                PowerRanks.getConfigManager().setBool("general.disable-op", !enable);
                break;
            case "bungeecord":
                sendMessage(sender, "config-state-changed", ImmutableMap.of( //
                        "player", sender.getName(), //
                        "config_target", "Chat formatting", //
                        "old_state", String.valueOf(PowerRanks.getConfigManager().getBool("bungeecord.enabled", true)), //
                        "new_state", String.valueOf(enable) //
                ), true);
                PowerRanks.getConfigManager().setBool("bungeecord.enabled", enable);
                break;
        }
    }

    @Subcommand("config set playtime_update_interval")
    @CommandPermission("powerranks.cmd.config")
    @Syntax("<interval>")
    @CommandCompletion("1|5|10|30|60")
    public void onConfigSetPlaytimeUpdateInterval(CommandSender sender, int interval) {
        sendMessage(sender, "config-state-changed", ImmutableMap.of( //
                "player", sender.getName(), //
                "config_target", "Player playtime update interval", //
                "old_state", String.valueOf(PowerRanks.getConfigManager().getInt("general.playtime-update-interval", 60)), //
                "new_state", String.valueOf(interval) //
        ), true);

        PowerRanks.getConfigManager().setInt("general.playtime-update-interval", interval);
    }

    @Subcommand("config set autosave_files_interval")
    @CommandPermission("powerranks.cmd.config")
    @Syntax("<interval>")
    @CommandCompletion("60|300|600|1200|1800|3600")
    public void onConfigSetAutosaveDataInterval(CommandSender sender, int interval) {
        sendMessage(sender, "config-state-changed", ImmutableMap.of( //
                "player", sender.getName(), //
                "config_target", "Data autosave interval", //
                "old_state", String.valueOf(PowerRanks.getConfigManager().getInt("general.autosave-files-interval", 600)), //
                "new_state", String.valueOf(interval) //
        ), true);

        PowerRanks.getConfigManager().setInt("general.autosave-files-interval", interval);
    }

    @Subcommand("config set language")
    @CommandPermission("powerranks.cmd.config")
    @Syntax("<language>")
    @CommandCompletion("en")
    public void onConfigSetLanguage(CommandSender sender, String language) {
        sendMessage(sender, "config-state-changed", ImmutableMap.of( //
                "player", sender.getName(), //
                "config_target", "PowerRanks language (lang.yml)", //
                "old_state", String.valueOf(PowerRanks.getConfigManager().getString("general.language", "en")), //
                "new_state", language //
        ), true);

        PowerRanks.getConfigManager().setString("general.language", language);
    }

    @Subcommand("config set bungeecord_servername")
    @CommandPermission("powerranks.cmd.config")
    @Syntax("<servername>")
    @CommandCompletion("Global")
    public void onConfigSetBungeeServerName(CommandSender sender, String servername) {
        sendMessage(sender, "config-state-changed", ImmutableMap.of( //
                "player", sender.getName(), //
                "config_target", "PowerRanks Bungeecord server name", //
                "old_state", String.valueOf(PowerRanks.getConfigManager().getString("bungeecors.server-name", "Global")), //
                "new_state", servername //
        ), true);

        PowerRanks.getConfigManager().setString("bungeecors.server-name", servername);
    }
}
