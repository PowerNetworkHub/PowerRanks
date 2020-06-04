package nl.svenar.PowerRanks.Data;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class PowerRanksGUI {

	private static Inventory inventoryGUI;

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

	public static void setupMainGUI() {
		inventoryGUI = Bukkit.createInventory(null, INVENTORY_SIZE.NORMAL.getSize(), "PowerRanks");

		inventoryGUI.addItem(createGuiItem(Material.CAKE, "CAKE!", "Isn't it nice?", "", "A cake", "for you?"));
		inventoryGUI.setItem(inventoryGUI.getSize() - 1, createGuiItem(Material.BARRIER, "Close"));
	}

	public static ItemStack createGuiItem(final Material material, final String name, final String... lore) {
		final ItemStack item = new ItemStack(material, 1);
		final ItemMeta meta = item.getItemMeta();

		meta.setDisplayName(name);
		meta.setLore(Arrays.asList(lore));
		item.setItemMeta(meta);
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
		PowerRanksGUI.setupMainGUI();
		player.openInventory(getPowerRanksGUI());
	}

	public static Inventory getPowerRanksGUI() {
		return inventoryGUI;
	}
}
