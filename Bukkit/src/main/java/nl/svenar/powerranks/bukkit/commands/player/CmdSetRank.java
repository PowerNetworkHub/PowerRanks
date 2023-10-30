package nl.svenar.powerranks.bukkit.commands.player;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.common.collect.ImmutableMap;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Optional;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;
import nl.svenar.powerranks.bukkit.PowerRanks;
import nl.svenar.powerranks.bukkit.commands.PowerBaseCommand;
import nl.svenar.powerranks.bukkit.util.Util;
import nl.svenar.powerranks.common.structure.PRPlayer;
import nl.svenar.powerranks.common.structure.PRPlayerRank;
import nl.svenar.powerranks.common.structure.PRRank;

@CommandAlias("%powerrankscommand")
@Description("Set a player's rank and remove all other ranks")
public class CmdSetRank extends PowerBaseCommand {

    public CmdSetRank(PowerRanks plugin) {
        super(plugin);
    }

    @Subcommand("setrank")
    @CommandPermission("powerranks.cmd.setrank")
    @Syntax("<player> <rank> [tags]")
    @CommandCompletion("@players @ranks expires:|expires:1h|expires:30d|expires:1y|world:|world:world|world:world_nether|world:world_the_end")
    public void onSetrank(CommandSender sender, String targetName, String rankname, @Optional String... tags) {
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

        prplayer.getRanks().clear();
        prplayer.getRanks().add(new PRPlayerRank(prrank, tags));

        sendMessage(sender, "commands.setrank.success-executor", ImmutableMap.of( //
                "player", targetName,
                "rank", prrank.getName() //
        ));

        Player target = Util.getPlayerByName(targetName);
        if (target != null) {
            sendMessage(target, "commands.setrank.success-receiver", ImmutableMap.of( //
                "player", sender.getName(),
                "rank", prrank.getName() //
        ));
        }
    }
}