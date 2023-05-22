package nl.svenar.powerranks.external;

import java.util.List;

import me.neznamy.tab.api.TabAPI;
import me.neznamy.tab.api.TabFeature;
import me.neznamy.tab.api.TabPlayer;
import me.neznamy.tab.api.team.TeamManager;
import nl.svenar.common.structure.PRRank;
import nl.svenar.powerranks.cache.CacheManager;

public class PRTABFeature extends TabFeature {

    protected PRTABFeature(String featureName, String refreshDisplayName) {
        super(featureName, refreshDisplayName);
    }

    protected PRTABFeature(String featureName, String refreshDisplayName, List<String> disabledServers,
            List<String> disabledWorlds) {
        super(featureName, refreshDisplayName, disabledServers, disabledWorlds);
    }

    /**
     * Processes join event
     * 
     * @param connectedPlayer - player who connected
     */
    @Override
    public void onJoin(TabPlayer connectedPlayer) {
        // PowerRanks.getInstance().getLogger().warning("PRTABFeature: onJoin() called");
        TeamManager teamManager = TabAPI.getInstance().getTeamManager();
        // teamManager.pauseTeamHandling(connectedPlayer);
        // teamManager.forceTeamName(connectedPlayer, "prtab" +
        // connectedPlayer.getName());

        List<PRRank> playerRanks = TABHook
                .getSortedRanks(CacheManager.getPlayer(connectedPlayer.getUniqueId().toString()));

        teamManager.setPrefix(connectedPlayer, playerRanks.get(0).getPrefix() + " ");
        teamManager.updateTeamData(connectedPlayer);

        TabAPI.getInstance().getTablistFormatManager().setPrefix(connectedPlayer, playerRanks.get(0).getPrefix() + " ");
    }

    /**
     * Performs refresh of specified player
     * 
     * @param refreshed - player to refresh
     * @param force     - if refresh should be forced despite refresh seemingly not
     *                  needed
     */
    @Override
    public void refresh(TabPlayer refreshed, boolean force) {
        // PowerRanks.getInstance().getLogger().warning("PRTABFeature: refresh() called");
    }
}
