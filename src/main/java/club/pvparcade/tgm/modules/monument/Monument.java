package club.pvparcade.tgm.modules.monument;

import club.pvparcade.tgm.modules.SpawnPointHandlerModule;
import club.pvparcade.tgm.modules.respawn.RespawnModule;
import club.pvparcade.tgm.modules.team.TeamManagerModule;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import club.pvparcade.tgm.TGM;
import club.pvparcade.tgm.match.Match;
import club.pvparcade.tgm.match.MatchStatus;
import club.pvparcade.tgm.modules.region.Region;
import club.pvparcade.tgm.modules.team.MatchTeam;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;

@AllArgsConstructor @Getter
public class Monument implements Listener {

    private WeakReference<Match> match;
    private String name;

    private final List<MatchTeam> owners;

    private final Region region;

    private final List<Material> materials;

    private int health;
    private int maxHealth;

    private final List<MonumentService> services = new ArrayList<>();

    private RespawnModule respawnModule;
    private SpawnPointHandlerModule spawnPointHandlerModule;
    private TeamManagerModule teamManagerModule;

    // Used in hacky fix to prevent monument breaking after death bug. Check to see if player within
    // spawnCheckRadius of their team's spawn when a BlockBreakEvent for a monument is fired.
    // Defaults to 3 blocks
    private int spawnCheckRadius;

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockBreak(BlockBreakEvent event) {
        if (region.contains(event.getBlock().getLocation())) {
            if (materials == null || materials.contains(event.getBlock().getType())) {
                if (!canDamage(event.getPlayer())) {
                    event.getPlayer().sendMessage(ChatColor.RED + "You cannot damage a monument you own.");
                }
                event.setCancelled(true); // override filters (event gets ignored)
            }
        }
    }

    /*
    Prevents filter messages
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreakHighest(BlockBreakEvent event) {
        // Prevents players in "spectator" state after death from mining monuments
        // By checking if they are dead
        if (respawnModule.isDead(event.getPlayer())) {
            event.setCancelled(true);
            return;
        }

        if (region.contains(event.getBlock().getLocation())) {
            // Or have just respawned (distance from respawn point < x) and breaking block in monument region, which should be impossible
            if (event.getPlayer().getLocation().distance(spawnPointHandlerModule.getTeamSpawn(teamManagerModule.getTeam(event.getPlayer())).getLocation()) < spawnCheckRadius) {
                event.setCancelled(true);
                return;
            }
            if (materials == null || materials.contains(event.getBlock().getType())) {
                if (canDamage(event.getPlayer())) {
                    Match match = this.match.get();
                    if (match != null && match.getMatchStatus().equals(MatchStatus.MID)) {
                        event.setCancelled(false); //override filters
                        event.getBlock().getDrops().clear();

                        health--;

                        if (health < 0) {
                            event.getPlayer().sendMessage(ChatColor.RED + "This monument is already destroyed.");
                        } else if (health == 0) {
                            for (MonumentService monumentService : services) {
                                monumentService.destroy(event.getPlayer(), event.getBlock());
                            }
                        } else {
                            for (MonumentService monumentService : services) {
                                monumentService.damage(event.getPlayer(), event.getBlock());
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onBlockBurn(BlockBurnEvent event) {
        if (region.contains(event.getBlock().getLocation())) {
            if (materials == null || materials.contains(event.getBlock().getType())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockIgniteEvent(BlockIgniteEvent event) {
        if (region.contains(event.getBlock().getLocation())) {
            event.setCancelled(true);
        }
    }
    @EventHandler(priority = EventPriority.HIGH)
    public void onPistonRetract(BlockPistonRetractEvent event) {
        if (region.contains(event.getBlock().getLocation())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPistonExtend(BlockPistonExtendEvent event) {
        if (region.contains(event.getBlock().getLocation())) {
            event.setCancelled(true);
        }
    }

    public boolean isAlive() {
        return this.health > 0;
    }

    public int getHealthPercentage() {
        return Math.min(100, Math.max(0, (health * 100) / maxHealth));
    }

    public void load() {
        TGM.registerEvents(this);
    }

    public void unload() {
        HandlerList.unregisterAll(this);

        owners.clear();
        materials.clear();
        services.clear();
    }


    public boolean canDamage(Player player) {
        for (MatchTeam matchTeam : owners) {
            if (matchTeam.containsPlayer(player)) {
                return false;
            }
        }
        return true;
    }

    public void addService(MonumentService monumentService) {
        this.services.add(monumentService);
    }
}
