package nl.svenar.common.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PowerColor {

    public static final char UNFORMATTED_COLOR_CHAR = '&';
    public static final char COLOR_CHAR = '\u00A7';
    private Map<String, String> hexToMCColors = new HashMap<String, String>(); // HEX, MC-Color
    
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
    
    public String formatHEX(char altColorChar, String text) {
        Pattern HEXPattern = Pattern.compile(altColorChar + "?#[a-fA-F0-9]{6}");
        Matcher HEXMatcher = HEXPattern.matcher(text);

        while (HEXMatcher.find()) {
            String rawHEX = text.substring(HEXMatcher.start(), HEXMatcher.end());
            String formattedHEX = rawHEX.startsWith(altColorChar + "") ? rawHEX.substring(1) : rawHEX;
            
            StringBuilder magic = new StringBuilder(altColorChar + "x");
            for (char c : formattedHEX.substring(1).toCharArray()) {
                magic.append(altColorChar).append(c);
            }
            formattedHEX = magic.toString();

            text = HEXMatcher.replaceFirst(formattedHEX);
            HEXMatcher = HEXPattern.matcher(text);
        }

        return text;
    }

    public String formatColor(char altColorChar, String text) {
        char[] b = text.toCharArray();
        for (int i = 0; i < b.length - 1; i++) {
            if (b[i] == altColorChar && "0123456789AaBbCcDdEeFfKkLlMmNnOoRrXx".indexOf(b[i + 1]) > -1) {
                b[i] = COLOR_CHAR;
                b[i + 1] = Character.toLowerCase(b[i + 1]);
            }
        }
        return new String(b);
    }

    public String hexCompatibilityConverter(char altColorChar, String input_hex) {
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

    public int calculateColorDistance(Color c1, Color c2) {
        int distance = Integer.MAX_VALUE;
        distance = (int) Math.round(Math.sqrt(Math.pow(c1.getRed() - c2.getRed(), 2)
                + Math.pow(c1.getGreen() - c2.getGreen(), 2) + Math.pow(c1.getBlue() - c2.getBlue(), 2)));
        return distance;
    }

    public Color hex2Rgb(String colorStr) {
        return Color.fromRGB(Integer.valueOf(colorStr.substring(1, 3), 16),
                Integer.valueOf(colorStr.substring(3, 5), 16), Integer.valueOf(colorStr.substring(5, 7), 16));
    }
}
