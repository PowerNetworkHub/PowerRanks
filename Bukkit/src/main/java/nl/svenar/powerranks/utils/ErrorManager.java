package nl.svenar.powerranks.utils;

import nl.svenar.powerranks.PowerRanks;

public class ErrorManager {
    
    public static void logWarning(String message) {
        PowerRanks.getInstance().getLogger().warning("===--------------------===");
        PowerRanks.getInstance().getLogger().warning("A non-critical error has occured:");
        PowerRanks.getInstance().getLogger().warning(message);
        PowerRanks.getInstance().getLogger().warning("===--------------------===");
    }

    public static void logError(String message) {
        PowerRanks.getInstance().getLogger().severe("===--------------------===");
        PowerRanks.getInstance().getLogger().severe("A critical error has occured:");
        PowerRanks.getInstance().getLogger().severe(message);
        PowerRanks.getInstance().getLogger().severe("===--------------------===");
    }
}
