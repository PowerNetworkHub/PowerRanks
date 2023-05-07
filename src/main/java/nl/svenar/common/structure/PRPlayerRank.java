package nl.svenar.common.structure;

import java.util.HashMap;

public class PRPlayerRank {
    
    private String rank;
    private HashMap<String, String> tags;

    public PRPlayerRank() {
        this.tags = new HashMap<>();
    }

    public PRPlayerRank(String name) {
        this();
        this.rank = name;
    }

    public String getName() {
        return this.rank;
    }

    public HashMap<String, String> getTags() {
        return this.tags;
    }
}
