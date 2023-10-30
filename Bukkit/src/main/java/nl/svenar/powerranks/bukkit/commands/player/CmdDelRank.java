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
    @CommandCompletion("@players @ranks")
    public void onDelrank(CommandSender sender, String targetName, String rankname) {
        PRPlayer prplayer = getPRPlayer(targetName);
        PRRank prrank = getPRRank(rankname);

        if (prplayer == null) {
            sendMessage(sender, "general.player-not-found", ImmutableMap.of( //
                    "target", targetName //
            ));
            return;
        }

        if (prrank == null) {
            sendMessage(sender, "general.rank-not-found", ImmutableMap.of( //
                    "rank", rankname //
            ));
            return;
        }

        boolean hasRank = false;
        for (PRPlayerRank prplayerrank : prplayer.getRanks()) {
            if (prplayerrank.getName().equalsIgnoreCase(prrank.getName())) {
                hasRank = true;
                prplayer.getRanks().remove(prplayerrank);
                break;
            }
        }

        if (hasRank) {
            sendMessage(sender, "commands.delrank.success-executor", ImmutableMap.of( //
                    "player", targetName,
                    "rank", prrank.getName() //
            ));

            Player target = Util.getPlayerByName(targetName);
            if (target != null) {
                sendMessage(target, "commands.delrank.success-receiver", ImmutableMap.of( //
                        "player", sender.getName(),
                        "rank", prrank.getName() //
                ));
            }
        } else {
            sendMessage(sender, "commands.delrank.failed-does-not-have-rank", ImmutableMap.of( //
                    "player", targetName,
                    "rank", prrank.getName() //
            ));
        }
    }
}