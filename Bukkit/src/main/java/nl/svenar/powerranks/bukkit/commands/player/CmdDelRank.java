package nl.svenar.powerranks.bukkit.commands.player;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.common.collect.ImmutableMap;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;
import nl.svenar.powerranks.bukkit.PowerRanks;
import nl.svenar.powerranks.bukkit.commands.PowerBaseCommand;
import nl.svenar.powerranks.bukkit.events.prevents.RankChangeEvent;
import nl.svenar.powerranks.bukkit.util.Util;
import nl.svenar.powerranks.common.structure.PRPlayer;
import nl.svenar.powerranks.common.structure.PRPlayerRank;
import nl.svenar.powerranks.common.structure.PRRank;

@CommandAlias("%powerrankscommand")
@Description("Add a rank to a player while keeping their other ranks")
public class CmdDelRank extends PowerBaseCommand {

    public CmdDelRank(PowerRanks plugin) {
        super(plugin);
    }

    @Subcommand("delrank")
    @CommandPermission("powerranks.cmd.delrank")
    @Syntax("<player> <rank> [tags]")
    @CommandCompletion("@prplayers @prranks")
    public void onDelrank(CommandSender sender, String targetName, String rankname) {
        PRPlayer prplayer = getPRPlayer(targetName);
        PRRank prrank = getPRRank(rankname);

        if (prplayer == null) {
            sendMessage(sender, "player-not-found", ImmutableMap.of( //
                    "player", targetName //
            ), true);
            return;
        }

        if (prrank == null) {
            sendMessage(sender, "rank-not-found", ImmutableMap.of( //
                    "rank", rankname //
            ), true);
            return;
        }

        PRPlayerRank targetRank = null;
        for (PRPlayerRank prplayerrank : prplayer.getRanks()) {
            if (prplayerrank.getName().equalsIgnoreCase(prrank.getName())) {
                targetRank = prplayerrank;
                break;
            }
        }

        if (targetRank == null) {
            sendMessage(sender, "player-does-not-have-rank", ImmutableMap.of( //
                    "player", targetName,
                    "rank", prrank.getName() //
            ), true);
        }

        RankChangeEvent event = new RankChangeEvent(prplayer, prrank, null);
        plugin.getServer().getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            sendMessage(sender, "cancelled-by-event", true);
            return;
        }

        prplayer.getRanks().remove(targetRank);

        sendMessage(sender, "player-rank-remove-success-sender", ImmutableMap.of( //
                "player", targetName,
                "rank", prrank.getName() //
        ), true);

        Player target = Util.getPlayerByName(targetName);
        if (target != null) {
            sendMessage(target, "player-rank-remove-success-receiver", ImmutableMap.of( //
                    "player", sender.getName(),
                    "rank", prrank.getName() //
            ), true);
        }
    }
}