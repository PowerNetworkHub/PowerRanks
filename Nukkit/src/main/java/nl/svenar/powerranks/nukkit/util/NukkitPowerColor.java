package nl.svenar.powerranks.nukkit.util;

import nl.svenar.powerranks.common.utils.PowerColor;

public class NukkitPowerColor {

    private PowerColor powerColor;

    public NukkitPowerColor() {
        this.powerColor = new PowerColor();
    }

    public String format(char altColorChar, String text, boolean special, boolean addLeadingReset,
            boolean addTrailingReset) {
        String targetText = text;
        targetText = this.powerColor.formatSpecial(altColorChar, targetText);
        targetText = this.powerColor.formatHEX(altColorChar, targetText);
        targetText = this.powerColor.formatColor(altColorChar, targetText);

        return targetText;
    }
}
