package nl.svenar.PowerRanks.Events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class TabExecutor implements TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
		if (sender instanceof Player) {
//			Player p = (Player) sender;
			List<String> list = new ArrayList<String>();
			if (args.length == 0) {
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
			}
			
			return list;
		}
		return null;
	}

}
