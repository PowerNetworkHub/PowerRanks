package nl.svenar.powerranks.common.structure;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PREconomyDetails {
    private ArrayList<String> buyableRanks;
    private float cost;
    private String description;
    private String buyCommand;

    public PREconomyDetails() {
        this.buyableRanks = new ArrayList<>();
        this.cost = 0L;
        this.description = "";
        this.buyCommand = "";
    }

    public ArrayList<String> getBuyableRanks() {
        return buyableRanks;
    }

    public void setBuyableRanks(ArrayList<String> buyableRanks) {
        this.buyableRanks = buyableRanks;
    }

    public void addBuyableRank(String buyableRankName) {
        if (this.buyableRanks == null) {
            this.buyableRanks = new ArrayList<>();
        }
        this.buyableRanks.add(buyableRankName);
    }

    public void removeBuyableRank(String buyableRankName) {
        if (this.buyableRanks != null) {
            this.buyableRanks.remove(buyableRankName);
        }
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBuyCommand() {
        return buyCommand;
    }

    public void setBuyCommand(String buyCommand) {
        this.buyCommand = buyCommand;
    }
}