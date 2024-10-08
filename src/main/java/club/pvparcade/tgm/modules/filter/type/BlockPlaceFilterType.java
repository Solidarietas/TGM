package club.pvparcade.tgm.modules.filter.type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import club.pvparcade.tgm.match.Match;
import club.pvparcade.tgm.modules.filter.FilterManagerModule;
import club.pvparcade.tgm.modules.filter.FilterResult;
import club.pvparcade.tgm.modules.filter.evaluate.FilterEvaluator;
import club.pvparcade.tgm.modules.region.Region;
import club.pvparcade.tgm.modules.region.RegionManagerModule;
import club.pvparcade.tgm.modules.team.MatchTeam;
import club.pvparcade.tgm.modules.team.TeamManagerModule;
import club.pvparcade.tgm.util.Strings;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

/**
 * Created by Jorge on 10/02/2019
 */
@AllArgsConstructor @Getter
public class BlockPlaceFilterType implements FilterType, Listener {

    private final List<MatchTeam> teams;
    private final List<Region> regions;
    private final FilterEvaluator evaluator;
    private final String message;
    private final List<Material> blocks;
    private final boolean inverted;

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockPlaceEvent(BlockPlaceEvent event) {
        for (Region region : regions) {
            if (contains(region, event.getBlockPlaced().getLocation())) {
                for (MatchTeam matchTeam : teams) {
                    if (matchTeam.containsPlayer(event.getPlayer())) {
                        FilterResult filterResult = evaluator.evaluate(event.getPlayer());
                        if (!canPlace(event, filterResult)) {
                            event.setCancelled(true);
                            if (message != null) event.getPlayer().sendMessage(message);
                        }
                        break;
                    }
                }
            }
        }
    }

    private boolean contains(Region region, Location location) {
        return (!inverted && region.contains(location)) || (inverted && !region.contains(location));
    }

    private boolean canPlace(BlockPlaceEvent event, FilterResult filterResult) {
        if (filterResult == FilterResult.ALLOW) {
            if (blocks == null || blocks.isEmpty()) return true;
            return blocks.contains(event.getBlockPlaced().getType());
        } else {
            if (blocks == null || blocks.isEmpty()) return false;
            return !blocks.contains(event.getBlockPlaced().getType());
        }
    }

    public static BlockPlaceFilterType parse(Match match, JsonObject jsonObject) {
        List<MatchTeam> matchTeams = match.getModule(TeamManagerModule.class).getTeams(jsonObject.get("teams").getAsJsonArray());
        List<Region> regions = new ArrayList<>();
        List<Material> blocks = new ArrayList<>();

        for (JsonElement regionElement : jsonObject.getAsJsonArray("regions")) {
            Region region = match.getModule(RegionManagerModule.class).getRegion(match, regionElement);
            if (region != null) {
                regions.add(region);
            }
        }
        if (jsonObject.has("blocks"))
            for (JsonElement materialElement : jsonObject.getAsJsonArray("blocks")) {
                if (!materialElement.isJsonPrimitive()) continue;
                Material material = Material.getMaterial(Strings.getTechnicalName(materialElement.getAsString()));
                if (material == null) continue;
                blocks.add(material);
            }

        FilterEvaluator filterEvaluator = FilterManagerModule.initEvaluator(match, jsonObject);
        String message = jsonObject.has("message") ? ChatColor.translateAlternateColorCodes('&', jsonObject.get("message").getAsString()) : null;
        boolean inverted = jsonObject.has("inverted") && jsonObject.get("inverted").getAsBoolean();
        return new BlockPlaceFilterType(matchTeams, regions, filterEvaluator, message, blocks, inverted);
    }
}
