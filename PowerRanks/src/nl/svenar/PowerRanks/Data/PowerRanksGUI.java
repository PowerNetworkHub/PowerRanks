package nl.svenar.PowerRanks.Data;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Util;

public class PowerRanksGUI {

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

	public static void setPlugin(PowerRanks powerRanks) {
		PowerRanksGUI.powerRanks = powerRanks;
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

	public static ItemStack createGuiHead(Player player) {
		ItemStack head = new ItemStack(Material.PLAYER_HEAD);
		SkullMeta headm = (SkullMeta) head.getItemMeta();
		headm.setDisplayName(player.getName());
		headm.setOwningPlayer(player);
		head.setItemMeta(headm);
		return head;
	}

	public static void openPowerRanksGUI(Player player) {
//		inventoryGUIMain.addItem(createGuiItem(Material.CAKE, "CAKE!", "Isn't it nice?", "", "A cake", "for you?"));
		inventoryGUIMain.setItem(inventoryGUIMain.getSize() - 1, createGuiItem(Material.BARRIER, "Close"));
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
		Object[] ranks = users.getGroups().toArray();
		int num_rank_on_page = inventoryGUIShop.getSize() - 9;
		
		if (num_rank_on_page * page > ranks.length) {
			openPowerRanksRankupGUI(player, page - 1);
			return;
		}

		for (int i = 0; i < num_rank_on_page; i++) {
			if (num_rank_on_page * page + i < ranks.length) {
				String rank = (String) ranks[num_rank_on_page * page + i];
				if (users.getRanksConfigFieldBoolean(rank, "economy.buyable") && !rank.equalsIgnoreCase(users.getGroup(player))) {
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
