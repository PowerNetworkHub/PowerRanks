package nl.svenar.common.structure;

import java.util.HashMap;

public class PRPlayerRank {
    
    private String name;
    private HashMap<String, String> tags;

    public PRPlayerRank() {
        this.tags = new HashMap<>();
    }

    public PRPlayerRank(String name) {
        this();
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public HashMap<String, String> getTags() {
        return this.tags;
    }
}
