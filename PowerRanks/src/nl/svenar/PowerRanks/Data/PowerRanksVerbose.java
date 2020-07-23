package nl.svenar.PowerRanks.Data;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import nl.svenar.PowerRanks.PowerRanks;

public class PowerRanksVerbose {

	public static boolean USE_VERBOSE = false;
	private static ArrayList<String> VERBOSE_LOG = new ArrayList<String>();
	private static PowerRanks plugin;
	
	public PowerRanksVerbose(PowerRanks plugin) {
		PowerRanksVerbose.plugin = plugin;
	}
	
	public static void log(String functionName, String msg) {
		if (!USE_VERBOSE) return;
		
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		
		VERBOSE_LOG.add("[" + dateFormat.format(date) + "] [" + functionName + "] " + msg);
	}
	
	public static void save() {
		if (USE_VERBOSE) return;
		
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
		
		final File logFile = new File(plugin.getDataFolder() + File.separator + "logs", "verbose_" + dateFormat.format(date) + ".prlog");
		
		if (!logFile.exists())
			logFile.getParentFile().mkdirs();
		try {
			logFile.createNewFile();
			
			FileWriter writer = new FileWriter("output.txt"); 
			for(String str: VERBOSE_LOG) {
			  writer.write(str + System.lineSeparator());
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
