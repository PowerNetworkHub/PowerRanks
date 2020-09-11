package nl.svenar.PowerRanks;

import java.util.logging.Logger;

import nl.svenar.powerranks.core.PowerRanksCore;
import nl.svenar.powerranks.core.PowerRanksCoreLogger;

public class PowerRanksLogger extends PowerRanksCoreLogger {
	
	private Logger log;
	
	public PowerRanksLogger(PowerRanksCore powerranks_core, Logger log) {
		super(powerranks_core);
		this.log = log;
	}

	@Override
	public void info(String msg) {
		log.info(msg);
	}

}
