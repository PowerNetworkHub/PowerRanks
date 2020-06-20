package nl.svenar.PowerRanks.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Util;
import nl.svenar.PowerRanks.Data.BannerItem;
import nl.svenar.PowerRanks.Data.Messages;
import nl.svenar.PowerRanks.Data.Users;
import nl.svenar.PowerRanks.gui.GUIPage.GUID_PAGE_ID;

public class GUI {

	private static PowerRanks powerRanks;
	public static HashMap<Player, GUIPage> guis = new HashMap<Player, GUIPage>();

	public static void setPlugin(PowerRanks powerRanks) {
		GUI.powerRanks = powerRanks;
	}

	public static void openGUI(Player player, GUID_PAGE_ID pageID) {
		if (guis.get(player) != null && guis.get(player).getPageID().getID() != pageID.getID()) {
			GUIPage new_gui = new GUIPage(player, pageID);
		}
	}
}
