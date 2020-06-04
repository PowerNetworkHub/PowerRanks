package nl.svenar.PowerRanks.Events;

import org.bukkit.event.EventHandler;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Data.PowerRanksGUI;

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
		
		if (inventory == PowerRanksGUI.getPowerRanksGUI()) {
			PowerRanksGUI.getPowerRanksGUI().setItem(PowerRanksGUI.getPowerRanksGUI().getSize() - 2, PowerRanksGUI.createGuiHead(player));
			e.setCancelled(true);
			
			if (clickedItem == null || clickedItem.getType() == Material.AIR) return;
			
			player.sendMessage("You clicked at slot " + e.getRawSlot() + " - " + clickedItem.getItemMeta().getDisplayName());
		}
	}
	
	@EventHandler
	public void onInventoryDragEvent(InventoryDragEvent e) {
		if (e.getInventory() == PowerRanksGUI.getPowerRanksGUI()) {
			e.setCancelled(true);
		}
	}
}