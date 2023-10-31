package nl.svenar.powerranks.bukkit.textcomponents;

import java.util.ArrayList;
import java.util.List;

public class PageNavigationManager {
    private int itemsPerPage;
    private List<String> items;
    private boolean isMonospace;

    public PageNavigationManager() {
        this.itemsPerPage = 10;
        this.items = new ArrayList<String>();
    }

    public void setItemsPerPage(int itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
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

    public int getNumPages() {
        return (int) Math.ceil((double) items.size() / itemsPerPage);
    }

    public Page getPage(int pageNum) {
        pageNum = Math.max(1, Math.min(pageNum, getNumPages()));
        int startIndex = (pageNum - 1) * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, items.size());
        return new Page(pageNum, items.subList(startIndex, endIndex), isMonospace);
    }
}
