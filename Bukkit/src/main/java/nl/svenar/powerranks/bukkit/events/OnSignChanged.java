package nl.svenar.powerranks.bukkit.events;

import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableMap;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import nl.svenar.powerranks.common.structure.PRRank;
import nl.svenar.powerranks.common.utils.PRUtil;
import nl.svenar.powerranks.bukkit.PowerRanks;
import nl.svenar.powerranks.bukkit.data.Users;
import nl.svenar.powerranks.bukkit.util.Util;

public class OnSignChanged implements Listener {

	PowerRanks m;

	public OnSignChanged(PowerRanks m) {
		this.m = m;
	}

	@EventHandler
	public void onSignChange(SignChangeEvent event) {
		if (Util.isPowerRanksSign(this.m, event.getLine(0))) {
			if (event.getPlayer().hasPermission("powerranks.signs.create")) {
				event.setLine(0,
						PowerRanks.chatColor(Util.replaceAll(
								PowerRanks.getConfigManager().getString("signs.title_format", "&0[&b%plugin_name%&0]"),
								"%plugin_name%", PowerRanks.pdf.getName()), true));

				final Users s = new Users(this.m);
				String sign_command = event.getLine(1);
				String sign_argument = event.getLine(2);
				String sign_argument2 = event.getLine(3);

				if (sign_command.equalsIgnoreCase("promote") || sign_command.equalsIgnoreCase("demote")
						|| sign_command.equalsIgnoreCase("checkrank") || sign_command.equalsIgnoreCase("gui")) {
					event.getPlayer().sendMessage(PRUtil.powerFormatter(
							PowerRanks.getLanguageManager().getFormattedMessage(
									"messages.signs.created"),
							ImmutableMap.<String, String>builder()
									.put("player", event.getPlayer().getName())
									.build(),
							'[', ']'));
				} else if (sign_command.equalsIgnoreCase("setrank")) {
					List<PRRank> ranks = s.getGroups();
					boolean rank_exists = false;
					for (PRRank rank : ranks) {
						if (rank.getName().equalsIgnoreCase(sign_argument)) {
							rank_exists = true;
							break;
						}
					}
					if (!rank_exists) {

						event.getPlayer().sendMessage(PRUtil.powerFormatter(
								PowerRanks.getLanguageManager().getFormattedMessage(
										"general.rank-not-found"),
								ImmutableMap.<String, String>builder()
										.put("player", event.getPlayer().getName())
										.put("rank", sign_argument)
										.build(),
								'[', ']'));
						event.setLine(3, PowerRanks.chatColor("&4Error", true));
					} else {
						event.getPlayer().sendMessage(PRUtil.powerFormatter(
								PowerRanks.getLanguageManager().getFormattedMessage(
										"messages.signs.created"),
								ImmutableMap.<String, String>builder()
										.put("player", event.getPlayer().getName())
										.build(),
								'[', ']'));
					}
				} else if (sign_command.equalsIgnoreCase("usertag")) {
					Set<String> usertags = s.getUserTags();
					boolean usertag_exists = false;
					if (sign_argument.length() > 0) {
						for (String usertag : usertags) {
							if (usertag.equalsIgnoreCase(sign_argument)) {
								usertag_exists = true;
								break;
							}
						}
					} else {
						usertag_exists = true;
					}
					if (!usertag_exists) {
						event.getPlayer().sendMessage(PRUtil.powerFormatter(
								PowerRanks.getLanguageManager().getFormattedMessage(
										"messages.usertags.not-found"),
								ImmutableMap.<String, String>builder()
										.put("player", event.getPlayer().getName())
										.put("usertag", sign_argument)
										.build(),
								'[', ']'));
						event.setLine(3, PowerRanks.chatColor("&4Error", true));
					} else {
						event.getPlayer().sendMessage(PRUtil.powerFormatter(
								PowerRanks.getLanguageManager().getFormattedMessage(
										"messages.signs.created"),
								ImmutableMap.<String, String>builder()
										.put("player", event.getPlayer().getName())
										.build(),
								'[', ']'));
					}
				} else if (sign_command.equalsIgnoreCase("rankup")) {
					List<PRRank> ranks = s.getGroups();
					if (sign_argument.length() == 0) {
						event.getPlayer().sendMessage(PRUtil.powerFormatter(
								PowerRanks.getLanguageManager().getFormattedMessage(
										"messages.signs.created"),
								ImmutableMap.<String, String>builder()
										.put("player", event.getPlayer().getName())
										.build(),
								'[', ']'));
					} else {
						boolean rank_exists = false;
						for (PRRank rank : ranks) {
							if (rank.getName().equalsIgnoreCase(sign_argument)) {
								rank_exists = true;
								break;
							}
						}
						if (!rank_exists) {
							event.getPlayer().sendMessage(PRUtil.powerFormatter(
									PowerRanks.getLanguageManager().getFormattedMessage(
											"general.rank-not-found"),
									ImmutableMap.<String, String>builder()
											.put("player", event.getPlayer().getName())
											.put("rank", sign_argument)
											.build(),
									'[', ']'));
							event.setLine(3, PowerRanks.chatColor("&4Error", true));
						} else {
							if (sign_argument2.length() > 0) {
								if (!sign_argument2.chars().anyMatch(Character::isLetter)) {
									event.getPlayer().sendMessage(PRUtil.powerFormatter(
											PowerRanks.getLanguageManager().getFormattedMessage(
													"messages.signs.created"),
											ImmutableMap.<String, String>builder()
													.put("player", event.getPlayer().getName())
													.build(),
											'[', ']'));
								} else {
									event.getPlayer().sendMessage(PRUtil.powerFormatter(
											PowerRanks.getLanguageManager().getFormattedMessage(
													"messages.signs.unknown-command"),
											ImmutableMap.<String, String>builder()
													.put("player", event.getPlayer().getName())
													.build(),
											'[', ']'));
									event.setLine(3, PowerRanks.chatColor("&4Error", true));
								}
							} else {
								event.setLine(3, String.valueOf(s.getRankCost(s.getRankIgnoreCase(sign_argument))));
								event.getPlayer().sendMessage(PRUtil.powerFormatter(
										PowerRanks.getLanguageManager().getFormattedMessage(
												"messages.signs.created"),
										ImmutableMap.<String, String>builder()
												.put("player", event.getPlayer().getName())
												.build(),
										'[', ']'));
							}

						}
					}
				} else {
					event.getPlayer().sendMessage(PRUtil.powerFormatter(
							PowerRanks.getLanguageManager().getFormattedMessage(
									"messages.signs.unknown-command"),
							ImmutableMap.<String, String>builder()
									.put("player", event.getPlayer().getName())
									.build(),
							'[', ']'));
					event.setLine(3, PowerRanks.chatColor("&4Error", true));
				}
			} else {
				event.setLine(0, "");
			}
		}
	}
}