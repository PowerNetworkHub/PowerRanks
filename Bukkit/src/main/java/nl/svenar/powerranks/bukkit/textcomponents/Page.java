package nl.svenar.powerranks.bukkit.textcomponents;

import java.util.ArrayList;
import java.util.List;

import nl.svenar.powerranks.bukkit.PowerRanks;
import nl.svenar.powerranks.common.utils.PowerColor;

public class Page {
    private int pageNum;
    private List<String> items;
    private boolean isMonospace;

    public Page(int pageNum, List<String> items, boolean isMonospace) {
        this.pageNum = pageNum;
        this.items = items;
        this.isMonospace = isMonospace;
    }

    public List<String> generate() {
        String outlineColorBegin = "#01b0fb";
        String outlineColorEnd = "#f50be5";
        int maxItemWidth = items.stream().mapToInt(String::length).max().orElse(0);

        List<String> generatedList = new ArrayList<String>();

        for (String item : items) {
            int itemWidth = item.length();
            int numSpaces = maxItemWidth - itemWidth;
            String numSpacesOffset = "";
            if (isMonospace) {
                for (int i = 0; i < numSpaces; i++) {
                    numSpacesOffset += " ";
                }
            } else {
                for (int i = 0; i < numSpaces * 2.5; i++) {
                    numSpacesOffset += " ";
                }
            }
            generatedList.add(outlineColorBegin + "│ " + item + numSpacesOffset + outlineColorEnd + " │");
        }

        for (String item : generatedList) {
            String newItem = PowerRanks.getPowerColor().format(PowerColor.UNFORMATTED_COLOR_CHAR, item, true, false,
                    false);
            generatedList.set(generatedList.indexOf(item), newItem);
        }

        return generatedList;
    }
}
