package nl.svenar.powerranks.nukkit.commands.rank;

import java.util.ArrayList;

import com.google.common.collect.ImmutableMap;

import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import cn.nukkit.Player;
import nl.svenar.powerranks.common.structure.PRPermission;
import nl.svenar.powerranks.common.structure.PRRank;
import nl.svenar.powerranks.common.utils.PRUtil;
import nl.svenar.powerranks.common.utils.PowerColor;
import nl.svenar.powerranks.nukkit.PowerRanks;
import nl.svenar.powerranks.common.utils.PRCache;
import nl.svenar.powerranks.nukkit.commands.PowerCommand;

public class cmd_rankinfo extends PowerCommand {

    public cmd_rankinfo(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
        super(plugin, command_name, ce);
        this.setCommandPermission("powerranks.cmd." + command_name.toLowerCase());
    }

    @Override
    public boolean onCommand(CommandSender sender, String commandLabel, String commandName, String[] args) {
        if (args.length == 1) {
            String target_rank_name = args[0];
            PRRank target_rank = PRCache.getRank(target_rank_name);
            if (target_rank != null) {
                messageRankInfo(sender, target_rank, 0);
            } else {
                sender.sendMessage(PRUtil.powerFormatter(
                        plugin.getLanguageManager().getFormattedMessage(
                                "general.rank-not-found"),
                        ImmutableMap.<String, String>builder()
                                .put("player", sender.getName())
                                .put("rank", target_rank_name)
                                .build(),
                        '[', ']'));
            }
        } else if (args.length == 2) {
            try {
                String target_rank_name = args[0];
                int page = Integer.parseInt(args[1].replaceAll("[a-zA-Z]", ""));
                PRRank target_rank = PRCache.getRank(target_rank_name);
                if (target_rank != null) {
                    messageRankInfo(sender, target_rank, page);
                } else {
                    sender.sendMessage(PRUtil.powerFormatter(
                            plugin.getLanguageManager().getFormattedMessage(
                                    "general.rank-not-found"),
                            ImmutableMap.<String, String>builder()
                                    .put("player", sender.getName())
                                    .put("rank", target_rank_name)
                                    .build(),
                            '[', ']'));
                }
            } catch (NumberFormatException e) {
                sender.sendMessage(
                        plugin.getLanguageManager().getFormattedUsageMessage(commandLabel, commandName,
                                "commands." + commandName.toLowerCase() + ".arguments", sender instanceof Player));
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
            for (PRRank rank : PRCache.getRanks()) {
                tabcomplete.add(rank.getName());
            }
        }

        return tabcomplete;
    }

    public void messageRankInfo(CommandSender sender, PRRank rank, int page) {
        int targetPage = page;

        String formattedInheritances = "";
        for (String rankname : rank.getInheritances()) {
            formattedInheritances += rankname + " ";
        }
        if (formattedInheritances.endsWith(" ")) {
            formattedInheritances = formattedInheritances.substring(0, formattedInheritances.length() - 1);
        }

        String formatted_buyableranks = "";
        for (String rankname : rank.getBuyableRanks()) {
            formattedInheritances += rankname + " ";
        }
        if (formattedInheritances.endsWith(" ")) {
            formattedInheritances = formattedInheritances.substring(0, formattedInheritances.length() - 1);
        }

        sender.sendMessage(TextFormat.BLUE + "===" + TextFormat.DARK_AQUA + "----------" + TextFormat.AQUA
                + plugin.getDescription().getName() + TextFormat.DARK_AQUA + "----------" + TextFormat.BLUE
                + "===");
        sender.sendMessage(TextFormat.GREEN + "Name: " + TextFormat.DARK_GREEN + rank.getName());
        sender.sendMessage(TextFormat.GREEN + "Weight: " + TextFormat.DARK_GREEN + rank.getWeight());
        sender.sendMessage(
                TextFormat.GREEN + "Prefix: " + TextFormat.RESET + plugin.getPowerColor()
                        .format(PowerColor.UNFORMATTED_COLOR_CHAR, rank.getPrefix(), true, false, false));
        sender.sendMessage(
                TextFormat.GREEN + "Suffix: " + TextFormat.RESET + plugin.getPowerColor()
                        .format(PowerColor.UNFORMATTED_COLOR_CHAR, rank.getSuffix(), true, false, false));
        sender.sendMessage(TextFormat.GREEN + "Chat format: " + TextFormat.RESET
                + getSampleChatFormat(null, sender.getName(), "world", rank));
        sender.sendMessage(TextFormat.YELLOW + "Buyable ranks: " + TextFormat.GOLD + formatted_buyableranks);
        sender.sendMessage(TextFormat.YELLOW + "Buy cost: " + TextFormat.GOLD + rank.getBuyCost());
        sender.sendMessage(TextFormat.YELLOW + "Buy description: " + TextFormat.GOLD + rank.getBuyDescription());
        sender.sendMessage(TextFormat.YELLOW + "Buy command: " + TextFormat.GOLD + rank.getBuyCommand());
        sender.sendMessage(TextFormat.GREEN + "Inheritance(s): " + TextFormat.DARK_GREEN + formattedInheritances);
        sender.sendMessage(TextFormat.GREEN + "Effective Permissions: ");

        ArrayList<PRPermission> playerPermissions = rank.getPermissions();
        int linesPerPage = sender instanceof Player ? 5 : 10;
        int lastPage = playerPermissions.size() / linesPerPage;

        if (!(sender instanceof Player)) {
            targetPage -= 1;
        }

        targetPage = targetPage < 0 ? 0 : targetPage;
        targetPage = targetPage > lastPage ? lastPage : targetPage;

        sender.sendMessage(TextFormat.AQUA + "Page " + TextFormat.BLUE + (targetPage + 1) + TextFormat.AQUA + "/"
                + TextFormat.BLUE + (lastPage + 1));
        sender.sendMessage(TextFormat.AQUA + "Next page " + TextFormat.BLUE + "/pr" + " rankinfo "
                + rank.getName() + " " + TextFormat.BLUE + (targetPage + 2 > lastPage + 1 ? lastPage + 1 : targetPage + 2));

        int lineIndex = 0;
        for (PRPermission permission : playerPermissions) {
            if (lineIndex >= targetPage * linesPerPage && lineIndex < targetPage * linesPerPage + linesPerPage) {
                sender.sendMessage(TextFormat.DARK_GREEN + "#" + (lineIndex + 1) + ". "
                        + (!permission.getValue() ? TextFormat.RED : TextFormat.GREEN) + permission.getName());
            }
            lineIndex += 1;
        }

        sender.sendMessage(TextFormat.BLUE + "===" + TextFormat.DARK_AQUA + "------------------------------"
                + TextFormat.BLUE + "===");
    }

    private String getSampleChatFormat(CommandSender sender, String name, String world, PRRank rank) {
        String playersChatMessage = "message";

        String format = plugin.getConfigManager().getString("chat.format", "");

        String formattedPrefix = "";
        String formattedSuffix = "";
        String chatColor = rank.getChatcolor();
        String nameColor = rank.getNamecolor();
        String usertag = "";

        formattedPrefix += rank.getPrefix();
        formattedSuffix += rank.getSuffix();

        String playerFormattedName = (nameColor.length() == 0 ? "&r" : nameColor) + name;
        String playerFormattedChatMessage = (chatColor.length() == 0 ? "&r" : chatColor) + playersChatMessage;

        format = PRUtil.powerFormatter(format, ImmutableMap.<String, String>builder().put("prefix", formattedPrefix)
                .put("suffix", formattedSuffix)
                .put("usertag", usertag)
                .put("player", playerFormattedName).put("msg", playerFormattedChatMessage)
                .put("world", world).build(), '[', ']');

        format = plugin.getPowerColor().format(PowerColor.UNFORMATTED_COLOR_CHAR, rank.getSuffix(), true, false, false);

        return format;
    }
}
