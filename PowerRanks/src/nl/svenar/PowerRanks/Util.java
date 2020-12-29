package nl.svenar.PowerRanks;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.block.Sign;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class Util {

	public static String replaceAll(String source, String key, String value) {
		String[] split = source.split(Pattern.quote(key));
		if (split.length > 0) {
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
		} else {
			return source;
		}
	}
	
	public static String[] splitStringEvery(String s, int interval) {
	    int arrayLength = (int) Math.ceil(((s.length() / (double) interval)));
	    String[] result = new String[arrayLength];

	    int j = 0;
	    int lastIndex = result.length - 1;
	    for (int i = 0; i < lastIndex; i++) {
	        result[i] = s.substring(j, j + interval);
	        j += interval;
	    }
	    result[lastIndex] = s.substring(j);

	    return result;
	}

	public static String powerFormatter(String text, Map<String, String> values, char openChar, char closeChar) {
		StringBuilder result = new StringBuilder();
		int textIdx = 0;
		for (int startIdx; (startIdx = text.indexOf(openChar, textIdx)) != -1;) {
			int endIdx = text.indexOf(closeChar, startIdx + 1);
			if (endIdx == -1)
				break;
			result.append(text.substring(textIdx, startIdx));
			textIdx = endIdx + 1;
			String value = values.get(text.substring(startIdx + 1, endIdx));
			if (value != null && !value.isEmpty()) {
				result.append(value); // Replace placeholder with non-empty value
			} else if (result.length() != 0 && result.charAt(result.length() - 1) == ' ') {
				result.setLength(result.length() - 1); // Remove space before placeholder
			} else if (textIdx < text.length() && text.charAt(textIdx) == ' ') {
				textIdx++; // Skip space after placeholder
			}
		}
		result.append(text.substring(textIdx));
		return result.toString();
	}

	public static boolean isPowerRanksSign(PowerRanks main, Sign sign) {
		String sign_header = sign.getLine(0);
		return isPowerRanksSign(main, sign_header);
	}
	
	public static boolean stringContainsItemFromList(String inputStr, String[] items) {
	    return Arrays.stream(items).anyMatch(inputStr::contains);
	}

	public static boolean isPowerRanksSign(PowerRanks main, String sign_header) {
		final File configFile = new File(String.valueOf(PowerRanks.configFileLoc) + "config" + ".yml");
		final YamlConfiguration configYaml = new YamlConfiguration();
		try {
			configYaml.load(configFile);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
		return sign_header.toLowerCase().contains("powerranks") && configYaml.getBoolean("signs.enabled");
	}

	public String getCraftBukkitClassName(String simpleName) {
		String version = Bukkit.getServer().getClass().getName().substring("org.bukkit.craftbukkit".length());
		version = version.substring(0, version.length() - "CraftServer".length());
		return "org.bukkit.craftbukkit" + version + simpleName;
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
	
	public static int calculateVersionFromString(String input) {
		int output = 0;
		input = input.replaceAll("[a-zA-Z ]", "");
		String[] input_split = input.split("\\.");
//		String calcString = "1";
//		for (int i = input_split.length - 1; i >= 0; i--) {
//			if (input_split[i].length() != 0) {
//				int num = Integer.parseInt(input_split[i]) * Integer.parseInt(calcString);
//				calcString += "0";
//				output += num;
//			}
//		}
		
		String calcString = "1000000";
		for (int i = 0; i < input_split.length; i++) {
			if (input_split[i].length() != 0) {
				int num = Integer.parseInt(input_split[i]) * Integer.parseInt(calcString);
				if (calcString.charAt(calcString.length() - 1) == '0') {
					calcString = calcString.substring(0, calcString.length() - 1);
				}
				output += num;
			}
		}
		
		return output;
	}
	
	public static <T> T[] array_push(T[] arr, T item) {
        T[] tmp = Arrays.copyOf(arr, arr.length + 1);
        tmp[tmp.length - 1] = item;
        return tmp;
    }

    public static <T> T[] array_pop(T[] arr) {
        T[] tmp = Arrays.copyOf(arr, arr.length - 1);
        return tmp;
    }

	public static Player getPlayerByName(String target_player_name) {
		Player target_player = null;
		for (Player online_player : Bukkit.getOnlinePlayers()) {
			if (online_player.getName().equalsIgnoreCase(target_player_name)) {
				target_player = online_player;
				break;
			}
		}
		return target_player;
	}
}