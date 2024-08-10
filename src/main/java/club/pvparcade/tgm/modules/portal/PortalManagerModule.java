package club.pvparcade.tgm.modules.portal;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import lombok.Getter;
import club.pvparcade.tgm.TGM;
import club.pvparcade.tgm.match.Match;
import club.pvparcade.tgm.match.MatchModule;
import club.pvparcade.tgm.match.ModuleData;
import club.pvparcade.tgm.match.ModuleLoadTime;
import club.pvparcade.tgm.modules.region.Region;
import club.pvparcade.tgm.modules.region.RegionManagerModule;
import club.pvparcade.tgm.modules.team.MatchTeam;
import club.pvparcade.tgm.modules.team.TeamManagerModule;
import club.pvparcade.tgm.util.Parser;
import club.pvparcade.tgm.util.Strings;
import org.bukkit.Location;

@ModuleData(load = ModuleLoadTime.EARLY) @Getter
public class PortalManagerModule extends MatchModule {
    private final HashSet<Portal> allPortals = new HashSet<>();
    private final HashMap<String, Portal> identifiablePortals = new HashMap<>();

    @Override
    public void load(Match match) {
        if (match.getMapContainer().getMapInfo().getJsonObject().has("portals")) {
            for (JsonElement portalElement : match.getMapContainer().getMapInfo().getJsonObject().getAsJsonArray("portals")) {
                getPortal(match, portalElement);
            }
        }
    }

    @Override
    public void unload() {
        allPortals.forEach(TGM::unregisterEvents);
        allPortals.clear();
        identifiablePortals.clear();
    }

    public Portal getPortal(Match match, JsonElement jsonElement) {
        if (jsonElement.isJsonPrimitive()) {
            return identifiablePortals.get(jsonElement.getAsString());
        } else {
            JsonObject json = jsonElement.getAsJsonObject();

            boolean active = true;
            if (json.has("active")) {
                active = json.get("active").getAsBoolean();
            }

            Portal.Type type = Portal.Type.RELATIVE_ANGLE_AND_VELOCITY;
            if (json.has("type")) {
                type = Portal.Type.valueOf(Strings.getTechnicalName(json.get("type").getAsString()));
            }
            Region from = TGM.get().getModule(RegionManagerModule.class).getRegion(match, json.get("from"));
            Location to = Parser.convertLocation(match.getWorld(), json.get("to"));

            List<MatchTeam> teams = new ArrayList<>();
            if (json.has("teams")) {
                for (JsonElement teamElement : json.getAsJsonArray("teams")) {
                    teams.add(TGM.get().getModule(TeamManagerModule.class).getTeamById(teamElement.getAsString()));
                }
            }

            boolean sound = true;
            if (json.has("sound")) {
                sound = json.get("sound").getAsBoolean();
            }

            Portal portal = new Portal(active, type, from, to, teams, sound);
            TGM.registerEvents(portal);
            allPortals.add(portal);
            if (json.has("id")) {
                identifiablePortals.put(json.get("id").getAsString(), portal);
            }

            return portal;
        }
    }
}
