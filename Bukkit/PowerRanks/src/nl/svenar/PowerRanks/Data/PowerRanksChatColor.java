package nl.svenar.PowerRanks.Data;

import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;

public class PowerRanksChatColor {

	private static final Random rand = new Random();

	private static final char[] list_colors = { '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	private static final char[] list_colors_effects = { '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'l', 'n', 'o', 'k', 'm' };
	public static char unformatted_char = '&';

	public static String colorize(String text, boolean special) {
		String output = "";

		if (text.charAt(0) != unformatted_char) {
			text = unformatted_char + "r" + text;
		}

		String pattern = "(?<=&[iIjJ]).*?(?=&[0-9a-fA-FrR])";

		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(text);

		while (m.find()) {
			int start = m.start();
			int end = m.end() - 2;
			String format_char = "";
			
			Matcher re = Pattern.compile("&[lLnNoOkKmM]").matcher(text.substring(start, end));
			while (re.find()) {
				int re_start = start + re.start();
				int re_end = start + re.end();
				format_char = text.substring(re_start, re_end);
				text = text.replace(text.substring(re_start, re_end), "");
			}
			
			text = text.replace(text.substring(start - 2, end), colorStyle(text.substring(start - 2, end)).special(format_char, true));
		}

		String[] text_split = text.split(String.valueOf(unformatted_char));

		for (String s : text_split) {
			if (s.length() > 0) {
				char color_char = s.charAt(0);

				String text_to_format = s.substring(1);
				if (String.valueOf(color_char).matches("[0-9a-fA-F]")) {
					text_to_format = colorStyle(unformatted_char + s).format();
				}

//				if (special && String.valueOf(color_char).matches("[iIjJ]")) {
//					text_to_format = colorStyle(unformatted_char + s).special();
//				}

				if (String.valueOf(color_char).matches("[lLnNoOkKmMRr]")) {
					text_to_format = colorStyle(unformatted_char + s).format();
				}

				output += text_to_format;
			}
		}
		
		
		return output;
	}

	public static String getRandomColorCode(boolean effects) {
		char[] randomize = effects ? list_colors_effects : list_colors;
		return unformatted_char + String.valueOf(randomize[rand.nextInt(randomize.length)]);
	}

	public static PowerRanksColorStyle colorStyle(String text) {
		return new PowerRanksColorStyle(text);
	}

	public static class PowerRanksColorStyle {

		private final String text;

		public PowerRanksColorStyle(String text) {
			this.text = text;
		}

		public String format() {
			return ChatColor.translateAlternateColorCodes(unformatted_char, text);
		}

		public String special() {
			return special("", false);
		}
		
		public String special(String format_char, boolean use_unformated_char) {
			return text.toLowerCase().charAt(1) == 'i' ? new PowerRanksColorStyle(text.substring(2)).toRainbow(format_char, use_unformated_char) : (text.toLowerCase().charAt(1) == 'j' ? new PowerRanksColorStyle(text.substring(2)).toRandom(format_char, use_unformated_char) : text.substring(2));
		}

		public String toRandom() {
			return toRandom("", false);
		}
		
		public String toRandom(String format_char, boolean use_unformated_char) {
			StringBuilder sb = new StringBuilder();

			for (char c : text.toCharArray()) {
				sb.append(PowerRanksChatColor.getRandomColorCode(false) + format_char + String.valueOf(c));
			}

			return use_unformated_char ? sb.toString() : ChatColor.translateAlternateColorCodes(unformatted_char, sb.toString());
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

			for (char c : text.toCharArray()) {
				sb.append(unformatted_char + rainbow_colors.get(index) + format_char + String.valueOf(c));
				if (index >= rainbow_colors.size() - 1) {
					index = 0;
				} else {
					index++;
				}
			}

			return use_unformated_char ? sb.toString() : ChatColor.translateAlternateColorCodes(unformatted_char, sb.toString());
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
