package nl.svenar.PowerRanks.Events;

import org.bukkit.event.EventHandler;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.gui.GUI;

import org.bukkit.event.Listener;

public class OnInventory implements Listener {
	PowerRanks m;
	
	public OnInventory(final PowerRanks m) {
		this.m = m;
	}

	@EventHandler
	public void onInventoryClickEvent(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();

		Inventory inventory = event.getInventory();

		if (!GUI.isPowerRanksGUI(inventory))
			return;
		
		event.setCancelled(true);
		
		GUI.clickedItem(player, event.getSlot());
	}

	@EventHandler
	public void onInventoryDragEvent(InventoryDragEvent event) {
		if (!GUI.isPowerRanksGUI(event.getInventory()))
			return;
		
		event.setCancelled(true);
	}
}