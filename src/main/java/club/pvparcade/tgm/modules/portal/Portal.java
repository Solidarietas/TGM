package club.pvparcade.tgm.modules.portal;

import io.papermc.paper.entity.TeleportFlag;
import java.util.ArrayList;
import java.util.List;
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

public class Portal implements Listener {
    @Getter @Setter
    private boolean active;
    private Region from;
    private Location to;
    private List<MatchTeam> teams;
    private boolean sound;
    private final PortalFlags portalFlags;

    private List<TeleportFlag> teleportFlags = new ArrayList<>();

    public Portal(boolean active, Region from, Location to, List<MatchTeam> teams, boolean sound, PortalFlags portalFlags) {
        this.active = active;
        this.from = from;
        this.to = to;
        this.teams = teams;
        this.sound = sound;
        this.portalFlags = portalFlags;

        if (portalFlags.getFlag(PortalFlags.Flag.RELATIVE_X_VELOCITY)) {
            teleportFlags.add(TeleportFlag.Relative.X);
        }
        if (portalFlags.getFlag(PortalFlags.Flag.RELATIVE_Y_VELOCITY)) {
            teleportFlags.add(TeleportFlag.Relative.Y);
        }
        if (portalFlags.getFlag(PortalFlags.Flag.RELATIVE_Z_VELOCITY)) {
            teleportFlags.add(TeleportFlag.Relative.Z);
        }
        if (portalFlags.getFlag(PortalFlags.Flag.RETAIN_PASSENGERS)) {
            teleportFlags.add(TeleportFlag.EntityState.RETAIN_PASSENGERS);
        }
        if (portalFlags.getFlag(PortalFlags.Flag.RETAIN_VEHICLE)) {
            teleportFlags.add(TeleportFlag.EntityState.RETAIN_VEHICLE);
        }
        if (portalFlags.getFlag(PortalFlags.Flag.RETAIN_OPEN_INVENTORY)) {
            teleportFlags.add(TeleportFlag.EntityState.RETAIN_OPEN_INVENTORY);
        }
    }

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
            this.teleport(event.getPlayer(), to);
            if (sound) {
                event.getFrom().getWorld().playSound(event.getFrom(), Sound.ENTITY_ENDERMAN_TELEPORT, 0.2f, 1);
                event.getTo().getWorld().playSound(event.getFrom(), Sound.ENTITY_ENDERMAN_TELEPORT, 0.2f, 1);
            }
        }
    }

//    public enum Type {
//        ABSOLUTE {
//            @Override
//            public void teleport(Player player, Location location) {
//                player.teleportAsync(location);
//            }
//        }, RELATIVE_ANGLE {
//            @Override
//            public void teleport(Player player, Location location) {
//                location.setYaw(player.getLocation().getYaw());
//                location.setPitch(player.getLocation().getPitch());
//                player.teleportAsync(location);
//            }
//        }, RELATIVE_ANGLE_AND_VELOCITY {
//            @Override
//            public void teleport(Player player, Location location) {
//                location.setYaw(player.getLocation().getYaw());
//                location.setPitch(player.getLocation().getPitch());
////                Relative location and angle teleport:
////                Location newLocation = player.getLocation().clone().add(location);
////                newLocation.setYaw(newLocation.getYaw() + location.getYaw());
////                newLocation.setPitch(newLocation.getPitch() + location.getPitch());
////                player.teleportAsync(newLocation);
//                player.teleportAsync(location,
//                    PlayerTeleportEvent.TeleportCause.PLUGIN,
//                    TeleportFlag.Relative.X,
//                    TeleportFlag.Relative.Y,
//                    TeleportFlag.Relative.Z);
//            }
//        };

    public void teleport(Player player, Location location) {
        Location destination = location.clone();
        if (portalFlags.getFlag(PortalFlags.Flag.RELATIVE_YAW))
            destination.setYaw(player.getLocation().getYaw() + destination.getYaw());
        if (portalFlags.getFlag(PortalFlags.Flag.RELATIVE_PITCH))
            destination.setPitch(player.getLocation().getPitch() + destination.getPitch());
        if (portalFlags.getFlag(PortalFlags.Flag.RELATIVE_X_POSITION))
            destination.setX(player.getLocation().getX() + destination.getX());
        if (portalFlags.getFlag(PortalFlags.Flag.RELATIVE_Y_POSITION))
            destination.setY(player.getLocation().getY() + destination.getY());
        if (portalFlags.getFlag(PortalFlags.Flag.RELATIVE_Z_POSITION))
            destination.setZ(player.getLocation().getZ() + destination.getZ());


        player.teleportAsync(destination,
            PlayerTeleportEvent.TeleportCause.PLUGIN,
            teleportFlags.toArray(new TeleportFlag[0])
            );
    }
}
