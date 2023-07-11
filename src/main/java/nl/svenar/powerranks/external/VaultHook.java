package nl.svenar.powerranks.external;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import nl.svenar.powerranks.PowerRanks;

public class VaultHook {

	private static Economy vaultEconomy;
	private static Permission vaultPermissions;
	private static Chat vaultChat;

	public VaultHook() {

	}

	public void hook(PowerRanks plugin, boolean setupPermissions, boolean setupExperimentalPermissions,
			boolean setupEconomy) {
		if (setupPermissions) {
			Plugin vault = Bukkit.getPluginManager().getPlugin("Vault");
			PowerRanksVaultPermission vaultPermsHook = new PowerRanksVaultPermission(plugin);
			Bukkit.getServicesManager().register(Permission.class, vaultPermsHook, vault, ServicePriority.High);
			vaultPermissions = vaultPermsHook;

			if (setupExperimentalPermissions) {
				PowerRanksVaultChat vaultChatHook = new PowerRanksVaultChat(plugin, vaultPermsHook);
				Bukkit.getServicesManager().register(Chat.class, vaultChatHook, vault, ServicePriority.High);
				vaultChat = vaultChatHook;
			}
		}

		if (setupEconomy) {
			try {
				RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager()
						.getRegistration(Economy.class);
				vaultEconomy = rsp.getProvider();

				PowerRanks.log.info("Vault compatible economy plugin found! (" + vaultEconomy.getName() + ")");
			} catch (Exception e) {
			}
		}
	}

	public static Economy getVaultEconomy() {
		return vaultEconomy;
	}

	public static Permission getVaultPermissions() {
		return vaultPermissions;
	}

	public static Chat getVaultChat() {
		return vaultChat;
	}
}
