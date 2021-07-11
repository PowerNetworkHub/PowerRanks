package nl.svenar.powerranks.configuration;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;

import nl.svenar.powerranks.PowerRanks;
import nl.svenar.powerranks.handlers.BaseConfig;

public class LangConfig extends BaseConfig {

    private final String language;

    public LangConfig(final PowerRanks plugin, final String lang) {
        super(plugin, "language");
        this.language = lang;
    }

    public final String getNode(final String node) {
        final String s = getConfig().getString("lang." + language + "." + node);
        if (s == null) {
            return "lang." + language + "." + node;
        }
        return s.replace("&", String.valueOf(ChatColor.COLOR_CHAR));
    }

    public final List<String> getNodeList(final String node) {
        return getConfig().getStringList("lang." + language + "." + node).parallelStream()
                .map(s -> s.replace("&", String.valueOf(ChatColor.COLOR_CHAR))).collect(Collectors.toList());
    }

}
