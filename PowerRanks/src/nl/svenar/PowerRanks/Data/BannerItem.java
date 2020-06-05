package nl.svenar.PowerRanks.Data;

import java.util.Arrays;
import java.util.List;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

public class BannerItem {

	public static ItemStack createEmpty(String name, String... lore) {
		ItemStack banner = new ItemStack(Material.WHITE_BANNER, 1);
		BannerMeta banner_meta = (BannerMeta) banner.getItemMeta();
		banner_meta.setDisplayName(name);
		banner_meta.setLore(Arrays.asList(lore));
		banner.setItemMeta(banner_meta);
		return banner;
	}
	
	public static ItemStack addPattern(ItemStack banner, DyeColor color, PatternType pattern) {
		BannerMeta banner_meta = (BannerMeta) banner.getItemMeta();
		
		List<Pattern> patterns = banner_meta.getPatterns();
		patterns.add(new Pattern(color, pattern));
		banner_meta.setPatterns(patterns);
		
		banner.setItemMeta(banner_meta);
		return banner;
	}
}
