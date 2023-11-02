package nl.svenar.powerranks.nukkit.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.nukkit.Player;
import cn.nukkit.Server;
import nl.svenar.powerranks.nukkit.PowerRanks;

public class Util {
    
    public static String DATA_DIR = "";

    public static String getServerVersion(Server server) {
        try {
            Matcher matcher = Pattern.compile("\\d{1,3}.\\d{1,3}.\\d{1,3}|\\d{1,3}.\\d{1,3}")
                    .matcher(server.getVersion());

            List<String> results = new ArrayList<String>();
            while (matcher.find()) {
                if (matcher.groupCount() > 0) {
                    results.add(matcher.group(1));
                } else {
                    results.add(matcher.group());
                }
            }

            return results.get(0);
        } catch (Exception e) {
            return server.getVersion();
        }
    }

    public static String getServerType(Server server) {
        try {
            Matcher matcher = Pattern.compile("-\\w{1,32}-").matcher(server.getVersion());

            List<String> results = new ArrayList<String>();
            while (matcher.find()) {
                if (matcher.groupCount() > 0) {
                    results.add(matcher.group(1));
                } else {
                    results.add(matcher.group());
                }
            }

            return server.getName() + " " + results.get(0);
        } catch (Exception e) {
            return server.getName() + " " + server.getNukkitVersion();
        }
    }

    public static Player getPlayerByName(String target_player_name) {
        Player target_player = null;
        for (Player online_player : PowerRanks.getInstance().getServer().getOnlinePlayers().values()) {
            if (online_player.getName().equalsIgnoreCase(target_player_name)) {
                target_player = online_player;
                break;
            }
        }
        return target_player;
    }
}
