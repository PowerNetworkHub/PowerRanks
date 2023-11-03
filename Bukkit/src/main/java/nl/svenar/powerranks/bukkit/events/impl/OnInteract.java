package nl.svenar.powerranks.bukkit.events.impl;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableMap;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import nl.svenar.powerranks.common.structure.PRPlayer;
import nl.svenar.powerranks.common.structure.PRPlayerRank;
import nl.svenar.powerranks.common.structure.PRRank;
import nl.svenar.powerranks.common.utils.PRCache;
import nl.svenar.powerranks.common.utils.PRUtil;
import nl.svenar.powerranks.bukkit.PowerRanks;
import nl.svenar.powerranks.bukkit.cache.CacheManager;
import nl.svenar.powerranks.bukkit.util.Util;

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

	@SuppressWarnings("deprecation")
	private void handlePowerRanksSign(Sign sign, Player player) {
		String sign_command;
		String sign_argument;
		boolean sign_error;

		try {
			Class.forName("org.bukkit.block.sign.Side");
			Side signSide = Side.FRONT;
			sign_command = sign.getSide(signSide).getLine(1);
			if (sign_command.length() == 0) {
				signSide = Side.BACK;
				sign_command = sign.getSide(signSide).getLine(1);
			}
			sign_argument = sign.getSide(signSide).getLine(2);
			sign_error = sign.getSide(signSide).getLine(3).toLowerCase().contains("error");
		} catch (ClassNotFoundException e) {
			sign_command = sign.getLine(1);
			sign_argument = sign.getLine(2);
			sign_error = sign.getLine(3).toLowerCase().contains("error");
		}

		if (!sign_error) {
			// if (sign_command.equalsIgnoreCase("promote")) {
			// if (player.hasPermission("powerranks.signs.promote")) {
			// if (s.promote(player.getName()))
			// Messages.messageCommandPromoteSuccess(player, player.getName());
			// else
			// Messages.messageCommandPromoteError(player, player.getName());
			// } else {
			// player.sendMessage(PowerRanks.getInstance().getLanguageManager().getFormattedMessage("general.no-permission"));
			// }
			// } else if (sign_command.equalsIgnoreCase("demote")) {
			// if (player.hasPermission("powerranks.signs.demote")) {
			// if (s.demote(player.getName()))
			// Messages.messageCommandDemoteSuccess(player, player.getName());
			// else
			// Messages.messageCommandDemoteError(player, player.getName());
			// } else {
			// player.sendMessage(PowerRanks.getInstance().getLanguageManager().getFormattedMessage("general.no-permission"));
			// }
			// } else
			if (sign_command.equalsIgnoreCase("setrank")) {
				if (player.hasPermission("powerranks.signs.setrank")) {
					PRRank rank = PRCache.getRankIgnoreCase(sign_argument);
					if (rank != null) {
						PRPlayerRank playerRank = new PRPlayerRank(rank.getName());
						CacheManager.getPlayer(player.getUniqueId().toString()).setRank(playerRank);
						player.sendMessage(PRUtil.powerFormatter(
								PowerRanks.getInstance().getLanguageManager().getFormattedMessage(
										"commands.setrank.success-receiver"),
								ImmutableMap.<String, String>builder()
										.put("player", player.getName())
										.put("rank", rank.getName())
										.build(),
								'[', ']'));
					}
					// s.setGroup(player, s.getRankIgnoreCase(sign_argument), true);
				} else {
					player.sendMessage(
							PowerRanks.getInstance().getLanguageManager().getFormattedMessage("general.no-permission"));
				}
			} else if (sign_command.equalsIgnoreCase("addrank")) {
				if (player.hasPermission("powerranks.signs.setrank")) {
					PRRank rank = PRCache.getRankIgnoreCase(sign_argument);
					if (rank != null) {
						PRPlayerRank playerRank = new PRPlayerRank(rank.getName());
						CacheManager.getPlayer(player.getUniqueId().toString()).addRank(playerRank);

						// Messages.messageSetRankSuccessSender(player, t, rank);
						player.sendMessage(PRUtil.powerFormatter(
								PowerRanks.getInstance().getLanguageManager().getFormattedMessage(
										"commands.setrank.success-receiver"),
								ImmutableMap.<String, String>builder()
										.put("player", player.getName())
										.put("rank", rank.getName())
										.build(),
								'[', ']'));
					}
					// s.setGroup(player, s.getRankIgnoreCase(sign_argument), true);
				} else {
					player.sendMessage(
							PowerRanks.getInstance().getLanguageManager().getFormattedMessage("general.no-permission"));
				}
			} else if (sign_command.equalsIgnoreCase("checkrank")) {
				if (player.hasPermission("powerranks.signs.checkrank")) {

					List<String> playerRanks = new ArrayList<>();
					for (PRPlayerRank rank : CacheManager.getPlayer(player.getUniqueId().toString()).getRanks()) {
						playerRanks.add(rank.getName());
					}
					if (playerRanks.size() > 0) {
						player.sendMessage(PRUtil.powerFormatter(
								PowerRanks.getInstance().getLanguageManager()
										.getFormattedMessage("commands.checkrank.success-self"),
								ImmutableMap.<String, String>builder()
										.put("player", player.getName())
										.put("ranks", String.join(", ", playerRanks))
										.build(),
								'[', ']'));
					} else {
						player.sendMessage(PRUtil.powerFormatter(
								PowerRanks.getInstance().getLanguageManager()
										.getFormattedMessage(
												"commands.checkrank.success-self-none"),
								ImmutableMap.<String, String>builder()
										.put("player", player.getName())
										.build(),
								'[', ']'));
					}

					// s.getGroup(player.getName(), player.getName());
				} else {
					player.sendMessage(
							PowerRanks.getInstance().getLanguageManager().getFormattedMessage("general.no-permission"));
				}
				// } else if (sign_command.equalsIgnoreCase("gui")) {
				// if (player.hasPermission("powerranks.signs.gui")) {
				// GUI.openGUI(player, GUIPAGEID.MAIN);
				// } else {
				// player.sendMessage(PowerRanks.getInstance().getLanguageManager().getFormattedMessage("general.no-permission"));
				// }
			} else if (sign_command.equalsIgnoreCase("usertag")) {
				if (player.hasPermission("powerranks.signs.usertag")) {
					PRPlayer prPlayer = CacheManager.getPlayer(player.getUniqueId().toString());
					if (prPlayer != null) {
						prPlayer.setUsertag(sign_argument);
						if (sign_argument.length() > 0) {
							player.sendMessage(PRUtil.powerFormatter(
									PowerRanks.getInstance().getLanguageManager().getFormattedMessage(
											"commands.setusertag.success"),
									ImmutableMap.<String, String>builder()
											.put("player", player.getName())
											.put("usertag", sign_argument)
											.build(),
									'[', ']'));
						} else {
							player.sendMessage(PRUtil.powerFormatter(
									PowerRanks.getInstance().getLanguageManager().getFormattedMessage(
											"commands.clearusertag.success"),
									ImmutableMap.<String, String>builder()
											.put("player", player.getName())
											.put("usertag", sign_argument)
											.build(),
									'[', ']'));
						}
					} else {
						player.sendMessage(PRUtil.powerFormatter(
								PowerRanks.getInstance().getLanguageManager().getFormattedMessage(
										"commands.setusertag.failed"),
								ImmutableMap.<String, String>builder()
										.put("player", player.getName())
										.put("usertag", sign_argument)
										.build(),
								'[', ']'));
					}
				} else {
					player.sendMessage(
							PowerRanks.getInstance().getLanguageManager().getFormattedMessage("general.no-permission"));
				}
			} else {
				if (player.hasPermission("powerranks.signs.admin")) {
					player.sendMessage(
							PowerRanks.getInstance().getLanguageManager()
									.getFormattedMessage("messages.signs.unknown-command"));
				}
			}
		}
	}
}