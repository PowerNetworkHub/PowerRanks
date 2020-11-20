package nl.svenar.PowerRanks.Cache;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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

	public Set<String> getKeys(String path, boolean deep) {
		Set<String> output = new HashSet<String>();

		for (String key : powerConfigurationSectionData.keySet()) {
			if (!deep && key.contains(path)) {
				output.add(key);
			}

			if (deep && key.equals(path)) {
				output.add(key);
			}
		}

		return output;
	}

	public Map<String, Object> getValues(String path, boolean deep) {
		Map<String, Object> output = new HashMap<String, Object>();

		for (String key : powerConfigurationSectionData.keySet()) {
			if (!deep && key.contains(path)) {
				output.put(key, powerConfigurationSectionData.get(key));
			}

			if (deep && key.equals(path)) {
				output.put(key, powerConfigurationSectionData.get(key));
			}
		}

		return output;
	}

	public boolean contains(String path) {
		return powerConfigurationSectionData.containsKey(path);
	}

	public boolean contains(String path, boolean ignoreDefault) { // TODO ignoreDefault does nothing here
		return contains(path);
	}

	public boolean isSet(String path) {
		return contains(path) && powerConfigurationSectionData.get(path) != null
				&& ((!(powerConfigurationSectionData.get(path) instanceof String)) || (powerConfigurationSectionData.get(path) instanceof String && powerConfigurationSectionData.get(path).toString().length() > 0));
	}

	public Object get(String path) {
		return powerConfigurationSectionData.get(path);
	}

	public Object get(String path, Object def) { // TODO def does nothing here
		return get(path);
	}

	public void set(String path, Object value) {
		powerConfigurationSectionData.put(path, value);
	}

	public String getString(String path) {
		return powerConfigurationSectionData.get(path) instanceof String ? powerConfigurationSectionData.get(path).toString() : null;
	}

	public String getString(String path, Object def) { // TODO def does nothing here
		return getString(path);
	}

	public boolean isString(String path) {
		return powerConfigurationSectionData.get(path) instanceof String;
	}

	public int getInt(String path) {
		return powerConfigurationSectionData.get(path) instanceof Integer ? (int) powerConfigurationSectionData.get(path) : -1;
	}

	public int getInt(String path, int def) { // TODO def does nothing here
		return getInt(path);
	}

	public boolean isInt(String path) {
		return powerConfigurationSectionData.get(path) instanceof Integer;
	}

	public boolean getBoolean(String path) {
		return powerConfigurationSectionData.get(path) instanceof String ? (boolean) powerConfigurationSectionData.get(path) : false;
	}

	public boolean getBoolean(String path, boolean def) { // TODO def does nothing here
		return getBoolean(path);
	}

	public boolean isBoolean(String path) {
		return powerConfigurationSectionData.get(path) instanceof Boolean;
	}

	public double getDouble(String path) {
		return powerConfigurationSectionData.get(path) instanceof String ? (double) powerConfigurationSectionData.get(path) : -1;
	}

	public double getDouble(String path, double def) { // TODO def does nothing here
		return getDouble(path);
	}

	public boolean isDouble(String path) {
		return powerConfigurationSectionData.get(path) instanceof Double;
	}

	public long getLong(String path) {
		return powerConfigurationSectionData.get(path) instanceof String ? (long) powerConfigurationSectionData.get(path) : -1;
	}

	public long getLong(String path, long def) { // TODO def does nothing here
		return getLong(path);
	}

	public boolean isLong(String path) {
		return powerConfigurationSectionData.get(path) instanceof Long;
	}

//    public String getCurrentPath();
//    public String getName();
//    public Configuration getRoot();
//    public PowerConfigurationSection getParent();
//    public PowerConfigurationSection createSection(String path);
//    public PowerConfigurationSection createSection(String path, Map<?, ?> map);
//    public List<?> getList(String path);
//    public List<?> getList(String path, List<?> def);
//    public boolean isList(String path);
//    public List<String> getStringList(String path);
//    public List<Integer> getIntegerList(String path);
//    public List<Boolean> getBooleanList(String path);
//    public List<Double> getDoubleList(String path);
//    public List<Float> getFloatList(String path);
//    public List<Long> getLongList(String path);
//    public List<Byte> getByteList(String path);
//    public List<Character> getCharacterList(String path);
//    public List<Short> getShortList(String path);
//    public List<Map<?, ?>> getMapList(String path);
//    public <T extends Object> T getObject(String path, Class<T> clazz);
//    public <T extends Object> T getObject(String path, Class<T> clazz, T def);
//    public <T extends ConfigurationSerializable> T getSerializable(String path, Class<T> clazz);
//    public <T extends ConfigurationSerializable> T getSerializable(String path, Class<T> clazz, T def);
//    public Vector getVector(String path);
//    public Vector getVector(String path, Vector def);
//    public boolean isVector(String path);
//    public OfflinePlayer getOfflinePlayer(String path);
//    public OfflinePlayer getOfflinePlayer(String path, OfflinePlayer def);
//    public boolean isOfflinePlayer(String path);
//    public ItemStack getItemStack(String path);
//    public ItemStack getItemStack(String path, ItemStack def);
//    public boolean isItemStack(String path);
//    public Color getColor(String path);
//    public Color getColor(String path, Color def);
//    public boolean isColor(String path);
//    public Location getLocation(String path);
//    public Location getLocation(String path, Location def);
//    public boolean isLocation(String path);
//    public PowerConfigurationSection getConfigurationSection(String path);
//    public boolean isConfigurationSection(String path);
//    public PowerConfigurationSection getDefaultSection();
//    public void addDefault(String path, Object value);
}