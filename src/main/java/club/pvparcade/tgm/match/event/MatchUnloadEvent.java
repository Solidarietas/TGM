package club.pvparcade.tgm.match.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import club.pvparcade.tgm.match.Match;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * This event fires when the match unloads.
 */

@AllArgsConstructor
public class MatchUnloadEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    @Getter private Match match;

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
