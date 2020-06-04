package nl.svenar.PowerRanks.Events;

import org.bukkit.event.EventHandler;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Data.Messages;
import nl.svenar.PowerRanks.Data.PowerRanksGUI;
import nl.svenar.PowerRanks.Data.Users;

import org.bukkit.event.Listener;

public class OnInventory implements Listener {
	PowerRanks m;

	public OnInventory(final PowerRanks m) {
		this.m = m;
	}

	@EventHandler
	public void onInventoryClickEvent(InventoryClickEvent e) {
		Player player = (Player) e.getWhoClicked();

		Inventory inventory = e.getInventory();
		ItemStack clickedItem = e.getCurrentItem();

		if (inventory == PowerRanksGUI.getPowerRanksGUI() || inventory == PowerRanksGUI.getPowerRanksGUIShop()) {
			e.setCancelled(true);

			if (clickedItem == null || clickedItem.getType() == Material.AIR)
				return;

			if (clickedItem.getItemMeta().getDisplayName().equalsIgnoreCase("close"))
				player.closeInventory();

			if (inventory == PowerRanksGUI.getPowerRanksGUI()) {
				player.sendMessage("You clicked at slot " + e.getRawSlot() + " - " + clickedItem.getItemMeta().getDisplayName());
				
			} else if (inventory == PowerRanksGUI.getPowerRanksGUIShop()) {
				if (e.getSlot() < PowerRanksGUI.getPowerRanksGUIShop().getSize() - 9) {
					Users users = new Users(this.m);
					String rankname = clickedItem.getItemMeta().getDisplayName();
					int cost = users.getRanksConfigFieldInt(rankname, "economy.cost");
					double player_balance = PowerRanks.getVaultEconomy().getBalance(player);
					if (cost >= 0 && player_balance >= cost) {
						PowerRanks.getVaultEconomy().withdrawPlayer(player, cost);
						users.setGroup(player, rankname);
						Messages.messageBuyRankSuccess(player, rankname);
					} else {
						Messages.messageBuyRankError(player, rankname);
					}
					player.closeInventory();
				}

				if (e.getSlot() == PowerRanksGUI.getPowerRanksGUIShop().getSize() - 3) {
					int current_page = Integer.parseInt(clickedItem.getItemMeta().getLore().get(0).toLowerCase().replaceAll("page", "").replaceAll(" ", "")) - 1;
					if (e.isLeftClick()) {
						PowerRanksGUI.openPowerRanksRankupGUI(player, current_page + 1);
					}

					if (e.isRightClick()) {
						PowerRanksGUI.openPowerRanksRankupGUI(player, current_page - 1);
					}
				}
			}
		}
	}

	@EventHandler
	public void onInventoryDragEvent(InventoryDragEvent e) {
		if (e.getInventory() == PowerRanksGUI.getPowerRanksGUI() || e.getInventory() == PowerRanksGUI.getPowerRanksGUIShop()) {
			e.setCancelled(true);
		}
	}
}