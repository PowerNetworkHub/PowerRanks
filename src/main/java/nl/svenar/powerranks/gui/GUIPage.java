package nl.svenar.powerranks.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import nl.svenar.powerranks.PowerRanks;
import nl.svenar.powerranks.cache.CacheManager;
import nl.svenar.powerranks.data.BannerItem;
import nl.svenar.powerranks.data.Users;
import nl.svenar.powerranks.external.VaultHook;

public class GUIPage {

	Inventory gui = null;
	private Player player;
	private Users users;
	private GUI_PAGE_ID pageID = null;
	public int current_page = 0;
	private static HashMap<String, String> storedData = new HashMap<String, String>();

	public static enum GUI_PAGE_ID {
		RANKUP(0, "rankup"), MAIN(1, "main"), CMD_SETRANK_INPUT_PLAYER(2, "select player"),
		CMD_SETRANK_INPUT_RANK(3, "select rank"), CMD_CHECKRANK_INPUT_PLAYER(4, "select player"),
		CMD_PROMOTE_INPUT_PLAYER(5, "select player"), CMD_DEMOTE_INPUT_PLAYER(6, "select player"),

		CMD_SETCHATCOLOR_INPUT_RANK(7, "select rank"), CMD_SETCHATCOLOR_INPUT_COLOR(8, "select color"),
		CMD_SETCHATCOLOR_INPUT_SPECIAL(9, "select color modifier"), CMD_SETNAMECOLOR_INPUT_RANK(10, "select rank"),
		CMD_SETNAMECOLOR_INPUT_COLOR(11, "select color"), CMD_SETNAMECOLOR_INPUT_SPECIAL(12, "select color modifier"),
		CMD_SETDEFAULTRANK_INPUT_RANK(15, "select rank"), CMD_ADDINHERITANCE_INPUT_RANK(16, "select rank"),
		CMD_ADDINHERITANCE_INPUT_RANK2(17, "select inheritance"), CMD_DELINHERITANCE_INPUT_RANK(18, "select rank"),
		CMD_DELINHERITANCE_INPUT_RANK2(19, "select inheritance"), CMD_ADDBUYABLERANK_INPUT_RANK(20, "select rank"),
		CMD_ADDBUYABLERANK_INPUT_RANK2(21, "select buyable rank"), CMD_DELBUYABLERANK_INPUT_RANK(22, "select rank"),
		CMD_DELBUYABLERANK_INPUT_RANK2(23, "select buyable rank");

		public final int id;
		public final String name;

		private GUI_PAGE_ID(int id, String name) {
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

	public GUIPage(PowerRanks powerRanks, Player player, GUI_PAGE_ID pageID) {
		this.player = player;
		this.users = new Users(powerRanks);
		this.pageID = pageID;
		this.gui = setupGUI(pageID);
	}

	Inventory setupGUI(GUI_PAGE_ID pageID) {
		if (current_page < 0) {
			current_page = 0;
		}

		Inventory new_gui = Bukkit.createInventory(null, INVENTORY_SIZE.NORMAL.getSize(),
				"PowerRanks " + pageID.getName());

		for (int i = 0; i < new_gui.getSize(); i++) {
			try {
				new_gui.setItem(i, createGuiItem(Material.RED_STAINED_GLASS_PANE, " "));
			} catch (NoSuchFieldError e) {
				new_gui.setItem(i, createEmptyGuiItem());
			}
		}

		new_gui.setItem(new_gui.getSize() - 5,
				createGuiItem(Material.BARRIER, "Close", PowerRanks.pdf.getName().toLowerCase()));

		if (pageID.getID() == GUI_PAGE_ID.RANKUP.getID()) {
			List<String> ranks = users.getBuyableRanks(users.getPrimaryRank(player));
			int num_rank_on_page = new_gui.getSize() - 9;

			while (num_rank_on_page * current_page > ranks.size()) {
				current_page -= 1;
			}

			try {
				new_gui.setItem(new_gui.getSize() - 6, createGuiItem(Material.PAPER, "Previous page",
						PowerRanks.pdf.getName().toLowerCase(), "Page " + current_page));
				new_gui.setItem(new_gui.getSize() - 4, createGuiItem(Material.PAPER, "Next page",
						PowerRanks.pdf.getName().toLowerCase(), "Page " + current_page));
				new_gui.setItem(new_gui.getSize() - 9, createGuiItem(Material.NOTE_BLOCK, "Current rank",
						users.getPrimaryRank(player),
						ChatColor.RESET + PowerRanks.chatColor(users.getPrefix(player), true)));
				new_gui.setItem(new_gui.getSize() - 1, createGuiItem(Material.EMERALD, "Balance",
						VaultHook.getVaultEconomy().format(VaultHook.getVaultEconomy().getBalance(player))));
			} catch (Exception e) {
				player.sendMessage(
						PowerRanks.getLanguageManager().getFormattedMessage(
								"commands.buyrank.buy-not-available"));
				return null;
			}

			for (int i = 0; i < num_rank_on_page; i++) {
				if (num_rank_on_page * current_page + i < ranks.size()) {
					String rank = (String) ranks.get(num_rank_on_page * current_page + i);
					if (!rank.equalsIgnoreCase(users.getPrimaryRank(player))) {
						try {
							Material icon = Material.BARRIER;

							float cost = CacheManager.getRank(rank).getBuyCost();
							final String description = CacheManager.getRank(rank).getBuyDescription();
							if (icon != null) {
								new_gui.setItem(i, createGuiItem(icon, rank,
										ChatColor.WHITE + PowerRanks.chatColorAlt(
												CacheManager.getRank(rank).getPrefix().replaceAll("&r", "&r&f"), true),
										"", ChatColor.WHITE + "Description:", PowerRanks.chatColor(description, true),
										"", ChatColor.WHITE + "Cost: ", ChatColor.GREEN + String.valueOf(cost)));
							// } else {
							// 	PowerRanks.log.warning("Rank '" + rank + "' has a invalid icon!");
							}

						} catch (Exception e) {
							PowerRanks.log.warning("[RANKUP] Rank '" + rank + "' not found!");
						}
					}
				}
			}
		}

		if (pageID.getID() == GUI_PAGE_ID.MAIN.getID()) {
			new_gui.setItem(0, createGuiItem(Material.DISPENSER, ChatColor.GREEN + "Set a players rank",
					ChatColor.BLACK + "cmd:setrank", ChatColor.RESET + "/pr set <player> <rank>"));
			new_gui.setItem(1, createGuiItem(Material.REDSTONE, ChatColor.GREEN + "Check a players rank",
					ChatColor.BLACK + "cmd:checkrank", ChatColor.RESET + "/pr check <player>"));
			new_gui.setItem(2, createGuiItem(Material.EMERALD, ChatColor.GREEN + "Promote a player",
					ChatColor.BLACK + "cmd:promote", ChatColor.RESET + "/pr promote <player>"));
			new_gui.setItem(3, createGuiItem(Material.BEDROCK, ChatColor.GREEN + "Demote a player",
					ChatColor.BLACK + "cmd:demote", ChatColor.RESET + "/pr demote <player>"));
			new_gui.setItem(4, createGuiItem(Material.DIAMOND, ChatColor.GREEN + "Change the chat color of a rank",
					ChatColor.BLACK + "cmd:setchatcolor", ChatColor.RESET + "/pr setchatcolor <rank> <color>"));
			new_gui.setItem(5, createGuiItem(Material.GOLD_INGOT, ChatColor.GREEN + "Change the name color of a rank",
					ChatColor.BLACK + "cmd:setnamecolor", ChatColor.RESET + "/pr setnamecolor <rank> <color>"));
			new_gui.setItem(7, createGuiItem(Material.STONE, ChatColor.GREEN + "Set the default rank for new player",
					ChatColor.BLACK + "cmd:setdefaultrank", ChatColor.RESET + "/pr setdefaultrank <rank>"));

			new_gui.setItem(8,
					createGuiItem(Material.GOLD_BLOCK, ChatColor.GREEN + "Add a inheritance to a rank",
							ChatColor.BLACK + "cmd:addinheritance",
							ChatColor.RESET + "/pr delinheritance <rank> <inheritance>"));
			new_gui.setItem(9,
					createGuiItem(Material.IRON_BLOCK, ChatColor.GREEN + "Remove a inheritance from a rank",
							ChatColor.BLACK + "cmd:delinheritance",
							ChatColor.RESET + "/pr delinheritance <rank> <inheritance>"));
			new_gui.setItem(10, createGuiItem(Material.FURNACE, ChatColor.GREEN + "Add a buyable rank to a rank",
					ChatColor.BLACK + "cmd:addbuyable", ChatColor.RESET + "/pr addbuyablerank <rank> <buyable_rank>"));
			new_gui.setItem(11, createGuiItem(Material.OBSIDIAN, ChatColor.GREEN + "Remove a buyable rank from a rank",
					ChatColor.BLACK + "cmd:delbuyable", ChatColor.RESET + "/pr delbuyablerank <rank> <buyable_rank>"));

			// inventoryGUIMain.addItem(createGuiItem(Material.STONE, "/pr setdefaultrank",
			// "Set the default rank for new players."));
			// inventoryGUIMain.addItem(createGuiItem(Material.YELLOW_FLOWER, "/pr
			// addinheritance", "Add a inheritance to a rank."));
			// inventoryGUIMain.addItem(createGuiItem(Material.RED_ROSE, "/pr
			// delinheritance", "Remove a inheritance from a rank."));
		}

		if (pageID.getID() == GUI_PAGE_ID.CMD_SETRANK_INPUT_PLAYER.getID()
				|| pageID.getID() == GUI_PAGE_ID.CMD_CHECKRANK_INPUT_PLAYER.getID()
				|| pageID.getID() == GUI_PAGE_ID.CMD_PROMOTE_INPUT_PLAYER.getID()
				|| pageID.getID() == GUI_PAGE_ID.CMD_DEMOTE_INPUT_PLAYER.getID()) {
			Object[] online_players = Bukkit.getServer().getOnlinePlayers().toArray();
			int num_items_on_page = new_gui.getSize() - 9;

			while (num_items_on_page * current_page > online_players.length) {
				current_page -= 1;
			}

			new_gui.setItem(new_gui.getSize() - 6, createGuiItem(Material.PAPER, "Previous page",
					PowerRanks.pdf.getName().toLowerCase(), "Page " + current_page));
			new_gui.setItem(new_gui.getSize() - 4, createGuiItem(Material.PAPER, "Next page",
					PowerRanks.pdf.getName().toLowerCase(), "Page " + current_page));

			for (int i = 0; i < num_items_on_page; i++) {
				if (num_items_on_page * current_page + i < online_players.length) {
					Player online_player = (Player) online_players[num_items_on_page * current_page + i];
					String online_player_current_rank = users.getPrimaryRank(online_player);
					new_gui.setItem(i, createGuiHead(online_player, ChatColor.RESET + online_player_current_rank,
							ChatColor.RESET + PowerRanks.chatColor(users.getPrefix(player), true)));
				}
			}
		}

		if (pageID.getID() == GUI_PAGE_ID.CMD_SETRANK_INPUT_RANK.getID()
				|| pageID.getID() == GUI_PAGE_ID.CMD_SETCHATCOLOR_INPUT_RANK.getID()
				|| pageID.getID() == GUI_PAGE_ID.CMD_SETNAMECOLOR_INPUT_RANK.getID()
				|| pageID.getID() == GUI_PAGE_ID.CMD_SETDEFAULTRANK_INPUT_RANK.getID()
				|| pageID.getID() == GUI_PAGE_ID.CMD_ADDINHERITANCE_INPUT_RANK.getID()
				|| pageID.getID() == GUI_PAGE_ID.CMD_DELINHERITANCE_INPUT_RANK.getID()
				|| pageID.getID() == GUI_PAGE_ID.CMD_ADDBUYABLERANK_INPUT_RANK.getID()
				|| pageID.getID() == GUI_PAGE_ID.CMD_DELBUYABLERANK_INPUT_RANK.getID()) {
			Object[] ranks = users.getGroups().toArray();
			int num_rank_on_page = new_gui.getSize() - 9;

			while (num_rank_on_page * current_page > ranks.length) {
				current_page -= 1;
			}

			new_gui.setItem(new_gui.getSize() - 6, createGuiItem(Material.PAPER, "Previous page",
					PowerRanks.pdf.getName().toLowerCase(), "Page " + current_page));
			new_gui.setItem(new_gui.getSize() - 4, createGuiItem(Material.PAPER, "Next page",
					PowerRanks.pdf.getName().toLowerCase(), "Page " + current_page));

			for (int i = 0; i < num_rank_on_page; i++) {
				if (num_rank_on_page * current_page + i < ranks.length) {
					String rank = (String) ranks[num_rank_on_page * current_page + i];
					Material icon = Material.BARRIER;

					if (icon != null) {
						new_gui.setItem(i, createGuiItem(icon, rank,
								ChatColor.RESET + PowerRanks.chatColor(CacheManager.getRank(rank).getPrefix(), true)));
					// } else {
					// 	PowerRanks.log.warning("Rank '" + rank + "' has a invalid icon!");
					}

				}
			}
		}

		if (pageID.getID() == GUI_PAGE_ID.CMD_ADDINHERITANCE_INPUT_RANK2.getID()
				|| pageID.getID() == GUI_PAGE_ID.CMD_ADDBUYABLERANK_INPUT_RANK2.getID()) {
			Object[] ranks = users.getGroups().toArray();
			int num_rank_on_page = new_gui.getSize() - 9;

			while (num_rank_on_page * current_page > ranks.length) {
				current_page -= 1;
			}

			new_gui.setItem(new_gui.getSize() - 6, createGuiItem(Material.PAPER, "Previous page",
					PowerRanks.pdf.getName().toLowerCase(), "Page " + current_page));
			new_gui.setItem(new_gui.getSize() - 4, createGuiItem(Material.PAPER, "Next page",
					PowerRanks.pdf.getName().toLowerCase(), "Page " + current_page));
			for (int i = 0; i < num_rank_on_page; i++) {
				if (num_rank_on_page * current_page + i < ranks.length) {
					String rank = (String) ranks[num_rank_on_page * current_page + i];
					if (!rank.equalsIgnoreCase(getData(player.getName() + ":rankname"))) {
						Material icon = Material.BARRIER;

						if (icon != null) {
							new_gui.setItem(i, createGuiItem(icon, rank,
									ChatColor.RESET
											+ PowerRanks.chatColor(CacheManager.getRank(rank).getPrefix(), true)));
						// } else {
						// 	PowerRanks.log.warning("Rank '" + rank + "' has a invallid icon!");
						}

					}
				}
			}
		}

		if (pageID.getID() == GUI_PAGE_ID.CMD_DELINHERITANCE_INPUT_RANK2.getID()) {
			Object[] ranks = users.getInheritances(getData(player.getName() + ":rankname")).toArray();
			int num_rank_on_page = new_gui.getSize() - 9;

			while (num_rank_on_page * current_page > ranks.length) {
				current_page -= 1;
			}

			new_gui.setItem(new_gui.getSize() - 6, createGuiItem(Material.PAPER, "Previous page",
					PowerRanks.pdf.getName().toLowerCase(), "Page " + current_page));
			new_gui.setItem(new_gui.getSize() - 4, createGuiItem(Material.PAPER, "Next page",
					PowerRanks.pdf.getName().toLowerCase(), "Page " + current_page));
			for (int i = 0; i < num_rank_on_page; i++) {
				if (num_rank_on_page * current_page + i < ranks.length) {
					String rank = (String) ranks[num_rank_on_page * current_page + i];
					Material icon = Material.BARRIER;

					if (icon != null) {
						new_gui.setItem(i, createGuiItem(icon, rank,
								ChatColor.RESET + PowerRanks.chatColor(CacheManager.getRank(rank).getPrefix(), true)));
					// } else {
					// 	PowerRanks.log.warning("Rank '" + rank + "' has a invallid icon!");
					}
				}
			}
		}

		if (pageID.getID() == GUI_PAGE_ID.CMD_DELBUYABLERANK_INPUT_RANK2.getID()) {
			Object[] ranks = users.getBuyableRanks(getData(player.getName() + ":rankname")).toArray();
			int num_rank_on_page = new_gui.getSize() - 9;

			while (num_rank_on_page * current_page > ranks.length) {
				current_page -= 1;
			}

			new_gui.setItem(new_gui.getSize() - 6, createGuiItem(Material.PAPER, "Previous page",
					PowerRanks.pdf.getName().toLowerCase(), "Page " + current_page));
			new_gui.setItem(new_gui.getSize() - 4, createGuiItem(Material.PAPER, "Next page",
					PowerRanks.pdf.getName().toLowerCase(), "Page " + current_page));
			for (int i = 0; i < num_rank_on_page; i++) {
				if (num_rank_on_page * current_page + i < ranks.length) {
					String rank = (String) ranks[num_rank_on_page * current_page + i];
					Material icon = Material.BARRIER;

					if (icon != null) {
						new_gui.setItem(i, createGuiItem(icon, rank,
								ChatColor.RESET + PowerRanks.chatColor(CacheManager.getRank(rank).getPrefix(), true)));
					// } else {
					// 	PowerRanks.log.warning("Rank '" + rank + "' has a invallid icon!");
					}

				}
			}
		}

		if (pageID.getID() == GUI_PAGE_ID.CMD_SETCHATCOLOR_INPUT_COLOR.getID()
				|| pageID.getID() == GUI_PAGE_ID.CMD_SETNAMECOLOR_INPUT_COLOR.getID()) {
			try {
				new_gui.setItem(0,
						BannerItem.addPattern(BannerItem.createEmpty("Black", "&0"), DyeColor.BLACK, PatternType.BASE));
				new_gui.setItem(1, BannerItem.addPattern(BannerItem.createEmpty("Dark Blue", "&1"), DyeColor.BLUE,
						PatternType.BASE));
				new_gui.setItem(2, BannerItem.addPattern(BannerItem.createEmpty("Dark Green", "&2"), DyeColor.GREEN,
						PatternType.BASE));
				new_gui.setItem(3,
						BannerItem.addPattern(BannerItem.addPattern(BannerItem.createEmpty("Dark Turquoise", "&3"),
								DyeColor.CYAN, PatternType.BASE), DyeColor.BLACK, PatternType.GRADIENT));
				new_gui.setItem(4, BannerItem.addPattern(
						BannerItem.addPattern(BannerItem.createEmpty("Dark Red", "&4"), DyeColor.RED, PatternType.BASE),
						DyeColor.BLACK, PatternType.GRADIENT));
				new_gui.setItem(5, BannerItem.addPattern(BannerItem.createEmpty("Purple", "&5"), DyeColor.PURPLE,
						PatternType.BASE));
				new_gui.setItem(6, BannerItem.addPattern(BannerItem.createEmpty("Orange", "&6"), DyeColor.ORANGE,
						PatternType.BASE));
				new_gui.setItem(7, BannerItem.addPattern(BannerItem.createEmpty("Light Gray", "&7"),
						DyeColor.LIGHT_GRAY, PatternType.BASE));
				new_gui.setItem(8, BannerItem.addPattern(BannerItem.createEmpty("Dark Gray", "&8"), DyeColor.GRAY,
						PatternType.BASE));
				new_gui.setItem(9,
						BannerItem.addPattern(BannerItem.addPattern(BannerItem.createEmpty("Light Blue", "&9"),
								DyeColor.LIGHT_BLUE, PatternType.BASE), DyeColor.BLUE, PatternType.GRADIENT));
				new_gui.setItem(10, BannerItem.addPattern(BannerItem.createEmpty("Light Green", "&a"), DyeColor.LIME,
						PatternType.BASE));
				new_gui.setItem(11, BannerItem.addPattern(BannerItem.createEmpty("Light Turquoise", "&b"),
						DyeColor.LIGHT_BLUE, PatternType.BASE));
				new_gui.setItem(12,
						BannerItem.addPattern(BannerItem.createEmpty("Red", "&c"), DyeColor.RED, PatternType.BASE));
				new_gui.setItem(13, BannerItem.addPattern(BannerItem.createEmpty("Magenta", "&d"), DyeColor.MAGENTA,
						PatternType.BASE));
				new_gui.setItem(14, BannerItem.addPattern(BannerItem.createEmpty("Yellow", "&e"), DyeColor.YELLOW,
						PatternType.BASE));
				new_gui.setItem(15,
						BannerItem.addPattern(BannerItem.createEmpty("White", "&f"), DyeColor.WHITE, PatternType.BASE));
			} catch (NoSuchFieldError e) {
				new_gui.setItem(0,
						createGuiItem(Material.STICK, "Black", "&0", ChatColor.RED + "Cannot show color icon"));
				new_gui.setItem(1,
						createGuiItem(Material.STICK, "Dark Blue", "&1", ChatColor.RED + "Cannot show color icon"));
				new_gui.setItem(2,
						createGuiItem(Material.STICK, "Dark Green", "&2", ChatColor.RED + "Cannot show color icon"));
				new_gui.setItem(3, createGuiItem(Material.STICK, "Dark Turquoise", "&3",
						ChatColor.RED + "Cannot show color icon"));
				new_gui.setItem(4,
						createGuiItem(Material.STICK, "Dark Red", "&4", ChatColor.RED + "Cannot show color icon"));
				new_gui.setItem(5,
						createGuiItem(Material.STICK, "Purple", "&5", ChatColor.RED + "Cannot show color icon"));
				new_gui.setItem(6,
						createGuiItem(Material.STICK, "Orange", "&6", ChatColor.RED + "Cannot show color icon"));
				new_gui.setItem(7,
						createGuiItem(Material.STICK, "Light Gray", "&7", ChatColor.RED + "Cannot show color icon"));
				new_gui.setItem(8,
						createGuiItem(Material.STICK, "Dark Gray", "&8", ChatColor.RED + "Cannot show color icon"));
				new_gui.setItem(9,
						createGuiItem(Material.STICK, "Light Blue", "&9", ChatColor.RED + "Cannot show color icon"));
				new_gui.setItem(10,
						createGuiItem(Material.STICK, "Light Green", "&a", ChatColor.RED + "Cannot show color icon"));
				new_gui.setItem(11, createGuiItem(Material.STICK, "Light Turquoise", "&b",
						ChatColor.RED + "Cannot show color icon"));
				new_gui.setItem(12,
						createGuiItem(Material.STICK, "Red", "&c", ChatColor.RED + "Cannot show color icon"));
				new_gui.setItem(13,
						createGuiItem(Material.STICK, "Magenta", "&d", ChatColor.RED + "Cannot show color icon"));
				new_gui.setItem(14,
						createGuiItem(Material.STICK, "Yellow", "&e", ChatColor.RED + "Cannot show color icon"));
				new_gui.setItem(15,
						createGuiItem(Material.STICK, "White", "&f", ChatColor.RED + "Cannot show color icon"));
			}
		}

		if (pageID.getID() == GUI_PAGE_ID.CMD_SETCHATCOLOR_INPUT_SPECIAL.getID()
				|| pageID.getID() == GUI_PAGE_ID.CMD_SETNAMECOLOR_INPUT_SPECIAL.getID()) {
			try {
				new_gui.setItem(0,
						BannerItem.addPattern(BannerItem.createEmpty("None", ""), DyeColor.WHITE, PatternType.BASE));
				new_gui.setItem(1, BannerItem.addPattern(
						BannerItem.addPattern(BannerItem.createEmpty("Bold", "&l"), DyeColor.WHITE, PatternType.BASE),
						DyeColor.BLACK, PatternType.BORDER));
				new_gui.setItem(2,
						BannerItem
								.addPattern(
										BannerItem
												.addPattern(
														BannerItem
																.addPattern(
																		BannerItem.addPattern(
																				BannerItem.createEmpty("Underline",
																						"&n"),
																				DyeColor.WHITE, PatternType.BASE),
																		DyeColor.BLACK,
																		PatternType.HALF_HORIZONTAL_MIRROR),
														DyeColor.WHITE, PatternType.STRIPE_BOTTOM),
										DyeColor.WHITE, PatternType.STRIPE_MIDDLE));
				new_gui.setItem(3, BannerItem.addPattern(BannerItem.addPattern(BannerItem.createEmpty("Itallic", "&o"),
						DyeColor.WHITE, PatternType.BASE), DyeColor.BLACK, PatternType.STRIPE_DOWNLEFT));
				new_gui.setItem(4,
						BannerItem
								.addPattern(
										BannerItem
												.addPattern(
														BannerItem
																.addPattern(
																		BannerItem.addPattern(
																				BannerItem.addPattern(
																						BannerItem.createEmpty("Magic",
																								"&k"),
																						DyeColor.WHITE,
																						PatternType.BASE),
																				DyeColor.BLACK,
																				PatternType.STRIPE_DOWNLEFT),
																		DyeColor.BLACK, PatternType.TRIANGLE_BOTTOM),
														DyeColor.BLACK, PatternType.TRIANGLE_TOP),
										DyeColor.BLACK, PatternType.CIRCLE_MIDDLE));
				new_gui.setItem(5, BannerItem.addPattern(
						BannerItem.addPattern(BannerItem.createEmpty("Strike", "&m"), DyeColor.WHITE, PatternType.BASE),
						DyeColor.BLACK, PatternType.STRIPE_MIDDLE));
			} catch (NoSuchFieldError e) {
				new_gui.setItem(0, createGuiItem(Material.STICK, "None", "", ChatColor.RED + "Cannot show color icon"));
				new_gui.setItem(1,
						createGuiItem(Material.STICK, "Bold", "&l", ChatColor.RED + "Cannot show color icon"));
				new_gui.setItem(2,
						createGuiItem(Material.STICK, "Underline", "&n", ChatColor.RED + "Cannot show color icon"));
				new_gui.setItem(3,
						createGuiItem(Material.STICK, "Itallic", "&o", ChatColor.RED + "Cannot show color icon"));
				new_gui.setItem(4,
						createGuiItem(Material.STICK, "Magic", "&k", ChatColor.RED + "Cannot show color icon"));
				new_gui.setItem(5,
						createGuiItem(Material.STICK, "Strike", "&m", ChatColor.RED + "Cannot show color icon"));
			}
		}

		return new_gui;
	}

	public Inventory getGUI() {
		return gui;
	}

	public GUI_PAGE_ID getPageID() {
		return pageID;
	}

	public static ItemStack createGuiItem(final Material material, final String name, final String... lore) {
		final ItemStack item = new ItemStack(material, 1);
		final ItemMeta meta = item.getItemMeta();

		meta.setDisplayName(name);
		meta.setLore(Arrays.asList(lore));
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack createEmptyGuiItem() {
		final ItemStack item = new ItemStack(Material.AIR, 1);
		return item;
	}

	public static ItemStack createGuiHead(Player player, final String... lore) {
		ItemStack head = null;
		try {
			head = new ItemStack(Material.PLAYER_HEAD);
			SkullMeta headm = (SkullMeta) head.getItemMeta();
			headm.setDisplayName(player.getName());
			headm.setOwningPlayer(player);
			headm.setLore(Arrays.asList(lore));
			head.setItemMeta(headm);

		} catch (NoSuchFieldError e) { // TODO Fix skull head for 1.8
			List<String> loreLines = new ArrayList<String>();
			loreLines.addAll(Arrays.asList(lore));
			loreLines.add(ChatColor.RED + "Cannot show player head");
			head = new ItemStack(Material.STICK);
			ItemMeta headm = head.getItemMeta();
			headm.setDisplayName(player.getName());
			headm.setLore(loreLines);
			head.setItemMeta(headm);
		}

		return head;
	}

	public void setData(String key, String value) {
		storedData.put(key, value);
	}

	public String getData(String key) {
		return storedData.get(key);
	}
}