package nl.svenar.PowerRanks.Events;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Data.Users;
import nl.svenar.PowerRanks.addons.PowerRanksAddon;

public class ChatTabExecutor implements TabCompleter {

	private PowerRanks m;
	private static ArrayList<String> addon_commands = new ArrayList<String>();

	public ChatTabExecutor(PowerRanks m) {
		this.m = m;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
		if (sender instanceof Player) {
//			Player p = (Player) sender;
			List<String> list = new ArrayList<String>();
			if (args.length == 1) {
				ArrayList<String> commands_list = new ArrayList<String>();
				commands_list.add("help");
				commands_list.add("createrank");
				commands_list.add("deleterank");
				commands_list.add("setrank");
				commands_list.add("setownrank");
				commands_list.add("promote");
				commands_list.add("demote");
				commands_list.add("check");
				commands_list.add("addperm");
				commands_list.add("delperm");
				commands_list.add("addplayerperm");
				commands_list.add("delplayerperm");
				commands_list.add("listranks");
				commands_list.add("listsubranks");
				commands_list.add("listpermissions");
				commands_list.add("addsubrank");
				commands_list.add("delsubrank");
				commands_list.add("enablesubrankprefix");
				commands_list.add("disablesubrankprefix");
				commands_list.add("enablesubranksuffix");
				commands_list.add("disablesubranksuffix");
				commands_list.add("enablesubrankpermissions");
				commands_list.add("disablesubrankpermissions");
				commands_list.add("setprefix");
				commands_list.add("setsuffix");
				commands_list.add("setchatcolor");
				commands_list.add("setnamecolor");
				commands_list.add("addinheritance");
				commands_list.add("delinheritance");
				commands_list.add("enablebuild");
				commands_list.add("disablebuild");
				commands_list.add("renamerank");
				commands_list.add("setdefaultrank");
				commands_list.add("reload");
				commands_list.add("factoryreset");
				commands_list.add("gui");
				commands_list.add("rankup");
				commands_list.add("addbuyablerank");
				commands_list.add("delbuyablerank");
				commands_list.add("setcost");
				commands_list.add("stats");
				commands_list.add("createusertag");
				commands_list.add("editusertag");
				commands_list.add("removeusertag");
				commands_list.add("setusertag");
				commands_list.add("listusertags");
				commands_list.add("clearusertag");
				commands_list.add("setpromoterank");
				commands_list.add("setdemoterank");
				commands_list.add("clearpromoterank");
				commands_list.add("cleardemoterank");
				commands_list.add("addoninfo");

				for (String command : addon_commands) {
					if (command.toLowerCase().contains(args[0].toLowerCase()))
						commands_list.add(command);
				}

				for (String command : commands_list) {
					if (command.toLowerCase().contains(args[0].toLowerCase()))
						list.add(command);
				}
			}

			if (args.length == 2) {
				if (args[0].equalsIgnoreCase("setownrank") || args[0].equalsIgnoreCase("addperm") || args[0].equalsIgnoreCase("delperm") || args[0].equalsIgnoreCase("setprefix") || args[0].equalsIgnoreCase("setsuffix")
						|| args[0].equalsIgnoreCase("setchatcolor") || args[0].equalsIgnoreCase("setnamecolor") || args[0].equalsIgnoreCase("addinheritance") || args[0].equalsIgnoreCase("delinheritance")
						|| args[0].equalsIgnoreCase("enablebuild") || args[0].equalsIgnoreCase("disablebuild") || args[0].equalsIgnoreCase("renamerank") || args[0].equalsIgnoreCase("setdefaultrank")
						|| args[0].equalsIgnoreCase("addbuyablerank") || args[0].equalsIgnoreCase("delbuyablerank") || args[0].equalsIgnoreCase("setpromoterank") || args[0].equalsIgnoreCase("setdemoterank")
						|| args[0].equalsIgnoreCase("clearpromoterank") || args[0].equalsIgnoreCase("cleardemoterank") || args[0].equalsIgnoreCase("setcost")) {
					Users s = new Users(this.m);
					for (String rank : s.getGroups()) {
						if (rank.toLowerCase().contains(args[1].toLowerCase()))
							list.add(rank);
					}
				}

				if (args[0].equalsIgnoreCase("setrank") || args[0].equalsIgnoreCase("promote") || args[0].equalsIgnoreCase("demote") || args[0].equalsIgnoreCase("check") || args[0].equalsIgnoreCase("addsubrank")
						|| args[0].equalsIgnoreCase("delsubrank") || args[0].equalsIgnoreCase("listsubranks") || args[0].equalsIgnoreCase("addplayerperm") || args[0].equalsIgnoreCase("delplayerperm")
						|| args[0].equalsIgnoreCase("enablesubrankprefix") || args[0].equalsIgnoreCase("disablesubrankprefix") || args[0].equalsIgnoreCase("enablesubranksuffix") || args[0].equalsIgnoreCase("disablesubranksuffix")
						|| args[0].equalsIgnoreCase("enablesubrankpermissions") || args[0].equalsIgnoreCase("disablesubrankpermissions") || args[0].equalsIgnoreCase("setusertag") || args[0].equalsIgnoreCase("clearusertag")) {
					for (Player player : Bukkit.getServer().getOnlinePlayers()) {
						if (player.getName().toLowerCase().contains(args[1].toLowerCase()))
							list.add(player.getName());
					}
				}

				if (args[0].equalsIgnoreCase("setusertag") || args[0].equalsIgnoreCase("editusertag") || args[0].equalsIgnoreCase("removeusertag")) {
					Users s = new Users(this.m);
					for (String tag : s.getUserTags()) {
						if (tag.toLowerCase().contains(args[1].toLowerCase()))
							list.add(tag);
					}
				}
				
				if (args[0].equalsIgnoreCase("addoninfo")) {
					for (Entry<File, PowerRanksAddon> addon : this.m.addonsManager.addonClasses.entrySet()) {
						list.add(addon.getValue().getIdentifier());
					}
				}
			}

			if (args.length == 3) {
				if (args[0].equalsIgnoreCase("setrank") || args[0].equalsIgnoreCase("setpromoterank") || args[0].equalsIgnoreCase("setdemoterank")) {
					Users s = new Users(this.m);
					for (String rank : s.getGroups()) {
						if (rank.toLowerCase().contains(args[2].toLowerCase()))
							list.add(rank);
					}
				}

				if (args[0].equalsIgnoreCase("setusertag")) {
					Users s = new Users(this.m);
					for (String tag : s.getUserTags()) {
						if (tag.toLowerCase().contains(args[2].toLowerCase()))
							list.add(tag);
					}
				}

				if (args[0].equalsIgnoreCase("addbuyablerank")) {
					Users s = new Users(this.m);
					for (String rank : s.getGroups()) {
						if (rank.toLowerCase().contains(args[2].toLowerCase()))
							if (!s.getBuyableRanks(s.getRankIgnoreCase(args[1])).contains(rank))
								if (!args[1].equalsIgnoreCase(rank))
									list.add(rank);
					}
				}

				if (args[0].equalsIgnoreCase("delbuyablerank")) {
					Users s = new Users(this.m);
					for (String rank : s.getBuyableRanks(s.getRankIgnoreCase(args[1]))) {
						if (rank.toLowerCase().contains(args[2].toLowerCase()))
							list.add(rank);
					}
				}

				if (args[0].equalsIgnoreCase("addperm") || args[0].equalsIgnoreCase("addplayerperm")) {
					for (PermissionAttachmentInfo pai : sender.getEffectivePermissions()) {
						String perm = pai.getPermission();
						if (perm.toLowerCase().contains(args[2].toLowerCase()))
							list.add(perm);
					}
				}

				if (args[0].equalsIgnoreCase("delperm")) {
					Users s = new Users(this.m);
					for (String perm : s.getPermissions(s.getRankIgnoreCase(args[1]))) {
						if (perm.toLowerCase().contains(args[2].toLowerCase()))
							list.add(perm);
					}
				}

				if (args[0].equalsIgnoreCase("delplayerperm")) {
					Users s = new Users(this.m);
					for (String perm : s.getPlayerPermissions(args[1])) {
						if (perm.toLowerCase().contains(args[2].toLowerCase()))
							list.add(perm);
					}
				}

				if (args[0].equalsIgnoreCase("addsubrank")) {
					Users s = new Users(this.m);
					for (String rank : s.getGroups()) {
						if (rank.toLowerCase().contains(args[2].toLowerCase()))
							if (!s.getSubranks(args[1]).contains(rank))
								if (!args[1].equalsIgnoreCase(rank))
									list.add(rank);
					}
				}

				if (args[0].equalsIgnoreCase("delsubrank") || args[0].equalsIgnoreCase("enablesubrankprefix") || args[0].equalsIgnoreCase("disablesubrankprefix") || args[0].equalsIgnoreCase("enablesubranksuffix")
						|| args[0].equalsIgnoreCase("disablesubranksuffix") || args[0].equalsIgnoreCase("enablesubrankpermissions") || args[0].equalsIgnoreCase("disablesubrankpermissions")) {
					Users s = new Users(this.m);
					for (String rank : s.getSubranks(args[1])) {
						if (rank.toLowerCase().contains(args[2].toLowerCase()))
							list.add(rank);
					}
				}

				if (args[0].equalsIgnoreCase("addinheritance")) {
					Users s = new Users(this.m);
					for (String rank : s.getGroups()) {
						if (rank.toLowerCase().contains(args[2].toLowerCase()))
							list.add(rank);
					}
				}

				if (args[0].equalsIgnoreCase("delinheritance")) {
					Users s = new Users(this.m);
					for (String inheritance : s.getInheritances(s.getRankIgnoreCase(args[1]))) {
						if (inheritance.toLowerCase().contains(args[2].toLowerCase()))
							list.add(inheritance);
					}
				}

				if (args[0].equalsIgnoreCase("setcost")) {
					list.add("0");
					list.add("10");
					list.add("100");
					list.add("1000");
					list.add("10000");
				}
			}

			return list;
		}
		return null;
	}

	public static void addAddonCommand(String command) {
		if (!addon_commands.contains(command.toLowerCase()))
			addon_commands.add(command.toLowerCase());
	}
}
