package nl.svenar.PowerRanks.Commands.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.common.collect.ImmutableMap;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Commands.PowerCommand;
import nl.svenar.PowerRanks.Util.Util;
import nl.svenar.common.utils.PRUtil;

public class cmd_tablist extends PowerCommand {

	public cmd_tablist(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
		this.setCommandPermission("powerranks.cmd." + command_name.toLowerCase());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String commandName,
			String[] args) {

		if (args.length >= 1) {
			if (args[0].equalsIgnoreCase("animation")) {
				handleAnimationCommand(sender, cmd, commandLabel, commandName,
						Arrays.copyOfRange(args, 1, args.length));
			}
		}

		if (args.length == 2) {
			if (args[0].equalsIgnoreCase("enable") || args[0].equalsIgnoreCase("disable")) {
				boolean enable = args[0].equalsIgnoreCase("enable");

				if (args[1].equalsIgnoreCase("tablist_sorting")) {
					sender.sendMessage(Util.powerFormatter(
							PowerRanks.getLanguageManager().getFormattedMessage(
									"commands." + commandName.toLowerCase() + ".state-changed"),
							ImmutableMap.<String, String>builder()
									.put("player", sender.getName())
									.put("config_target", "Tablist sorting")
									.put("old_state",
											String.valueOf(PowerRanks.getTablistConfigManager()
													.getBool("sorting.enabled", true)))
									.put("new_state", String.valueOf(enable))
									.build(),
							'[', ']'));

					PowerRanks.getTablistConfigManager().setBool("sorting.enabled", enable);
					PowerRanks.getTablistConfigManager().save();
					PowerRanks.getInstance().getTablistManager().stop();
					PowerRanks.getInstance().getTablistManager().start();

				} else if (args[1].equalsIgnoreCase("reverse_tablist_sorting")) {
					sender.sendMessage(Util.powerFormatter(
							PowerRanks.getLanguageManager().getFormattedMessage(
									"commands." + commandName.toLowerCase() + ".state-changed"),
							ImmutableMap.<String, String>builder()
									.put("player", sender.getName())
									.put("config_target", "Reverse tablist sorting")
									.put("old_state",
											String.valueOf(PowerRanks.getTablistConfigManager()
													.getBool("sorting.reverse", true)))
									.put("new_state", String.valueOf(enable))
									.build(),
							'[', ']'));

					PowerRanks.getTablistConfigManager().setBool("sorting.reverse", enable);
					PowerRanks.getTablistConfigManager().save();
					PowerRanks.getInstance().getTablistManager().stop();
					PowerRanks.getInstance().getTablistManager().start();

				} else if (args[1].equalsIgnoreCase("header_footer")) {
					sender.sendMessage(Util.powerFormatter(
							PowerRanks.getLanguageManager().getFormattedMessage(
									"commands." + commandName.toLowerCase() + ".state-changed"),
							ImmutableMap.<String, String>builder()
									.put("player", sender.getName())
									.put("config_target", "Header & Footer")
									.put("old_state",
											String.valueOf(PowerRanks.getTablistConfigManager()
													.getBool("header-footer.enabled", true)))
									.put("new_state", String.valueOf(enable))
									.build(),
							'[', ']'));

					PowerRanks.getTablistConfigManager().setBool("header-footer.enabled", enable);
					PowerRanks.getTablistConfigManager().save();
					PowerRanks.getInstance().getTablistManager().stop();
					PowerRanks.getInstance().getTablistManager().start();

				} else {
					sender.sendMessage(
							PowerRanks.getLanguageManager().getFormattedUsageMessage(commandLabel, commandName,
									"commands." + commandName.toLowerCase() + ".arguments", sender instanceof Player));
				}
			} else {
				sender.sendMessage(
						PowerRanks.getLanguageManager().getFormattedUsageMessage(commandLabel, commandName,
								"commands." + commandName.toLowerCase() + ".arguments", sender instanceof Player));
			}
		} else if (args.length == 3) {
			if (args[0].equalsIgnoreCase("set")) {

				if (args[1].equalsIgnoreCase("sorting_update_interval")) {
					try {
						int time = Integer.parseInt(args[2]);

						sender.sendMessage(Util.powerFormatter(
								PowerRanks.getLanguageManager().getFormattedMessage(
										"commands." + commandName.toLowerCase() + ".state-changed"),
								ImmutableMap.<String, String>builder()
										.put("player", sender.getName())
										.put("config_target", "Player playtime update interval")
										.put("old_state",
												String.valueOf(PowerRanks.getTablistConfigManager()
														.getInt("sorting.update-interval", 60)))
										.put("new_state", String.valueOf(time))
										.build(),
								'[', ']'));

						PowerRanks.getTablistConfigManager().setInt("sorting.update-interval", time);
						PowerRanks.getTablistConfigManager().save();
						PowerRanks.getInstance().getTablistManager().stop();
						PowerRanks.getInstance().getTablistManager().start();
					} catch (Exception e) {
						sender.sendMessage(
								PowerRanks.getLanguageManager().getFormattedMessage(
										"commands." + commandName.toLowerCase() + ".numbers-only"));
					}
				} else if (args[1].equalsIgnoreCase("header_footer_update_interval")) {
					try {
						int time = Integer.parseInt(args[2]);

						sender.sendMessage(Util.powerFormatter(
								PowerRanks.getLanguageManager().getFormattedMessage(
										"commands." + commandName.toLowerCase() + ".state-changed"),
								ImmutableMap.<String, String>builder()
										.put("player", sender.getName())
										.put("config_target", "Player playtime update interval")
										.put("old_state",
												String.valueOf(PowerRanks.getTablistConfigManager()
														.getInt("header-footer.update-interval", 60)))
										.put("new_state", String.valueOf(time))
										.build(),
								'[', ']'));

						PowerRanks.getTablistConfigManager().setInt("header-footer.update-interval", time);
						PowerRanks.getTablistConfigManager().save();
						PowerRanks.getInstance().getTablistManager().stop();
						PowerRanks.getInstance().getTablistManager().start();
					} catch (Exception e) {
						sender.sendMessage(
								PowerRanks.getLanguageManager().getFormattedMessage(
										"commands." + commandName.toLowerCase() + ".numbers-only"));
					}
				}
			}
		} else {
			sender.sendMessage(
					PowerRanks.getLanguageManager().getFormattedUsageMessage(commandLabel, commandName,
							"commands." + commandName.toLowerCase() + ".arguments", sender instanceof Player));
		}

		return false;
	}

	private void handleAnimationCommand(CommandSender sender, Command cmd, String commandLabel, String commandName,
			String[] args) {

		if (args.length >= 1) {
			if (args[0].equalsIgnoreCase("list")) {
				List<String> animationNames = PowerRanks.getTablistConfigManager().getKeys("header-footer.animations");

				sender.sendMessage(ChatColor.BLUE + "===" + ChatColor.DARK_AQUA + "----------" + ChatColor.AQUA
						+ plugin.getDescription().getName() + ChatColor.DARK_AQUA + "----------" + ChatColor.BLUE
						+ "===");
				sender.sendMessage(ChatColor.AQUA + "Animations (" + animationNames.size() + "):");
				int index = 0;

				for (String animationName : animationNames) {
					index++;
					sender.sendMessage(
							ChatColor.DARK_GREEN + "#" + index + ". " + ChatColor.GRAY + animationName);
				}
				sender.sendMessage(ChatColor.BLUE + "===" + ChatColor.DARK_AQUA + "------------------------------"
						+ ChatColor.BLUE + "===");

			} else if (args[0].equalsIgnoreCase("create")) {
				if (args.length == 2) {
					String animationName = args[1];
					if (PRUtil.containsIgnoreCase(
							PowerRanks.getTablistConfigManager().getKeys("header-footer.animations"), animationName)) {
						sender.sendMessage(
								PowerRanks.getLanguageManager().getFormattedUsageMessage(commandLabel, commandName,
										"commands." + commandName.toLowerCase() + ".animation.already-exists",
										sender instanceof Player));
						return;
					}
					PowerRanks.getTablistConfigManager().setKV("header-footer.animations" + animationName + ".delay",
							20);
					PowerRanks.getTablistConfigManager().setKV("header-footer.animations" + animationName + ".frames",
							new ArrayList<String>());
					PowerRanks.getTablistConfigManager().save();
					sender.sendMessage(
							PowerRanks.getLanguageManager().getFormattedUsageMessage(commandLabel, commandName,
									"commands." + commandName.toLowerCase() + ".animation.created",
									sender instanceof Player));
				}

			} else if (args[0].equalsIgnoreCase("delete")) {
				if (args.length == 2) {
					String animationName = args[1];
					if (!PRUtil.containsIgnoreCase(
							PowerRanks.getTablistConfigManager().getKeys("header-footer.animations"), animationName)) {
						sender.sendMessage(
								PowerRanks.getLanguageManager().getFormattedUsageMessage(commandLabel, commandName,
										"commands." + commandName.toLowerCase() + ".animation.does-not-exists",
										sender instanceof Player));
						return;
					}
					PowerRanks.getTablistConfigManager().setKV("header-footer.animations" + animationName, null);
					PowerRanks.getTablistConfigManager().save();
					// TODO: take animation name case into account
					sender.sendMessage(
							PowerRanks.getLanguageManager().getFormattedUsageMessage(commandLabel, commandName,
									"commands." + commandName.toLowerCase() + ".animation.deleted",
									sender instanceof Player));
				}

			} else if (args[0].equalsIgnoreCase("delay")) {
				if (args.length == 3) {
					try {
						String animationName = args[1];
						if (!PRUtil.containsIgnoreCase(
								PowerRanks.getTablistConfigManager().getKeys("header-footer.animations"),
								animationName)) {
							sender.sendMessage(
									PowerRanks.getLanguageManager().getFormattedUsageMessage(commandLabel, commandName,
											"commands." + commandName.toLowerCase() + ".animation.unknown-animation",
											sender instanceof Player));
							return;
						}
						int time = Integer.parseInt(args[2]);

						sender.sendMessage(Util.powerFormatter(
								PowerRanks.getLanguageManager().getFormattedMessage(
										"commands." + commandName.toLowerCase() + ".state-changed"),
								ImmutableMap.<String, String>builder()
										.put("player", sender.getName())
										.put("config_target", "Tablist animation update interval")
										.put("old_state",
												String.valueOf(PowerRanks.getTablistConfigManager()
														.getInt("header-footer.animations" + animationName + ".delay",
																20)))
										.put("new_state", String.valueOf(time))
										.build(),
								'[', ']'));

						PowerRanks.getTablistConfigManager()
								.setInt("header-footer.animations" + animationName + ".delay", time);
						PowerRanks.getInstance().getTablistManager().stop();
						PowerRanks.getInstance().getTablistManager().start();
					} catch (Exception e) {
						sender.sendMessage(
								PowerRanks.getLanguageManager().getFormattedMessage(
										"commands." + commandName.toLowerCase() + ".numbers-only"));
					}
				}

			}
		} else {
			sender.sendMessage(
					PowerRanks.getLanguageManager().getFormattedUsageMessage(commandLabel, commandName,
							"commands." + commandName.toLowerCase() + ".animation.arguments",
							sender instanceof Player));
		}
	}

	public ArrayList<String> tabCompleteEvent(CommandSender sender, String[] args) {
		ArrayList<String> tabcomplete = new ArrayList<String>();

		if (args.length == 1) {
			tabcomplete.add("enable");
			tabcomplete.add("disable");
			tabcomplete.add("set");
			tabcomplete.add("animation");
		}

		if (args.length == 2) {
			if (args[0].equalsIgnoreCase("enable") || args[0].equalsIgnoreCase("disable")) {
				tabcomplete.add("tablist_sorting");
				tabcomplete.add("reverse_tablist_sorting");
				tabcomplete.add("header_footer");
			}

			if (args[0].equalsIgnoreCase("animation")) {
				tabcomplete.add("list");
				tabcomplete.add("create");
				tabcomplete.add("delete");
				tabcomplete.add("delay");
			}

			if (args[0].equalsIgnoreCase("set")) {
				tabcomplete.add("sorting_update_interval");
				tabcomplete.add("header_footer_update_interval");
			}
		}

		if (args.length == 3) {
			if (args[0].equalsIgnoreCase("set")) {
				if (args[1].equalsIgnoreCase("sorting_update_interval")) {
					tabcomplete.add("1");
					tabcomplete.add("5");
					tabcomplete.add("10");
				}

				if (args[1].equalsIgnoreCase("header_footer_update_interval")) {
					tabcomplete.add("1");
					tabcomplete.add("5");
					tabcomplete.add("20");
					tabcomplete.add("100");
					tabcomplete.add("200");
				}
			}

			if (args[0].equalsIgnoreCase("animation")) {
				if (args[1].equalsIgnoreCase("delete")) {
					for (String key : PowerRanks.getTablistConfigManager().getKeys("header-footer.animations")) {
						tabcomplete.add(key);
					}
				}
				if (args[1].equalsIgnoreCase("delay")) {
					tabcomplete.add("1");
					tabcomplete.add("5");
					tabcomplete.add("10");
				}
			}
		}

		return tabcomplete;
	}
}
