package nl.svenar.PowerRanks.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Util;
import nl.svenar.PowerRanks.Data.Users;

public class GUIPage {

	Inventory gui = null;
	private Player player;
	private Users users;
	private GUI_PAGE_ID pageID = null;
	public int current_page = 0;
	private static HashMap<String, String> storedData = new HashMap<String, String>();

	public static enum GUI_PAGE_ID {
		RANKUP(0, "rankup"), MAIN(1, "main"), CMD_SETRANK_INPUT_PLAYER(2, "select player"), CMD_SETRANK_INPUT_RANK(3, "select rank");

		public final int id;
		public final String name;

		private GUI_PAGE_ID(int id, String name) {
			this.id = id;
			this.name = name;
		}

		int getID() {
			return id;
		}

		String getName() {
			return name;
		}
	}

	public static enum INVENTORY_SIZE {
		SMALL(9), NORMAL(27), BIG(54);

		public final int size;

		private INVENTORY_SIZE(int size) {
			this.size = size;
		}

		int getSize() {
			return size;
		}
	}

	public GUIPage(PowerRanks powerRanks, Player player, GUI_PAGE_ID pageID) {
		this.player = player;
		this.users = new Users(powerRanks);
		this.pageID = pageID;
		this.gui = setupGUI(pageID);
	}

	Inventory setupGUI(GUI_PAGE_ID pageID) {
		if (current_page < 0) {
			current_page = 0;
		}

		Inventory new_gui = Bukkit.createInventory(null, INVENTORY_SIZE.NORMAL.getSize(), "PowerRanks " + pageID.getName());

		for (int i = 0; i < new_gui.getSize(); i++) {
			try {
				new_gui.setItem(i, createGuiItem(Material.RED_STAINED_GLASS_PANE, " "));
			} catch (NoSuchFieldError e) {
				new_gui.setItem(i, createEmptyGuiItem());
			}
		}

		new_gui.setItem(new_gui.getSize() - 5, createGuiItem(Material.BARRIER, "Close", PowerRanks.pdf.getName().toLowerCase()));

		if (pageID.getID() == GUI_PAGE_ID.RANKUP.getID()) {
			List<String> ranks = users.getBuyableRanks(users.getGroup(player));
			int num_rank_on_page = new_gui.getSize() - 9;

			while (num_rank_on_page * current_page > ranks.size()) {
				current_page -= 1;
			}

			new_gui.setItem(new_gui.getSize() - 6, createGuiItem(Material.PAPER, "Previous page", PowerRanks.pdf.getName().toLowerCase(), "Page " + current_page));
			new_gui.setItem(new_gui.getSize() - 4, createGuiItem(Material.PAPER, "Next page", PowerRanks.pdf.getName().toLowerCase(), "Page " + current_page));
			new_gui.setItem(new_gui.getSize() - 9, createGuiItem(Material.NOTE_BLOCK, "Current rank", users.getGroup(player), ChatColor.RESET + PowerRanks.chatColor(PowerRanks.colorChar.charAt(0), users.getPrefix(player), true)));

			for (int i = 0; i < num_rank_on_page; i++) {
				if (num_rank_on_page * current_page + i < ranks.size()) {
					String rank = (String) ranks.get(num_rank_on_page * current_page + i);
					if (!rank.equalsIgnoreCase(users.getGroup(player))) {
						if (users.getRanksConfigFieldString(rank, "gui.icon").length() > 0) {
							Material icon = Material.BARRIER;
							try {
								icon = Material.matchMaterial(Util.replaceAll(users.getRanksConfigFieldString(rank, "gui.icon"), " ", "_").toUpperCase(), true);
							} catch (NoSuchMethodError e) {
								icon = Material.getMaterial(Util.replaceAll(users.getRanksConfigFieldString(rank, "gui.icon"), " ", "_").toUpperCase());
							}
							int cost = users.getRanksConfigFieldInt(rank, "economy.cost");
							if (icon != null)
								new_gui.setItem(i, createGuiItem(icon, rank, ChatColor.RESET + PowerRanks.chatColor(PowerRanks.colorChar.charAt(0), users.getPrefix(rank), true), "Cost: " + String.valueOf(cost)));
							else
								PowerRanks.log.warning("Rank '" + rank + "' has a invallid icon!");
						} else {
							PowerRanks.log.warning("Rank '" + rank + "' has no icon!");
						}
					}
				}
			}

			new_gui.setItem(new_gui.getSize() - 1, createGuiItem(Material.EMERALD, "Balance", String.valueOf(PowerRanks.getVaultEconomy().getBalance(player))));
		}

		if (pageID.getID() == GUI_PAGE_ID.MAIN.getID()) {
			new_gui.setItem(0, createGuiItem(Material.DISPENSER, ChatColor.GREEN + "Set a players rank", ChatColor.RESET + "/pr set <player> <rank>", ChatColor.BLACK + "cmd:setrank"));
		}

		if (pageID.getID() == GUI_PAGE_ID.CMD_SETRANK_INPUT_PLAYER.getID()) {
			Object[] online_players = Bukkit.getServer().getOnlinePlayers().toArray();
			int num_items_on_page = new_gui.getSize() - 9;

			while (num_items_on_page * current_page > online_players.length) {
				current_page -= 1;
			}

			new_gui.setItem(new_gui.getSize() - 6, createGuiItem(Material.PAPER, "Previous page", PowerRanks.pdf.getName().toLowerCase(), "Page " + current_page));
			new_gui.setItem(new_gui.getSize() - 4, createGuiItem(Material.PAPER, "Next page", PowerRanks.pdf.getName().toLowerCase(), "Page " + current_page));

			for (int i = 0; i < num_items_on_page; i++) {
				if (num_items_on_page * current_page + i < online_players.length) {
					Player online_player = (Player) online_players[num_items_on_page * current_page + i];
					String online_player_current_rank = users.getGroup(online_player);
					new_gui.setItem(i, createGuiHead(online_player, ChatColor.RESET + online_player_current_rank, ChatColor.RESET + PowerRanks.chatColor(PowerRanks.colorChar.charAt(0), users.getPrefix(player), true)));
				}
			}
		}

		if (pageID.getID() == GUI_PAGE_ID.CMD_SETRANK_INPUT_RANK.getID()) {
			Object[] ranks = users.getGroups().toArray();
			int num_rank_on_page = new_gui.getSize() - 9;

			while (num_rank_on_page * current_page > ranks.length) {
				current_page -= 1;
			}

			new_gui.setItem(new_gui.getSize() - 6, createGuiItem(Material.PAPER, "Previous page", PowerRanks.pdf.getName().toLowerCase(), "Page " + current_page));
			new_gui.setItem(new_gui.getSize() - 4, createGuiItem(Material.PAPER, "Next page", PowerRanks.pdf.getName().toLowerCase(), "Page " + current_page));

			for (int i = 0; i < num_rank_on_page; i++) {
				if (num_rank_on_page * current_page + i < ranks.length) {
					String rank = (String) ranks[num_rank_on_page * current_page + i];
					if (users.getRanksConfigFieldString(rank, "gui.icon").length() > 0) {
						Material icon = Material.BARRIER;
						try {
							icon = Material.matchMaterial(Util.replaceAll(users.getRanksConfigFieldString(rank, "gui.icon"), " ", "_").toUpperCase(), true);
						} catch (NoSuchMethodError e) {
							icon = Material.getMaterial(Util.replaceAll(users.getRanksConfigFieldString(rank, "gui.icon"), " ", "_").toUpperCase());
						}
						if (icon != null)
							new_gui.setItem(i, createGuiItem(icon, rank, ChatColor.RESET + PowerRanks.chatColor(PowerRanks.colorChar.charAt(0), users.getPrefix(rank), true)));
						else
							PowerRanks.log.warning("Rank '" + rank + "' has a invallid icon!");
					} else {
						PowerRanks.log.warning("Rank '" + rank + "' has no icon!");
					}
				}
			}
		}

		return new_gui;
	}

	public Inventory getGUI() {
		return gui;
	}

	public GUI_PAGE_ID getPageID() {
		return pageID;
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
		ItemStack head = null;
		try {
			head = new ItemStack(Material.PLAYER_HEAD);
			SkullMeta headm = (SkullMeta) head.getItemMeta();
			headm.setDisplayName(player.getName());
			headm.setOwningPlayer(player);
			headm.setLore(Arrays.asList(lore));
			head.setItemMeta(headm);

		} catch (NoSuchFieldError e) { // TODO Fix skull head for 1.8
			List<String> loreLines = new ArrayList<String>();
			loreLines.addAll(Arrays.asList(lore));
			loreLines.add(ChatColor.RED + "Cannot show player head");
			head = new ItemStack(Material.STICK);
			ItemMeta headm = head.getItemMeta();
			headm.setDisplayName(player.getName());
			headm.setLore(loreLines);
			head.setItemMeta(headm);
		}

		return head;
	}

	public void setData(String key, String value) {
		storedData.put(key, value);
	}

	public String getData(String key) {
		return storedData.get(key);
	}
}
