package nl.svenar.common.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PowerColor {

    public static final char UNFORMATTED_COLOR_CHAR = '&';
    public static final char COLOR_CHAR = '\u00A7';
    private Map<String, String> hexToMCColors = new HashMap<String, String>(); // HEX, MC-Color
    private List<String> rainbowHEXColors = new ArrayList<String>(); // HEX

    private final String regexRainbowSyntax = "(\\[[rR][aA][iI][nN][bB][oO][wW]\\])(.+)(\\[\\/[rR][aA][iI][nN][bB][oO][wW]\\])";
    private final String regexRainbowSyntaxOpen = "(\\[[rR][aA][iI][nN][bB][oO][wW]\\])";
    private final String regexRainbowSyntaxClose = "(\\[\\/[rR][aA][iI][nN][bB][oO][wW]\\])";

    private final String regexGradientSyntax = "(\\[[gG][rR][aA][dD][iI][eE][nN][tT]=(%s#|#)[a-fA-F0-9]{6},(%s#|#)[a-fA-F0-9]{6}\\])(.+)(\\[\\/[gG][rR][aA][dD][iI][eE][nN][tT]\\])";
    private final String regexGradientSyntaxOpen = "(\\[[gG][rR][aA][dD][iI][eE][nN][tT]=(%s#|#)[a-fA-F0-9]{6},(%s#|#)[a-fA-F0-9]{6}\\])";
    private final String regexGradientSyntaxClose = "(\\[\\/[gG][rR][aA][dD][iI][eE][nN][tT]\\])";
    private final String regexGradientHEXColors = "(%s#|#)[a-fA-F0-9]{6},(%s#|#)[a-fA-F0-9]{6}";

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

        rainbowHEXColors.add("#E81416");
        rainbowHEXColors.add("#FFA500");
        rainbowHEXColors.add("#FAEB36");
        rainbowHEXColors.add("#79C314");
        rainbowHEXColors.add("#487DE7");
        rainbowHEXColors.add("#4B369D");
        rainbowHEXColors.add("#70369D");
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

    public String formatSpecial(char altColorChar, String text) {
        // Rainbow
        Pattern HEXPattern = Pattern.compile(regexRainbowSyntax.replaceAll("%s", altColorChar + ""));
        Matcher HEXMatcher = HEXPattern.matcher(text);

        while (HEXMatcher.find()) {
            String rawMatch = text.substring(HEXMatcher.start(), HEXMatcher.end());
            Pattern HEXPatternOpen = Pattern.compile(regexRainbowSyntaxOpen.replaceAll("%s", altColorChar + ""));
            Matcher HEXMatcherOpen = HEXPatternOpen.matcher(rawMatch);
            HEXMatcherOpen.find();
            String openSyntax = rawMatch.substring(HEXMatcherOpen.start(), HEXMatcherOpen.end());

            Pattern HEXPatternClose = Pattern.compile(regexRainbowSyntaxClose.replaceAll("%s", altColorChar + ""));
            Matcher HEXMatcherClose = HEXPatternClose.matcher(rawMatch);
            HEXMatcherClose.find();
            String closeSyntax = rawMatch.substring(HEXMatcherClose.start(), HEXMatcherClose.end());

            String targetText = rawMatch.replace(openSyntax, "").replace(closeSyntax, "");
            String rainbowText = "";
            int rainbowStep = Math.round((float) targetText.length() / (float) (rainbowHEXColors.size() - 1));
            for (int i = 0; i < rainbowHEXColors.size() - 1; i++) {
                String subText = targetText.substring(i * rainbowStep,
                        i != rainbowHEXColors.size() - 2 ? (i + 1) * rainbowStep : targetText.length());

                String fromhex = rainbowHEXColors.get(i);
                String tohex = rainbowHEXColors.get(i + 1);

                List<String> rainbowGradientPart = interpolateColors(fromhex, tohex, subText.length());

                for (int j = 0; j < subText.length(); j++) {
                    rainbowText += rainbowGradientPart.get(j) + subText.charAt(j);
                }
            }

            rainbowText += altColorChar + "r";

            text = HEXMatcher.replaceFirst(rainbowText);
            HEXMatcher = HEXPattern.matcher(text);
        }

        // Gradient
        HEXPattern = Pattern.compile(regexGradientSyntax.replaceAll("%s", altColorChar + ""));
        HEXMatcher = HEXPattern.matcher(text);

        while (HEXMatcher.find()) {
            String rawMatch = text.substring(HEXMatcher.start(), HEXMatcher.end());

            Pattern HEXPatternOpen = Pattern.compile(regexGradientSyntaxOpen.replaceAll("%s", altColorChar + ""));
            Matcher HEXMatcherOpen = HEXPatternOpen.matcher(rawMatch);
            HEXMatcherOpen.find();
            String openSyntax = rawMatch.substring(HEXMatcherOpen.start(), HEXMatcherOpen.end());

            Pattern HEXPatternClose = Pattern.compile(regexGradientSyntaxClose.replaceAll("%s", altColorChar + ""));
            Matcher HEXMatcherClose = HEXPatternClose.matcher(rawMatch);
            HEXMatcherClose.find();
            String closeSyntax = rawMatch.substring(HEXMatcherClose.start(), HEXMatcherClose.end());

            Pattern HEXPatternColors = Pattern.compile(regexGradientHEXColors.replaceAll("%s", altColorChar + ""));
            Matcher HEXMatcherColors = HEXPatternColors.matcher(openSyntax);
            HEXMatcherColors.find();
            String hexColors = openSyntax.substring(HEXMatcherColors.start(), HEXMatcherColors.end());

            String targetText = rawMatch.replace(openSyntax, "").replace(closeSyntax, "");

            String fromhex = hexColors.split(",")[0];
            String tohex = hexColors.split(",")[1];

            String gradientText = "";

            List<String> rainbowGradientPart = interpolateColors(fromhex, tohex, targetText.length());

            for (int j = 0; j < targetText.length(); j++) {
                gradientText += rainbowGradientPart.get(j) + targetText.charAt(j);
            }

            gradientText += altColorChar + "r";

            text = HEXMatcher.replaceFirst(gradientText);
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

    /**
     * Interpolate between two colors with a specific factor
     * 
     * @param color1
     * @param color2
     * @param factor
     * @return String
     */
    public String interpolateColor(String color1, String color2, double factor) {
        if (factor < 0.0) {
            throw new IllegalArgumentException("Invallid factor");
        }

        if (!color1.startsWith("#") && color1.length() != 7) {
            throw new IllegalArgumentException("Invallid color1");
        }

        if (!color2.startsWith("#") && color2.length() != 7) {
            throw new IllegalArgumentException("Invallid color2");
        }

        int red1 = Integer.parseInt(color1.substring(1, 3), 16);
        int green1 = Integer.parseInt(color1.substring(3, 5), 16);
        int blue1 = Integer.parseInt(color1.substring(5, 7), 16);

        int red2 = Integer.parseInt(color2.substring(1, 3), 16);
        int green2 = Integer.parseInt(color2.substring(3, 5), 16);
        int blue2 = Integer.parseInt(color2.substring(5, 7), 16);

        int[] numColor1 = { red1, green1, blue1 };
        int[] numColor2 = { red2, green2, blue2 };

        int[] startColor = numColor1.clone();
        startColor[0] += factor * (numColor2[0] - numColor1[0]);
        startColor[1] += factor * (numColor2[1] - numColor1[1]);
        startColor[2] += factor * (numColor2[2] - numColor1[2]);

        return "#"
                + (Integer.toHexString(startColor[0]).length() == 1 ? "0" + Integer.toHexString(startColor[0])
                        : Integer.toHexString(startColor[0]))
                + (Integer.toHexString(startColor[1]).length() == 1 ? "0" + Integer.toHexString(startColor[1])
                        : Integer.toHexString(startColor[1]))
                + (Integer.toHexString(startColor[2]).length() == 1 ? "0" + Integer.toHexString(startColor[2])
                        : Integer.toHexString(startColor[2]));
    }

    /**
     * Interpolate between two colors in steps
     * 
     * @param color1
     * @param color2
     * @param steps
     * @return List<String>
     */
    public List<String> interpolateColors(String color1, String color2, int steps) {
        if (steps <= 0) {
            throw new IllegalArgumentException("Invallid steps");
        }

        if (!color1.startsWith("#") && color1.length() != 7) {
            throw new IllegalArgumentException("Invallid color1");
        }

        if (!color2.startsWith("#") && color2.length() != 7) {
            throw new IllegalArgumentException("Invallid color2");
        }

        double stepFactor = 1.0 / (float) (steps - 1);
        List<String> interpolatedColorArray = new ArrayList<String>();

        for (int i = 0; i < steps; i++) {
            interpolatedColorArray.add(interpolateColor(color1, color2, stepFactor * i));
        }

        return interpolatedColorArray;
    }
}
