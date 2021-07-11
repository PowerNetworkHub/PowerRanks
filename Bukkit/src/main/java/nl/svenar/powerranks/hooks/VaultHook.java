package nl.svenar.powerranks.hooks;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import nl.svenar.powerranks.PowerRanks;

public class VaultHook {

    private static Economy vaultEconomy;
    private static Permission vaultPermissions;

    public VaultHook() {
    }

    public void hook(PowerRanks plugin, boolean setupPermissions) {
        if (setupPermissions) {
            Plugin vault = Bukkit.getPluginManager().getPlugin("Vault");
            PowerRanksVaultPermission vaultPermsHook = new PowerRanksVaultPermission(plugin);
            Bukkit.getServicesManager().register(Permission.class, vaultPermsHook, vault, ServicePriority.High);
        }
    }

    public static Economy getVaultEconomy() {
        return vaultEconomy;
    }

    public static Permission getVaultPermissions() {
        return vaultPermissions;
    }
}
