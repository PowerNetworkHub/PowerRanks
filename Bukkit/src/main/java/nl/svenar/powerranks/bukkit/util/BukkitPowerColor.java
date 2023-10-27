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

}