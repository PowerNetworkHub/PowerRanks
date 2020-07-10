package nl.svenar.PowerRanks.addons;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.bukkit.Color;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import nl.svenar.PowerRanks.PowerRanks;

public class PowerRanksConfig {

	private String filename = "";
	private File configFile;
	
	public PowerRanksConfig(String configName) {
		String filedir = PowerRanks.configFileLoc + File.separator + "Addons";
		filename = filedir + File.separator + configName + ".yml";
		final File configDir = new File(filedir);
		configFile = new File(filename);
		if (!configDir.exists()) {
			configDir.mkdirs();
		}
		
		try {
			configFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private final YamlConfiguration loadConfigFile() {
		if (filename.length() > 0) {
			YamlConfiguration yamlconf = new YamlConfiguration();
			try {
				yamlconf.load(configFile);
			} catch (IOException | InvalidConfigurationException e) {
				return null;
			}
			return yamlconf;
		} else {
			return null;
		}
	}
	
	private final void saveConfigFile(YamlConfiguration yamlconf) {
		if (yamlconf != null) {
			try {
				yamlconf.save(configFile);
			} catch (IOException e) {
			}
		}
	}
	
	public void set(String field, Object value) {
		YamlConfiguration config = loadConfigFile();
		if (config != null) {
			config.set(field, value);
		}
		
		saveConfigFile(config);
	}
	
	public boolean isSet(String field) {
		YamlConfiguration config = loadConfigFile();
		return config != null ? config.isSet(field) : null;
	}
	
	public Object get(String field) {
		YamlConfiguration config = loadConfigFile();
		return config != null ? config.get(field) : null;
	}
	
	public String getString(String field) {
		YamlConfiguration config = loadConfigFile();
		return config != null ? config.getString(field) : null;
	}
	
	public int getInt(String field) {
		YamlConfiguration config = loadConfigFile();
		return config != null ? config.getInt(field) : null;
	}
	
	public boolean getBoolean(String field) {
		YamlConfiguration config = loadConfigFile();
		return config != null ? config.getBoolean(field) : null;
	}
	
	public double getDouble(String field) {
		YamlConfiguration config = loadConfigFile();
		return config != null ? config.getDouble(field) : null;
	}
	
	public List<String> getStringList(String field) {
		YamlConfiguration config = loadConfigFile();
		return config != null ? config.getStringList(field) : null;
	}
	
	public List<Character> getCharacterList(String field) {
		YamlConfiguration config = loadConfigFile();
		return config != null ? config.getCharacterList(field) : null;
	}
	
	public List<Byte> getByteList(String field) {
		YamlConfiguration config = loadConfigFile();
		return config != null ? config.getByteList(field) : null;
	}
	
	public Color getColor(String field) {
		YamlConfiguration config = loadConfigFile();
		return config != null ? config.getColor(field) : null;
	}
	
	public ConfigurationSection getConfigurationSection(String field) {
		YamlConfiguration config = loadConfigFile();
		return config != null ? config.getConfigurationSection(field) : null;
	}
	
	public List<Float> getFloatList(String field) {
		YamlConfiguration config = loadConfigFile();
		return config != null ? config.getFloatList(field) : null;
	}
	
	public boolean isString(String field) {
		YamlConfiguration config = loadConfigFile();
		return config != null ? config.isString(field) : null;
	}
	
	public boolean isBoolean(String field) {
		YamlConfiguration config = loadConfigFile();
		return config != null ? config.isBoolean(field) : null;
	}
	
	public boolean isConfigurationSection(String field) {
		YamlConfiguration config = loadConfigFile();
		return config != null ? config.isConfigurationSection(field) : null;
	}
	
	public boolean isColor(String field) {
		YamlConfiguration config = loadConfigFile();
		return config != null ? config.isColor(field) : null;
	}
	
	public boolean isDouble(String field) {
		YamlConfiguration config = loadConfigFile();
		return config != null ? config.isDouble(field) : null;
	}
	
	public boolean isInt(String field) {
		YamlConfiguration config = loadConfigFile();
		return config != null ? config.isInt(field) : null;
	}
	
	public boolean isList(String field) {
		YamlConfiguration config = loadConfigFile();
		return config != null ? config.isList(field) : null;
	}
	
	public boolean isLocation(String field) {
		YamlConfiguration config = loadConfigFile();
		return config != null ? config.isLocation(field) : null;
	}
	
	public boolean isLong(String field) {
		YamlConfiguration config = loadConfigFile();
		return config != null ? config.isLong(field) : null;
	}
}
