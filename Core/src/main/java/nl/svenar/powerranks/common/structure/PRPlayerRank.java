package nl.svenar.powerranks.common.structure;

import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties("disabled")
public class PRPlayerRank {

    private String name;
    private HashMap<String, Object> tags;
    private boolean disabled;

    public PRPlayerRank() {
        this.disabled = false;
        this.tags = new HashMap<>();
    }

    public PRPlayerRank(String name) {
        this();
        this.name = name;
    }

    public PRPlayerRank(PRRank rank) {
        this(rank.getName());
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        if (name == null || name.length() == 0) {
            return;
        }
        this.name = name;
    }

    public HashMap<String, Object> getTags() {
        return this.tags;
    }

    public void addTag(String tagName, Object tagValue) {
        if (tagName == null || tagValue == null) {
            return;
        }
        if (tagName.length() == 0) {
            return;
        }
        this.tags.put(tagName, tagValue);
    }

    public void addTagRaw(String tagName, Object tagValue) {
        if (tagName == null || tagValue == null) {
            return;
        }
        if (tagName.length() == 0) {
            return;
        }

        this.tags.put(tagName, tagValue);
    }

    public boolean isDisabled() {
        return this.disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }
}