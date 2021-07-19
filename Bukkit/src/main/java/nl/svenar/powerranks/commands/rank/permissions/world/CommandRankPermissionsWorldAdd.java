package nl.svenar.powerranks.commands.rank.permissions.world;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import nl.svenar.powerranks.PowerRanks;
import nl.svenar.powerranks.commands.PowerCommand;
import nl.svenar.powerranks.data.PRPermission;
import nl.svenar.powerranks.data.PRRank;
import nl.svenar.powerranks.handlers.BaseDataHandler;

public class CommandRankPermissionsWorldAdd extends PowerCommand {

    public CommandRankPermissionsWorldAdd(PowerRanks plugin, COMMAND_EXECUTOR ce, boolean showInHelp) {
        super(plugin, ce, showInHelp);
    }

    @Override
    public String getArgumentSuggestions() {
        return "<rankname> <permission.node> <worldname>";
    }

    @Override
    public String getDescription() {
        return "Add a world to a permission node on a rank";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!sender.hasPermission("powerranks.command.rank.permissions.world.add")) {
            sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.RED
                    + plugin.getLangConfig().getNode("plugin.commands.no-permission"));
            return false;
        }

        if (args.length == 3 || args.length == 4) {
            String rankName = args[0];
            String permissionName = args[1];
            String worldName = args[2];

            /*
             * 
             * return POWERRANKS_API_STATE.RANK_PERMISSION_WORLD_ADDED_SUCCESSFULLY; } else
             * { return POWERRANKS_API_STATE.RANK_PERMISSION_FAILED_WORLD_ALREADY_EXISTS; }
             * } else { return POWERRANKS_API_STATE.RANK_PERMISSION_FAILED_WORLD_NOT_FOUND;
             * }
             * 
             * } else { return POWERRANKS_API_STATE.RANK_FAILED_PERMISSION_NOT_FOUND; } }
             * else { return POWERRANKS_API_STATE.RANK_FAILED_NAME_NOT_FOUND;
             * 
             */

            switch (PowerRanks.getAPI().addWorldToRankPermission(rankName, permissionName, worldName)) {
                case RANK_FAILED_NAME_NOT_FOUND:
                    sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.RED
                            + plugin.getLangConfig().getNode("plugin.commands.rank.general.does-not-exists"));
                    break;
                case RANK_FAILED_PERMISSION_NOT_FOUND:
                    sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.RED
                            + plugin.getLangConfig().getNode("plugin.commands.rank.general.permission-does-not-exist"));
                    break;
                case RANK_PERMISSION_FAILED_WORLD_NOT_FOUND:
                    sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.RED
                            + plugin.getLangConfig().getNode("plugin.commands.invalid-worldname-argument"));
                    break;

                case RANK_PERMISSION_FAILED_WORLD_ALREADY_EXISTS:
                    sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.RED + plugin.getLangConfig()
                            .getNode("plugin.commands.rank.permissions.add.world.already-exists"));
                    break;

                case RANK_PERMISSION_WORLD_ADDED_SUCCESSFULLY:
                    sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.GREEN + plugin.getLangConfig()
                            .getNode("plugin.commands.rank.permissions.add.world.added-successfully"));
                    break;
                default:
                    break;
            }

        } else if (args.length > 3) {
            sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.RED
                    + plugin.getLangConfig().getNode("plugin.commands.too-many-arguments"));
        } else {
            sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.RED
                    + plugin.getLangConfig().getNode("plugin.commands.insufficient-arguments"));
        }
        return false;
    }

    @Override
    public ArrayList<String> tabCompleteEvent(CommandSender sender, String[] args) {
        ArrayList<String> list = new ArrayList<String>();
        if (args.length == 1) {
            for (PRRank rank : BaseDataHandler.getRanks()) {
                list.add(rank.getName());
            }
        }

        if (args.length == 2) {
            PRRank rank = BaseDataHandler.getRank(args[0]);
            if (rank != null) {
                for (PRPermission permission : rank.getPermissions()) {
                    list.add(permission.getName());
                }
            }
        }

        if (args.length == 3) {
            for (World world : Bukkit.getServer().getWorlds()) {
                list.add(world.getName());
            }
        }

        return list;
    }
}