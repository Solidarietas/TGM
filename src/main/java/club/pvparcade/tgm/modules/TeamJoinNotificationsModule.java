package club.pvparcade.tgm.modules;

import net.md_5.bungee.api.ChatColor;
import club.pvparcade.tgm.match.MatchModule;
import club.pvparcade.tgm.modules.team.event.TeamChangeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class TeamJoinNotificationsModule extends MatchModule implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onTeamJoin(TeamChangeEvent event) {
        if (!event.isCancelled() && !event.isSilent())
            event.getPlayerContext().getPlayer().sendMessage(ChatColor.WHITE + "You joined " + event.getTeam().getColor() + ChatColor.BOLD + event.getTeam().getAlias());
    }
}
