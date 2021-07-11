package nl.svenar.powerranks;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.Plugin;

public class PowerRanks extends Plugin {

    @Override
    public void onEnable() {
        Instant startTime = Instant.now();

        List<String> logoLines = new ArrayList<String>();
        logoLines.add("  ██████  ██████ ");
        logoLines.add("  ██   ██ ██   ██");
        logoLines.add("  ██████  ██████ ");
        logoLines.add("  ██      ██   ██");
        logoLines.add("  ██      ██   ██");
        Iterator<String> logoLinesIterator = logoLines.iterator();

        // String preload_message = "Loaded " + BaseDataHandler.getRanks().size() + " ranks and "
        //         + BaseDataHandler.getPlayers().size() + " players";

        getLogger().info("");
        getLogger().info(ChatColor.AQUA + logoLinesIterator.next() + ChatColor.GREEN + "  " + getDescription().getName()
                + " v" + getDescription().getVersion());
        getLogger().info(ChatColor.AQUA + logoLinesIterator.next() + ChatColor.GREEN + "");
        getLogger().info(ChatColor.AQUA + logoLinesIterator.next() + ChatColor.GREEN + "  Startup time: "
                + Duration.between(startTime, Instant.now()).toMillis() + "ms");
        getLogger().info(ChatColor.AQUA + logoLinesIterator.next() + ChatColor.GREEN + "");
        getLogger().info(ChatColor.AQUA + logoLinesIterator.next() + ChatColor.RED + "");
        getLogger().info("");
        // log = ProxyServer.getInstance().getLogger();
        // pdf = this.getDescription();

        // getProxy().getPluginManager().registerCommand(this, new
        // Commands("powerranks"));
        // getProxy().getPluginManager().registerCommand(this, new Commands("pr"));
        // getProxy().getPluginManager().registerListener(this, new onJoin());

        // setup_config();
        // new PowerRanksConfiguration(getDataFolder());
        // new PowerRanksExceptionsHandler(getDataFolder());
    }
}
