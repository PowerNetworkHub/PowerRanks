package nl.svenar.PowerRanks.Util;

import java.util.HashMap;
import java.util.Random;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.Color;

public class PowerColor {

    private final Random random = new Random();
    private HashMap<String, String> hexToMCColors = new HashMap<String, String>(); // HEX, MC-Color
    public static char UNFORMATTED_COLOR_CHAR = '&';

    public PowerColor() {
        hexToMCColors.put("#000000", "0");
        hexToMCColors.put("#00002A", "1");
        hexToMCColors.put("#002A00", "2");
        hexToMCColors.put("#002A2A", "3");
        hexToMCColors.put("#2A0000", "4");
        hexToMCColors.put("#2A002A", "5");
        hexToMCColors.put("#2A2A00", "6");
        hexToMCColors.put("#2A2A2A", "7");
        hexToMCColors.put("#151515", "8");
        hexToMCColors.put("#15153F", "9");
        hexToMCColors.put("#153F15", "a");
        hexToMCColors.put("#153F3F", "b");
        hexToMCColors.put("#3F1515", "c");
        hexToMCColors.put("#3F153F", "d");
        hexToMCColors.put("#3F3F15", "e");
        hexToMCColors.put("#3F3F3F", "f");
    }

    public String format(char altColorChar, String input, boolean special, boolean addLeadingReset) {
        String output = input;
        boolean hexSupported = false;

        if (addLeadingReset && !input.toLowerCase().endsWith(altColorChar + "r")) {
            input += altColorChar + "r";
        }

        try {
            Class.forName("net.md_5.bungee.api.ChatColor");
            hexSupported = true;
        } catch (ClassNotFoundException e) {
            hexSupported = false;
        }

        if (special) {
            output = formatSpecial(altColorChar, output, hexSupported);
        }

        if (hexSupported) {
            output = net.md_5.bungee.api.ChatColor.translateAlternateColorCodes(altColorChar, output);
        } else {
            output = ChatColor.translateAlternateColorCodes(altColorChar, hexCompatibilityConverter(altColorChar, output));
        }

        return output;
    }

    private String formatSpecial(char altColorChar, String input, boolean hexSupported) {
        String output = "";
        String format = "";
        String formatColor = "";
        String formatModifier = "";
        int charIndex = 0;
        boolean doFormat = true;
        String[] availableColors = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
        String[] rainbowColors = { "4", "6", "e", "2", "3", "5", "d" };
        String[] rainbowHEXColors = { "FF0000", "FF7F00", "FFFF00", "00FF00", "0000FF", "4B0082", "9400D3" };

        if (input.toLowerCase().contains(altColorChar + "i")) {
            output = input.split(altColorChar + "i").length > 1 ? input.split(altColorChar + "i")[0]
                    : input.split(altColorChar + "I")[0];
            format = input.split(altColorChar + "i").length > 1 ? input.split(altColorChar + "i")[1]
                    : input.split(altColorChar + "I")[1];
            formatColor = "i";
        } else if (input.toLowerCase().contains(altColorChar + "j")) {
            output = input.split(altColorChar + "j").length > 1 ? input.split(altColorChar + "j")[0]
                    : input.split(altColorChar + "J")[0];
            format = input.split(altColorChar + "j").length > 1 ? input.split(altColorChar + "j")[1]
                    : input.split(altColorChar + "J")[1];
            formatColor = "j";
        } else {
            output = input;
        }

        if (format.toLowerCase().startsWith("&l") || format.toLowerCase().startsWith("&m")
                || format.toLowerCase().startsWith("&n") || format.toLowerCase().startsWith("&o")
                || format.toLowerCase().startsWith("&k")) {
            formatModifier = format.substring(1, 2);
            format = format.substring(2);
        }

        for (String c : format.split("")) {
            if (c.equals(altColorChar + "")) {
                doFormat = false;
            }

            if (doFormat && !c.equals(" ")) {
                output += (formatColor == "i" ? altColorChar + (hexSupported ? rainbowHEXColors[charIndex % rainbowHEXColors.length] : rainbowColors[charIndex % rainbowColors.length])
                        : (formatColor == "j"
                                ? altColorChar + availableColors[random.nextInt(0, availableColors.length)]
                                : ""))
                        + (formatModifier.length() > 0 ? altColorChar + formatModifier : "")
                        + c;
                charIndex++;
            } else {
                output += c;
            }
        }

        return output;
    }

    private String hexCompatibilityConverter(char altColorChar, String input_hex) {
        String output = "";
        int last_distance = Integer.MAX_VALUE;
        Color input_color = hex2Rgb(input_hex);

        for (Entry<String, String> entry : hexToMCColors.entrySet()) {
            int distance = calculateColorDistance(input_color, hex2Rgb(entry.getKey()));
            if (distance < last_distance) {
                last_distance = distance;
                output = altColorChar + entry.getValue();
            }
        }
        return output;
    }

    private int calculateColorDistance(Color c1, Color c2) {
        int distance = Integer.MAX_VALUE;
        distance = (int) Math.round(Math.sqrt(Math.pow(c1.getRed() - c2.getRed(), 2)
                + Math.pow(c1.getGreen() - c2.getGreen(), 2) + Math.pow(c1.getBlue() - c2.getBlue(), 2)));
        return distance;
    }

    private Color hex2Rgb(String colorStr) {
        return Color.fromRGB(Integer.valueOf(colorStr.substring(1, 3), 16),
                Integer.valueOf(colorStr.substring(3, 5), 16), Integer.valueOf(colorStr.substring(5, 7), 16));
    }

}