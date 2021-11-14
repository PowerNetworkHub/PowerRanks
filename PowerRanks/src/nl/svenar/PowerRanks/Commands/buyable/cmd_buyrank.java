package nl.svenar.PowerRanks.Commands.buyable;

import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.VaultHook;
import nl.svenar.PowerRanks.Cache.CachedConfig;
import nl.svenar.PowerRanks.Commands.PowerCommand;
import nl.svenar.PowerRanks.Data.Messages;
import nl.svenar.PowerRanks.Data.Users;

public class cmd_buyrank extends PowerCommand {

	private Users users;

	public cmd_buyrank(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
		this.users = new Users(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (sender.hasPermission("powerranks.cmd.buyrank")) {
			Player player = (Player) sender;
			if (PowerRanks.vaultEconomyEnabled) {
				if (args.length == 0) {
					Messages.messageCommandBuyrank(sender, this.users, null);
				} else if (args.length == 1) {
					final String rankname = this.users.getRankIgnoreCase(args[0]);
					Messages.messageCommandBuyrank(sender, this.users, rankname);
				} else if (args.length == 2) {
					final String rankname = this.users.getRankIgnoreCase(args[0]);
					final boolean confirm = args[1].equalsIgnoreCase("confirm");

					if (confirm) {
						int cost = this.users.getRanksConfigFieldInt(rankname, "economy.cost");
						double player_balance = VaultHook.getVaultEconomy() != null ? VaultHook.getVaultEconomy().getBalance(player) : 0;
						if (cost >= 0 && player_balance >= cost) {
							VaultHook.getVaultEconomy().withdrawPlayer(player, cost);
							this.users.setGroup(player, rankname, true);
							if (CachedConfig.getBoolean("rankup.buy_command.enabled")) {
								if (CachedConfig.getString("rankup.buy_command.command").length() > 0) {
									this.plugin.getServer().dispatchCommand((CommandSender) this.plugin.getServer().getConsoleSender(), CachedConfig.getString("rankup.buy_command.command").replaceAll("%playername%", sender.getName()).replaceAll("%rankname%", rankname));
								}
							}
							Messages.messageBuyRankSuccess(player, rankname);
						} else {
							Messages.messageBuyRankError(player, rankname);
						}
					}
				} else {
					Messages.messageCommandUsageBuyrank(sender);
				}
			} else {
				Messages.messageBuyRankNotAvailable(sender);
			}
		} else {
			Messages.noPermission(sender);
		}

		return false;
	}

	public ArrayList<String> tabCompleteEvent(CommandSender sender, String[] args) {
		ArrayList<String> tabcomplete = new ArrayList<String>();

		if (args.length == 1) {
			for (String rank : this.users.getBuyableRanks(this.users.getGroup((Player) sender))) {
				tabcomplete.add(rank);
			}
		}

		if (args.length == 2) {
			tabcomplete.add("confirm");
		}

		return tabcomplete;
	}
}
