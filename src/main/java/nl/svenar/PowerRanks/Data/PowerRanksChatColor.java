package nl.svenar.PowerRanks.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.Color;

public class PowerRanksChatColor {

	private static final Random rand = new Random();

	private static final char[] list_colors = { '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	private static final char[] list_colors_effects = { '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'l', 'n', 'o', 'k', 'm' };
	public static char unformatted_default_char = '&';
	public static char unformatted_hex_char = '#';
	private static HashMap<String, String> regular_colorcodes_hex = new HashMap<String, String>(); // HEX, MC-Color

	public PowerRanksChatColor() {
		regular_colorcodes_hex.put("#000000", "0");
		regular_colorcodes_hex.put("#00002A", "1");
		regular_colorcodes_hex.put("#002A00", "2");
		regular_colorcodes_hex.put("#002A2A", "3");
		regular_colorcodes_hex.put("#2A0000", "4");
		regular_colorcodes_hex.put("#2A002A", "5");
		regular_colorcodes_hex.put("#2A2A00", "6");
		regular_colorcodes_hex.put("#2A2A2A", "7");
		regular_colorcodes_hex.put("#151515", "8");
		regular_colorcodes_hex.put("#15153F", "9");
		regular_colorcodes_hex.put("#153F15", "a");
		regular_colorcodes_hex.put("#153F3F", "b");
		regular_colorcodes_hex.put("#3F1515", "c");
		regular_colorcodes_hex.put("#3F153F", "d");
		regular_colorcodes_hex.put("#3F3F15", "e");
		regular_colorcodes_hex.put("#3F3F3F", "f");
	}

	public static String colorize(String text, boolean special) {
		return colorizeRaw(text, special, true);
	}

	public static String colorizeRaw(String text, boolean special, boolean addLeadingReset) {
		String output = "";

		if (addLeadingReset) {
            if (text.length() == 0 || text.charAt(0) != PowerRanksChatColor.unformatted_default_char) {
                text = String.valueOf(PowerRanksChatColor.unformatted_default_char) + "r" + text;
            }
        } else if (text.length() == 0 || text.charAt(0) != PowerRanksChatColor.unformatted_default_char) {
            text = String.valueOf(PowerRanksChatColor.unformatted_default_char) + "f" + text;
        }

		if (text.length() == 0 || text.charAt(0) != unformatted_default_char) {
			text = unformatted_default_char + "r" + text;
		}

		String pattern = "(?<=&[iIjJ]).*?((?=&[0-9a-fA-FrR])|$)";
		Pattern hex_color_pattern = Pattern.compile("(&){0,1}#[a-fA-F0-9]{6}");

		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(text);

		while (m.find()) {
			int start = m.start();
			int end = m.end() - 2;
			String format_char = "";

			if (end > start) {
				Matcher re = Pattern.compile("&[lLnNoOkKmM]").matcher(text.substring(start, end));
				while (re.find()) {
					int re_start = start + re.start();
					int re_end = start + re.end();
					format_char = text.substring(re_start, re_end);
					text = text.replace(text.substring(re_start, re_end), "");
				}

				if (special) {
					text = text.replace(text.substring(start - 2, end + 2), colorStyle(text.substring(start - 2, end + 2)).special(format_char, true));
				}
			}
		}

		Matcher hex_color_matcher = hex_color_pattern.matcher(text);
		while (hex_color_matcher.find()) {
			String hex_color = text.substring(hex_color_matcher.start(), hex_color_matcher.end());

			try {
				text = text.replace(hex_color, net.md_5.bungee.api.ChatColor.of(hex_color.startsWith("&") ? hex_color.replaceFirst("&", "") : hex_color) + "");
			} catch(Exception|NoSuchMethodError e) {
				text = text.replace(hex_color, hex_compatibility_converter(hex_color.startsWith("&") ? hex_color.replaceFirst("&", "") : hex_color) + "");
			}
			hex_color_matcher = hex_color_pattern.matcher(text);
		}

		String[] text_split = text.split(String.valueOf(unformatted_default_char));

		for (String s : text_split) {
			if (s.length() > 0) {
				char color_char = s.charAt(0);

				String text_to_format = s.substring(1);
				if (String.valueOf(color_char).matches("[0-9a-fA-F]")) {
					text_to_format = colorStyle(unformatted_default_char + s).format();
				}

				if (String.valueOf(color_char).matches("[lLnNoOkKmMRr]")) {
					text_to_format = colorStyle(unformatted_default_char + s).format();
				}

				output += text_to_format;
			}
		}

		return output;
	}

	private static String getRandomColorCode(boolean effects) {
		char[] randomize = effects ? list_colors_effects : list_colors;
		return unformatted_default_char + String.valueOf(randomize[rand.nextInt(randomize.length)]);
	}

	private static PowerRanksColorStyle colorStyle(String text) {
		return new PowerRanksColorStyle(text);
	}

	private static int calculate_color_distance(Color c1, Color c2) {
		int distance = Integer.MAX_VALUE;
		distance = (int) Math.round(Math.sqrt(Math.pow(c1.getRed() - c2.getRed(), 2) + Math.pow(c1.getGreen() - c2.getGreen(), 2) + Math.pow(c1.getBlue() - c2.getBlue(), 2)));
		return distance;
	}

	private static Color hex2Rgb(String colorStr) {
		return Color.fromRGB(Integer.valueOf(colorStr.substring(1, 3), 16), Integer.valueOf(colorStr.substring(3, 5), 16), Integer.valueOf(colorStr.substring(5, 7), 16));
	}

	private static String hex_compatibility_converter(String input_hex) {
		String output = "";
		int last_distance = Integer.MAX_VALUE;
		Color input_color = hex2Rgb(input_hex);

		for (Map.Entry<String, String> entry : regular_colorcodes_hex.entrySet()) {
			int distance = calculate_color_distance(input_color, hex2Rgb(entry.getKey()));
			if (distance < last_distance) {
				last_distance = distance;
				output = unformatted_default_char + entry.getValue();
			}
		}
		return output;
	}

	public static class PowerRanksColorStyle {

		private final String text;

		public PowerRanksColorStyle(String text) {
			this.text = text;
		}

		public String format() {
			return ChatColor.translateAlternateColorCodes(unformatted_default_char, text);
		}

		public String special() {
			return special("", false);
		}

		public String special(String format_char, boolean use_unformated_char) {
			return text.toLowerCase().charAt(1) == 'i' ? new PowerRanksColorStyle(text.substring(2)).toRainbow(format_char, use_unformated_char)
					: (text.toLowerCase().charAt(1) == 'j' ? new PowerRanksColorStyle(text.substring(2)).toRandom(format_char, use_unformated_char) : text.substring(2));
		}

		public String toRandom() {
			return toRandom("", false);
		}

		public String toRandom(String format_char, boolean use_unformated_char) {
			StringBuilder sb = new StringBuilder();
            
            String textToFormat = text.toLowerCase().contains("&r") ? text.split("&r")[0] : text;

			for (char c : textToFormat.toCharArray()) {
				sb.append(PowerRanksChatColor.getRandomColorCode(false) + format_char + String.valueOf(c));
			}

            if (text.toLowerCase().contains("&r")) {
                String[] split = text.split("&r");
                if (split.length > 1) {
                    sb.append(split[1]);
                }

                sb.append("&r");
            }

			return use_unformated_char ? sb.toString() : ChatColor.translateAlternateColorCodes(unformatted_default_char, sb.toString());
		}

		public String toRainbow() {
			return toRainbow("", false);
		}

		public String toRainbow(String format_char, boolean use_unformated_char) {
			StringBuilder sb = new StringBuilder();
			ArrayList<String> rainbow_colors = new ArrayList<String>();
			rainbow_colors.add("4");
			rainbow_colors.add("6");
			rainbow_colors.add("e");
			rainbow_colors.add("2");
			rainbow_colors.add("3");
			rainbow_colors.add("5");
			rainbow_colors.add("d");
			int index = 0;

            String textToFormat = text.toLowerCase().contains("&r") ? text.split("&r")[0] : text;

			for (char c : textToFormat.toCharArray()) {
				sb.append(unformatted_default_char + rainbow_colors.get(index) + format_char + String.valueOf(c));
				if (index >= rainbow_colors.size() - 1) {
					index = 0;
				} else {
					index++;
				}
			}

            if (text.toLowerCase().contains("&r")) {
                String[] split = text.split("&r");
                if (split.length > 1) {
                    sb.append(split[1]);
                }

                sb.append("&r");
            }

			return use_unformated_char ? sb.toString() : ChatColor.translateAlternateColorCodes(unformatted_default_char, sb.toString());
		}

		public String toStripe(ChatColor colorOne, ChatColor colorTwo) {
			StringBuilder sb = new StringBuilder();
			boolean a = true;
			for (char c : text.toCharArray()) {
				sb.append(a ? colorOne : colorTwo);
				sb.append(c);
			}

			return sb.toString();
		}
	}
}