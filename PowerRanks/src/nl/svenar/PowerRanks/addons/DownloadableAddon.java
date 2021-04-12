package nl.svenar.PowerRanks.addons;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.util.ArrayList;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Util;
import nl.svenar.PowerRanks.addons.AddonDownloader.Addon;

public class DownloadableAddon {

	private Addon addon;

	public DownloadableAddon(Addon addon) {
		this.addon = addon;
	}

	public String getName() {
		return addon.name;
	}

	public boolean isDownloadable() {
		return addon.autoDownloadable;
	}

	public boolean isCompatible() {
		return Util.calculateVersionFromString(PowerRanks.pdf.getVersion().replaceAll("[a-zA-Z ]", "")) >= Util
				.calculateVersionFromString(addon.powerranksVersion.replaceAll("[a-zA-Z ]", ""));
	}

	public String getAuthor() {
		return addon.author;
	}

	public String getVersion() {
		return addon.version;
	}

	public String getURL() {
		return (addon.download.toLowerCase().endsWith(".jar") && !addon.download.toLowerCase().startsWith("https://")
				? "https://addons.powerranks.nl/"
				: "") + addon.download;
	}

	public String getMinPowerRanksVersion() {
		return addon.powerranksVersion;
	}

	public ArrayList<String> getDescription() {
		return addon.description;
	}

	public boolean download() {
		final File targetDirectory = new File(PowerRanks.configFileLoc + File.separator + "Addons" + File.separator);
		try {
			URLConnection urlConnection = Util.getURL(getURL());

			String filePathName = urlConnection.getURL().getFile();
			String fileName = filePathName.substring(filePathName.lastIndexOf(File.separatorChar) + 1);

			File targetFile = new File(targetDirectory, fileName);
			InputStream inputStream = urlConnection.getInputStream();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            OutputStream outputStream = new FileOutputStream(targetFile);

            int size;
            byte[] buf = new byte[1024];

			while ((size = bufferedInputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, size);
			}
			
            outputStream.close();
            bufferedInputStream.close();
            inputStream.close();

			reloadAddons();

			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private void reloadAddons() {
		PowerRanks.getInstance().addonsManager.disable();
		PowerRanks.getInstance().addonsManager.setup();
	}

	private File getFile() {
		try {
			URLConnection urlConnection = Util.getURL(getURL());

			if (urlConnection != null) {
				String filePathName = urlConnection.getURL().getFile();
				String fileName = filePathName.substring(filePathName.lastIndexOf(File.separatorChar) + 1);
				final File target = new File(PowerRanks.configFileLoc + File.separator + "Addons", fileName);
				return target;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	public boolean isInstalled() {
		File file = getFile();
		return file != null ? file.exists() : false;
	}

	public void uninstall() {
		File file = getFile();
		if (file.exists()) {
			file.delete();
			reloadAddons();
		}
	}
}
