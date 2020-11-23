package nl.svenar.PowerRanks;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PowerRanksExceptionsHandler {
	private static File data_directory;

	public PowerRanksExceptionsHandler(File data_directory) {
		PowerRanksExceptionsHandler.data_directory = data_directory;
	}

	public static void except(String className, String errorlog) {
		except(className, errorlog, false);
	}

	public static void silent_except(String className, String errorlog) {
		except(className, errorlog, true);		
	}
	
	private static void except(String className, String errorlog, boolean silent) {
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy");

		File file_log = new File(data_directory, "logs");
		file_log.mkdirs();
		file_log = new File(data_directory, "logs" + File.separator + dateFormat.format(date) + ".log");

		if (!silent) {
			PowerRanks.log.warning("----------------------------------------");
			PowerRanks.log.warning(PowerRanks.pdf.getName() + " v" + PowerRanks.pdf.getVersion());
			PowerRanks.log.warning("An error occurred in " + className);
			PowerRanks.log.warning("Detailed error log is available in: " + file_log.getPath());
			PowerRanks.log.warning("----------------------------------------");
		}
		FileWriter fr;
		try {
			fr = new FileWriter(file_log, true);
			fr.write("----------------------------------------" + System.lineSeparator());
			fr.write(PowerRanks.pdf.getName() + " v" + PowerRanks.pdf.getVersion() + System.lineSeparator());
			fr.write("An error occurred in " + className + System.lineSeparator());
			fr.write("----------------------------------------" + System.lineSeparator());
			String[] lines = errorlog.split("\n");
			for (String line : lines) {
				fr.write(line + System.lineSeparator());
				
				if (line.toLowerCase().contains("powerranks")) {
					String name = line.contains("(") ? (line.contains(":") ? line.split("\\(")[1].split(":")[0] : line.split("\\(")[1]) : "<UNKNOWN FILE>";
					String lineNumber = line.contains(":") ? line.replace(")", "").split(":")[1] : "<UNKNOWN LINE>";
					if (!silent) PowerRanks.log.warning("File: " + name + " >> " + lineNumber);
				}
			}
			fr.write("----------------------------------------" + System.lineSeparator());
			fr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (!silent) PowerRanks.log.warning("----------------------------------------");		
	}

	public static void exceptCustom(String className, String error) {
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy");

		File file_log = new File(data_directory, "logs");
		file_log.mkdirs();
		file_log = new File(data_directory, "logs" + File.separator + dateFormat.format(date) + ".log");

		PowerRanks.log.warning("----------------------------------------");
		PowerRanks.log.warning(PowerRanks.pdf.getName() + " v" + PowerRanks.pdf.getVersion());
		PowerRanks.log.warning("An error occurred in " + className);
		PowerRanks.log.warning("Detailed error log is available in: " + file_log.getPath());
		PowerRanks.log.warning("----------------------------------------");
		FileWriter fr;
		try {
			fr = new FileWriter(file_log, true);
			fr.write("----------------------------------------" + System.lineSeparator());
			fr.write(PowerRanks.pdf.getName() + " v" + PowerRanks.pdf.getVersion() + System.lineSeparator());
			fr.write("An error occurred in " + className + System.lineSeparator());
			fr.write("----------------------------------------" + System.lineSeparator());
			PowerRanks.log.warning(error);
			fr.write("----------------------------------------" + System.lineSeparator());
			fr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		PowerRanks.log.warning("----------------------------------------");
	}

}