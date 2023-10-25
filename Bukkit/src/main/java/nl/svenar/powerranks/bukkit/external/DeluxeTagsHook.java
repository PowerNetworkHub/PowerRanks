package nl.svenar.powerranks.bukkit.external;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.clip.deluxetags.DeluxeTags;
import me.clip.deluxetags.tags.DeluxeTag;
import nl.svenar.powerranks.bukkit.PowerRanks;

public class DeluxeTagsHook {

    public String getPlayerDisplayTag(Player player) {
        String formattedTags = "";

        DeluxeTags plugin = (DeluxeTags) Bukkit.getPluginManager().getPlugin("DeluxeTags");
        if (plugin == null || !plugin.isEnabled()) {
            return "";
        }

        List<String> usertags = DeluxeTag.getAllVisibleTagIdentifiers(player);
        if (usertags == null) {
            return "";
        }

        for (DeluxeTag tag : DeluxeTag.getLoadedTags()) {
            if (usertags.contains(tag.getIdentifier())) {
                formattedTags += tag.getDisplayTag() + " ";
            }
        }

        if (formattedTags.endsWith(" ")) {
            formattedTags = formattedTags.substring(0, formattedTags.length() - 1);
        }

        PowerRanks.getInstance().getLogger().info(String.join(" ", usertags));
        // return "";
        return formattedTags;
    }
}
