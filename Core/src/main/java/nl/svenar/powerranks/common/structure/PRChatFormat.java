package nl.svenar.powerranks.common.structure;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PRChatFormat {
    private String prefix;
    private String suffix;
    private String namecolor;
    private String chatcolor;

    public PRChatFormat() {
        this.prefix = "";
        this.suffix = "";
        this.namecolor = "";
        this.chatcolor = "";
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getNamecolor() {
        return namecolor;
    }

    public void setNamecolor(String namecolor) {
        this.namecolor = namecolor;
    }

    public String getChatcolor() {
        return chatcolor;
    }

    public void setChatcolor(String chatcolor) {
        this.chatcolor = chatcolor;
    }
}
