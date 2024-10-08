package club.pvparcade.tgm.modules.filter.type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerShearEntityEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vice & Thrasilias on 9/14/2018.
 */

@AllArgsConstructor @Getter
public class UseShearFilterType implements FilterType, Listener {

    private final List<MatchTeam> teams;
    private final List<Region> regions;
    private final FilterEvaluator evaluator;
    private final String message;
    private final boolean inverted;

    @EventHandler
    public void onShear(PlayerShearEntityEvent e) {
        for (Region region : regions) {
            if (contains(region, e.getPlayer().getLocation())) {
                for (MatchTeam matchTeam : teams) {
                    if (matchTeam.containsPlayer(e.getPlayer())) {
                        FilterResult filterResult = evaluator.evaluate(e.getPlayer());
                        if (filterResult == FilterResult.DENY) {
                            e.setCancelled(true);
                            if (message != null) e.getPlayer().sendMessage(message);
                        } else if (filterResult == FilterResult.ALLOW) {
                            e.setCancelled(false);
                        }
                    }
                }
            }
        }
    }

    private boolean contains(Region region, Location location) {
        return (!inverted && region.contains(location)) || (inverted && !region.contains(location));
    }

    public static UseShearFilterType parse(Match match, JsonObject jsonObject) {
        List<MatchTeam> matchTeams = match.getModule(TeamManagerModule.class).getTeams(jsonObject.get("teams").getAsJsonArray());
        List<Region> regions = new ArrayList<>();

        for (JsonElement regionElement : jsonObject.getAsJsonArray("regions")) {
            Region region = match.getModule(RegionManagerModule.class).getRegion(match, regionElement);
            if (region != null) {
                regions.add(region);
            }
        }

        FilterEvaluator filterEvaluator = FilterManagerModule.initEvaluator(match, jsonObject);
        String message = jsonObject.has("message") ? ChatColor.translateAlternateColorCodes('&', jsonObject.get("message").getAsString()) : null;
        boolean inverted = jsonObject.has("inverted") && jsonObject.get("inverted").getAsBoolean();
        return new UseShearFilterType(matchTeams, regions, filterEvaluator, message, inverted);
    }
}
