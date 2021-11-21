package nl.svenar.PowerRanks;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

public class VaultHook {
	
	private static Economy vaultEconomy;
	private static Permission vaultPermissions;
	private static Chat vaultChat;

	public VaultHook() {

    }

    public void hook(PowerRanks plugin, boolean setupPermissions, boolean setupEconomy) {
    	if (setupPermissions) {
	        Plugin vault = Bukkit.getPluginManager().getPlugin("Vault");
	        PowerRanksVaultPermission vaultPermsHook = new PowerRanksVaultPermission(plugin);
	        Bukkit.getServicesManager().register(Permission.class, vaultPermsHook, vault, ServicePriority.High);

			PowerRanksVaultChat vaultChatHook = new PowerRanksVaultChat(plugin, vaultPermsHook);
			Bukkit.getServicesManager().register(Chat.class, vaultChatHook, vault, ServicePriority.High);

			vaultPermissions = vaultPermsHook;
			vaultChat = vaultChatHook;
    	}
        
    	if (setupEconomy) {
	        try {
				RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
				vaultEconomy = rsp.getProvider();
			} catch (Exception e) {
				PowerRanks.log.warning("Failed to load Vault Economy! Is an economy plugin present?");
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
