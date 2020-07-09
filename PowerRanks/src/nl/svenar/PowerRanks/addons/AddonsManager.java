package nl.svenar.PowerRanks.addons;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Util;

public class AddonsManager {

	public static HashMap<File, Boolean> loadedAddons = new HashMap<File, Boolean>(); // file_path, is_loaded
	public HashMap<File, PowerRanksAddon> addonClasses = new HashMap<File, PowerRanksAddon>(); // file_path, PowerRanksAddon

	public AddonsManager() {

	}

	public void setup() {
		final File folder = new File(PowerRanks.configFileLoc + File.separator + "Addons");
		if (!folder.exists()) {
			folder.mkdirs();
		}

		for (String fileName : folder.list()) {
			File file = new File(folder.getAbsolutePath() + File.separator + fileName);
			if (isJar(file)) {
				loadedAddons.put(file, false);
			}
		}

		for (Entry<File, Boolean> prAddon : loadedAddons.entrySet()) {
			List<Class<?>> classes = PowerRanksFileUtil.getClasses(prAddon.getKey(), PowerRanksAddon.class);

			if (classes == null || classes.isEmpty()) {
				PowerRanks.log.info("Addon " + prAddon.getKey().getName() + " is invalid");
			} else {
				PowerRanksAddon pra = null;
				for (Class<?> c : classes) {
					if (PowerRanksAddon.class.isAssignableFrom(c)) {
						try {
							pra = (PowerRanksAddon) c.getDeclaredConstructor().newInstance();
							addonClasses.put(prAddon.getKey(), pra);
							prAddon.setValue(true);
						} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
							e.printStackTrace();
						}

					}
				}
			}
		}

		for (Entry<File, PowerRanksAddon> prAddon : addonClasses.entrySet()) {
			if (Util.calculateVersionFromString(prAddon.getValue().minimalPowerRanksVersion().replaceAll("[a-zA-Z ]", "")) >= Util.calculateVersionFromString(PowerRanks.pdf.getVersion().replaceAll("[a-zA-Z ]", ""))) {
				PowerRanks.log.info("PowerRanks addon: " + prAddon.getValue().getIdentifier() + " v" + prAddon.getValue().getVersion() + " by: " + prAddon.getValue().getAuthor() + " loaded!");
				prAddon.getValue().setup();
			} else {
				PowerRanks.log.info("PowerRanks addon: " + prAddon.getValue().getIdentifier() + " requires PowerRanks v" + prAddon.getValue().minimalPowerRanksVersion().replaceAll("[a-zA-Z ]", "") + " or higher");
				addonClasses.remove(prAddon.getKey());
				loadedAddons.put(prAddon.getKey(), false);
				
			}
		}

		int addonCount = 0;
		for (Entry<File, Boolean> prAddon : loadedAddons.entrySet()) {
			if (prAddon.getValue() == true)
				addonCount++;
		}

		PowerRanks.log.info("Loaded " + addonCount + " add-on(s)!");
	}

	private boolean isJar(File path) {
		String extension = ".jar";
		if (path.getName().toLowerCase().endsWith(extension)) {
			return true;
		}
		return false;
	}
}
