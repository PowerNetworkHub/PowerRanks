package nl.svenar.powerranks.bukkit.commands.buyable;

import java.util.ArrayList;

import com.google.common.collect.ImmutableMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import nl.svenar.powerranks.common.structure.PRPlayerRank;
import nl.svenar.powerranks.common.structure.PRRank;
import nl.svenar.powerranks.common.utils.PRUtil;
import nl.svenar.powerranks.bukkit.PowerRanks;
import nl.svenar.powerranks.bukkit.cache.CacheManager;
import nl.svenar.powerranks.bukkit.commands.PowerCommand;
import nl.svenar.powerranks.bukkit.data.Messages;
import nl.svenar.powerranks.bukkit.data.Users;
import nl.svenar.powerranks.bukkit.external.VaultHook;

public class cmd_buyrank extends PowerCommand {

	private Users users;

	public cmd_buyrank(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
		this.users = new Users(plugin);
		this.setCommandPermission("powerranks.cmd." + command_name.toLowerCase());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String commandName,
			String[] args) {
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
					float cost = CacheManager.getRank(rankname).getBuyCost();
					double player_balance = VaultHook.getVaultEconomy() != null
							? VaultHook.getVaultEconomy().getBalance(player)
							: 0;
					if (cost >= 0 && player_balance >= cost) {
						VaultHook.getVaultEconomy().withdrawPlayer(player, cost);
						// this.users.setGroup(player, rankname, true);
						PRRank rank = CacheManager.getRank(users.getRankIgnoreCase(rankname));
						if (rank != null) {
							if (rank != null) {
								PRPlayerRank playerRank = new PRPlayerRank(rank.getName());
								CacheManager.getPlayer(player.getUniqueId().toString()).setRank(playerRank);

								sender.sendMessage(PRUtil.powerFormatter(
										PowerRanks.getLanguageManager().getFormattedMessage(
												"commands." + commandName.toLowerCase() + ".success-buy"),
										ImmutableMap.<String, String>builder()
												.put("player", sender.getName())
												.put("rank", rank.getName())
												.build(),
										'[', ']'));
								// users.fireSetRankAddonEvent();
							}
						}
						if (PowerRanks.getConfigManager().getBool("rankup.buy_command.enabled", false)) {
							if (PowerRanks.getConfigManager().getString("rankup.buy_command.command", "")
									.length() > 0) {
								this.plugin.getServer().dispatchCommand(
										(CommandSender) this.plugin.getServer().getConsoleSender(),
										PowerRanks.getConfigManager().getString("rankup.buy_command.command", "")
												.replaceAll("%playername%", sender.getName())
												.replaceAll("%rankname%", rankname));
							}
						}
					} else {
						sender.sendMessage(PRUtil.powerFormatter(
								PowerRanks.getLanguageManager().getFormattedMessage(
										"commands." + commandName.toLowerCase() + ".failed-buy-not-enough-money"),
								ImmutableMap.<String, String>builder()
										.put("player", sender.getName())
										.put("rank", rankname)
										.build(),
								'[', ']'));
					}
				}
			} else {
				sender.sendMessage(
						PowerRanks.getLanguageManager().getFormattedUsageMessage(commandLabel, commandName,
								"commands." + commandName.toLowerCase() + ".arguments", sender instanceof Player));
			}
		} else {
			sender.sendMessage(
					PowerRanks.getLanguageManager().getFormattedMessage(
							"commands." + commandName.toLowerCase() + ".buy-not-available"));
		}

		return false;
	}

	public ArrayList<String> tabCompleteEvent(CommandSender sender, String[] args) {
		ArrayList<String> tabcomplete = new ArrayList<String>();

		if (args.length == 1) {
			for (String rank : this.users.getBuyableRanks(this.users.getPrimaryRank((Player) sender))) {
				tabcomplete.add(rank);
			}
		}

		if (args.length == 2) {
			tabcomplete.add("confirm");
		}

		return tabcomplete;
	}
}
