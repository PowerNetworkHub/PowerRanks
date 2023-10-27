package nl.svenar.powerranks.api;

public class PowerRanksAPI {

	private final String API_VERSION = "2.0";

	private RanksAPI ranksAPI;
	private PlayersAPI playersAPI;

	/**
	 * Initialize the API
	 */
	public PowerRanksAPI() {
		ranksAPI = new RanksAPI();
		playersAPI = new PlayersAPI();
	}

	/**
	 * Get the api version
	 * 
	 * @return String
	 */
	public String getApiVersion() {
		return API_VERSION;
	}

	/**
	 * Get the RanksAPI
	 * 
	 * @return RanksAPI
	 */
	public RanksAPI getRanksAPI() {
		return ranksAPI;
	}

	/**
	 * Get the PlayersAPI
	 * 
	 * @return PlayersAPI
	 */
	public PlayersAPI getPlayersAPI() {
		return playersAPI;
	}

}