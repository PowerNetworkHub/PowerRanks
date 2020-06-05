package nl.svenar.PowerRanks.Events;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Util;
import nl.svenar.PowerRanks.Data.Messages;
import nl.svenar.PowerRanks.Data.Users;

public class OnSignChanged implements Listener {

	PowerRanks m;

	public OnSignChanged(PowerRanks m) {
		this.m = m;
	}

	@EventHandler
	public void onSignChange(SignChangeEvent event) {
		if (Util.isPowerRanksSign(this.m, event.getLine(0))) {
			if (event.getPlayer().hasPermission("powerranks.cmd.admin")) {
				final File configFile = new File(String.valueOf(this.m.configFileLoc) + "config" + ".yml");
				final YamlConfiguration configYaml = new YamlConfiguration();
				try {
					configYaml.load(configFile);
				} catch (IOException | InvalidConfigurationException e) {
					e.printStackTrace();
				}

				event.setLine(0, PowerRanks.chatColor(PowerRanks.colorChar.charAt(0), Util.replaceAll(configYaml.getString("signs.title_format"), "%plugin_name%", PowerRanks.pdf.getName()), true));

				final Users s = new Users(this.m);
				String sign_command = event.getLine(1);
				String sign_argument = event.getLine(2);

				if (sign_command.equalsIgnoreCase("promote") || sign_command.equalsIgnoreCase("demote") || sign_command.equalsIgnoreCase("check") || sign_command.equalsIgnoreCase("gui")) {
					Messages.messageSignCreated(event.getPlayer());
				} else if (sign_command.equalsIgnoreCase("set")) {
					Set<String> ranks = s.getGroups();
					boolean rank_exists = false;
					for (String rank : ranks) {
						if (rank.equalsIgnoreCase(sign_argument)) {
							rank_exists = true;
							break;
						}
					}
					if (!rank_exists) {
						Messages.messageGroupNotFound(event.getPlayer(), sign_argument);
						event.setLine(3, PowerRanks.chatColor(PowerRanks.colorChar.charAt(0), "&4Error", true));
					} else {
						Messages.messageSignCreated(event.getPlayer());
					}
				} else {
					Messages.messageSignUnknownCommand(event.getPlayer());
					event.setLine(3, PowerRanks.chatColor(PowerRanks.colorChar.charAt(0), "&4Error", true));
				}
			} else {
				event.setLine(0, "");
			}
		}
	}
}
