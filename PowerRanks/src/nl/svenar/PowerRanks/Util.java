package nl.svenar.PowerRanks;

import java.util.regex.Pattern;

import org.bukkit.Bukkit;

public class Util {

	public String getCraftBukkitClassName(String simpleName) {
		String version = Bukkit.getServer().getClass().getName().substring("org.bukkit.craftbukkit".length());
		version = version.substring(0, version.length() - "CraftServer".length());
		return "org.bukkit.craftbukkit" + version + simpleName;
	}
	
	public static String replaceAll(String source, final String key, final String value) {
        final String[] split = source.split(Pattern.quote(key));
        final StringBuilder builder = new StringBuilder();
        builder.append(split[0]);
        for (int i = 1; i < split.length; ++i) {
            builder.append(value);
            builder.append(split[i]);
        }
        while (source.endsWith(key)) {
            builder.append(value);
            source = source.substring(0, source.length() - key.length());
        }
        return builder.toString();
    }
}
