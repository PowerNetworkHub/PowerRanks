package nl.svenar.PowerRanks.Events;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Util;
import nl.svenar.PowerRanks.Data.Messages;
import nl.svenar.PowerRanks.Data.PowerRanksGUI;
import nl.svenar.PowerRanks.Data.Users;

public class OnInteract implements Listener {

	PowerRanks m;

	public OnInteract(PowerRanks m) {
		this.m = m;
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			Player player = event.getPlayer();
			Block block = event.getClickedBlock();
			if (block.getState() instanceof Sign) {
				Sign sign = (Sign) block.getState();
				if (Util.isPowerRanksSign(this.m, sign)) {
					handlePowerRanksSign(sign, player);
				}
			}
		}
	}

	private void handlePowerRanksSign(Sign sign, Player player) {
		final Users s = new Users(this.m);
		String sign_command = sign.getLine(1);
		String sign_argument = sign.getLine(2);
		boolean sign_error = sign.getLine(3).toLowerCase().contains("error");

		if (!sign_error) {
			if (sign_command.equalsIgnoreCase("promote")) {
				if (player.hasPermission("powerranks.cmd.set")) {
					if (s.promote(player.getName()))
						Messages.messageCommandPromoteSuccess(player, player.getName());
					else
						Messages.messageCommandPromoteError(player, player.getName());
				} else {
					Messages.noPermission(player);
				}
			} else if (sign_command.equalsIgnoreCase("demote")) {
				if (player.hasPermission("powerranks.cmd.set")) {
					if (s.demote(player.getName()))
						Messages.messageCommandDemoteSuccess(player, player.getName());
					else
						Messages.messageCommandDemoteError(player, player.getName());
				} else {
					Messages.noPermission(player);
				}
			} else if (sign_command.equalsIgnoreCase("set")) {
				if (player.hasPermission("powerranks.cmd.set")) {
					s.setGroup(player, s.getRankIgnoreCase(sign_argument));
				} else {
					Messages.noPermission(player);
				}
			} else if (sign_command.equalsIgnoreCase("check")) {
				s.getGroup(player.getName(), player.getName());
			} else if (sign_command.equalsIgnoreCase("gui")) {
				if (player.hasPermission("powerranks.cmd.admin")) {
					PowerRanksGUI.openPowerRanksGUI(player, PowerRanksGUI.MAIN_GUI_PAGE.MAIN, 0, "");
				} else {
					Messages.noPermission(player);
				}
			} else {
				if (player.hasPermission("powerranks.cmd.admin")) {
					Messages.messageSignUnknownCommand(player);
				}
			}
		}
	}
}