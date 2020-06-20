package nl.svenar.PowerRanks.gui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class GUIPage {
	
	private Player player = null;
	private Inventory gui = null;
	private GUID_PAGE_ID pageID = null;
	
	public static enum GUID_PAGE_ID {
		RANKUP(0, "rankup"), MAIN(1, "main");

		public final int id;
		public final String name;

		private GUID_PAGE_ID(int id, String name) {
			this.id = id;
			this.name = name;
		}

		int getID() {
			return id;
		}
		
		String getName() {
			return name;
		}
	}
	
	public static enum INVENTORY_SIZE {
		SMALL(9), NORMAL(27), BIG(54);

		public final int size;

		private INVENTORY_SIZE(int size) {
			this.size = size;
		}

		int getSize() {
			return size;
		}
	}
	
	public GUIPage(Player player, GUID_PAGE_ID pageID) {
		this.player = player;
		this.pageID = pageID;
		this.gui = setupGUI(pageID);
	}
	
	private Inventory setupGUI(GUID_PAGE_ID pageID) {
		return null;
	}

	public Inventory getGUI() {
		return gui;
	}

	public GUID_PAGE_ID getPageID() {
		return null;
	}

}
