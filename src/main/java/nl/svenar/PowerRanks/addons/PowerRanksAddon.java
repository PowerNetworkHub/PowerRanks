package nl.svenar.PowerRanks.addons;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Events.ChatTabExecutor;
import nl.svenar.common.storage.PowerStorageManager;

public abstract class PowerRanksAddon {

	private PowerRanksConfig powerranksConfig = null;
	private ArrayList<String> registeredCommands = new ArrayList<String>();
	private ArrayList<String> registeredPermissions = new ArrayList<String>();
	private List<String> storageManagers = new ArrayList<String>();

	public enum RankChangeCause {
		SET, PROMOTE, DEMOTE
	}

	public enum BlockChangeCause {
		BREAK, // Called when a block is broken by a player.
		PLACE, // Called when a block is placed by a player.

		MOISTURE, // Called when the moisture level of a soil block changes.
		FERTILIZE, // Called with the block changes resulting from a player fertilizing a given
					// block with bonemeal.
		GROW, // Called when a block grows naturally in the world.

		IGNITE, // Called when a block is ignited.
		EXPLODE, // Called when a block explodes
		BURN, // Called when a block is destroyed as a result of being burnt by fire.

		REDSTONE // Called when a redstone current changes
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
	public void setup(PowerRanks powerranks) {
	}

	// This function is called once on add-on load
	public void setup() {
	}

	// This function is called once on add-on unload
	public void unload() {
	}

	// Called when a player joins the server
	public void onPlayerJoin(PowerRanksPlayer prPlayer) {
	}

	// Called when a player joins the server
	// Used for custom join messages
	// Return 'false' to cancel the default join message
	public boolean onPlayerJoinMessage(PowerRanksPlayer prPlayer) {
		return true;
	}

	// Called when a player leaves the server
	public void onPlayerLeave(PowerRanksPlayer prPlayer) {
	}

	// Player movement handler
	// Executed when a player has moved
	// return true to cancel the event
	public boolean onPlayerMove(PowerRanksPlayer prPlayer, Location from, Location to) {
		return false;
	}

	// Called when a player's rank changes
	public void onPlayerRankChange(PowerRanksPlayer prPlayer, String oldRank, String newRank, RankChangeCause cause,
			boolean isPlayerOnline) {
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
	public boolean onPowerRanksCommand(PowerRanksPlayer prPlayer, boolean sendAsPlayer, String command,
			String[] arguments) {
		return false;
	}

	// Player world change handler
	// Executed when a player has entered a different world
	public void onPlayerWorldChange(PowerRanksPlayer prPlayer, World world, World world2) {
	}

	// Block change handler
	// prPlayer.getPlayer() may be null for events not caused by a player
	// Executed when a block changes
	// return true to cancel the event
	public boolean onBlockChange(PowerRanksPlayer prPlayer, Block block, BlockChangeCause blockChangeCause) {
		return false;
	}

	// Add a storage manager that PowerRanks users can use to store data with
	// Argument: name, the name of the storage manager (used in config.yml >
	// storage.type)
	// Argument: storageManager, the storage manager class
	public void registerStorageManager(String name) {
		this.storageManagers.add(name);
	}

	// Get all registered storage engines in this add-on
	// return a list of available storage engines
	public List<String> getStorageManagerNames() {
		return this.storageManagers;
	}

	public PowerStorageManager getStorageManager(String name) {
		return null;
	}

	public void setupStorageManager(String name) {
	}
}
