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
import nl.svenar.PowerRanks.Data.Messages;
import nl.svenar.PowerRanks.Data.Users;
import nl.svenar.PowerRanks.gui.GUI;
import nl.svenar.PowerRanks.gui.GUIPage.GUI_PAGE_ID;

public class OnInteract implements Listener {

	PowerRanks m;

	public OnInteract(PowerRanks m) {
		this.m = m;
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			Player player = event.getPlayer();
			Block block = event.getClickedBlock();
//			PowerRanks.log.info(block.toString());
//			PowerRanks.log.info(block.getState().toString());
			System.out.println(block.getType());
			System.out.println(block.getBlockData().getMaterial());
			if (block.getState() instanceof Sign) {
				Sign sign = (Sign) block.getState();
				if (Util.isPowerRanksSign(this.m, sign)) {
					handlePowerRanksSign(sign, player);
				}
			}
		}
	}

	private void handlePowerRanksSign(Sign sign, Player player) {
		final Users s = new Users(this.m);
		String sign_command = sign.getLine(1);
		String sign_argument = sign.getLine(2);
		String sign_argument2 = sign.getLine(3);
		boolean sign_error = sign.getLine(3).toLowerCase().contains("error");

		if (!sign_error) {
			if (sign_command.equalsIgnoreCase("promote")) {
				if (player.hasPermission("powerranks.signs.set")) {
					if (s.promote(player.getName()))
						Messages.messageCommandPromoteSuccess(player, player.getName());
					else
						Messages.messageCommandPromoteError(player, player.getName());
				} else {
					Messages.noPermission(player);
				}
			} else if (sign_command.equalsIgnoreCase("demote")) {
				if (player.hasPermission("powerranks.signs.set")) {
					if (s.demote(player.getName()))
						Messages.messageCommandDemoteSuccess(player, player.getName());
					else
						Messages.messageCommandDemoteError(player, player.getName());
				} else {
					Messages.noPermission(player);
				}
			} else if (sign_command.equalsIgnoreCase("set")) {
				if (player.hasPermission("powerranks.signs.set")) {
					s.setGroup(player, s.getRankIgnoreCase(sign_argument));
				} else {
					Messages.noPermission(player);
				}
			} else if (sign_command.equalsIgnoreCase("check")) {
				if (player.hasPermission("powerranks.signs.check")) {
					s.getGroup(player.getName(), player.getName());
				} else {
					Messages.noPermission(player);
				}
			} else if (sign_command.equalsIgnoreCase("gui")) {
				if (player.hasPermission("powerranks.signs.admin")) {
//					GUI.openPowerRanksGUI(player, 0, "");
					GUI.openGUI(player, GUI_PAGE_ID.MAIN);
				} else {
					Messages.noPermission(player);
				}
			} else if (sign_command.equalsIgnoreCase("rankup")) {
				if (player.hasPermission("powerranks.signs.rankup")) {
					if (sign_argument.length() == 0) {
//						GUI.openPowerRanksRankupGUI(player, 0);
						GUI.openGUI(player, GUI_PAGE_ID.RANKUP);
					} else {
						if (PowerRanks.getVaultEconomy() != null) {
							if (sign_argument2.length() > 0) {
								Users users = new Users(this.m);
								int cost = Integer.parseInt(sign_argument2);
								double player_balance = PowerRanks.getVaultEconomy().getBalance(player);
								if (cost >= 0 && player_balance >= cost) {
									PowerRanks.getVaultEconomy().withdrawPlayer(player, cost);
									users.setGroup(player, users.getRankIgnoreCase(sign_argument));
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