package club.pvparcade.tgm.modules.visibility;

import java.util.function.Function;
import lombok.Getter;
import club.pvparcade.tgm.TGM;
import club.pvparcade.tgm.match.Match;
import club.pvparcade.tgm.match.MatchModule;
import club.pvparcade.tgm.modules.SpectatorModule;
import club.pvparcade.tgm.modules.team.event.TeamChangeEvent;
import club.pvparcade.tgm.player.event.TGMPlayerDeathEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;

@Getter
public class VisibilityModule extends MatchModule implements Listener {
    @Getter
    private static Function<Match, VisibilityController> visibilityControllerProvider =
            match -> new VisibilityControllerImpl(match.getModule(SpectatorModule.class));

    public static void setVisibilityControllerProvider(Function<Match, VisibilityController> newProvider) {
        visibilityControllerProvider = newProvider;
        VisibilityModule module = TGM.get().getModule(VisibilityModule.class);
        if (module != null) module.load(module.match);
    }

    private Match match;
    private VisibilityController visibilityController;

    @Override
    public void load(Match match) {
        this.match = match;
        visibilityController = visibilityControllerProvider.apply(match);
        refreshAllPlayers();
    }

    @EventHandler
    public void onDeath(TGMPlayerDeathEvent event) {
        refreshPlayer(event.getVictim());
    }

    @EventHandler
    public void onTeamChange(TeamChangeEvent event) {
        if (event.isCancelled()) return;
        if (event.getOldTeam() != null) refreshPlayer(event.getPlayerContext().getPlayer());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        refreshPlayer(event.getPlayer());
    }

    @EventHandler
    public void onWorldSwitch(PlayerChangedWorldEvent event) {
        refreshPlayer(event.getPlayer());
    }

    public void refreshPlayer(Player player) {
        if (player == null) return;

        // Update who can see who
        Bukkit.getOnlinePlayers().stream().filter(looker -> !looker.equals(player)).forEach(looker -> {
            if (visibilityController.canSee(looker, player)) {
                looker.showPlayer(TGM.get(), player);
            } else {
                looker.hidePlayer(TGM.get(), player);
            }

            if (visibilityController.canSee(player, looker)) {
                player.showPlayer(TGM.get(), looker);
            } else {
                player.hidePlayer(TGM.get(), looker);
            }
        });
    }

    public void refreshAllPlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            refreshPlayer(player);
        }
    }
}