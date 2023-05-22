package nl.svenar.powerranks.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import nl.svenar.common.storage.PowerConfigManager;
import nl.svenar.common.storage.provided.YAMLConfigManager;
import nl.svenar.powerranks.PowerRanks;

public class LanguageManager {

    private PowerConfigManager languageManager;

    private String language = "en";

    /**
     * Constructor - Loads language data from the file system.
     */
    public LanguageManager() {
        this.languageManager = new YAMLConfigManager(PowerRanks.fileLoc, "lang.yml", "lang.yml");

        if (!this.languageManager.hasKey("lang")) {
            this.languageManager.destroyFile();
            this.languageManager = new YAMLConfigManager(PowerRanks.fileLoc, "lang.yml", "lang.yml");
        }
    }

    /**
     * Get a list of keys for the given path
     * 
     * @param path
     * @return List of found keys
     */
    public List<String> getKeys(String path) {
        path = "lang." + this.language + "." + path;

        List<String> keys = new ArrayList<String>();

        for (Entry<?, ?> entry : this.languageManager.getMap(path, new HashMap<String, String>()).entrySet()) {
            keys.add(String.valueOf(entry.getKey()));
        }

        return keys;
    }

    /**
     * Get a message from the language cache in a specific language.
     * 
     * @param path
     * @return Unformatted message with plugin prefix
     */
    public String getMessage(String path) {
        path = "lang." + this.language + "." + path;
        String output = this.languageManager.getString(path);
        output = output == null ? path : output;
        output = this.languageManager.getString("lang." + this.language + ".general.prefix") + " " + output;
        return output;
    }

    /**
     * Get a message from the language cache in a specific language.
     * 
     * @param path
     * @return Unformatted message
     */
    public String getUnformattedMessage(String path) {
        path = "lang." + this.language + "." + path;
        String output = this.languageManager.getString(path);
        output = output == null ? path : output;
        return output;
    }

    /**
     * Get a message from the language cache in a specific language and return the
     * chatcolor formatted message.
     * 
     * @param path
     * @return Chatcolor formatted message
     */
    public String getFormattedMessage(String path) {
        return PowerRanks.chatColor(getMessage(path), true);
    }

    public String getUsageMessage(String commandLabel, String commandName, String path, boolean isPlayer) {
        path = "lang." + this.language + "." + path;
        String output = this.languageManager.getString(path);
        output = output == null ? path : output;
        output = this.languageManager.getString("lang." + this.language + ".general.prefix") + " " + (isPlayer ? "/" : "") + commandLabel + " " + commandName + " " + output;
        return output;
    }

    public String getFormattedUsageMessage(String commandLabel, String commandName, String path, boolean isPlayer) {
        return PowerRanks.chatColor(getUsageMessage(commandLabel, commandName, path, isPlayer), true);
    }

    /**
     * Get the currently configured language.
     * 
     * @return current language
     */
    public String getLanguage() {
        return this.language;
    }

    /**
     * Change the language.
     * 
     * @param language
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * Save the language data to the files system
     */
    public void save() {
        this.languageManager.save();
    }
    /**
     * Reload the language data
     */
    public void reload() {
        this.languageManager.reload();
    }

    /**
     * Get the config file instance
     * 
     * @return PowerConfigManager for lang.yml
     */
    public PowerConfigManager getInstance() {
        return this.languageManager;
    }
}
