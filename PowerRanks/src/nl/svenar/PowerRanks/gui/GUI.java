package nl.svenar.PowerRanks.gui;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Data.Messages;
import nl.svenar.PowerRanks.Data.Users;
import nl.svenar.PowerRanks.gui.GUIPage.GUI_PAGE_ID;

public class GUI {

	private static PowerRanks powerRanks;
	public static HashMap<Player, GUIPage> guis = new HashMap<Player, GUIPage>();

	public static void setPlugin(PowerRanks powerRanks) {
		GUI.powerRanks = powerRanks;
	}

	public static void openGUI(Player player, GUI_PAGE_ID pageID) {
		if (guis.get(player) == null || guis.get(player).getPageID().getID() != pageID.getID()) {
			GUIPage new_gui = new GUIPage(powerRanks, player, pageID);
			guis.put(player, new_gui);
			player.openInventory(new_gui.getGUI());
		} else {
			guis.get(player).gui = guis.get(player).setupGUI(guis.get(player).getPageID());
			player.openInventory(guis.get(player).getGUI());
		}
	}

	public static boolean isPowerRanksGUI(Inventory inventory) {
		boolean is_powerranks_gui = false;

		for (int i = 0; i < inventory.getSize(); i++) {
			if (inventory.getItem(i) != null && inventory.getItem(i).getItemMeta() != null && inventory.getItem(i).getItemMeta().getLore() != null
					&& inventory.getItem(i).getItemMeta().getLore().get(0).toLowerCase().contains(PowerRanks.pdf.getName().toLowerCase())) {
				is_powerranks_gui = true;
				break;
			}
		}

		return is_powerranks_gui;
	}

	public static void clickedItem(Player player, int slot) {
		GUIPage gui = guis.get(player);
		if (gui == null)
			return;

		if (gui.getGUI().getItem(slot) == null || gui.getGUI().getItem(slot).getItemMeta() == null)
			return;

		if (gui.getGUI().getItem(slot).getItemMeta().getDisplayName().equalsIgnoreCase("close")) {
			closeGUI(player);

		} else if (gui.getGUI().getItem(slot).getItemMeta().getDisplayName().equalsIgnoreCase("next page")) {
			gui.current_page += 1;
			gui.gui = gui.setupGUI(gui.getPageID());
			openGUI(player, gui.getPageID());

		} else if (gui.getGUI().getItem(slot).getItemMeta().getDisplayName().equalsIgnoreCase("previous page")) {
			gui.current_page -= 1;
			gui.gui = gui.setupGUI(gui.getPageID());
			openGUI(player, gui.getPageID());
		}

		if (gui.getPageID().getID() == GUI_PAGE_ID.RANKUP.getID()) {
			if (slot < gui.getGUI().getSize() - 9) {
				Users users = new Users(powerRanks);
				String rankname = gui.getGUI().getItem(slot).getItemMeta().getDisplayName();
				int cost = users.getRanksConfigFieldInt(rankname, "economy.cost");
				double player_balance = PowerRanks.getVaultEconomy().getBalance(player);
				if (cost >= 0 && player_balance >= cost) {
					PowerRanks.getVaultEconomy().withdrawPlayer(player, cost);
					users.setGroup(player, rankname);
					Messages.messageBuyRankSuccess(player, rankname);
				} else {
					Messages.messageBuyRankError(player, rankname);
				}
				closeGUI(player);
			}
		}

		if (gui.getPageID().getID() == GUI_PAGE_ID.MAIN.getID()) {
			if (gui.getGUI().getItem(slot).getItemMeta().hasLore()) {
				if (slot < gui.getGUI().getSize() - 9) {
					List<String> itemLore = gui.getGUI().getItem(slot).getItemMeta().getLore();
					String cmdField = "";
					for (String lore : itemLore) {
						if (lore.toLowerCase().contains("cmd:")) {
							cmdField = lore.split(":")[1];
							break;
						}
					}

					if (cmdField.equalsIgnoreCase("setrank")) {
						openGUI(player, GUI_PAGE_ID.CMD_SETRANK_INPUT_PLAYER);
					}
				}
			}
		}
		
		if (gui.getPageID().getID() == GUI_PAGE_ID.CMD_SETRANK_INPUT_PLAYER.getID()) {
			if (slot < gui.getGUI().getSize() - 9) {
				String playername = gui.getGUI().getItem(slot).getItemMeta().getDisplayName();
				openGUI(player, GUI_PAGE_ID.CMD_SETRANK_INPUT_RANK);
				gui.setData(player.getName() + ":playername", playername);
			}
		}
		
		if (gui.getPageID().getID() == GUI_PAGE_ID.CMD_SETRANK_INPUT_RANK.getID()) {
			if (slot < gui.getGUI().getSize() - 9) {
				Users users = new Users(powerRanks);
				String rankname = gui.getGUI().getItem(slot).getItemMeta().getDisplayName();
				String playername = gui.getData(player.getName() + ":playername");
				users.setGroup(Bukkit.getPlayer(playername), rankname);
				closeGUI(player);
			}
		}
	}

	public static void closeGUI(Player player) {
		player.closeInventory();
		guis.remove(player);
	}
}
