package nl.svenar.PowerRanks.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Util;
import nl.svenar.PowerRanks.Data.BannerItem;
import nl.svenar.PowerRanks.Data.Messages;
import nl.svenar.PowerRanks.Data.Users;

public class PowerRanksGUI_OLD {

	private static Inventory inventoryGUIMain, inventoryGUIShop;
	private static PowerRanks powerRanks;

	private static enum INVENTORY_SIZE {
		SMALL(9), NORMAL(27), BIG(54);

		public final int size;

		private INVENTORY_SIZE(int size) {
			this.size = size;
		}

		int getSize() {
			return size;
		}
	}

	public static enum MAIN_GUI_PAGE {
		NONE(-1, "None"), MAIN(0, "Main"), CMD_SET_SELECT_USER(1, "Main>Set>Select user"), CMD_SET_SELECT_RANK(2, "Main>Set>Select user>Select rank"), CMD_CHECK_SELECT_USER(3, "Main>Check>Select user"),
		CMD_PROMOTE_SELECT_USER(4, "Main>Promote>Select user"), CMD_DEMOTE_SELECT_USER(5, "Main>Demote>Select user"), CMD_SETCHATCOLOR_SELECT_RANK(6, "Main>Set chat color>Select rank"),
		CMD_SETNAMECOLOR_SELECT_RANK(7, "Main>Set name color>Select rank"), CMD_SETCHATCOLOR_SELECT_COLOR(8, "Main>Set chat color>Select rank>Select color"), CMD_SETNAMECOLOR_SELECT_COLOR(9, "Main>Set name color>Select rank>Select color"),
		CMD_SETCHATCOLOR_SELECT_SPECIAL(10, "Main>Set chat color>Select rank>Select color>Select special"), CMD_SETNAMECOLOR_SELECT_SPECIAL(11, "Main>Set name color>Select rank>Select color>Select special"),
		CMD_ALLOWBUILD_SELECT_RANK(12, "Main>Allow build>Select rank"), CMD_ALLOWBUILD_YESNO(13, "Main>Allow build>Select rank>yes/no"), CMD_SETDEFAULTRANK_SELECT_RANK(14, "Main>Set default rank>Select rank"),
		CMD_ADDINHERITANCE_SELECT_RANK(15, "Main>Add inheritance>Select rank"), CMD_DELINHERITANCE_SELECT_RANK(16, "Main>Delete inheritance>Select rank"),
		CMD_ADDINHERITANCE_SELECT_RANK2(17, "Main>Add inheritance>Select rank>Select inheritance"), CMD_DELINHERITANCE_SELECT_RANK2(18, "Main>Delete inheritance>Select rank>Select inheritance");

		final int id;
		final String name;

		private MAIN_GUI_PAGE(int id, String name) {
			this.id = id;
			this.name = name;
		}

		public int getId() {
			return id;
		}

		public String getName() {
			return name;
		}

		public MAIN_GUI_PAGE getFromName(String name) {
			if (name.equalsIgnoreCase(MAIN.getName())) {
				return MAIN;
			} else if (name.equalsIgnoreCase(CMD_SET_SELECT_USER.getName())) {
				return CMD_SET_SELECT_USER;
			} else if (name.equalsIgnoreCase(CMD_SET_SELECT_RANK.getName())) {
				return CMD_SET_SELECT_RANK;
			} else if (name.equalsIgnoreCase(CMD_CHECK_SELECT_USER.getName())) {
				return CMD_CHECK_SELECT_USER;
			} else if (name.equalsIgnoreCase(CMD_PROMOTE_SELECT_USER.getName())) {
				return CMD_PROMOTE_SELECT_USER;
			} else if (name.equalsIgnoreCase(CMD_DEMOTE_SELECT_USER.getName())) {
				return CMD_DEMOTE_SELECT_USER;
			} else if (name.equalsIgnoreCase(CMD_SETCHATCOLOR_SELECT_RANK.getName())) {
				return CMD_SETCHATCOLOR_SELECT_RANK;
			} else if (name.equalsIgnoreCase(CMD_SETNAMECOLOR_SELECT_RANK.getName())) {
				return CMD_SETNAMECOLOR_SELECT_RANK;
			} else if (name.equalsIgnoreCase(CMD_SETCHATCOLOR_SELECT_COLOR.getName())) {
				return CMD_SETCHATCOLOR_SELECT_COLOR;
			} else if (name.equalsIgnoreCase(CMD_SETNAMECOLOR_SELECT_COLOR.getName())) {
				return CMD_SETNAMECOLOR_SELECT_COLOR;
			} else if (name.equalsIgnoreCase(CMD_SETCHATCOLOR_SELECT_SPECIAL.getName())) {
				return CMD_SETCHATCOLOR_SELECT_SPECIAL;
			} else if (name.equalsIgnoreCase(CMD_SETNAMECOLOR_SELECT_SPECIAL.getName())) {
				return CMD_SETNAMECOLOR_SELECT_SPECIAL;
			} else if (name.equalsIgnoreCase(CMD_ALLOWBUILD_SELECT_RANK.getName())) {
				return CMD_ALLOWBUILD_SELECT_RANK;
			} else if (name.equalsIgnoreCase(CMD_ALLOWBUILD_YESNO.getName())) {
				return CMD_ALLOWBUILD_YESNO;
			} else if (name.equalsIgnoreCase(CMD_SETDEFAULTRANK_SELECT_RANK.getName())) {
				return CMD_SETDEFAULTRANK_SELECT_RANK;
			} else if (name.equalsIgnoreCase(CMD_ADDINHERITANCE_SELECT_RANK.getName())) {
				return CMD_ADDINHERITANCE_SELECT_RANK;
			} else if (name.equalsIgnoreCase(CMD_DELINHERITANCE_SELECT_RANK.getName())) {
				return CMD_DELINHERITANCE_SELECT_RANK;
			} else if (name.equalsIgnoreCase(CMD_ADDINHERITANCE_SELECT_RANK2.getName())) {
				return CMD_ADDINHERITANCE_SELECT_RANK2;
			} else if (name.equalsIgnoreCase(CMD_DELINHERITANCE_SELECT_RANK2.getName())) {
				return CMD_DELINHERITANCE_SELECT_RANK2;
			} else {
				return null;
			}
		}
	}

	public static void setPlugin(PowerRanks powerRanks) {
		PowerRanksGUI_OLD.powerRanks = powerRanks;
	}

	public static void setupGUI() {
		inventoryGUIMain = Bukkit.createInventory(null, INVENTORY_SIZE.NORMAL.getSize(), "PowerRanks");
		inventoryGUIShop = Bukkit.createInventory(null, INVENTORY_SIZE.NORMAL.getSize(), "Rank shop");
	}

	public static ItemStack createGuiItem(final Material material, final String name, final String... lore) {
		final ItemStack item = new ItemStack(material, 1);
		final ItemMeta meta = item.getItemMeta();

		meta.setDisplayName(name);
		meta.setLore(Arrays.asList(lore));
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack createEmptyGuiItem() {
		final ItemStack item = new ItemStack(Material.AIR, 1);
		return item;
	}

	public static ItemStack createGuiHead(Player player, final String... lore) {
		ItemStack head = new ItemStack(Material.PLAYER_HEAD);
		SkullMeta headm = (SkullMeta) head.getItemMeta();
		headm.setDisplayName(player.getName());
		headm.setOwningPlayer(player);
		headm.setLore(Arrays.asList(lore));
		head.setItemMeta(headm);
		return head;
	}

	public static void openPowerRanksGUI(Player player, MAIN_GUI_PAGE menu, int page, String data) {
		if (page < 0) {
			openPowerRanksGUI(player, menu, page + 1, data);
			return;
		}

		int num_items_on_page = inventoryGUIMain.getSize() - 9;
		Users users = new Users(powerRanks);

		for (int i = 0; i < inventoryGUIMain.getSize() - 1; i++) {
			inventoryGUIMain.setItem(i, createEmptyGuiItem());
		}

		inventoryGUIMain.setItem(inventoryGUIMain.getSize() - 1, createGuiItem(Material.BARRIER, "Close"));
		inventoryGUIMain.setItem(inventoryGUIMain.getSize() - 2, createGuiItem(Material.COMPASS, "Navigation", menu.getName(), "Page " + String.valueOf(page + 1), data, "Left click: next page", "Right click: previous page"));

		if (menu.getId() == MAIN_GUI_PAGE.MAIN.getId()) {
			if (page > 0) {
				openPowerRanksGUI(player, menu, page - 1, data);
				return;
			}
			inventoryGUIMain.addItem(createGuiItem(Material.DISPENSER, "/pr set", "Change the rank of a player."));
			inventoryGUIMain.addItem(createGuiItem(Material.OBSERVER, "/pr check", "Check the current rank of a player."));
			inventoryGUIMain.addItem(createGuiItem(Material.GREEN_WOOL, "/pr promote", "Promote a player."));
			inventoryGUIMain.addItem(createGuiItem(Material.RED_WOOL, "/pr demote", "Demote a player."));
			inventoryGUIMain.addItem(createGuiItem(Material.YELLOW_WOOL, "/pr setchatcolor", "Change the chat color of a rank."));
			inventoryGUIMain.addItem(createGuiItem(Material.LIGHT_BLUE_WOOL, "/pr setnamecolor", "Change the name color of a rank."));
			inventoryGUIMain.addItem(createGuiItem(Material.GRASS_BLOCK, "/pr enablebuild /pr disablebuild", "Allow building for a specific rank."));
			inventoryGUIMain.addItem(createGuiItem(Material.STONE, "/pr setdefaultrank", "Set the default rank for new players."));
			inventoryGUIMain.addItem(createGuiItem(Material.DANDELION, "/pr addinheritance", "Add a inheritance to a rank."));
			inventoryGUIMain.addItem(createGuiItem(Material.POPPY, "/pr delinheritance", "Remove a inheritance from a rank."));

			if (data.length() > 0) {
				String[] data_split = data.split(":");

				if (data_split[0].equalsIgnoreCase("setrank")) {
					String playername = data_split[1];
					String rankname = data_split[2];
					users.setGroup(player, playername, users.getRankIgnoreCase(rankname));
				}

				if (data_split[0].equalsIgnoreCase("checkrank")) {
					String playername = data_split[1];
					users.getGroup(player.getName(), playername);
				}

				if (data_split[0].equalsIgnoreCase("promote")) {
					String playername = data_split[1];
					if (users.promote(playername)) {
						Messages.messageCommandPromoteSuccess(player, playername);
					} else {
						Messages.messageCommandPromoteError(player, playername);
					}
				}

				if (data_split[0].equalsIgnoreCase("demote")) {
					String playername = data_split[1];
					if (users.demote(playername)) {
						Messages.messageCommandDemoteSuccess(player, playername);
					} else {
						Messages.messageCommandDemoteError(player, playername);
					}
				}

				if (data_split[0].equalsIgnoreCase("setchatcolor")) {
					String rankname = data_split[1];
					String color = data_split.length >= 3 ? data_split[2] : "";
					String special = data_split.length >= 4 ? data_split[3] : "";
					users.setChatColor(users.getRankIgnoreCase(rankname), color + special);
					Messages.messageCommandSetChatColor(player, color + special, rankname);
				}

				if (data_split[0].equalsIgnoreCase("setnamecolor")) {
					String rankname = data_split[1];
					String color = data_split.length >= 3 ? data_split[2] : "";
					String special = data_split.length >= 4 ? data_split[3] : "";
					users.setNameColor(users.getRankIgnoreCase(rankname), color + special);
					Messages.messageCommandSetNameColor(player, color + special, rankname);
				}

				if (data_split[0].equalsIgnoreCase("allowbuild")) {
					String rankname = data_split[1];
					Boolean allow = data_split[2].equalsIgnoreCase("true");
					users.setBuild(rankname, allow);
					if (allow)
						Messages.messageCommandBuildEnabled(player, rankname);
					else
						Messages.messageCommandBuildDisabled(player, rankname);
				}

				if (data_split[0].equalsIgnoreCase("setdefaultrank")) {
					String rankname = data_split[1];
					users.setDefaultRank(rankname);
					Messages.messageCommandSetDefaultRankSuccess(player, rankname);
				}

				if (data_split[0].equalsIgnoreCase("addinheritance")) {
					String rankname = data_split[1];
					String inheritance = data_split[2];
					users.addInheritance(rankname, inheritance);
					Messages.messageCommandInheritanceAdded(player, inheritance, rankname);
				}

				if (data_split[0].equalsIgnoreCase("removeinheritance")) {
					String rankname = data_split[1];
					String inheritance = data_split[2];
					users.removeInheritance(rankname, inheritance);
					Messages.messageCommandInheritanceRemoved(player, inheritance, rankname);
				}

				openPowerRanksGUI(player, menu, page, "");
			}
		}

		if (menu.getId() == MAIN_GUI_PAGE.CMD_SET_SELECT_USER.getId() || menu.getId() == MAIN_GUI_PAGE.CMD_CHECK_SELECT_USER.getId() || menu.getId() == MAIN_GUI_PAGE.CMD_PROMOTE_SELECT_USER.getId()
				|| menu.getId() == MAIN_GUI_PAGE.CMD_DEMOTE_SELECT_USER.getId()) {
			Object[] online_players = Bukkit.getServer().getOnlinePlayers().toArray();

			if (num_items_on_page * page > online_players.length) {
				openPowerRanksGUI(player, menu, page - 1, data);
				return;
			}

			for (int i = 0; i < num_items_on_page; i++) {
				if (num_items_on_page * page + i < online_players.length) {
					Player online_player = (Player) online_players[num_items_on_page * page + i];
					String online_player_current_rank = users.getGroup(online_player);
					inventoryGUIMain.addItem(createGuiHead(online_player, online_player_current_rank));
				}
			}
		}

		if (menu.getId() == MAIN_GUI_PAGE.CMD_SET_SELECT_RANK.getId() || menu.getId() == MAIN_GUI_PAGE.CMD_SETCHATCOLOR_SELECT_RANK.getId() || menu.getId() == MAIN_GUI_PAGE.CMD_SETNAMECOLOR_SELECT_RANK.getId()
				|| menu.getId() == MAIN_GUI_PAGE.CMD_ALLOWBUILD_SELECT_RANK.getId() || menu.getId() == MAIN_GUI_PAGE.CMD_SETDEFAULTRANK_SELECT_RANK.getId() || menu.getId() == MAIN_GUI_PAGE.CMD_ADDINHERITANCE_SELECT_RANK.getId()
				|| menu.getId() == MAIN_GUI_PAGE.CMD_DELINHERITANCE_SELECT_RANK.getId() || menu.getId() == MAIN_GUI_PAGE.CMD_ADDINHERITANCE_SELECT_RANK2.getId() || menu.getId() == MAIN_GUI_PAGE.CMD_DELINHERITANCE_SELECT_RANK2.getId()) {
			Object[] ranks = users.getGroups().toArray();

			if (menu.getId() == MAIN_GUI_PAGE.CMD_ADDINHERITANCE_SELECT_RANK2.getId()) {
				ArrayList<String> ranks2 = new ArrayList<String>();
				for (String rank : users.getGroups()) {
					if (!rank.equalsIgnoreCase(data.split(":")[1]))
						ranks2.add(rank);
				}
				for (String inheritance : users.getInheritances(data.split(":")[1])) {
					if (ranks2.contains(inheritance)) ranks2.remove(inheritance);
				}
				ranks = ranks2.toArray();
			}

			if (menu.getId() == MAIN_GUI_PAGE.CMD_DELINHERITANCE_SELECT_RANK2.getId()) {
				ranks = users.getInheritances(data.split(":")[1]).toArray();
			}

			if (num_items_on_page * page > ranks.length) {
				openPowerRanksGUI(player, menu, page - 1, data);
				return;
			}

			for (int i = 0; i < num_items_on_page; i++) {
				if (num_items_on_page * page + i < ranks.length) {
					String rank = (String) ranks[num_items_on_page * page + i];
					if (users.getRanksConfigFieldString(rank, "gui.icon").length() > 0) {
						Material icon = Material.matchMaterial(Util.replaceAll(users.getRanksConfigFieldString(rank, "gui.icon"), " ", "_").toUpperCase(), true);
						if (icon != null)
							inventoryGUIMain.addItem(createGuiItem(icon, rank));
						else
							PowerRanks.log.warning("Rank '" + rank + "' has a invallid icon!");
					} else {
						PowerRanks.log.warning("Rank '" + rank + "' has no icon!");
					}
				}
			}

		}

		if (menu.getId() == MAIN_GUI_PAGE.CMD_SETNAMECOLOR_SELECT_COLOR.getId() || menu.getId() == MAIN_GUI_PAGE.CMD_SETCHATCOLOR_SELECT_COLOR.getId()) {
			if (page > 0) {
				openPowerRanksGUI(player, menu, page - 1, data);
				return;
			}
			inventoryGUIMain.addItem(BannerItem.addPattern(BannerItem.createEmpty("Black", "&0"), DyeColor.BLACK, PatternType.BASE));
			inventoryGUIMain.addItem(BannerItem.addPattern(BannerItem.createEmpty("Dark Blue", "&1"), DyeColor.BLUE, PatternType.BASE));
			inventoryGUIMain.addItem(BannerItem.addPattern(BannerItem.createEmpty("Dark Green", "&2"), DyeColor.GREEN, PatternType.BASE));
			inventoryGUIMain.addItem(BannerItem.addPattern(BannerItem.addPattern(BannerItem.createEmpty("Dark Turquoise", "&3"), DyeColor.CYAN, PatternType.BASE), DyeColor.BLACK, PatternType.GRADIENT));
			inventoryGUIMain.addItem(BannerItem.addPattern(BannerItem.addPattern(BannerItem.createEmpty("Dark Red", "&4"), DyeColor.RED, PatternType.BASE), DyeColor.BLACK, PatternType.GRADIENT));
			inventoryGUIMain.addItem(BannerItem.addPattern(BannerItem.createEmpty("Purple", "&5"), DyeColor.PURPLE, PatternType.BASE));
			inventoryGUIMain.addItem(BannerItem.addPattern(BannerItem.createEmpty("Orange", "&6"), DyeColor.ORANGE, PatternType.BASE));
			inventoryGUIMain.addItem(BannerItem.addPattern(BannerItem.createEmpty("Light Gray", "&7"), DyeColor.LIGHT_GRAY, PatternType.BASE));
			inventoryGUIMain.addItem(BannerItem.addPattern(BannerItem.createEmpty("Dark Gray", "&8"), DyeColor.GRAY, PatternType.BASE));
			inventoryGUIMain.addItem(BannerItem.addPattern(BannerItem.addPattern(BannerItem.createEmpty("Light Blue", "&9"), DyeColor.LIGHT_BLUE, PatternType.BASE), DyeColor.BLUE, PatternType.GRADIENT));
			inventoryGUIMain.addItem(BannerItem.addPattern(BannerItem.createEmpty("Light Green", "&a"), DyeColor.LIME, PatternType.BASE));
			inventoryGUIMain.addItem(BannerItem.addPattern(BannerItem.createEmpty("Light Turquoise", "&b"), DyeColor.LIGHT_BLUE, PatternType.BASE));
			inventoryGUIMain.addItem(BannerItem.addPattern(BannerItem.createEmpty("Red", "&c"), DyeColor.RED, PatternType.BASE));
			inventoryGUIMain.addItem(BannerItem.addPattern(BannerItem.createEmpty("Magenta", "&d"), DyeColor.MAGENTA, PatternType.BASE));
			inventoryGUIMain.addItem(BannerItem.addPattern(BannerItem.createEmpty("Yellow", "&e"), DyeColor.YELLOW, PatternType.BASE));
			inventoryGUIMain.addItem(BannerItem.addPattern(BannerItem.createEmpty("White", "&f"), DyeColor.WHITE, PatternType.BASE));
		}

		if (menu.getId() == MAIN_GUI_PAGE.CMD_SETNAMECOLOR_SELECT_SPECIAL.getId() || menu.getId() == MAIN_GUI_PAGE.CMD_SETCHATCOLOR_SELECT_SPECIAL.getId()) {
			if (page > 0) {
				openPowerRanksGUI(player, menu, page - 1, data);
				return;
			}
			inventoryGUIMain.addItem(BannerItem.addPattern(BannerItem.createEmpty("None", ""), DyeColor.WHITE, PatternType.BASE));
			inventoryGUIMain.addItem(BannerItem.addPattern(BannerItem.addPattern(BannerItem.createEmpty("Bold", "&l"), DyeColor.WHITE, PatternType.BASE), DyeColor.BLACK, PatternType.BORDER));
			inventoryGUIMain.addItem(
					BannerItem.addPattern(BannerItem.addPattern(BannerItem.addPattern(BannerItem.addPattern(BannerItem.createEmpty("Underline", "&n"), DyeColor.WHITE, PatternType.BASE), DyeColor.BLACK, PatternType.HALF_HORIZONTAL_MIRROR),
							DyeColor.WHITE, PatternType.STRIPE_BOTTOM), DyeColor.WHITE, PatternType.STRIPE_MIDDLE));
			inventoryGUIMain.addItem(BannerItem.addPattern(BannerItem.addPattern(BannerItem.createEmpty("Itallic", "&o"), DyeColor.WHITE, PatternType.BASE), DyeColor.BLACK, PatternType.STRIPE_DOWNLEFT));

			inventoryGUIMain.addItem(BannerItem
					.addPattern(BannerItem.addPattern(BannerItem.addPattern(BannerItem.addPattern(BannerItem.addPattern(BannerItem.createEmpty("Magic", "&k"), DyeColor.WHITE, PatternType.BASE), DyeColor.BLACK, PatternType.STRIPE_DOWNLEFT),
							DyeColor.BLACK, PatternType.TRIANGLE_BOTTOM), DyeColor.BLACK, PatternType.TRIANGLE_TOP), DyeColor.BLACK, PatternType.CIRCLE_MIDDLE));

			inventoryGUIMain.addItem(BannerItem.addPattern(BannerItem.addPattern(BannerItem.createEmpty("Strike", "&m"), DyeColor.WHITE, PatternType.BASE), DyeColor.BLACK, PatternType.STRIPE_MIDDLE));
		}

		if (menu.getId() == MAIN_GUI_PAGE.CMD_ALLOWBUILD_YESNO.getId()) {
			if (page > 0) {
				openPowerRanksGUI(player, menu, page - 1, data);
				return;
			}
			inventoryGUIMain.addItem(createGuiItem(Material.GREEN_WOOL, "Yes", "true"));
			inventoryGUIMain.addItem(createGuiItem(Material.RED_WOOL, "No", "false"));
		}

		player.openInventory(getPowerRanksGUI());
	}

	public static void openPowerRanksRankupGUI(Player player, int page) {
		if (page < 0) {
			openPowerRanksRankupGUI(player, page + 1);
			return;
		}
		Users users = new Users(powerRanks);
		for (int i = 0; i < inventoryGUIShop.getSize() - 1; i++) {
			inventoryGUIShop.setItem(i, createEmptyGuiItem());
		}
		List<String> ranks = users.getBuyableRanks(users.getGroup(player));
		int num_rank_on_page = inventoryGUIShop.getSize() - 9;

		if (num_rank_on_page * page > ranks.size()) {
			openPowerRanksRankupGUI(player, page - 1);
			return;
		}

		for (int i = 0; i < num_rank_on_page; i++) {
			if (num_rank_on_page * page + i < ranks.size()) {
				String rank = (String) ranks.get(num_rank_on_page * page + i);
				if (!rank.equalsIgnoreCase(users.getGroup(player))) {
					if (users.getRanksConfigFieldString(rank, "gui.icon").length() > 0) {
						Material icon = Material.matchMaterial(Util.replaceAll(users.getRanksConfigFieldString(rank, "gui.icon"), " ", "_").toUpperCase(), true);
						int cost = users.getRanksConfigFieldInt(rank, "economy.cost");
						if (icon != null)
							inventoryGUIShop.addItem(createGuiItem(icon, rank, "Cost: " + String.valueOf(cost)));
						else
							PowerRanks.log.warning("Rank '" + rank + "' has a invallid icon!");
					} else {
						PowerRanks.log.warning("Rank '" + rank + "' has no icon!");
					}
				}
			}
		}

		inventoryGUIShop.setItem(inventoryGUIShop.getSize() - 2, createGuiItem(Material.EMERALD, "Balance", String.valueOf(PowerRanks.getVaultEconomy().getBalance(player))));
		inventoryGUIShop.setItem(inventoryGUIShop.getSize() - 3, createGuiItem(Material.COMPASS, "Navigation", "Page " + String.valueOf(page + 1), "Left click: next page", "Right click: previous page"));
		inventoryGUIShop.setItem(inventoryGUIShop.getSize() - 1, createGuiItem(Material.BARRIER, "Close"));
		player.openInventory(inventoryGUIShop);
	}

	public static Inventory getPowerRanksGUI() {
		return inventoryGUIMain;
	}

	public static Inventory getPowerRanksGUIShop() {
		return inventoryGUIShop;
	}
}
