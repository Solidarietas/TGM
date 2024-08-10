package club.pvparcade.tgm.modules.portal;

import io.papermc.paper.entity.TeleportFlag;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import club.pvparcade.tgm.TGM;
import club.pvparcade.tgm.modules.region.Region;
import club.pvparcade.tgm.modules.team.MatchTeam;
import club.pvparcade.tgm.modules.team.TeamManagerModule;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

@AllArgsConstructor
public class Portal implements Listener {
    @Getter @Setter
    private boolean active;
    private Type type;
    private Region from;
    private Location to;
    private List<MatchTeam> teams;
    private boolean sound;

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (!active) return;
        if (from == null) return;
        if (from.getBlocks().contains(event.getTo().getBlock()) && !from.getBlocks().contains(event.getFrom().getBlock())) {
            if (!teams.isEmpty()) {

                //allow spectators to use portals
                MatchTeam spectators = TGM.get().getModule(TeamManagerModule.class).getSpectators();
                if (!spectators.containsPlayer(event.getPlayer())) {
                    boolean onTeam = false;

                    for (MatchTeam team : teams) {
                        if (team.containsPlayer(event.getPlayer())) {
                            onTeam = true;
                            break;
                        }
                    }
                    if(!onTeam) return;
                }
            }
            type.teleport(event.getPlayer(), to);
            if (sound) {
                event.getFrom().getWorld().playSound(event.getFrom(), Sound.ENTITY_ENDERMAN_TELEPORT, 0.2f, 1);
                event.getTo().getWorld().playSound(event.getFrom(), Sound.ENTITY_ENDERMAN_TELEPORT, 0.2f, 1);
            }
        }
    }

    public enum Type {
        ABSOLUTE {
            @Override
            public void teleport(Player player, Location location) {
                player.teleportAsync(location);
            }
        }, RELATIVE_ANGLE {
            @Override
            public void teleport(Player player, Location location) {
                location.setYaw(player.getLocation().getYaw());
                location.setPitch(player.getLocation().getPitch());
                player.teleportAsync(location);
            }
        }, RELATIVE_ANGLE_AND_VELOCITY {
            @Override
            public void teleport(Player player, Location location) {
                location.setYaw(player.getLocation().getYaw());
                location.setPitch(player.getLocation().getPitch());
//                Relative location and angle teleport:
//                Location newLocation = player.getLocation().clone().add(location);
//                newLocation.setYaw(newLocation.getYaw() + location.getYaw());
//                newLocation.setPitch(newLocation.getPitch() + location.getPitch());
//                player.teleportAsync(newLocation);
                player.teleportAsync(location,
                    PlayerTeleportEvent.TeleportCause.PLUGIN,
                    TeleportFlag.Relative.X,
                    TeleportFlag.Relative.Y,
                    TeleportFlag.Relative.Z);
            }
        };

        public abstract void teleport(Player player, Location location);
    }
}
