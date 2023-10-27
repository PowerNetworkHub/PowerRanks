package nl.svenar.powerranks.nukkit.util;

import nl.svenar.powerranks.common.utils.PowerColor;

public class NukkitPowerColor {

    private PowerColor powerColor;

    public NukkitPowerColor() {
        this.powerColor = new PowerColor();
    }

    public String format(char altColorChar, String text, boolean special, boolean addLeadingReset, boolean addTrailingReset) {
        text = this.powerColor.formatSpecial(altColorChar, text);
        text = this.powerColor.formatHEX(altColorChar, text);
        text = this.powerColor.formatColor(altColorChar, text);

        return text;
    }
}
