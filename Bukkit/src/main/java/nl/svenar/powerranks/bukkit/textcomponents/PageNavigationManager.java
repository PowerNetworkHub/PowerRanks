package nl.svenar.powerranks.bukkit.textcomponents;

import java.util.ArrayList;
import java.util.List;

import nl.svenar.powerranks.bukkit.PowerRanks;
import nl.svenar.powerranks.common.utils.PowerColor;

public class PageNavigationManager {
    private int itemsPerPage;
    private String title, baseCommand;
    private List<String> items;
    private boolean isMonospace, fancyPageControls;

    public PageNavigationManager() {
        this.itemsPerPage = 10;
        this.title = "PowerRanks";
        this.items = new ArrayList<String>();
    }

    public void setItemsPerPage(int itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBaseCommand(String baseCommand) {
        this.baseCommand = baseCommand;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }

    public void addItem(String item) {
        this.items.add(item);
    }

    public void setMonospace(boolean isMonospace) {
        this.isMonospace = isMonospace;
    }

    public void setFancyPageControls(boolean fancyPageControls) {
        this.fancyPageControls = fancyPageControls;
    }

    public int getNumPages() {
        return (int) Math.ceil((double) items.size() / itemsPerPage);
    }

    public Page getPage(int pageNum) {
        pageNum = Math.max(1, Math.min(pageNum, getNumPages()));
        int startIndex = (pageNum - 1) * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, items.size());
        List<String> pageItems = items.subList(startIndex, endIndex);

        boolean everyLineBeginsWithSpace = true;
        while (everyLineBeginsWithSpace) {
            for (String item : pageItems) {
                if (!PowerRanks.getPowerColor().removeFormat(PowerColor.COLOR_CHAR, item).startsWith(" ")) {
                    everyLineBeginsWithSpace = false;
                    break;
                }
            }
            if (everyLineBeginsWithSpace) {
                for (int i = 0; i < pageItems.size(); i++) {
                    String color = "";
                    for (char c : pageItems.get(i).toCharArray()) {
                        if (c != ' ') {
                            color += c;
                        } else {
                            break;
                        }

                    }
                    pageItems.set(i, color + pageItems.get(i).substring(pageItems.get(i).startsWith(" ") ? 1 : 3));
                }
            }
        }

        return new Page(pageNum, getNumPages(), title, baseCommand, pageItems, isMonospace, fancyPageControls);
    }
}
