package nl.svenar.PowerRanks.Commands.core;

import java.util.ArrayList;

import com.google.common.collect.ImmutableMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Commands.PowerCommand;
import nl.svenar.PowerRanks.Util.Util;

public class cmd_tablist extends PowerCommand {

	public cmd_tablist(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
		this.setCommandPermission("powerranks.cmd." + command_name.toLowerCase());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String commandName,
			String[] args) {
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

	public ArrayList<String> tabCompleteEvent(CommandSender sender, String[] args) {
		ArrayList<String> tabcomplete = new ArrayList<String>();

		if (args.length == 1) {
			tabcomplete.add("enable");
			tabcomplete.add("disable");
			tabcomplete.add("set");
		}

		if (args.length == 2) {
			if (args[0].equalsIgnoreCase("enable") || args[0].equalsIgnoreCase("disable")) {
				tabcomplete.add("tablist_sorting");
				tabcomplete.add("reverse_tablist_sorting");
			}
		}

		if (args.length == 2) {
			if (args[0].equalsIgnoreCase("set")) {
				tabcomplete.add("sorting_update_interval");
				tabcomplete.add("header_footer_update_interval");
			}
		}

		if (args.length == 3) {
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

		if (args.length == 3) {
			if (args[1].equalsIgnoreCase("playtime_update_interval")) {
				tabcomplete.add("1");
				tabcomplete.add("5");
				tabcomplete.add("10");
				tabcomplete.add("60");
			}
		}

		return tabcomplete;
	}
}
