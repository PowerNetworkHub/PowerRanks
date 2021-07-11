package nl.svenar.powerranks.configuration;

import nl.svenar.powerranks.PowerRanks;
import nl.svenar.powerranks.handlers.BaseConfig;

public class CoreConfig extends BaseConfig {
    
    public CoreConfig(PowerRanks plugin, String name) {
        super(plugin, name);
    }

    public final boolean isUsingYAML() {
        return getConfig().getString("storage.method") != null && getConfig().getString("storage.method").equalsIgnoreCase("yaml");
    }

    public final boolean isUsingSQLite() {
        return getConfig().getString("storage.method") != null && getConfig().getString("storage.method").equalsIgnoreCase("sqlite");
    }

    public final boolean isUsingMySQL() {
        return getConfig().getString("storage.method") != null && getConfig().getString("storage.method").equalsIgnoreCase("mysql");
    }

    public final String getSQLAdress() {
        return String.format("jdbc:mysql://%1$s:%2$s/%3$s", getConfig().getString("storage.mysql.host"), getConfig().getString("storage.mysql.port"), getConfig().getString("storage.mysql.database")) + "?autoReconnect=true&useSSL=false";
    }

    public final String getSQLUsername() {
        return getConfig().getString("storage.mysql.username");
    }

    public final String getSQLPassword() {
        return getConfig().getString("storage.mysql.password");
    }

    public final String getLanguage() {
        return getConfig().getString("language").toLowerCase();
    }

    public boolean doUpdateCheck() {
        return getConfig().getBoolean("check-for-updates");
    }

    public boolean pluginhookEnabled(String option) {
        return getConfig().getBoolean("pluginhooks." + option);
    }
}
