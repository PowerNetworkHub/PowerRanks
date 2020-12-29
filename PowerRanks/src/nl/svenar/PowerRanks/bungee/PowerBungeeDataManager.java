package nl.svenar.PowerRanks.bungee;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Cache.CachedRanks;
import nl.svenar.PowerRanks.api.PowerRanksAPI;

public class PowerBungeeDataManager {

	public HashMap<String, String> createRankHashes() {
		HashMap<String, String> output = new HashMap<String, String>();
		
		for (String key : CachedRanks.getConfigurationSection("Groups").getKeys(false)) {
			output.put(key, String.valueOf(serializeRank(key).hashCode()));
		}
		
		return output;
	}
	
	public String serializeRank(String rankName) {
		String output = "";
		String permissions = "";
		try {
			String.join(",", CachedRanks.getStringList("Groups." + rankName + ".permissions"));
		} catch (Exception e) {
		}
		String inheritance = "";
		try {
			String.join(",", CachedRanks.getStringList("Groups." + rankName + ".inheritance"));
		} catch (Exception e) {
		}
		String chatPrefix = CachedRanks.getString("Groups." + rankName + ".chat.prefix");
		String chatSuffix = CachedRanks.getString("Groups." + rankName + ".chat.suffix");
		String chatColor = CachedRanks.getString("Groups." + rankName + ".chat.chatColor");
		String nameColor = CachedRanks.getString("Groups." + rankName + ".chat.nameColor");
		String levelPromote = CachedRanks.getString("Groups." + rankName + ".level.promote");
		String levelDemote = CachedRanks.getString("Groups." + rankName + ".level.demote");
		String economyBuyable = "";
		try {
			String.join(",", CachedRanks.getStringList("Groups." + rankName + ".economy.buyable"));
		} catch (Exception e) {
		}
		String economyCost = String.valueOf(CachedRanks.getInt("Groups." + rankName + ".economy.cost"));
		String guiIcon = CachedRanks.getString("Groups." + rankName + ".gui.icon");
		
		output += "name:" + rankName + ";";
		output += "permissions:" + permissions + ";";
		output += "inheritance:" + inheritance + ";";
		output += "prefix:" + chatPrefix + ";";
		output += "suffix:" + chatSuffix + ";";
		output += "chatColor:" + chatColor + ";";
		output += "nameColor:" + nameColor + ";";
		output += "levelPromote:" + levelPromote + ";";
		output += "levelDemote:" + levelDemote + ";";
		output += "economyBuyable:" + economyBuyable + ";";
		output += "economyCost:" + economyCost + ";";
		output += "guiIcon:" + guiIcon + ";";
		
		return new String(Base64.getEncoder().encode(output.getBytes()));
	}
	
	public HashMap<String, String> deserializeRank(String rankData) {
		HashMap<String, String> output = new HashMap<String, String>();
		String unencodedData = new String(Base64.getDecoder().decode(rankData));
		for (String item : unencodedData.split(";")) {
			String[] itemData = item.split(":", 2);
			output.put(itemData[0], itemData[1]);
		}
		return output;
	}
	
	public void saveRankDataToRank(PowerRanksAPI prAPI, HashMap<String, String> rankData) {
//		if (!CachedRanks.contains("Groups." + rankData.get("name"))) {
//			prAPI.createRank(rankData.get("name"));
//		}
		
		if (rankData.get("name").length() == 0) {
			return;
		}
		
		CachedRanks.set("Groups." + rankData.get("name") + ".permissions", rankData.get("permissions").split(",").length > 0 ? rankData.get("permissions").split(",") : new ArrayList<String>());
		CachedRanks.set("Groups." + rankData.get("name") + ".inheritance", rankData.get("inheritance").split(",").length > 0 ? rankData.get("inheritance").split(",") : new ArrayList<String>());
		CachedRanks.set("Groups." + rankData.get("name") + ".chat.prefix", rankData.get("prefix"));
		CachedRanks.set("Groups." + rankData.get("name") + ".chat.suffix", rankData.get("suffix"));
		CachedRanks.set("Groups." + rankData.get("name") + ".chat.chatColor", rankData.get("chatColor"));
		CachedRanks.set("Groups." + rankData.get("name") + ".chat.nameColor", rankData.get("nameColor"));
		CachedRanks.set("Groups." + rankData.get("name") + ".level.promote", rankData.get("levelPromote"));
		CachedRanks.set("Groups." + rankData.get("name") + ".level.demote", rankData.get("levelDemote"));
		CachedRanks.set("Groups." + rankData.get("name") + ".economy.buyable", rankData.get("economyBuyable").split(",").length > 0 ? rankData.get("economyBuyable").split(",") : new ArrayList<String>());
		CachedRanks.set("Groups." + rankData.get("name") + ".economy.cost", Integer.parseInt(rankData.get("economyCost")));
		CachedRanks.set("Groups." + rankData.get("name") + ".gui.icon", rankData.get("guiIcon"));
		CachedRanks.update();
		
		//PowerRanks.log.info(String.join("\n", rankData.keySet()));
	}
}
