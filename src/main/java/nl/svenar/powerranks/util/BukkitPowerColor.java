package nl.svenar.powerranks.util;

import org.bukkit.ChatColor;

import nl.svenar.common.utils.PowerColor;

public class BukkitPowerColor {

    private PowerColor powerColor;

    public BukkitPowerColor() {
        this.powerColor = new PowerColor();
    }

    public String format(char altColorChar, String text, boolean special, boolean addLeadingReset, boolean addTrailingReset) {
        boolean hexSupported = false;

        try {
            Class.forName("net.md_5.bungee.api.ChatColor");
            hexSupported = true;
        } catch (ClassNotFoundException e) {
            hexSupported = false;
        }


        if (hexSupported) {
            text = this.powerColor.formatHEX(altColorChar, text);
            text = net.md_5.bungee.api.ChatColor.translateAlternateColorCodes(altColorChar, text);
        } else {
            text = ChatColor.translateAlternateColorCodes(altColorChar, this.powerColor.hexCompatibilityConverter(altColorChar, text));
        }

        return text;
    }

}