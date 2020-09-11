package nl.svenar.powerranks.core;

public abstract class PowerRanksCoreLogger {
	
	private PowerRanksCore powerranks_core;
	
	public PowerRanksCoreLogger(PowerRanksCore powerranks_core) {
		this.powerranks_core = powerranks_core;
	}
	
	public String format(String msg) {
		return "[" + powerranks_core.get_name() + " " + powerranks_core.get_server_type() + "] " + msg;
	}
	
	public abstract void info(String msg);
}
