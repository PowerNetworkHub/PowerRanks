package nl.svenar.powerranks.bukkit.textcomponents;

import java.util.ArrayList;
import java.util.List;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import nl.svenar.powerranks.bukkit.PowerRanks;
import nl.svenar.powerranks.common.utils.PowerColor;

public class Page {

    private int pageNum;
    
    private int totalPages;

    private String title;

    private String baseCommand;

    private List<String> items;

    private boolean isMonospace;

    private boolean fancyPageControls;

    private int internalTitleWidth;

    public Page(int pageNum, int totalPages, String title, String baseCommand, List<String> items, boolean isMonospace,
            boolean fancyPageControls) {
        this.pageNum = pageNum;
        this.totalPages = totalPages;
        this.title = title;
        this.baseCommand = baseCommand;
        this.items = items;
        this.isMonospace = isMonospace;
        this.fancyPageControls = fancyPageControls;
    }

    private String getTitle() {
        return title;
    }

    public List<Object> generate() {
        int nextPage = pageNum + 1 > totalPages ? totalPages : pageNum + 1;
        int previousPage = pageNum - 1 < 1 ? 1 : pageNum - 1;

        List<Object> generatedList = new ArrayList<>();

        int maxItemWidth = items.stream().map(e -> {
            return PowerRanks.getPowerColor().removeFormat(
                    PowerColor.UNFORMATTED_COLOR_CHAR,
                    PowerRanks.getPowerColor().removeFormat(PowerColor.COLOR_CHAR, e));
        }).mapToInt(e -> {
            return isMonospace ? e.length() : DefaultFontInfo.getStringLength(e);
        }).max().orElse(0);

        createHeader(maxItemWidth, true, previousPage, nextPage);

        generatedList.addAll(createHeader(maxItemWidth, false, previousPage, nextPage));
        generatedList.addAll(createSpacedItems(maxItemWidth));
        generatedList.addAll(createFooter(maxItemWidth));

        if (!fancyPageControls) {
            String offset = "";
            if (pageNum != previousPage) {
                generatedList.add(
                        "[gradient=#ffff00,#ef3300]Previous page[/gradient]&r » /" + baseCommand + " " + previousPage);
                while ((isMonospace ? ("Next page" + offset).length()
                        : DefaultFontInfo.getStringLength("Next page" + offset)) < (isMonospace
                                ? "Previous page".length()
                                : DefaultFontInfo.getStringLength("Previous page"))) {
                    offset += " ";
                }
            }
            if (pageNum != nextPage) {
                generatedList.add("[gradient=#ffff00,#ef3300]Next page " + offset + "[/gradient]&r» /" + baseCommand
                        + " " + nextPage);
            }
        }

        // Apply colors
        for (Object item : generatedList) {
            Object newItem = item;
            if (item instanceof String) {
                newItem = PowerRanks.getPowerColor().format(PowerColor.UNFORMATTED_COLOR_CHAR, (String) item,
                        true, false, false);
            // } else {
            //     for (BaseComponent component : ((TextComponent) item).getExtra()) {
            //         System.out.println(((TextComponent) component).getText());
            //         ((TextComponent) component)
            //                 .setText(PowerRanks.getPowerColor().format(PowerColor.UNFORMATTED_COLOR_CHAR,
            //                         ((TextComponent) component).getText(), true, false, false));
            //                         System.out.println(((TextComponent) component).getText() + "\n");
            //     }
            }
            generatedList.set(generatedList.indexOf(item), newItem);
        }
        return generatedList;
    }

    private List<String> createSpacedItems(int maxItemWidth) {
        int targetWidth = Math.max(internalTitleWidth, maxItemWidth);

        List<String> generatedList = new ArrayList<String>();

        for (String item : items) {
            String unformattedItemString = PowerRanks.getPowerColor().removeFormat(PowerColor.UNFORMATTED_COLOR_CHAR,
                    PowerRanks.getPowerColor().removeFormat(PowerColor.COLOR_CHAR, item));
            String offset = "";
            while ((isMonospace ? unformattedItemString.length()
                    : DefaultFontInfo.getStringLength(unformattedItemString + " ") - 0.5) < targetWidth) {
                offset += " ";
                unformattedItemString += " ";
            }
            generatedList.add("#01b0fb│ " + item + offset + " #f50be5│");
        }

        return generatedList;
    }

    private List<Object> createHeader(int maxItemWidth, boolean generateTitleWidth, int previousPage, int nextPage) {
        int targetWidth = Math.max(internalTitleWidth, maxItemWidth);

        List<Object> generatedList = new ArrayList<>();
        String title = "#01b0fb│ [gradient=#ffff00,#ef3300]" + getTitle() + "[/gradient] #7E63DE│";
        String unformattedTitle = PowerRanks.getPowerColor().removeFormat(PowerColor.UNFORMATTED_COLOR_CHAR,
                PowerRanks.getPowerColor().removeFormat(PowerColor.COLOR_CHAR, title));
        int titleWidth = isMonospace ? unformattedTitle.length() : DefaultFontInfo.getStringLength(unformattedTitle);

        Object pageInfo = "";
        String baseText = "";
        if (!fancyPageControls) {
            pageInfo = "#7E63DE│ [gradient=#ffff00,#ef3300]" + pageNum + "/" + totalPages
                    + "[/gradient] #f50be5│";
        } else {
            baseText = "#7E63DE│ &r◀[gradient=#ffff00,#ef3300]" + pageNum + "/" + totalPages
            + "[/gradient]&r▶ #f50be5│";

            pageInfo = new ComponentBuilder();

            TextComponent leftArrow = new TextComponent("◀");
            TextComponent rightArrow = new TextComponent("▶");
            leftArrow.setColor(ChatColor.WHITE);
            rightArrow.setColor(ChatColor.WHITE);
            leftArrow.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Previous page")));
            rightArrow.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Next page")));
            leftArrow.setClickEvent(
                    new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + baseCommand + " " + previousPage));
            rightArrow.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + baseCommand + " " + nextPage));

            ((ComponentBuilder) pageInfo).appendLegacy(PowerRanks.getPowerColor().format(PowerColor.UNFORMATTED_COLOR_CHAR, "#7E63DE│ ", true, false, false));
            ((ComponentBuilder) pageInfo).append(leftArrow);
            ((ComponentBuilder) pageInfo).appendLegacy(PowerRanks.getPowerColor().format(PowerColor.UNFORMATTED_COLOR_CHAR, "[gradient=#ffff00,#ef3300]" + pageNum + "/" + totalPages
                    + "[/gradient]", true, false, false));
            ((ComponentBuilder) pageInfo).append(rightArrow);
            ((ComponentBuilder) pageInfo).appendLegacy(PowerRanks.getPowerColor().format(PowerColor.UNFORMATTED_COLOR_CHAR, " #f50be5│", true, false, false));

            pageInfo = ((ComponentBuilder) pageInfo).create();
        }
        String unformattedPageInfo = PowerRanks.getPowerColor().removeFormat(PowerColor.UNFORMATTED_COLOR_CHAR,
                PowerRanks.getPowerColor().removeFormat(PowerColor.COLOR_CHAR, pageInfo instanceof String
                        ? (String) pageInfo
                        : baseText));
        String pageInfoOffset = "";

        while ((isMonospace ? (unformattedTitle + pageInfoOffset + unformattedPageInfo).length()
                : DefaultFontInfo
                        .getStringLength(unformattedTitle + pageInfoOffset + unformattedPageInfo)) < targetWidth - 0.5
                                + (isMonospace ? "│  │".length() : DefaultFontInfo.getStringLength("│ │"))) {
            pageInfoOffset += " ";
        }

        if (generateTitleWidth) {
            internalTitleWidth = isMonospace ? (unformattedTitle + pageInfoOffset + unformattedPageInfo).length()
                    : DefaultFontInfo.getStringLength(unformattedTitle + pageInfoOffset + unformattedPageInfo);
            return null;
        }

        String top = "┌";
        String bottom = "├";
        while ((isMonospace ? top.length() + 1 : DefaultFontInfo.getStringLength(top + "─┐")) < titleWidth) {
            top += "─";
            bottom += "─";
        }
        top += "┐";
        bottom += "┴";
        if (totalPages > 1) {
            top += pageInfoOffset + "┌";
            while ((isMonospace ? top.length() - 3 : DefaultFontInfo.getStringLength(top + "┐")) < targetWidth) {
                top += "─";
            }
            top += "┐";

            while ((isMonospace ? bottom.length() : DefaultFontInfo.getStringLength(bottom)) < (isMonospace
                    ? (unformattedTitle + pageInfoOffset).length()
                    : DefaultFontInfo.getStringLength(unformattedTitle + pageInfoOffset)
                            - DefaultFontInfo.getStringLength("─┐"))) {
                bottom += "─";
            }
            bottom += "┴";
        }
        while ((isMonospace ? bottom.length() - 3 : DefaultFontInfo.getStringLength(bottom + "┐")) < targetWidth) {
            bottom += "─";
        }
        bottom += totalPages > 1 ? "┤" : "┐";

        generatedList.add("[gradient=#01b0fb,#f50be5]" + top + "[/gradient]");
        if (pageInfo instanceof String) {
            generatedList.add(title + pageInfoOffset + pageInfo);
        } else {
            ComponentBuilder titleComponent = new ComponentBuilder();
            titleComponent.appendLegacy(PowerRanks.getPowerColor().format(PowerColor.UNFORMATTED_COLOR_CHAR, title, true, false, false));
            titleComponent.append(pageInfoOffset);
            titleComponent.append((BaseComponent[]) pageInfo);
            generatedList.add(titleComponent.create());
        }
        generatedList.add("[gradient=#01b0fb,#f50be5]" + bottom + "[/gradient]");
        return generatedList;
    }

    private List<String> createFooter(int maxItemWidth) {
        int targetWidth = Math.max(internalTitleWidth, maxItemWidth);

        List<String> generatedList = new ArrayList<String>();
        String bottom = "└";
        while ((isMonospace ? bottom.length() - 3 : DefaultFontInfo.getStringLength(bottom + "┐")) < targetWidth) {
            bottom += "─";
        }
        bottom += "┘";

        generatedList.add("[gradient=#01b0fb,#f50be5]" + bottom + "[/gradient]");
        return generatedList;
    }
}