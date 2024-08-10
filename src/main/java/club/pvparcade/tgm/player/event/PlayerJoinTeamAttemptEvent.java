package club.pvparcade.tgm.player.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import club.pvparcade.tgm.modules.team.MatchTeam;
import club.pvparcade.tgm.user.PlayerContext;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by Jorge on 11/05/2019
 */
@AllArgsConstructor @Getter
public class PlayerJoinTeamAttemptEvent extends Event implements Cancellable {

    private PlayerContext playerContext;
    private MatchTeam oldTeam;
    @Setter private MatchTeam team;
    private boolean autoJoin;
    @Setter private boolean cancelled;
    protected static final HandlerList handlers = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
