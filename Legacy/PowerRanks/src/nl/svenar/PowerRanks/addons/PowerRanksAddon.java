package nl.svenar.PowerRanks.addons;

import java.util.ArrayList;

import org.bukkit.World;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Events.ChatTabExecutor;

public abstract class PowerRanksAddon {

	private PowerRanksConfig powerranksConfig = null;
	private ArrayList<String> registeredCommands = new ArrayList<String>();
	private ArrayList<String> registeredPermissions = new ArrayList<String>();

	public enum RankChangeCause {
		SET, PROMOTE, DEMOTE
	}

	// Create a configuration file with the same name as the add-on (identifier) if
	// it does not exist yet
	public final void setupConfigfile() {
		powerranksConfig = new PowerRanksConfig(getIdentifier());
	}

	// Get the current configuration file of this add-on
	// returns null if no configuration file is loaded yet using createConfigfile()
	public final PowerRanksConfig getConfig() {
		return powerranksConfig;
	}

	// Register custom commands for auto complete in the chat
	// Used for chat auto-complete and /pr addoninfo <addon_name>
	public final void registerCommandAutocomplete(String command) {
		ChatTabExecutor.addAddonCommand(command);
		if (!registeredCommands.contains(command.toLowerCase()))
			registeredCommands.add(command.toLowerCase());
	}

	// Register custom permissions for the add-on
	// Used for /pr addoninfo <addon_name>
	public final void registerPermission(String permission) {
		if (!registeredPermissions.contains(permission.toLowerCase()))
			registeredPermissions.add(permission.toLowerCase());
	}

	// Get the list of registered commands
	public final ArrayList<String> getRegisteredCommands() {
		return registeredCommands;
	}

	// Get the list of registered permissions
	public final ArrayList<String> getRegisteredPermissions() {
		return registeredPermissions;
	}

	// The author's name
	// ex. return "Your Name";
	public abstract String getAuthor();

	// The addon's name
	// ex. return "myAwesomeAddon";
	public abstract String getIdentifier();

	// The addon's version
	// ex. return "1.0";
	public abstract String getVersion();

	// The minimal requires PowerRanks version
	// ex. return "1.0";
	public abstract String minimalPowerRanksVersion();

	// This function is called once on add-on load
	public void setup(PowerRanks powerranks) {}

	// This function is called once on add-on load
	public void setup() {}

	// Called when a player joins the server
	public void onPlayerJoin(PowerRanksPlayer prPlayer) {
	}

	// Called when a player leaves the server
	public void onPlayerLeave(PowerRanksPlayer prPlayer) {
	}

	// Player movement handler
	// Executed when a player has moved
	// return true to cancel the event
	public boolean onPlayerMove(PowerRanksPlayer prPlayer) {
		return false;
	}

	// Called when a player's rank changes
	public void onPlayerRankChange(PowerRanksPlayer prPlayer, String oldRank, String newRank, RankChangeCause cause, boolean isPlayerOnline) {
	}

	// Chat handler
	// The chat message can be altered here
	// has the current chat format as argument, and it must be returned again
	public String onPlayerChat(PowerRanksPlayer prPlayer, String chatFormat, String message) {
		return chatFormat;
	}

	// Command handler
	// Executed when a default PowerRanks command is not found
	// return true after a custom command is handled, otherwise the unknown command
	// message will display, by default it should return false
	public boolean onPowerRanksCommand(PowerRanksPlayer prPlayer, boolean sendAsPlayer, String command, String[] arguments) {
		return false;
	}

	// Player world change handler
	// Executed when a player has entered a different world
	public void onPlayerWorldChange(PowerRanksPlayer prPlayer, World world, World world2) {
	}
}
