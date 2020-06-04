package nl.svenar.PowerRanks.Events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Data.Users;

public class ChatTabExecutor implements TabCompleter {

	private PowerRanks m;
	public ChatTabExecutor(PowerRanks m) {
		this.m = m;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
		if (sender instanceof Player) {
//			Player p = (Player) sender;
			List<String> list = new ArrayList<String>();
			if (args.length == 1) {
				list.add("help");
				list.add("createrank");
				list.add("deleterank");
				list.add("set");
				list.add("setown");
				list.add("promote");
				list.add("demote");
				list.add("check");
				list.add("addperm");
				list.add("delperm");
				list.add("setprefix");
				list.add("setsuffix");
				list.add("setchatcolor");
				list.add("setnamecolor");
				list.add("addinheritance");
				list.add("delinheritance");
				list.add("enablebuild");
				list.add("disablebuild");
				list.add("renamerank");
				list.add("setdefaultrank");
				list.add("reload");
				list.add("forceupdateconfigversion");
				list.add("gui");
			}

			if (args.length == 2) {
				if (args[0].equalsIgnoreCase("setown") || args[0].equalsIgnoreCase("addperm") || args[0].equalsIgnoreCase("delperm") || args[0].equalsIgnoreCase("setprefix")
						|| args[0].equalsIgnoreCase("setsuffix") || args[0].equalsIgnoreCase("setchatcolor") || args[0].equalsIgnoreCase("setnamecolor") || args[0].equalsIgnoreCase("addinheritance")
						|| args[0].equalsIgnoreCase("delinheritance") || args[0].equalsIgnoreCase("enablebuild") || args[0].equalsIgnoreCase("disablebuild") || args[0].equalsIgnoreCase("renamerank")
						|| args[0].equalsIgnoreCase("setdefaultrank")) {
					Users s = new Users(this.m);
					for (String rank : s.getGroups()) {
						list.add(rank);
					}
				}
			}
			
			if (args.length == 3) {
				if (args[0].equalsIgnoreCase("set")) {
					Users s = new Users(this.m);
					for (String rank : s.getGroups()) {
						list.add(rank);
					}
				}
			}

			return list;
		}
		return null;
	}

}
