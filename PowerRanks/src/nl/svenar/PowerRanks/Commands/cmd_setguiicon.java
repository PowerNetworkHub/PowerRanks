package nl.svenar.PowerRanks.Commands;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Data.Messages;
import nl.svenar.PowerRanks.Data.Users;

public class cmd_setguiicon extends PowerCommand {

	private Users users;

	public cmd_setguiicon(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
		this.users = new Users(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (sender.hasPermission("powerranks.cmd.setguiicon")) {
			Player player = (Player) sender;
			if (args.length == 1) {
				String rankName = this.users.getRankIgnoreCase(args[0]);
				Material material = player.getInventory().getItemInMainHand().getType();
				if (material != Material.AIR) {
					this.users.setRanksConfigFieldString(rankName, "gui.icon", material.name().toLowerCase());
					Messages.messageSuccessSetIcon(sender, material.name().toLowerCase(), rankName);
				} else {
					Messages.messageErrorMustHoldItem(sender);
				}

			} else {
				Messages.messageCommandUsageSeticon(sender);
			}
		} else {
			Messages.noPermission(sender);
		}

		return false;
	}

	public ArrayList<String> tabCompleteEvent(CommandSender sender, String[] args) {
		ArrayList<String> tabcomplete = new ArrayList<String>();

		if (args.length == 1) {
			for (String rank : this.users.getGroups()) {
				tabcomplete.add(rank);
			}
		}

		return tabcomplete;
	}
}
