package nl.svenar.PowerRanks.Cache;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;

public class PowerConfigurationSection {

	private HashMap<String, Object> powerConfigurationSectionData = new HashMap<String, Object>();

	public PowerConfigurationSection() {
	}

	public PowerConfigurationSection(ConfigurationSection cs) {
		for (String path : cs.getKeys(true)) {
			powerConfigurationSectionData.put(path, cs.get(path));
		}
	}
	
	public Set<String> getKeys(boolean deep) {
		Set<String> output = new HashSet<String>();

		for (String key : powerConfigurationSectionData.keySet()) {
			if (!deep && !key.contains(".")) {
				output.add(key);
			}

			if (deep) {
				output.add(key);
			}
		}

		return output;
	}
	
	public void set(String path, Object value) {
		powerConfigurationSectionData.put(path, value);
	}

//	public Set<String> getKeys(String path, boolean deep) {
//		Set<String> output = new HashSet<String>();
//
//		for (String key : powerConfigurationSectionData.keySet()) {
//			if (!deep && key.contains(path)) {
//				output.add(key);
//			}
//
//			if (deep && key.equals(path)) {
//				output.add(key);
//			}
//		}
//
//		return output;
//	}
//	
//	public Map<String, Object> getValues(boolean deep) {
//		Map<String, Object> output = new HashMap<String, Object>();
//
//		for (String key : powerConfigurationSectionData.keySet()) {
//			if (!deep && !key.contains(".")) {
//				output.put(key, powerConfigurationSectionData.get(key));
//			}
//
//			if (deep) {
//				output.put(key, powerConfigurationSectionData.get(key));
//			}
//		}
//
//		return output;
//	}
//
//	public Map<String, Object> getValues(String path, boolean deep) {
//		Map<String, Object> output = new HashMap<String, Object>();
//
//		for (String key : powerConfigurationSectionData.keySet()) {
//			if (!deep && key.contains(path)) {
//				output.put(key, powerConfigurationSectionData.get(key));
//			}
//
//			if (deep && key.equals(path)) {
//				output.put(key, powerConfigurationSectionData.get(key));
//			}
//		}
//
//		return output;
//	}
//
//	public boolean contains(String path) {
//		return powerConfigurationSectionData.containsKey(path);
//	}
//
//	public boolean contains(String path, boolean ignoreDefault) { // TODO ignoreDefault does nothing here
//		return contains(path);
//	}
//
//	public boolean isSet(String path) {
//		return contains(path) && powerConfigurationSectionData.get(path) != null
//				&& ((!(powerConfigurationSectionData.get(path) instanceof String)) || (powerConfigurationSectionData.get(path) instanceof String && powerConfigurationSectionData.get(path).toString().length() > 0));
//	}
//
//	public Object get(String path) {
//		return powerConfigurationSectionData.get(path);
//	}
//
//	public Object get(String path, Object def) { // TODO def does nothing here
//		return get(path);
//	}
//
//	public String getString(String path) {
//		return isString(path) ? powerConfigurationSectionData.get(path).toString() : null;
//	}
//
//	public String getString(String path, Object def) { // TODO def does nothing here
//		return getString(path);
//	}
//
//	public boolean isString(String path) {
//		return powerConfigurationSectionData.get(path) instanceof String;
//	}
//
//	public int getInt(String path) {
//		return isInt(path) ? (int) powerConfigurationSectionData.get(path) : -1;
//	}
//
//	public int getInt(String path, int def) { // TODO def does nothing here
//		return getInt(path);
//	}
//
//	public boolean isInt(String path) {
//		return powerConfigurationSectionData.get(path) instanceof Integer;
//	}
//
//	public boolean getBoolean(String path) {
//		return isBoolean(path) ? (boolean) powerConfigurationSectionData.get(path) : false;
//	}
//
//	public boolean getBoolean(String path, boolean def) { // TODO def does nothing here
//		return getBoolean(path);
//	}
//
//	public boolean isBoolean(String path) {
//		return powerConfigurationSectionData.get(path) instanceof Boolean;
//	}
//
//	public double getDouble(String path) {
//		return isDouble(path) ? (double) powerConfigurationSectionData.get(path) : -1;
//	}
//
//	public double getDouble(String path, double def) { // TODO def does nothing here
//		return getDouble(path);
//	}
//
//	public boolean isDouble(String path) {
//		return powerConfigurationSectionData.get(path) instanceof Double;
//	}
//
//	public long getLong(String path) {
//		return isLong(path) ? (long) powerConfigurationSectionData.get(path) : -1;
//	}
//
//	public long getLong(String path, long def) { // TODO def does nothing here
//		return getLong(path);
//	}
//
//	public boolean isLong(String path) {
//		return powerConfigurationSectionData.get(path) instanceof Long;
//	}
//
//	public List<?> getList(String path) {
//		return isList(path) ? (List<?>) powerConfigurationSectionData.get(path) : null;
//	}
//
//	public List<?> getList(String path, List<?> def) {return getList(path);
//	}
//
//	public boolean isList(String path) {
//		return powerConfigurationSectionData.get(path) instanceof List;
//	}

}