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
				String current_menu = inventory.getItem(inventory.getSize() - 2).getItemMeta().getLore().get(0);

				if (e.getSlot() < PowerRanksGUI.getPowerRanksGUI().getSize() - 9) {
					if (current_menu.equalsIgnoreCase(PowerRanksGUI.MAIN_GUI_PAGE.MAIN.getName())) {
						if (clickedItem.getItemMeta().getDisplayName().equalsIgnoreCase("/pr set")) {
							PowerRanksGUI.openPowerRanksGUI(player, PowerRanksGUI.MAIN_GUI_PAGE.CMD_SET_SELECT_USER, 0, "setrank");
						}

						if (clickedItem.getItemMeta().getDisplayName().equalsIgnoreCase("/pr check")) {
							PowerRanksGUI.openPowerRanksGUI(player, PowerRanksGUI.MAIN_GUI_PAGE.CMD_CHECK_SELECT_USER, 0, "checkrank");
						}

						if (clickedItem.getItemMeta().getDisplayName().equalsIgnoreCase("/pr promote")) {
							PowerRanksGUI.openPowerRanksGUI(player, PowerRanksGUI.MAIN_GUI_PAGE.CMD_PROMOTE_SELECT_USER, 0, "promote");
						}

						if (clickedItem.getItemMeta().getDisplayName().equalsIgnoreCase("/pr demote")) {
							PowerRanksGUI.openPowerRanksGUI(player, PowerRanksGUI.MAIN_GUI_PAGE.CMD_DEMOTE_SELECT_USER, 0, "demote");
						}

						if (clickedItem.getItemMeta().getDisplayName().equalsIgnoreCase("/pr setnamecolor")) {
							PowerRanksGUI.openPowerRanksGUI(player, PowerRanksGUI.MAIN_GUI_PAGE.CMD_SETNAMECOLOR_SELECT_RANK, 0, "setnamecolor");
						}

						if (clickedItem.getItemMeta().getDisplayName().equalsIgnoreCase("/pr setchatcolor")) {
							PowerRanksGUI.openPowerRanksGUI(player, PowerRanksGUI.MAIN_GUI_PAGE.CMD_SETCHATCOLOR_SELECT_RANK, 0, "setchatcolor");
						}
						
						if (clickedItem.getItemMeta().getDisplayName().equalsIgnoreCase("/pr enablebuild /pr disablebuild")) {
							PowerRanksGUI.openPowerRanksGUI(player, PowerRanksGUI.MAIN_GUI_PAGE.CMD_ALLOWBUILD_SELECT_RANK, 0, "allowbuild");
						}
						
						if (clickedItem.getItemMeta().getDisplayName().equalsIgnoreCase("/pr setdefaultrank")) {
							PowerRanksGUI.openPowerRanksGUI(player, PowerRanksGUI.MAIN_GUI_PAGE.CMD_SETDEFAULTRANK_SELECT_RANK, 0, "setdefaultrank");
						}
						
						if (clickedItem.getItemMeta().getDisplayName().equalsIgnoreCase("/pr addinheritance")) {
							PowerRanksGUI.openPowerRanksGUI(player, PowerRanksGUI.MAIN_GUI_PAGE.CMD_ADDINHERITANCE_SELECT_RANK, 0, "addinheritance");
						}
						
						if (clickedItem.getItemMeta().getDisplayName().equalsIgnoreCase("/pr delinheritance")) {
							PowerRanksGUI.openPowerRanksGUI(player, PowerRanksGUI.MAIN_GUI_PAGE.CMD_DELINHERITANCE_SELECT_RANK, 0, "removeinheritance");
						}
					}

					if (current_menu.equalsIgnoreCase(PowerRanksGUI.MAIN_GUI_PAGE.CMD_SET_SELECT_USER.getName())) {
						String playername = clickedItem.getItemMeta().getDisplayName();
						String data = inventory.getItem(inventory.getSize() - 2).getItemMeta().getLore().get(2);
						PowerRanksGUI.openPowerRanksGUI(player, PowerRanksGUI.MAIN_GUI_PAGE.CMD_SET_SELECT_RANK, 0, data + (data.length() > 0 ? ":" : "") + playername);
					}

					if (current_menu.equalsIgnoreCase(PowerRanksGUI.MAIN_GUI_PAGE.CMD_SET_SELECT_RANK.getName())) {
						String rankname = clickedItem.getItemMeta().getDisplayName();
						String data = inventory.getItem(inventory.getSize() - 2).getItemMeta().getLore().get(2);
						PowerRanksGUI.openPowerRanksGUI(player, PowerRanksGUI.MAIN_GUI_PAGE.MAIN, 0, data + (data.length() > 0 ? ":" : "") + rankname);
					}

					if (current_menu.equalsIgnoreCase(PowerRanksGUI.MAIN_GUI_PAGE.CMD_CHECK_SELECT_USER.getName()) || current_menu.equalsIgnoreCase(PowerRanksGUI.MAIN_GUI_PAGE.CMD_PROMOTE_SELECT_USER.getName())
							|| current_menu.equalsIgnoreCase(PowerRanksGUI.MAIN_GUI_PAGE.CMD_DEMOTE_SELECT_USER.getName())) {
						String playername = clickedItem.getItemMeta().getDisplayName();
						String data = inventory.getItem(inventory.getSize() - 2).getItemMeta().getLore().get(2);
						PowerRanksGUI.openPowerRanksGUI(player, PowerRanksGUI.MAIN_GUI_PAGE.MAIN, 0, data + (data.length() > 0 ? ":" : "") + playername);
					}

					
					if (current_menu.equalsIgnoreCase(PowerRanksGUI.MAIN_GUI_PAGE.CMD_SETNAMECOLOR_SELECT_RANK.getName())) {
						String rankname = clickedItem.getItemMeta().getDisplayName();
						String data = inventory.getItem(inventory.getSize() - 2).getItemMeta().getLore().get(2);
						PowerRanksGUI.openPowerRanksGUI(player, PowerRanksGUI.MAIN_GUI_PAGE.CMD_SETNAMECOLOR_SELECT_COLOR, 0, data + (data.length() > 0 ? ":" : "") + rankname);
					}

					if (current_menu.equalsIgnoreCase(PowerRanksGUI.MAIN_GUI_PAGE.CMD_SETCHATCOLOR_SELECT_RANK.getName())) {
						String rankname = clickedItem.getItemMeta().getDisplayName();
						String data = inventory.getItem(inventory.getSize() - 2).getItemMeta().getLore().get(2);
						PowerRanksGUI.openPowerRanksGUI(player, PowerRanksGUI.MAIN_GUI_PAGE.CMD_SETCHATCOLOR_SELECT_COLOR, 0, data + (data.length() > 0 ? ":" : "") + rankname);
					}

					if (current_menu.equalsIgnoreCase(PowerRanksGUI.MAIN_GUI_PAGE.CMD_SETNAMECOLOR_SELECT_COLOR.getName())) {
						String color = clickedItem.getItemMeta().getLore().get(0);
						String data = inventory.getItem(inventory.getSize() - 2).getItemMeta().getLore().get(2);
						PowerRanksGUI.openPowerRanksGUI(player, PowerRanksGUI.MAIN_GUI_PAGE.CMD_SETNAMECOLOR_SELECT_SPECIAL, 0, data + (data.length() > 0 && color.length() > 0 ? ":" : "") + color);
					}

					if (current_menu.equalsIgnoreCase(PowerRanksGUI.MAIN_GUI_PAGE.CMD_SETCHATCOLOR_SELECT_COLOR.getName())) {
						String color = clickedItem.getItemMeta().getLore().get(0);
						String data = inventory.getItem(inventory.getSize() - 2).getItemMeta().getLore().get(2);
						PowerRanksGUI.openPowerRanksGUI(player, PowerRanksGUI.MAIN_GUI_PAGE.CMD_SETCHATCOLOR_SELECT_SPECIAL, 0, data + (data.length() > 0 && color.length() > 0 ? ":" : "") + color);
					}

					if (current_menu.equalsIgnoreCase(PowerRanksGUI.MAIN_GUI_PAGE.CMD_SETNAMECOLOR_SELECT_SPECIAL.getName())) {
						String special = clickedItem.getItemMeta().getLore().get(0);
						String data = inventory.getItem(inventory.getSize() - 2).getItemMeta().getLore().get(2);
						PowerRanksGUI.openPowerRanksGUI(player, PowerRanksGUI.MAIN_GUI_PAGE.MAIN, 0, data + (data.length() > 0 && special.length() > 0 ? ":" : "") + special);
					}

					if (current_menu.equalsIgnoreCase(PowerRanksGUI.MAIN_GUI_PAGE.CMD_SETCHATCOLOR_SELECT_SPECIAL.getName())) {
						String special = clickedItem.getItemMeta().getLore().get(0);
						String data = inventory.getItem(inventory.getSize() - 2).getItemMeta().getLore().get(2);
						PowerRanksGUI.openPowerRanksGUI(player, PowerRanksGUI.MAIN_GUI_PAGE.MAIN, 0, data + (data.length() > 0 && special.length() > 0 ? ":" : "") + special);
					}
					
					if (current_menu.equalsIgnoreCase(PowerRanksGUI.MAIN_GUI_PAGE.CMD_ALLOWBUILD_SELECT_RANK.getName())) {
						String rankname = clickedItem.getItemMeta().getDisplayName();
						String data = inventory.getItem(inventory.getSize() - 2).getItemMeta().getLore().get(2);
						PowerRanksGUI.openPowerRanksGUI(player, PowerRanksGUI.MAIN_GUI_PAGE.CMD_ALLOWBUILD_YESNO, 0, data + (data.length() > 0 ? ":" : "") + rankname);
					}
					
					if (current_menu.equalsIgnoreCase(PowerRanksGUI.MAIN_GUI_PAGE.CMD_ALLOWBUILD_YESNO.getName())) {
						String option = clickedItem.getItemMeta().getLore().get(0);
						String data = inventory.getItem(inventory.getSize() - 2).getItemMeta().getLore().get(2);
						PowerRanksGUI.openPowerRanksGUI(player, PowerRanksGUI.MAIN_GUI_PAGE.MAIN, 0, data + (data.length() > 0 ? ":" : "") + option);
					}
					
					if (current_menu.equalsIgnoreCase(PowerRanksGUI.MAIN_GUI_PAGE.CMD_SETDEFAULTRANK_SELECT_RANK.getName())) {
						String rankname = clickedItem.getItemMeta().getDisplayName();
						String data = inventory.getItem(inventory.getSize() - 2).getItemMeta().getLore().get(2);
						PowerRanksGUI.openPowerRanksGUI(player, PowerRanksGUI.MAIN_GUI_PAGE.MAIN, 0, data + (data.length() > 0 ? ":" : "") + rankname);
					}
					
					if (current_menu.equalsIgnoreCase(PowerRanksGUI.MAIN_GUI_PAGE.CMD_ADDINHERITANCE_SELECT_RANK.getName())) {
						String rankname = clickedItem.getItemMeta().getDisplayName();
						String data = inventory.getItem(inventory.getSize() - 2).getItemMeta().getLore().get(2);
						PowerRanksGUI.openPowerRanksGUI(player, PowerRanksGUI.MAIN_GUI_PAGE.CMD_ADDINHERITANCE_SELECT_RANK2, 0, data + (data.length() > 0 ? ":" : "") + rankname);
					}
					
					if (current_menu.equalsIgnoreCase(PowerRanksGUI.MAIN_GUI_PAGE.CMD_ADDINHERITANCE_SELECT_RANK2.getName())) {
						String rankname = clickedItem.getItemMeta().getDisplayName();
						String data = inventory.getItem(inventory.getSize() - 2).getItemMeta().getLore().get(2);
						PowerRanksGUI.openPowerRanksGUI(player, PowerRanksGUI.MAIN_GUI_PAGE.MAIN, 0, data + (data.length() > 0 ? ":" : "") + rankname);
					}
					
					if (current_menu.equalsIgnoreCase(PowerRanksGUI.MAIN_GUI_PAGE.CMD_DELINHERITANCE_SELECT_RANK.getName())) {
						String rankname = clickedItem.getItemMeta().getDisplayName();
						String data = inventory.getItem(inventory.getSize() - 2).getItemMeta().getLore().get(2);
						PowerRanksGUI.openPowerRanksGUI(player, PowerRanksGUI.MAIN_GUI_PAGE.CMD_DELINHERITANCE_SELECT_RANK2, 0, data + (data.length() > 0 ? ":" : "") + rankname);
					}
					
					if (current_menu.equalsIgnoreCase(PowerRanksGUI.MAIN_GUI_PAGE.CMD_DELINHERITANCE_SELECT_RANK2.getName())) {
						String rankname = clickedItem.getItemMeta().getDisplayName();
						String data = inventory.getItem(inventory.getSize() - 2).getItemMeta().getLore().get(2);
						PowerRanksGUI.openPowerRanksGUI(player, PowerRanksGUI.MAIN_GUI_PAGE.MAIN, 0, data + (data.length() > 0 ? ":" : "") + rankname);
					}
				} else {
					if (e.getSlot() == PowerRanksGUI.getPowerRanksGUI().getSize() - 2) {
						int current_page = Integer.parseInt(clickedItem.getItemMeta().getLore().get(1).toLowerCase().replaceAll("page", "").replaceAll(" ", "")) - 1;
						String data = inventory.getItem(inventory.getSize() - 2).getItemMeta().getLore().get(2);
						if (e.isLeftClick()) {
							PowerRanksGUI.openPowerRanksGUI(player, PowerRanksGUI.MAIN_GUI_PAGE.NONE.getFromName(current_menu), current_page + 1, data);
						}

						if (e.isRightClick()) {
							PowerRanksGUI.openPowerRanksGUI(player, PowerRanksGUI.MAIN_GUI_PAGE.NONE.getFromName(current_menu), current_page - 1, data);
						}
					}
				}

			} else if (inventory == PowerRanksGUI.getPowerRanksGUIShop()) {
				if (PowerRanks.getVaultEconomy() != null) {
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
	}

	@EventHandler
	public void onInventoryDragEvent(InventoryDragEvent e) {
		if (e.getInventory() == PowerRanksGUI.getPowerRanksGUI() || e.getInventory() == PowerRanksGUI.getPowerRanksGUIShop()) {
			e.setCancelled(true);
		}
	}
}