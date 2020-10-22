package nl.svenar.PowerRanks.gui;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.VaultHook;
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
			if (new_gui.getGUI() != null) player.openInventory(new_gui.getGUI());
		} else {
			guis.get(player).gui = guis.get(player).setupGUI(guis.get(player).getPageID());
			if (guis.get(player).getGUI() != null) player.openInventory(guis.get(player).getGUI());
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

	@SuppressWarnings("deprecation")
	public static void clickedItem(Player player, int slot) {
		GUIPage gui = guis.get(player);
		if (gui == null || slot < 0 || slot > gui.getGUI().getSize())
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
				double player_balance = VaultHook.getVaultEconomy().getBalance(player);
				if (cost >= 0 && player_balance >= cost) {
					VaultHook.getVaultEconomy().withdrawPlayer(player, cost);
					users.setGroup(player, rankname, true);
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

					if (cmdField.equalsIgnoreCase("setrank"))
						openGUI(player, GUI_PAGE_ID.CMD_SETRANK_INPUT_PLAYER);

					if (cmdField.equalsIgnoreCase("checkrank"))
						openGUI(player, GUI_PAGE_ID.CMD_CHECKRANK_INPUT_PLAYER);
					
					if (cmdField.equalsIgnoreCase("promote"))
						openGUI(player, GUI_PAGE_ID.CMD_PROMOTE_INPUT_PLAYER);
					
					if (cmdField.equalsIgnoreCase("demote"))
						openGUI(player, GUI_PAGE_ID.CMD_DEMOTE_INPUT_PLAYER);
					
					if (cmdField.equalsIgnoreCase("setchatcolor"))
						openGUI(player, GUI_PAGE_ID.CMD_SETCHATCOLOR_INPUT_RANK);
					
					if (cmdField.equalsIgnoreCase("setnamecolor"))
						openGUI(player, GUI_PAGE_ID.CMD_SETNAMECOLOR_INPUT_RANK);
					
					if (cmdField.equalsIgnoreCase("allowbuild"))
						openGUI(player, GUI_PAGE_ID.CMD_ALLOWBUILD_INPUT_RANK);
					
					if (cmdField.equalsIgnoreCase("setdefaultrank"))
						openGUI(player, GUI_PAGE_ID.CMD_SETDEFAULTRANK_INPUT_RANK);
					
					if (cmdField.equalsIgnoreCase("addinheritance"))
						openGUI(player, GUI_PAGE_ID.CMD_ADDINHERITANCE_INPUT_RANK);
					
					if (cmdField.equalsIgnoreCase("delinheritance"))
						openGUI(player, GUI_PAGE_ID.CMD_DELINHERITANCE_INPUT_RANK);
					
					if (cmdField.equalsIgnoreCase("addbuyable"))
						openGUI(player, GUI_PAGE_ID.CMD_ADDBUYABLERANK_INPUT_RANK);
					
					if (cmdField.equalsIgnoreCase("delbuyable"))
						openGUI(player, GUI_PAGE_ID.CMD_DELBUYABLERANK_INPUT_RANK);
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
				users.setGroup(Bukkit.getPlayer(playername), rankname, true);
				closeGUI(player);
			}
		}

		if (gui.getPageID().getID() == GUI_PAGE_ID.CMD_CHECKRANK_INPUT_PLAYER.getID()) {
			if (slot < gui.getGUI().getSize() - 9) {
				String playername = gui.getGUI().getItem(slot).getItemMeta().getDisplayName();
				Users users = new Users(powerRanks);
				users.getGroup(player.getName(), playername);
				closeGUI(player);
			}
		}
		
		if (gui.getPageID().getID() == GUI_PAGE_ID.CMD_PROMOTE_INPUT_PLAYER.getID()) {
			if (slot < gui.getGUI().getSize() - 9) {
				String playername = gui.getGUI().getItem(slot).getItemMeta().getDisplayName();
				Users users = new Users(powerRanks);
				if (users.promote(playername)) {
					Messages.messageCommandPromoteSuccess(player, playername);
				} else {
					Messages.messageCommandPromoteError(player, playername);
				}
				closeGUI(player);
			}
		}
		
		if (gui.getPageID().getID() == GUI_PAGE_ID.CMD_DEMOTE_INPUT_PLAYER.getID()) {
			if (slot < gui.getGUI().getSize() - 9) {
				String playername = gui.getGUI().getItem(slot).getItemMeta().getDisplayName();
				Users users = new Users(powerRanks);
				if (users.demote(playername)) {
					Messages.messageCommandDemoteSuccess(player, playername);
				} else {
					Messages.messageCommandDemoteError(player, playername);
				}
				closeGUI(player);
			}
		}
		
		if (gui.getPageID().getID() == GUI_PAGE_ID.CMD_SETCHATCOLOR_INPUT_RANK.getID()) {
			if (slot < gui.getGUI().getSize() - 9) {
				String rankname = gui.getGUI().getItem(slot).getItemMeta().getDisplayName();
				openGUI(player, GUI_PAGE_ID.CMD_SETCHATCOLOR_INPUT_COLOR);
				gui.setData(player.getName() + ":rankname", rankname);
			}
		}
		
		if (gui.getPageID().getID() == GUI_PAGE_ID.CMD_SETCHATCOLOR_INPUT_COLOR.getID()) {
			if (slot < gui.getGUI().getSize() - 9) {
				String color = gui.getGUI().getItem(slot).getItemMeta().getLore().get(0);
				openGUI(player, GUI_PAGE_ID.CMD_SETCHATCOLOR_INPUT_SPECIAL);
				gui.setData(player.getName() + ":color", color);
			}
		}
		
		if (gui.getPageID().getID() == GUI_PAGE_ID.CMD_SETCHATCOLOR_INPUT_SPECIAL.getID()) {
			if (slot < gui.getGUI().getSize() - 9) {
				String special = gui.getGUI().getItem(slot).getItemMeta().getLore().get(0);
				String rankname = gui.getData(player.getName() + ":rankname");
				String color = gui.getData(player.getName() + ":color");
				Users users = new Users(powerRanks);
				users.setChatColor(users.getRankIgnoreCase(rankname), color + special);
				Messages.messageCommandSetChatColor(player, color + special, rankname);
				closeGUI(player);
			}
		}
		
		if (gui.getPageID().getID() == GUI_PAGE_ID.CMD_SETNAMECOLOR_INPUT_RANK.getID()) {
			if (slot < gui.getGUI().getSize() - 9) {
				String rankname = gui.getGUI().getItem(slot).getItemMeta().getDisplayName();
				openGUI(player, GUI_PAGE_ID.CMD_SETNAMECOLOR_INPUT_COLOR);
				gui.setData(player.getName() + ":rankname", rankname);
			}
		}
		
		if (gui.getPageID().getID() == GUI_PAGE_ID.CMD_SETNAMECOLOR_INPUT_COLOR.getID()) {
			if (slot < gui.getGUI().getSize() - 9) {
				String color = gui.getGUI().getItem(slot).getItemMeta().getLore().get(0);
				openGUI(player, GUI_PAGE_ID.CMD_SETNAMECOLOR_INPUT_SPECIAL);
				gui.setData(player.getName() + ":color", color);
			}
		}
		
		if (gui.getPageID().getID() == GUI_PAGE_ID.CMD_SETNAMECOLOR_INPUT_SPECIAL.getID()) {
			if (slot < gui.getGUI().getSize() - 9) {
				String special = gui.getGUI().getItem(slot).getItemMeta().getLore().get(0);
				String rankname = gui.getData(player.getName() + ":rankname");
				String color = gui.getData(player.getName() + ":color");
				Users users = new Users(powerRanks);
				users.setNameColor(users.getRankIgnoreCase(rankname), color + special);
				Messages.messageCommandSetNameColor(player, color + special, rankname);
				closeGUI(player);
			}
		}
		
		if (gui.getPageID().getID() == GUI_PAGE_ID.CMD_ALLOWBUILD_INPUT_RANK.getID()) {
			if (slot < gui.getGUI().getSize() - 9) {
				String rankname = gui.getGUI().getItem(slot).getItemMeta().getDisplayName();
				openGUI(player, GUI_PAGE_ID.CMD_ALLOWBUILD_INPUT_BOOLEAN);
				gui.setData(player.getName() + ":rankname", rankname);
			}
		}
		
		if (gui.getPageID().getID() == GUI_PAGE_ID.CMD_ALLOWBUILD_INPUT_BOOLEAN.getID()) {
			if (slot < gui.getGUI().getSize() - 9) {
				boolean allow = gui.getGUI().getItem(slot).getItemMeta().getLore().get(0).equalsIgnoreCase("true");
				String rankname = gui.getData(player.getName() + ":rankname");
				Users users = new Users(powerRanks);
				users.setBuild(rankname, allow);
				if (allow)
					Messages.messageCommandBuildEnabled(player, rankname);
				else
					Messages.messageCommandBuildDisabled(player, rankname);
				closeGUI(player);
			}
		}
		
		if (gui.getPageID().getID() == GUI_PAGE_ID.CMD_SETDEFAULTRANK_INPUT_RANK.getID()) {
			if (slot < gui.getGUI().getSize() - 9) {
				String rankname = gui.getGUI().getItem(slot).getItemMeta().getDisplayName();
				Users users = new Users(powerRanks);
				users.setDefaultRank(rankname);
				Messages.messageCommandSetDefaultRankSuccess(player, rankname);
				closeGUI(player);
			}
		}
		
		if (gui.getPageID().getID() == GUI_PAGE_ID.CMD_ADDINHERITANCE_INPUT_RANK.getID()) {
			if (slot < gui.getGUI().getSize() - 9) {
				String rankname = gui.getGUI().getItem(slot).getItemMeta().getDisplayName();
				gui.setData(player.getName() + ":rankname", rankname);
				openGUI(player, GUI_PAGE_ID.CMD_ADDINHERITANCE_INPUT_RANK2);
			}
		}
		
		if (gui.getPageID().getID() == GUI_PAGE_ID.CMD_ADDINHERITANCE_INPUT_RANK2.getID()) {
			if (slot < gui.getGUI().getSize() - 9) {
				String inheritance = gui.getGUI().getItem(slot).getItemMeta().getDisplayName();
				String rankname = gui.getData(player.getName() + ":rankname");
				Users users = new Users(powerRanks);
				users.addInheritance(rankname, inheritance);
				Messages.messageCommandInheritanceAdded(player, inheritance, rankname);
				closeGUI(player);
			}
		}
		
		if (gui.getPageID().getID() == GUI_PAGE_ID.CMD_DELINHERITANCE_INPUT_RANK.getID()) {
			if (slot < gui.getGUI().getSize() - 9) {
				String rankname = gui.getGUI().getItem(slot).getItemMeta().getDisplayName();
				gui.setData(player.getName() + ":rankname", rankname);
				openGUI(player, GUI_PAGE_ID.CMD_DELINHERITANCE_INPUT_RANK2);
			}
		}
		
		if (gui.getPageID().getID() == GUI_PAGE_ID.CMD_DELINHERITANCE_INPUT_RANK2.getID()) {
			if (slot < gui.getGUI().getSize() - 9) {
				String inheritance = gui.getGUI().getItem(slot).getItemMeta().getDisplayName();
				String rankname = gui.getData(player.getName() + ":rankname");
				Users users = new Users(powerRanks);
				users.removeInheritance(rankname, inheritance);
				Messages.messageCommandInheritanceRemoved(player, inheritance, rankname);
				closeGUI(player);
			}
		}
		
		if (gui.getPageID().getID() == GUI_PAGE_ID.CMD_ADDBUYABLERANK_INPUT_RANK.getID()) {
			if (slot < gui.getGUI().getSize() - 9) {
				String rankname = gui.getGUI().getItem(slot).getItemMeta().getDisplayName();
				gui.setData(player.getName() + ":rankname", rankname);
				openGUI(player, GUI_PAGE_ID.CMD_ADDBUYABLERANK_INPUT_RANK2);
			}
		}
		
		if (gui.getPageID().getID() == GUI_PAGE_ID.CMD_ADDBUYABLERANK_INPUT_RANK2.getID()) {
			if (slot < gui.getGUI().getSize() - 9) {
				String buyablerank = gui.getGUI().getItem(slot).getItemMeta().getDisplayName();
				String rankname = gui.getData(player.getName() + ":rankname");
				Users users = new Users(powerRanks);
				users.addBuyableRank(rankname, buyablerank);
				Messages.messageCommandAddbuyablerankSuccess(player, rankname, buyablerank);
				closeGUI(player);
			}
		}
		
		if (gui.getPageID().getID() == GUI_PAGE_ID.CMD_DELBUYABLERANK_INPUT_RANK.getID()) {
			if (slot < gui.getGUI().getSize() - 9) {
				String rankname = gui.getGUI().getItem(slot).getItemMeta().getDisplayName();
				gui.setData(player.getName() + ":rankname", rankname);
				openGUI(player, GUI_PAGE_ID.CMD_DELBUYABLERANK_INPUT_RANK2);
			}
		}
		
		if (gui.getPageID().getID() == GUI_PAGE_ID.CMD_DELBUYABLERANK_INPUT_RANK2.getID()) {
			if (slot < gui.getGUI().getSize() - 9) {
				String buyablerank = gui.getGUI().getItem(slot).getItemMeta().getDisplayName();
				String rankname = gui.getData(player.getName() + ":rankname");
				Users users = new Users(powerRanks);
				users.delBuyableRank(rankname, buyablerank);
				Messages.messageCommandDelbuyablerankSuccess(player, rankname, buyablerank);
				closeGUI(player);
			}
		}
	}

	public static void closeGUI(Player player) {
		player.closeInventory();
		guis.remove(player);
	}
}