package nl.svenar.PowerRanks.Data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import nl.svenar.PowerRanks.PowerRanks;

public class PowerRanksVerbose {

	public static boolean USE_VERBOSE = false;
	public static boolean USE_VERBOSE_LIVE = false;
	private static ArrayList<String> VERBOSE_LOG = new ArrayList<String>();
	private static PowerRanks plugin;
	private static String filter = "";

	public PowerRanksVerbose(PowerRanks plugin) {
		PowerRanksVerbose.plugin = plugin;
	}
	
	public static void start(boolean live) {
		USE_VERBOSE = true;
		USE_VERBOSE_LIVE = live;
		VERBOSE_LOG.clear();
	}
	
	public static void stop() {
		USE_VERBOSE = false;
		USE_VERBOSE_LIVE = false;
		setFilter("");
	}

	public static void clear() {
		VERBOSE_LOG.clear();
		setFilter("");
	}

	public static void setFilter(String filter) {
		PowerRanksVerbose.filter = filter;
	}

	public static String getFilter() {
		return PowerRanksVerbose.filter;
	}

	public static void log(String functionName, String msg) {
		if (!USE_VERBOSE)
			return;

		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

		// String text = "[" + dateFormat.format(date) + "] [" + functionName + "] " + msg;
		// VERBOSE_LOG.add(text);
		// if (USE_VERBOSE_LIVE) {
		// 	PowerRanks.log.info("[verbose] " + text);
		// }

		if (functionName.length() == 0 && msg.length() == 0) {
			VERBOSE_LOG.add("");
			if (USE_VERBOSE_LIVE) {
				PowerRanks.log.info("");
			}
		} else {
			String text = "[" + dateFormat.format(date) + "] [" + functionName + "] " + msg;
			VERBOSE_LOG.add(text);
			if (USE_VERBOSE_LIVE) {
				PowerRanks.log.info("[verbose] " + text);
			}
		}
	}

	public static boolean save() {
		if (USE_VERBOSE || VERBOSE_LOG.size() == 0)
			return false;

		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");

		final File logFile = new File(plugin.getDataFolder() + File.separator + "logs", "verbose_" + dateFormat.format(date) + ".prlog");
		final String serverPath = plugin.getServer().getWorldContainer().getAbsolutePath();
		final File serverLogFile = new File(serverPath.substring(0, serverPath.length() - 1), "logs/latest.log");
		PowerRanks.log.info("Saving " + VERBOSE_LOG.size() + " lines to " + logFile.getName());

		if (!logFile.exists())
			logFile.getParentFile().mkdirs();
		try {
			logFile.createNewFile();

			FileWriter writer = new FileWriter(logFile);

			writer.write("=---------------------=" + System.lineSeparator());
			writer.write("POWERRANKS LOG" + System.lineSeparator());
			writer.write("=---------------------=" + System.lineSeparator());
			writer.write("" + System.lineSeparator());
			for (String str : VERBOSE_LOG) {
				writer.write(str + System.lineSeparator());
			}

			writer.write("" + System.lineSeparator());
			writer.write("=---------------------=" + System.lineSeparator());
			writer.write("SERVER LOG" + System.lineSeparator());
			writer.write("=---------------------=" + System.lineSeparator());
			writer.write("" + System.lineSeparator());

			if (serverLogFile.exists()) {
				try (BufferedReader br = new BufferedReader(new FileReader(serverLogFile))) {
					String line;
					while ((line = br.readLine()) != null) {
						writer.write(line + System.lineSeparator());
					}
				}
			} else {
				writer.write("Server log not found." + System.lineSeparator());
			}

			writer.close();
			
			VERBOSE_LOG.clear();
			
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return false;
	}

	public static int logSize() {
		return VERBOSE_LOG.size();
	}
}
