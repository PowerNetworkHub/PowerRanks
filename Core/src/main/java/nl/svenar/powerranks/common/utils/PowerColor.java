package nl.svenar.powerranks.common.utils;

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
    private final String ALL_COLOR_CODES = "0123456789AaBbCcDdEeFfKkLlMmNnOoRrXx";

    private Map<String, String> hexToMCColors = new HashMap<String, String>(); // HEX, MC-Color
    private final String[] rainbowHEXColors = {
            "#E81416", // Red
            "#FFA500", // Orange
            "#FAEB36", // Yellow
            "#79C314", // Green
            "#487DE7", // Blue
            "#4B369D", // Indigo
            "#70369D" // Violet
    };

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

        StringBuilder result = new StringBuilder(text.length());

        int lastAppendPosition = 0;

        while (HEXMatcher.find()) {
            String rawHEX = text.substring(HEXMatcher.start(), HEXMatcher.end());
            String formattedHEX = rawHEX.startsWith(altColorChar + "") ? rawHEX.substring(1) : rawHEX;

            StringBuilder magic = new StringBuilder(altColorChar + "x");
            for (int i = 1; i < formattedHEX.length(); i++) {
                magic.append(altColorChar).append(formattedHEX.charAt(i));
            }
            formattedHEX = magic.toString();

            result.append(text, lastAppendPosition, HEXMatcher.start()).append(formattedHEX);

            lastAppendPosition = HEXMatcher.end();
        }

        result.append(text.substring(lastAppendPosition));

        return result.toString();
    }

    /**
     * Format special syntax to Minecraft color codes
     * 
     * @param altColorChar
     * @param text
     * @return String
     */
    public String formatSpecial(char altColorChar, String text) {
        text = parseGradient(altColorChar, text);
        text += altColorChar + "r";
        text = parseRainbow(altColorChar, text);
        text += altColorChar + "r";
        text = text.replaceAll(altColorChar + "r" + altColorChar + "r", altColorChar + "r");

        return text;
    }

    public String parseGradient(char altColorChar, String input) {
        int patternStart = input.indexOf("[gradient=");
        if (patternStart == -1) {
            return input;
        }

        int inputLength = input.length();
        char[] inputChars = input.toCharArray();

        int lastAppendPosition = 0;

        Pattern pattern = Pattern.compile("\\[gradient=([^,]+),([^\\]]+)\\]([^\\[]+)\\[/gradient\\]");
        Matcher matcher = pattern.matcher(input);

        StringBuilder result = new StringBuilder(); // Initial capacity

        while (matcher.find(patternStart)) {
            String startColor = matcher.group(1);
            String endColor = matcher.group(2);
            String content = matcher.group(3);

            String gradient = generateGradient(startColor, endColor, content);

            result.append(inputChars, lastAppendPosition, matcher.start() - lastAppendPosition);
            result.append(gradient);

            lastAppendPosition = matcher.end();
            patternStart = lastAppendPosition;

            // Restrict the search region for the next match
            matcher.region(patternStart, inputLength);
        }

        result.append(inputChars, lastAppendPosition, inputLength - lastAppendPosition);

        return result.toString();
    }

    public String parseRainbow(char altColorChar, String input) {
        int patternStart = input.indexOf("[rainbow]");
        if (patternStart == -1) {
            return input;
        }

        int inputLength = input.length();
        StringBuilder result = new StringBuilder();
        int lastAppendPosition = 0;

        Pattern pattern = Pattern.compile("\\[rainbow\\]([^\\[]+)\\[/rainbow\\]");

        do {

            Matcher matcher = pattern.matcher(input);

            if (!matcher.find(patternStart)) {
                break;
            }

            String content = matcher.group(1);
            String rainbow = generateRainbow(content);
            result.append(input, lastAppendPosition, matcher.start()).append(rainbow);

            lastAppendPosition = matcher.end();
            patternStart = lastAppendPosition;

        } while (patternStart < inputLength);

        result.append(input, lastAppendPosition, inputLength);

        return result.toString();
    }

    private String generateGradient(String startColor, String endColor, String content) {
        StringBuilder result = new StringBuilder();

        Color numColor1 = hexToRGB(startColor);
        Color numColor2 = hexToRGB(endColor);

        for (int i = 0; i < content.length(); i++) {
            char c = content.charAt(i);
            double ratio = (double) i / (content.length() - 1);
            int r = (int) (numColor1.getRed() * (1 - ratio) + numColor2.getRed() * ratio);
            int g = (int) (numColor1.getGreen() * (1 - ratio) + numColor2.getGreen() * ratio);
            int b = (int) (numColor1.getBlue() * (1 - ratio) + numColor2.getBlue() * ratio);
            result.append('#');
            result.append(toHexString(r));
            result.append(toHexString(g));
            result.append(toHexString(b));
            result.append(c);
        }

        return result.toString();
    }

    private String generateRainbow(String text) {
        int numColors = rainbowHEXColors.length;
        StringBuilder result = new StringBuilder();

        if (text.length() <= 9) {
            int step = (int) Math.round((float) numColors / (float) text.length());
            int index = 0;

            for (int i = 0; i < text.length(); i++) {
                char c = text.charAt(i);
                String color = rainbowHEXColors[index % numColors];
                index += step;
                result.append(color);
                result.append(c);
            }
        } else {
            int rainbowStep = Math.round((float) text.length() / (float) (numColors - 1));

            for (int i = 0; i < numColors - 1; i++) {
                int start = i * rainbowStep;
                int end = (i != numColors - 2) ? (i + 1) * rainbowStep : text.length();

                String fromhex = rainbowHEXColors[i];
                String tohex = rainbowHEXColors[i + 1];

                if (start < end) {
                    List<String> rainbowGradientPart = interpolateColors(fromhex, tohex, end - start);

                    for (int j = start; j < end; j++) {
                        result.append(rainbowGradientPart.get(j - start)).append(text.charAt(j));
                    }
                }
            }
        }

        return result.toString();
    }

    /**
     * Convert int to HEX String
     * 
     * @param value
     * @return String
     */
    private String toHexString(int value) {
        char[] hexChars = new char[2];
        hexChars[0] = Character.forDigit((value >> 4) & 0xF, 16);
        hexChars[1] = Character.forDigit(value & 0xF, 16);
        return new String(hexChars);
    }

    /**
     * Format color codes to Minecraft color codes
     * 
     * @param altColorChar
     * @param text
     * @return String
     */
    public String formatColor(char altColorChar, String text) {
        char[] b = text.toCharArray();
        for (int i = 0; i < b.length - 1; i++) {
            if (b[i] == altColorChar && ALL_COLOR_CODES.indexOf(b[i + 1]) > -1) {
                b[i] = COLOR_CHAR;
                b[i + 1] = Character.toLowerCase(b[i + 1]);
            }
        }
        return new String(b);
    }

    /**
     * Convert HEX to legacy Minecraft color codes
     * 
     * @param altColorChar
     * @param inputHEX
     * @return String
     */
    public String hexCompatibilityConverter(char altColorChar, String inputHEX) {
        String output = "";
        int last_distance = Integer.MAX_VALUE;
        Color input_color = hexToRGB(inputHEX);

        for (Entry<String, String> entry : hexToMCColors.entrySet()) {
            int distance = calculateColorDistance(input_color, hexToRGB(entry.getKey()));
            if (distance < last_distance) {
                last_distance = distance;
                output = altColorChar + entry.getValue();
            }
        }
        return output;
    }

    /**
     * Calculate the distance between two colors
     * 
     * @param color1
     * @param color2
     * @return int
     */
    public int calculateColorDistance(Color color1, Color color2) {
        int distance = Integer.MAX_VALUE;
        distance = (int) Math.round(Math.sqrt(Math.pow(color1.getRed() - color2.getRed(), 2)
                + Math.pow(color1.getGreen() - color2.getGreen(), 2)
                + Math.pow(color1.getBlue() - color2.getBlue(), 2)));
        return distance;
    }

    /**
     * Convert HEX to RGB
     * 
     * @param colorStr
     * @return Color
     */
    private Color hexToRGB(String colorStr) {
        return Color.fromRGB(
                Integer.valueOf(colorStr.substring(1, 3), 16),
                Integer.valueOf(colorStr.substring(3, 5), 16),
                Integer.valueOf(colorStr.substring(5, 7), 16));
    }

    /**
     * Interpolate between two colors with a specific factor
     * 
     * @param color1
     * @param color2
     * @param factor
     * @return String
     * @throws IllegalArgumentException
     */
    public String interpolateColor(String color1, String color2, double factor) {
        if (factor < 0.0) {
            throw new IllegalArgumentException("Invalid factor");
        }

        if (!color1.startsWith("#") && color1.length() != 7) {
            throw new IllegalArgumentException("Invalid color1");
        }

        if (!color2.startsWith("#") && color2.length() != 7) {
            throw new IllegalArgumentException("Invalid color2");
        }

        int red1 = Integer.parseInt(color1.substring(1, 3), 16);
        int green1 = Integer.parseInt(color1.substring(3, 5), 16);
        int blue1 = Integer.parseInt(color1.substring(5, 7), 16);

        int red2 = Integer.parseInt(color2.substring(1, 3), 16);
        int green2 = Integer.parseInt(color2.substring(3, 5), 16);
        int blue2 = Integer.parseInt(color2.substring(5, 7), 16);

        int red = (int) (red1 * (1 - factor) + red2 * factor);
        int green = (int) (green1 * (1 - factor) + green2 * factor);
        int blue = (int) (blue1 * (1 - factor) + blue2 * factor);

        StringBuilder result = new StringBuilder("#");
        result.append(toHexString(red));
        result.append(toHexString(green));
        result.append(toHexString(blue));
        return result.toString();
    }

    /**
     * Interpolate between two colors in steps
     * 
     * @param color1
     * @param color2
     * @param steps
     * @return List<String>
     * @throws IllegalArgumentException
     */
    public List<String> interpolateColors(String color1, String color2, int steps) {
        if (steps <= 0) {
            throw new IllegalArgumentException("Invalid steps");
        }

        if (!color1.startsWith("#") && color1.length() != 7) {
            throw new IllegalArgumentException("Invalid color1");
        }

        if (!color2.startsWith("#") && color2.length() != 7) {
            throw new IllegalArgumentException("Invalid color2");
        }

        double stepFactor = 1.0 / (double) (steps - 1.0);
        List<String> interpolatedColorArray = new ArrayList<>(steps);

        for (int i = 0; i < steps; i++) {
            interpolatedColorArray.add(interpolateColor(color1, color2, stepFactor * i));
        }

        return interpolatedColorArray;
    }
}
