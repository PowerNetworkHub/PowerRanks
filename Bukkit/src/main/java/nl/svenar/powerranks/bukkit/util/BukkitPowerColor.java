package nl.svenar.powerranks.bukkit.util;

import nl.svenar.powerranks.common.utils.PowerColor;

public class BukkitPowerColor {

    private PowerColor powerColor;

    public BukkitPowerColor() {
        this.powerColor = new PowerColor();
    }

    public String format(char altColorChar, String text, boolean special, boolean addLeadingReset, boolean addTrailingReset) {
        text = this.powerColor.formatSpecial(altColorChar, text);
        text = this.powerColor.formatHEX(altColorChar, text);
        text = this.powerColor.formatColor(altColorChar, text);

        return text;
    }

    public String removeFormat(char altColorChar, String text) {
        String targetText = text;
        targetText = this.powerColor.removeFormatSpecial(altColorChar, text);
        targetText = this.powerColor.removeFormatHEX(altColorChar, targetText);
        targetText = this.powerColor.removeFormatColor(altColorChar, targetText);

        return targetText;
    }

}