package nl.svenar.powerranks.bukkit.gui;

import java.util.HashMap;

import com.google.common.collect.ImmutableMap;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import nl.svenar.powerranks.common.structure.PRPlayerRank;
import nl.svenar.powerranks.common.structure.PRRank;
import nl.svenar.powerranks.common.utils.PRUtil;
import nl.svenar.powerranks.bukkit.PowerRanks;
import nl.svenar.powerranks.bukkit.cache.CacheManager;
import nl.svenar.powerranks.bukkit.data.Users;
import nl.svenar.powerranks.bukkit.external.VaultHook;
import nl.svenar.powerranks.bukkit.gui.GUIPage.GUI_PAGE_ID;

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
			if (new_gui.getGUI() != null)
				player.openInventory(new_gui.getGUI());
		} else {
			guis.get(player).gui = guis.get(player).setupGUI(guis.get(player).getPageID());
			if (guis.get(player).getGUI() != null)
				player.openInventory(guis.get(player).getGUI());
		}
	}

	public static boolean isPowerRanksGUI(Inventory inventory) {
		boolean is_powerranks_gui = false;

		for (int i = 0; i < inventory.getSize(); i++) {
			try {
				if (inventory.getItem(i) != null && inventory.getItem(i).getItemMeta() != null
						&& inventory.getItem(i).getItemMeta().getLore() != null
						&& inventory.getItem(i).getItemMeta().getLore().get(0).toLowerCase()
								.contains(PowerRanks.pdf.getName().toLowerCase())) {
					is_powerranks_gui = true;
					break;
				}
			} catch (IndexOutOfBoundsException e) {
			}
		}

		return is_powerranks_gui;
	}

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
				if (users.rankExists(rankname)) {
					float cost = CacheManager.getRank(rankname).getBuyCost();
					double player_balance = VaultHook.getVaultEconomy().getBalance(player);
					if (cost >= 0 && player_balance >= cost) {
						VaultHook.getVaultEconomy().withdrawPlayer(player, cost);

						PRRank rank = CacheManager.getRank(users.getRankIgnoreCase(rankname));
						if (rank != null) {
							PRPlayerRank playerRank = new PRPlayerRank(rank.getName());
							CacheManager.getPlayer(player.getUniqueId().toString()).setRank(playerRank);
							player.sendMessage(PRUtil.powerFormatter(
									PowerRanks.getLanguageManager().getFormattedMessage(
											"commands.setrank.success-receiver"),
									ImmutableMap.<String, String>builder()
											.put("player", player.getName())
											.put("rank", rank.getName())
											.build(),
									'[', ']'));
						}

						// users.setGroup(player, rankname, true);
						if (PowerRanks.getConfigManager().getBool("rankup.buy_command.enabled", false)) {
							if (PowerRanks.getConfigManager().getString("rankup.buy_command.command", "")
									.length() > 0) {
								powerRanks.getServer().dispatchCommand(
										(CommandSender) powerRanks.getServer().getConsoleSender(),
										PowerRanks.getConfigManager().getString("rankup.buy_command.command", "")
												.replaceAll("%playername%", player.getName())
												.replaceAll("%rankname%", rankname));
							}
						}
						player.sendMessage(PRUtil.powerFormatter(
								PowerRanks.getLanguageManager().getFormattedMessage(
										"commands.buyrank.success-buy"),
								ImmutableMap.<String, String>builder()
										.put("player", player.getName())
										.put("rank", rankname)
										.build(),
								'[', ']'));
					} else {
						player.sendMessage(PRUtil.powerFormatter(
								PowerRanks.getLanguageManager().getFormattedMessage(
										"commands.buyrank.failed-buy-not-enough-money"),
								ImmutableMap.<String, String>builder()
										.put("player", player.getName())
										.put("rank", rankname)
										.build(),
								'[', ']'));
					}
					closeGUI(player);
				}
			}
		}
	}

	public static void closeGUI(Player player) {
		player.closeInventory();
		guis.remove(player);
	}
}