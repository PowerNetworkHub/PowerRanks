package nl.svenar.powerranks.utils;

import java.util.Map;
import java.util.regex.Pattern;

public class PowerFormatter {

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

    public static String format(String text, Map<String, String> values, char openChar, char closeChar) {
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
}
