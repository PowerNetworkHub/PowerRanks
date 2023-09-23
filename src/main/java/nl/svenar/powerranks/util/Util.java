package nl.svenar.powerranks.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import nl.svenar.powerranks.PowerRanks;

public class Util {

	public static final long TASK_TPS = 20;

	public static String getServerVersion(Server server) {
		try {
			Matcher matcher = Pattern.compile("\\d{1,3}.\\d{1,3}|\\d{1,3}.\\d{1,3}.\\d{1,3}")
					.matcher(server.getVersion());

			List<String> results = new ArrayList<String>();
			while (matcher.find()) {
				if (matcher.groupCount() > 0) {
					results.add(matcher.group(1));
				} else {
					results.add(matcher.group());
				}
			}

			return results.get(0);
		} catch (Exception e) {
			return "Unknown";
		}
	}

	public static String getServerType(Server server) {
		try {
			Matcher matcher = Pattern.compile("-\\w{1,32}-").matcher(server.getVersion());

			List<String> results = new ArrayList<String>();
			while (matcher.find()) {
				if (matcher.groupCount() > 0) {
					results.add(matcher.group(1));
				} else {
					results.add(matcher.group());
				}
			}

			return results.get(0).replaceAll("-", "");
		} catch (Exception e) {
			return "Unknown";
		}
	}

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

	public static String powerFormatter(String text, Map<String, String> values, char openChar, char closeChar) {
		StringBuilder result = new StringBuilder();
		int textIdx = 0;

		for (int startIdx; (startIdx = text.indexOf(openChar, textIdx)) != -1;) {
			int endIdx = text.indexOf(closeChar, startIdx + 1);

			if (endIdx == -1)
				break;

			if (startIdx < 2 || text.charAt(startIdx - 1) != '\\') {
				result.append(text.substring(textIdx, startIdx));
				textIdx = endIdx + 1;
				String value = values.get(text.substring(startIdx + 1, endIdx));

				if (value != null && !value.isEmpty()) {
					result.append(value); // Replace placeholder with non-empty value

				} else if (result.length() != 0 && result.charAt(result.length() - 1) == ' ') {
					result.setLength(result.length() - 1); // Remove space before placeholder

				} else if (textIdx < text.length() && text.charAt(textIdx) == ' ') {
					textIdx++; // Skip space after placeholder
				} else {
					result.append(openChar + text.substring(startIdx + 1, endIdx) + closeChar); // Add back the original
																								// placeholder when an
																								// replacement isn't
																								// found
				}
			} else {
				String unformatted = text.substring(textIdx, endIdx + 1).replaceFirst("\\\\", "");
				if (unformatted.length() > 1) {
					String replaceText = text.substring(startIdx, endIdx + 1);
					String baseText = text.substring(startIdx, startIdx + 1);
					String endText = text.substring(endIdx + 1, endIdx + 1);
					String formattedReplacement = baseText
							+ powerFormatter(text.substring(startIdx + 1, endIdx + 1), values, openChar, closeChar)
							+ endText;

					unformatted = unformatted.replace(replaceText, formattedReplacement);
				}
				result.append(unformatted);
				textIdx = endIdx + 1;
			}
		}
		result.append(text.substring(textIdx));
		return result.toString();
	}

	public static boolean isPowerRanksSign(PowerRanks main, Sign sign) {
		String sign_header = sign.getLine(0);
		return isPowerRanksSign(main, sign_header);
	}

	public static boolean isPowerRanksSign(PowerRanks main, String sign_header) {
		return sign_header.toLowerCase().contains("powerranks")
				&& PowerRanks.getConfigManager().getBool("signs.enabled", false);
	}

	public static boolean stringContainsItemFromList(String inputStr, String[] items) {
		return Arrays.stream(items).anyMatch(inputStr::contains);
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
		input = input.replaceAll("[a-zA-Z- ]", "");
		String[] input_split = input.split("\\.");

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

	public static URLConnection getURL(String urlString) throws Exception {
		int MAX_REDIRECTS = 10;

		URLConnection urlConnection = new URL(urlString).openConnection();
		String redirect = urlConnection.getHeaderField("Location");
		for (int i = 0; i < MAX_REDIRECTS; i++) {
			if (redirect != null) {
				urlConnection = new URL(redirect).openConnection();
				redirect = urlConnection.getHeaderField("Location");
			} else {
				break;
			}
		}

		return urlConnection;
	}

	public static String readUrl(String urlString) throws Exception {
		String output = "";

		URLConnection urlConnection = getURL(urlString);

		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
		String line;
		while ((line = bufferedReader.readLine()) != null) {
			output += line + "\n";
		}
		bufferedReader.close();

		return output;
	}

	public static ArrayList<String> generateWildcardList(String permission) {
		ArrayList<String> output = new ArrayList<String>();
		String[] permission_split = permission.split("\\.");

		permission_split = Util.array_pop(permission_split);
		for (int i = 0; i < permission_split.length + 1; i++) {
			if (permission_split.length == 0)
				break;
			output.add(String.join(".", permission_split) + ".*");
			permission_split = Util.array_pop(permission_split);
		}

		return output;
	}

	/**
	 * Convert a time string to seconds
	 * 
	 * @param time_input
	 * @return
	 */
	public static int timeStringToSecondsConverter(String time_input) {
		Matcher regex_int = Pattern.compile("^\\d+[^a-zA-Z]{0,1}$").matcher(time_input);

		Matcher regex_seconds = Pattern.compile("\\d+[sS]").matcher(time_input);
		Matcher regex_minutes = Pattern.compile("\\d+[mM]").matcher(time_input);
		Matcher regex_hours = Pattern.compile("\\d+[hH]").matcher(time_input);
		Matcher regex_days = Pattern.compile("\\d+[dD]").matcher(time_input);
		Matcher regex_weeks = Pattern.compile("\\d+[wW]").matcher(time_input);
		Matcher regex_years = Pattern.compile("\\d+[yY]").matcher(time_input);

		int seconds = 0;

		if (regex_int.find()) {
			seconds = Integer.parseInt(time_input);
		} else {
			if (regex_seconds.find()) {
				seconds += Integer.parseInt(time_input.substring(regex_seconds.start(), regex_seconds.end() - 1));
			}

			if (regex_minutes.find()) {
				seconds += Integer.parseInt(time_input.substring(regex_minutes.start(), regex_minutes.end() - 1)) * 60;
			}

			if (regex_hours.find()) {
				seconds += Integer.parseInt(time_input.substring(regex_hours.start(), regex_hours.end() - 1))
						* (60 * 60);
			}

			if (regex_days.find()) {
				seconds += Integer.parseInt(time_input.substring(regex_days.start(), regex_days.end() - 1))
						* (60 * 60 * 24);
			}

			if (regex_weeks.find()) {
				seconds += Integer.parseInt(time_input.substring(regex_weeks.start(), regex_weeks.end() - 1))
						* (60 * 60 * 24 * 7);
			}

			if (regex_years.find()) {
				seconds += Integer.parseInt(time_input.substring(regex_years.start(), regex_years.end() - 1))
						* (60 * 60 * 24 * 365);
			}
		}

		return seconds;
	}

	public static UUID getUUIDFromAPI(String playerName) {
		try {
			URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + playerName);

			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

			String UUIDJson = "";
			String line;
			while ((line = in.readLine()) != null) {
				UUIDJson += line + "\n";
			}
			in.close();

			JSONObject UUIDObject = (JSONObject) JSONValue.parseWithException(UUIDJson);
			String uuid = UUIDObject.get("id").toString();
			if (!uuid.contains("-")) {
				uuid = uuid.replaceFirst(
						"(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)",
						"$1-$2-$3-$4-$5");
			}
			return UUID.fromString(uuid);
		} catch (Exception e) {
			return null;
		}
	}

	public static String getNameFromAPI(String uuid) {
		try {
			URL url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid.replaceAll("-", ""));

			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

			String nameJson = "";
			String line;
			while ((line = in.readLine()) != null) {
				nameJson += line + "\n";
			}
			in.close();
			JSONObject nameObject = (JSONObject) JSONValue.parseWithException(nameJson);
			return nameObject.get("name").toString();
		} catch (Exception e) {
			return null;
		}
	}

	public static Long convertToLong(Object value) {
		if (value instanceof Integer) {
			return Long.valueOf(((Integer) value).intValue());
		} else if (value instanceof Long) {
			return (Long) value;
		} else {
			throw new IllegalArgumentException("Value is not an Integer or Long");
		}
	}

    public static Object formatStringToType(String input) {
		Object output = input;

		if (input.equalsIgnoreCase("true") || input.equalsIgnoreCase("false")) {
			output = Boolean.parseBoolean(input);
		} else if (input.matches("^\\d+$")) {
			try {
				output = Integer.parseInt(input);
			} catch (Exception e) {
				output = Long.parseLong(input);
			}
		} else if (input.matches("^\\d+\\.\\d+$")) {
			output = Double.parseDouble(input);
		} else if (input.startsWith("[") && input.endsWith("]")) {
			String[] inputSplit = input.substring(1, input.length() - 1).split(",");
			ArrayList<Object> outputList = new ArrayList<Object>();
			for (String inputSplitItem : inputSplit) {
				outputList.add(inputSplitItem);
			}
			output = outputList;
		}

        return output;
    }
}