package nl.svenar.powerranks.bukkit.events.prevents;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import nl.svenar.powerranks.common.structure.PRPlayer;
import nl.svenar.powerranks.common.structure.PRRank;

public class RankChangeEvent extends Event {
    
    private static final HandlerList handlers = new HandlerList();

    private PRPlayer player;

    private PRRank newRank;

    private String[] tags;

    private boolean cancelled = false;

    public RankChangeEvent(PRPlayer player, PRRank newRank, String[] tags) {
        this.player = player;
        this.newRank = newRank;
        this.tags = tags == null ? new String[0] : tags;
    }

    public PRPlayer getPlayer() {
        return player;
    }

    public PRRank getNewRank() {
        return newRank;
    }

    public String[] getTags() {
        return tags;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
    
}
