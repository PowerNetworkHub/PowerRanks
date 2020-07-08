package nl.svenar.PowerRanks.addons;

public abstract class PowerRanksAddon {

	public String getAuthor() {
		return "";
	}

	public String getIdentifier() {
		return "";
	}

	public String getVersion() {
		return "";
	}
	
	public void onPlayerJoin(PowerRanksPlayer prPlayer) {}
}
