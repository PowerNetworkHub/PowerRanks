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
import nl.svenar.PowerRanks.Data.Users;
import nl.svenar.PowerRanks.gui.PowerRanksGUI_OLD;

import org.bukkit.event.Listener;

public class OnInventory_OLD implements Listener {
	PowerRanks m;

	public OnInventory_OLD(final PowerRanks m) {
		this.m = m;
	}

	@EventHandler
	public void onInventoryClickEvent(InventoryClickEvent e) {
		Player player = (Player) e.getWhoClicked();

		Inventory inventory = e.getInventory();
		ItemStack clickedItem = e.getCurrentItem();

		if (inventory == PowerRanksGUI_OLD.getPowerRanksGUI() || inventory == PowerRanksGUI_OLD.getPowerRanksGUIShop()) {
			e.setCancelled(true);

			if (clickedItem == null || clickedItem.getType() == Material.AIR)
				return;

			if (clickedItem.getItemMeta().getDisplayName().equalsIgnoreCase("close"))
				player.closeInventory();

			if (inventory == PowerRanksGUI_OLD.getPowerRanksGUI()) {
				String current_menu = inventory.getItem(inventory.getSize() - 2).getItemMeta().getLore().get(0);

				if (e.getSlot() < PowerRanksGUI_OLD.getPowerRanksGUI().getSize() - 9) {
					if (current_menu.equalsIgnoreCase(PowerRanksGUI_OLD.MAIN_GUI_PAGE.MAIN.getName())) {
						if (clickedItem.getItemMeta().getDisplayName().equalsIgnoreCase("/pr set")) {
							PowerRanksGUI_OLD.openPowerRanksGUI(player, PowerRanksGUI_OLD.MAIN_GUI_PAGE.CMD_SET_SELECT_USER, 0, "setrank");
						}

						if (clickedItem.getItemMeta().getDisplayName().equalsIgnoreCase("/pr check")) {
							PowerRanksGUI_OLD.openPowerRanksGUI(player, PowerRanksGUI_OLD.MAIN_GUI_PAGE.CMD_CHECK_SELECT_USER, 0, "checkrank");
						}

						if (clickedItem.getItemMeta().getDisplayName().equalsIgnoreCase("/pr promote")) {
							PowerRanksGUI_OLD.openPowerRanksGUI(player, PowerRanksGUI_OLD.MAIN_GUI_PAGE.CMD_PROMOTE_SELECT_USER, 0, "promote");
						}

						if (clickedItem.getItemMeta().getDisplayName().equalsIgnoreCase("/pr demote")) {
							PowerRanksGUI_OLD.openPowerRanksGUI(player, PowerRanksGUI_OLD.MAIN_GUI_PAGE.CMD_DEMOTE_SELECT_USER, 0, "demote");
						}

						if (clickedItem.getItemMeta().getDisplayName().equalsIgnoreCase("/pr setnamecolor")) {
							PowerRanksGUI_OLD.openPowerRanksGUI(player, PowerRanksGUI_OLD.MAIN_GUI_PAGE.CMD_SETNAMECOLOR_SELECT_RANK, 0, "setnamecolor");
						}

						if (clickedItem.getItemMeta().getDisplayName().equalsIgnoreCase("/pr setchatcolor")) {
							PowerRanksGUI_OLD.openPowerRanksGUI(player, PowerRanksGUI_OLD.MAIN_GUI_PAGE.CMD_SETCHATCOLOR_SELECT_RANK, 0, "setchatcolor");
						}
						
						if (clickedItem.getItemMeta().getDisplayName().equalsIgnoreCase("/pr enablebuild /pr disablebuild")) {
							PowerRanksGUI_OLD.openPowerRanksGUI(player, PowerRanksGUI_OLD.MAIN_GUI_PAGE.CMD_ALLOWBUILD_SELECT_RANK, 0, "allowbuild");
						}
						
						if (clickedItem.getItemMeta().getDisplayName().equalsIgnoreCase("/pr setdefaultrank")) {
							PowerRanksGUI_OLD.openPowerRanksGUI(player, PowerRanksGUI_OLD.MAIN_GUI_PAGE.CMD_SETDEFAULTRANK_SELECT_RANK, 0, "setdefaultrank");
						}
						
						if (clickedItem.getItemMeta().getDisplayName().equalsIgnoreCase("/pr addinheritance")) {
							PowerRanksGUI_OLD.openPowerRanksGUI(player, PowerRanksGUI_OLD.MAIN_GUI_PAGE.CMD_ADDINHERITANCE_SELECT_RANK, 0, "addinheritance");
						}
						
						if (clickedItem.getItemMeta().getDisplayName().equalsIgnoreCase("/pr delinheritance")) {
							PowerRanksGUI_OLD.openPowerRanksGUI(player, PowerRanksGUI_OLD.MAIN_GUI_PAGE.CMD_DELINHERITANCE_SELECT_RANK, 0, "removeinheritance");
						}
					}

					if (current_menu.equalsIgnoreCase(PowerRanksGUI_OLD.MAIN_GUI_PAGE.CMD_SET_SELECT_USER.getName())) {
						String playername = clickedItem.getItemMeta().getDisplayName();
						String data = inventory.getItem(inventory.getSize() - 2).getItemMeta().getLore().get(2);
						PowerRanksGUI_OLD.openPowerRanksGUI(player, PowerRanksGUI_OLD.MAIN_GUI_PAGE.CMD_SET_SELECT_RANK, 0, data + (data.length() > 0 ? ":" : "") + playername);
					}

					if (current_menu.equalsIgnoreCase(PowerRanksGUI_OLD.MAIN_GUI_PAGE.CMD_SET_SELECT_RANK.getName())) {
						String rankname = clickedItem.getItemMeta().getDisplayName();
						String data = inventory.getItem(inventory.getSize() - 2).getItemMeta().getLore().get(2);
						PowerRanksGUI_OLD.openPowerRanksGUI(player, PowerRanksGUI_OLD.MAIN_GUI_PAGE.MAIN, 0, data + (data.length() > 0 ? ":" : "") + rankname);
					}

					if (current_menu.equalsIgnoreCase(PowerRanksGUI_OLD.MAIN_GUI_PAGE.CMD_CHECK_SELECT_USER.getName()) || current_menu.equalsIgnoreCase(PowerRanksGUI_OLD.MAIN_GUI_PAGE.CMD_PROMOTE_SELECT_USER.getName())
							|| current_menu.equalsIgnoreCase(PowerRanksGUI_OLD.MAIN_GUI_PAGE.CMD_DEMOTE_SELECT_USER.getName())) {
						String playername = clickedItem.getItemMeta().getDisplayName();
						String data = inventory.getItem(inventory.getSize() - 2).getItemMeta().getLore().get(2);
						PowerRanksGUI_OLD.openPowerRanksGUI(player, PowerRanksGUI_OLD.MAIN_GUI_PAGE.MAIN, 0, data + (data.length() > 0 ? ":" : "") + playername);
					}

					
					if (current_menu.equalsIgnoreCase(PowerRanksGUI_OLD.MAIN_GUI_PAGE.CMD_SETNAMECOLOR_SELECT_RANK.getName())) {
						String rankname = clickedItem.getItemMeta().getDisplayName();
						String data = inventory.getItem(inventory.getSize() - 2).getItemMeta().getLore().get(2);
						PowerRanksGUI_OLD.openPowerRanksGUI(player, PowerRanksGUI_OLD.MAIN_GUI_PAGE.CMD_SETNAMECOLOR_SELECT_COLOR, 0, data + (data.length() > 0 ? ":" : "") + rankname);
					}

					if (current_menu.equalsIgnoreCase(PowerRanksGUI_OLD.MAIN_GUI_PAGE.CMD_SETCHATCOLOR_SELECT_RANK.getName())) {
						String rankname = clickedItem.getItemMeta().getDisplayName();
						String data = inventory.getItem(inventory.getSize() - 2).getItemMeta().getLore().get(2);
						PowerRanksGUI_OLD.openPowerRanksGUI(player, PowerRanksGUI_OLD.MAIN_GUI_PAGE.CMD_SETCHATCOLOR_SELECT_COLOR, 0, data + (data.length() > 0 ? ":" : "") + rankname);
					}

					if (current_menu.equalsIgnoreCase(PowerRanksGUI_OLD.MAIN_GUI_PAGE.CMD_SETNAMECOLOR_SELECT_COLOR.getName())) {
						String color = clickedItem.getItemMeta().getLore().get(0);
						String data = inventory.getItem(inventory.getSize() - 2).getItemMeta().getLore().get(2);
						PowerRanksGUI_OLD.openPowerRanksGUI(player, PowerRanksGUI_OLD.MAIN_GUI_PAGE.CMD_SETNAMECOLOR_SELECT_SPECIAL, 0, data + (data.length() > 0 && color.length() > 0 ? ":" : "") + color);
					}

					if (current_menu.equalsIgnoreCase(PowerRanksGUI_OLD.MAIN_GUI_PAGE.CMD_SETCHATCOLOR_SELECT_COLOR.getName())) {
						String color = clickedItem.getItemMeta().getLore().get(0);
						String data = inventory.getItem(inventory.getSize() - 2).getItemMeta().getLore().get(2);
						PowerRanksGUI_OLD.openPowerRanksGUI(player, PowerRanksGUI_OLD.MAIN_GUI_PAGE.CMD_SETCHATCOLOR_SELECT_SPECIAL, 0, data + (data.length() > 0 && color.length() > 0 ? ":" : "") + color);
					}

					if (current_menu.equalsIgnoreCase(PowerRanksGUI_OLD.MAIN_GUI_PAGE.CMD_SETNAMECOLOR_SELECT_SPECIAL.getName())) {
						String special = clickedItem.getItemMeta().getLore().get(0);
						String data = inventory.getItem(inventory.getSize() - 2).getItemMeta().getLore().get(2);
						PowerRanksGUI_OLD.openPowerRanksGUI(player, PowerRanksGUI_OLD.MAIN_GUI_PAGE.MAIN, 0, data + (data.length() > 0 && special.length() > 0 ? ":" : "") + special);
					}

					if (current_menu.equalsIgnoreCase(PowerRanksGUI_OLD.MAIN_GUI_PAGE.CMD_SETCHATCOLOR_SELECT_SPECIAL.getName())) {
						String special = clickedItem.getItemMeta().getLore().get(0);
						String data = inventory.getItem(inventory.getSize() - 2).getItemMeta().getLore().get(2);
						PowerRanksGUI_OLD.openPowerRanksGUI(player, PowerRanksGUI_OLD.MAIN_GUI_PAGE.MAIN, 0, data + (data.length() > 0 && special.length() > 0 ? ":" : "") + special);
					}
					
					if (current_menu.equalsIgnoreCase(PowerRanksGUI_OLD.MAIN_GUI_PAGE.CMD_ALLOWBUILD_SELECT_RANK.getName())) {
						String rankname = clickedItem.getItemMeta().getDisplayName();
						String data = inventory.getItem(inventory.getSize() - 2).getItemMeta().getLore().get(2);
						PowerRanksGUI_OLD.openPowerRanksGUI(player, PowerRanksGUI_OLD.MAIN_GUI_PAGE.CMD_ALLOWBUILD_YESNO, 0, data + (data.length() > 0 ? ":" : "") + rankname);
					}
					
					if (current_menu.equalsIgnoreCase(PowerRanksGUI_OLD.MAIN_GUI_PAGE.CMD_ALLOWBUILD_YESNO.getName())) {
						String option = clickedItem.getItemMeta().getLore().get(0);
						String data = inventory.getItem(inventory.getSize() - 2).getItemMeta().getLore().get(2);
						PowerRanksGUI_OLD.openPowerRanksGUI(player, PowerRanksGUI_OLD.MAIN_GUI_PAGE.MAIN, 0, data + (data.length() > 0 ? ":" : "") + option);
					}
					
					if (current_menu.equalsIgnoreCase(PowerRanksGUI_OLD.MAIN_GUI_PAGE.CMD_SETDEFAULTRANK_SELECT_RANK.getName())) {
						String rankname = clickedItem.getItemMeta().getDisplayName();
						String data = inventory.getItem(inventory.getSize() - 2).getItemMeta().getLore().get(2);
						PowerRanksGUI_OLD.openPowerRanksGUI(player, PowerRanksGUI_OLD.MAIN_GUI_PAGE.MAIN, 0, data + (data.length() > 0 ? ":" : "") + rankname);
					}
					
					if (current_menu.equalsIgnoreCase(PowerRanksGUI_OLD.MAIN_GUI_PAGE.CMD_ADDINHERITANCE_SELECT_RANK.getName())) {
						String rankname = clickedItem.getItemMeta().getDisplayName();
						String data = inventory.getItem(inventory.getSize() - 2).getItemMeta().getLore().get(2);
						PowerRanksGUI_OLD.openPowerRanksGUI(player, PowerRanksGUI_OLD.MAIN_GUI_PAGE.CMD_ADDINHERITANCE_SELECT_RANK2, 0, data + (data.length() > 0 ? ":" : "") + rankname);
					}
					
					if (current_menu.equalsIgnoreCase(PowerRanksGUI_OLD.MAIN_GUI_PAGE.CMD_ADDINHERITANCE_SELECT_RANK2.getName())) {
						String rankname = clickedItem.getItemMeta().getDisplayName();
						String data = inventory.getItem(inventory.getSize() - 2).getItemMeta().getLore().get(2);
						PowerRanksGUI_OLD.openPowerRanksGUI(player, PowerRanksGUI_OLD.MAIN_GUI_PAGE.MAIN, 0, data + (data.length() > 0 ? ":" : "") + rankname);
					}
					
					if (current_menu.equalsIgnoreCase(PowerRanksGUI_OLD.MAIN_GUI_PAGE.CMD_DELINHERITANCE_SELECT_RANK.getName())) {
						String rankname = clickedItem.getItemMeta().getDisplayName();
						String data = inventory.getItem(inventory.getSize() - 2).getItemMeta().getLore().get(2);
						PowerRanksGUI_OLD.openPowerRanksGUI(player, PowerRanksGUI_OLD.MAIN_GUI_PAGE.CMD_DELINHERITANCE_SELECT_RANK2, 0, data + (data.length() > 0 ? ":" : "") + rankname);
					}
					
					if (current_menu.equalsIgnoreCase(PowerRanksGUI_OLD.MAIN_GUI_PAGE.CMD_DELINHERITANCE_SELECT_RANK2.getName())) {
						String rankname = clickedItem.getItemMeta().getDisplayName();
						String data = inventory.getItem(inventory.getSize() - 2).getItemMeta().getLore().get(2);
						PowerRanksGUI_OLD.openPowerRanksGUI(player, PowerRanksGUI_OLD.MAIN_GUI_PAGE.MAIN, 0, data + (data.length() > 0 ? ":" : "") + rankname);
					}
				} else {
					if (e.getSlot() == PowerRanksGUI_OLD.getPowerRanksGUI().getSize() - 2) {
						int current_page = Integer.parseInt(clickedItem.getItemMeta().getLore().get(1).toLowerCase().replaceAll("page", "").replaceAll(" ", "")) - 1;
						String data = inventory.getItem(inventory.getSize() - 2).getItemMeta().getLore().get(2);
						if (e.isLeftClick()) {
							PowerRanksGUI_OLD.openPowerRanksGUI(player, PowerRanksGUI_OLD.MAIN_GUI_PAGE.NONE.getFromName(current_menu), current_page + 1, data);
						}

						if (e.isRightClick()) {
							PowerRanksGUI_OLD.openPowerRanksGUI(player, PowerRanksGUI_OLD.MAIN_GUI_PAGE.NONE.getFromName(current_menu), current_page - 1, data);
						}
					}
				}

			} else if (inventory == PowerRanksGUI_OLD.getPowerRanksGUIShop()) {
				if (PowerRanks.getVaultEconomy() != null) {
					if (e.getSlot() < PowerRanksGUI_OLD.getPowerRanksGUIShop().getSize() - 9) {
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

					if (e.getSlot() == PowerRanksGUI_OLD.getPowerRanksGUIShop().getSize() - 3) {
						int current_page = Integer.parseInt(clickedItem.getItemMeta().getLore().get(0).toLowerCase().replaceAll("page", "").replaceAll(" ", "")) - 1;
						if (e.isLeftClick()) {
							PowerRanksGUI_OLD.openPowerRanksRankupGUI(player, current_page + 1);
						}

						if (e.isRightClick()) {
							PowerRanksGUI_OLD.openPowerRanksRankupGUI(player, current_page - 1);
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void onInventoryDragEvent(InventoryDragEvent e) {
		if (e.getInventory() == PowerRanksGUI_OLD.getPowerRanksGUI() || e.getInventory() == PowerRanksGUI_OLD.getPowerRanksGUIShop()) {
			e.setCancelled(true);
		}
	}
}