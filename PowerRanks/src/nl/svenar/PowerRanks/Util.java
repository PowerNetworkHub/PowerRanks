package nl.svenar.PowerRanks;

import java.util.regex.Pattern;

import org.bukkit.Bukkit;

public class Util {

	public String getCraftBukkitClassName(String simpleName) {
		String version = Bukkit.getServer().getClass().getName().substring("org.bukkit.craftbukkit".length());
		version = version.substring(0, version.length() - "CraftServer".length());
		return "org.bukkit.craftbukkit" + version + simpleName;
	}

	public static String replaceAll(String source, String key, String value) {
		String[] split = source.split(Pattern.quote(key));
		StringBuilder builder = new StringBuilder();
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
	
	private static String getServerVersion() {
        Class<?> server = Bukkit.getServer().getClass();
        if (!server.getSimpleName().equals("CraftServer")) {
            return ".";
        }
        if (server.getName().equals("org.bukkit.craftbukkit.CraftServer")) {
            // Non versioned class
            return ".";
        } else {
            String version = server.getName().substring("org.bukkit.craftbukkit".length());
            return version.substring(0, version.length() - "CraftServer".length());
        }
    }

	public static String nms(String className) {
		return "net.minecraft.server" + getServerVersion() + className;
	}

	public static Class<?> nmsClass(String className) throws ClassNotFoundException {
		return Class.forName(nms(className));
	}

	public static String obc(String className) {
		return "org.bukkit.craftbukkit" + getServerVersion() + className;
	}

	public static Class<?> obcClass(String className) throws ClassNotFoundException {
		return Class.forName(obc(className));
	}
}
