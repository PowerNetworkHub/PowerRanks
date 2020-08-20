package nl.svenar.PowerRanks.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;

public class cmd_help extends PowerRanksCommand {

	public cmd_help() {
		super("help");
	}

	@Override
	public void onCommand(CommandSender sender, String[] args) {
		sender.sendMessage(new ComponentBuilder("Help").create());
	}
}
