package nl.svenar.PowerRanks.Events;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Util;
import nl.svenar.PowerRanks.Cache.CacheManager;
import nl.svenar.PowerRanks.Data.Messages;
import nl.svenar.PowerRanks.Data.Users;
import nl.svenar.PowerRanks.External.VaultHook;
import nl.svenar.PowerRanks.gui.GUI;
import nl.svenar.PowerRanks.gui.GUIPage.GUI_PAGE_ID;
import nl.svenar.common.structure.PRRank;

public class OnInteract implements Listener {

	PowerRanks plugin;

	public OnInteract(PowerRanks plugin) {
		this.plugin = plugin;
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			Player player = event.getPlayer();
			Block block = event.getClickedBlock();
			if (block.getState() instanceof Sign) {
				Sign sign = (Sign) block.getState();
				if (Util.isPowerRanksSign(this.plugin, sign)) {
					handlePowerRanksSign(sign, player);
				}
			}
		}
	}

	private void handlePowerRanksSign(Sign sign, Player player) {
		final Users users = new Users(this.plugin);
		String sign_command = sign.getLine(1);
		String sign_argument = sign.getLine(2);
		String sign_argument2 = sign.getLine(3);
		boolean sign_error = sign.getLine(3).toLowerCase().contains("error");

		if (!sign_error) {
			// if (sign_command.equalsIgnoreCase("promote")) {
			// if (player.hasPermission("powerranks.signs.promote")) {
			// if (s.promote(player.getName()))
			// Messages.messageCommandPromoteSuccess(player, player.getName());
			// else
			// Messages.messageCommandPromoteError(player, player.getName());
			// } else {
			// Messages.noPermission(player);
			// }
			// } else if (sign_command.equalsIgnoreCase("demote")) {
			// if (player.hasPermission("powerranks.signs.demote")) {
			// if (s.demote(player.getName()))
			// Messages.messageCommandDemoteSuccess(player, player.getName());
			// else
			// Messages.messageCommandDemoteError(player, player.getName());
			// } else {
			// Messages.noPermission(player);
			// }
			// } else
			if (sign_command.equalsIgnoreCase("setrank")) {
				if (player.hasPermission("powerranks.signs.setrank")) {
					PRRank rank = CacheManager.getRank(users.getRankIgnoreCase(sign_argument));
					if (rank != null) {
						CacheManager.getPlayer(player.getUniqueId().toString()).setRank(rank.getName());
						Messages.messageSetRankSuccessTarget(player, "Sign", rank.getName());
					}
					// s.setGroup(player, s.getRankIgnoreCase(sign_argument), true);
				} else {
					Messages.noPermission(player);
				}
			} else if (sign_command.equalsIgnoreCase("addrank")) {
				if (player.hasPermission("powerranks.signs.setrank")) {
					PRRank rank = CacheManager.getRank(users.getRankIgnoreCase(sign_argument));
					if (rank != null) {
						CacheManager.getPlayer(player.getUniqueId().toString()).addRank(rank.getName());

						// Messages.messageSetRankSuccessSender(player, t, rank);
						Messages.messageSetRankSuccessTarget(player, "Sign", rank.getName());
					}
					// s.setGroup(player, s.getRankIgnoreCase(sign_argument), true);
				} else {
					Messages.noPermission(player);
				}
			} else if (sign_command.equalsIgnoreCase("checkrank")) {
				if (player.hasPermission("powerranks.signs.checkrank")) {
					Messages.messagePlayerCheckRank(player, player.getName(), users.getPrimaryRank(player));
					// s.getGroup(player.getName(), player.getName());
				} else {
					Messages.noPermission(player);
				}
			// } else if (sign_command.equalsIgnoreCase("gui")) {
			// 	if (player.hasPermission("powerranks.signs.gui")) {
			// 		GUI.openGUI(player, GUI_PAGE_ID.MAIN);
			// 	} else {
			// 		Messages.noPermission(player);
			// 	}
			} else if (sign_command.equalsIgnoreCase("usertag")) {
				if (player.hasPermission("powerranks.signs.usertag")) {
					if (users.setUserTag(player, sign_argument)) {
						if (sign_argument.length() > 0) {
							Messages.messageCommandSetusertagSuccess(player, player.getName(), sign_argument);
						} else {
							Messages.messageCommandClearusertagSuccess(player, player.getName());
						}
					} else {
						Messages.messageCommandSetusertagError(player, player.getName(), sign_argument);
					}
				} else {
					Messages.noPermission(player);
				}
			} else if (sign_command.equalsIgnoreCase("rankup")) {
				if (player.hasPermission("powerranks.signs.rankup")) {
					if (sign_argument.length() == 0) {
						GUI.openGUI(player, GUI_PAGE_ID.RANKUP);
					} else {
						if (PowerRanks.vaultEconomyEnabled) {
							if (sign_argument2.length() > 0) {
								// Users users = new Users(this.plugin);
								int cost = Integer.parseInt(sign_argument2);
								double player_balance = VaultHook.getVaultEconomy() != null
										? VaultHook.getVaultEconomy().getBalance(player)
										: 0;
								if (cost >= 0 && player_balance >= cost) {
									VaultHook.getVaultEconomy().withdrawPlayer(player, cost);
									PRRank rank = CacheManager.getRank(users.getRankIgnoreCase(sign_argument));
									if (rank != null) {
										if (rank != null) {
											CacheManager.getPlayer(player.getUniqueId().toString()).addRank(rank.getName());
					
											Messages.messageSetRankSuccessTarget(player, "Sign", rank.getName());
										}
									}
									// users.setGroup(player, users.getRankIgnoreCase(sign_argument), true);
									Messages.messageBuyRankSuccess(player, sign_argument);
								} else {
									Messages.messageBuyRankError(player, sign_argument);
								}
							} else {
								Messages.messageBuyRankError(player, sign_argument);
							}
						} else {
							Messages.messageBuyRankNotAvailable(player);
						}
					}
				} else {
					Messages.noPermission(player);
				}
			} else {
				if (player.hasPermission("powerranks.signs.admin")) {
					Messages.messageSignUnknownCommand(player);
				}
			}
		}
	}
}