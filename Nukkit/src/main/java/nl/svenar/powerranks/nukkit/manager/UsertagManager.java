package nl.svenar.powerranks.nukkit.manager;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import cn.nukkit.Player;
import nl.svenar.powerranks.common.structure.PRPlayer;
import nl.svenar.powerranks.common.utils.PRCache;
import nl.svenar.powerranks.nukkit.PowerRanks;

public class UsertagManager {

    private PowerRanks plugin;
    public UsertagManager(PowerRanks plugin) {
        this.plugin = plugin;
    }
    
    @SuppressWarnings("unchecked")
    public boolean createUsertag(String tag, String format) {

        Map<String, String> availableUsertags = (Map<String, String>) plugin.getUsertagStorage().getMap("usertags", new HashMap<String, String>());

        boolean exists = false;
        for (Entry<?, ?> entry : availableUsertags.entrySet()) {
            if (entry.getKey().toString().equalsIgnoreCase(tag)) {
                exists = true;
            }
        }

        if (exists) {
            return false;
        }

        availableUsertags.put(tag, format);

        plugin.getUsertagStorage().setMap("usertags", availableUsertags);

        return true;
    }

    @SuppressWarnings("unchecked")
    public boolean editUsertag(String tag, String format) {
        Map<String, String> availableUsertags = (Map<String, String>) plugin.getUsertagStorage().getMap("usertags", new HashMap<String, String>());

        String key = "";
        for (Entry<?, ?> entry : availableUsertags.entrySet()) {
            if (entry.getKey().toString().equalsIgnoreCase(tag)) {
                key = entry.getKey().toString();
            }
        }

        if (key.length() == 0) {
            return false;
        }

        availableUsertags.put(key, format);

        plugin.getUsertagStorage().setMap("usertags", availableUsertags);

        return true;
    }

    @SuppressWarnings("unchecked")
    public boolean removeUsertag(String tag) {
        Map<String, String> availableUsertags = (Map<String, String>) plugin.getUsertagStorage().getMap("usertags",
                new HashMap<String, String>());

        String key = "";
        for (Entry<?, ?> entry : availableUsertags.entrySet()) {
            if (entry.getKey().toString().equalsIgnoreCase(tag)) {
                key = entry.getKey().toString();
            }
        }

        if (key.length() == 0) {
            return false;
        }

        availableUsertags.remove(key);

        plugin.getUsertagStorage().setMap("usertags", availableUsertags);

        return true;
    }

    @SuppressWarnings("unchecked")
    public boolean setUsertag(Player player, String tag) {
        Map<String, String> availableUsertags = (Map<String, String>) plugin.getUsertagStorage().getMap("usertags",
                new HashMap<String, String>());

        PRPlayer targetPlayer = PRCache.getPlayer(player.getUniqueId().toString());
        if (targetPlayer == null) {
            return false;
        }

        String key = "";
        for (Entry<?, ?> entry : availableUsertags.entrySet()) {
            if (entry.getKey().toString().equalsIgnoreCase(tag)) {
                key = entry.getKey().toString();
            }
        }

        if (key.length() == 0) {
            return false;
        }

        targetPlayer.setUsertag(key);

        return true;
    }

    @SuppressWarnings("unchecked")
    public boolean addUsertag(Player player, String tag) {
        Map<String, String> availableUsertags = (Map<String, String>) plugin.getUsertagStorage().getMap("usertags",
                new HashMap<String, String>());

        PRPlayer targetPlayer = PRCache.getPlayer(player.getUniqueId().toString());
        if (targetPlayer == null) {
            return false;
        }

        String key = "";
        for (Entry<?, ?> entry : availableUsertags.entrySet()) {
            if (entry.getKey().toString().equalsIgnoreCase(tag)) {
                key = entry.getKey().toString();
            }
        }

        if (key.length() == 0) {
            return false;
        }

        targetPlayer.addUsertag(key);

        return true;
    }

    @SuppressWarnings("unchecked")
    public boolean delUsertag(Player player, String tag) {
        Map<String, String> availableUsertags = (Map<String, String>) plugin.getUsertagStorage().getMap("usertags",
                new HashMap<String, String>());

        PRPlayer targetPlayer = PRCache.getPlayer(player.getUniqueId().toString());
        if (targetPlayer == null) {
            return false;
        }

        String key = "";
        for (Entry<?, ?> entry : availableUsertags.entrySet()) {
            if (entry.getKey().toString().equalsIgnoreCase(tag)) {
                key = entry.getKey().toString();
            }
        }

        if (key.length() == 0) {
            return false;
        }

        targetPlayer.removeUsertag(key);

        return true;
    }

    public boolean setUsertag(String playername, String tag) {
        Player player = plugin.getServer().getPlayer(playername);
        return setUsertag(player, tag);
    }

    public boolean addUsertag(String playername, String tag) {
        Player player = plugin.getServer().getPlayer(playername);
        return addUsertag(player, tag);
    }

    public boolean delUsertag(String playername, String tag) {
        Player player = plugin.getServer().getPlayer(playername);
        return delUsertag(player, tag);
    }

    @SuppressWarnings("unchecked")
    public Set<String> getUsertags() {
        Set<String> tags = new HashSet<String>();

        Map<String, String> availableUsertags = (Map<String, String>) plugin.getUsertagStorage().getMap("usertags",
                new HashMap<String, String>());

        for (Entry<String, String> entry : availableUsertags.entrySet()) {
            tags.add(entry.getKey());
        }

        return tags;
    }

    @SuppressWarnings("unchecked")
    public String getUsertagValue(String tag) {

        Map<String, String> availableUsertags = (Map<String, String>) plugin.getUsertagStorage().getMap("usertags",
                new HashMap<String, String>());

        String key = "";
        for (Entry<?, ?> entry : availableUsertags.entrySet()) {
            if (entry.getKey().toString().equalsIgnoreCase(tag)) {
                key = entry.getValue().toString();
            }
        }

        if (key.length() == 0) {
            return "";
        }

        return key;
    }

    public String getUsertagValue(Player player) {
        PRPlayer targetPlayer = PRCache.getPlayer(player.getUniqueId().toString());
        if (targetPlayer == null) {
            return "";
        }

        if (targetPlayer.getUsertags().size() == 0) {
            return "";
        }

        String usertag = targetPlayer.getUsertags().iterator().next();
        if (usertag.length() > 0) {
            return getUsertagValue(usertag);
        }

        return "";
    }

    public boolean clearUsertag(String playername) {
        PRPlayer targetPlayer = PRCache.getPlayer(playername);
        if (targetPlayer == null) {
            targetPlayer = PRCache.getPlayer(plugin.getServer().getPlayer(playername).getUniqueId().toString());
        }
        if (targetPlayer == null) {
            return false;
        }

        targetPlayer.setUsertags(new HashSet<String>());

        return true;
    }
}
