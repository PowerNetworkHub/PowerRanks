package nl.svenar.powerranks.bukkit.commands.base;

import org.bukkit.command.CommandSender;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import nl.svenar.powerranks.bukkit.PowerRanks;
import nl.svenar.powerranks.bukkit.commands.PowerBaseCommand;

@CommandAlias("%powerrankscommand")
@Description("Show stats about PowerRanks")
public class CmdStats extends PowerBaseCommand {

    public CmdStats(PowerRanks plugin) {
        super(plugin);
    }
    
    @Subcommand("stats")
    @CommandPermission("powerranks.cmd.stats")
    public void onStats(CommandSender sender) {
        sender.sendMessage("Hello stats!");
    }
}