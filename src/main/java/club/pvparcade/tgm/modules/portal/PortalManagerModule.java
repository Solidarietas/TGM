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

            PortalFlags flags = new PortalFlags();
            if (json.has("flags")) {
                JsonObject flagsObject = json.getAsJsonObject("flags");

                for (String key : flagsObject.keySet()) {
                    boolean value = flagsObject.get(key).getAsBoolean();

                    switch (key) {
                        case "relative-x-position":
                            flags.setFlag(PortalFlags.Flag.RELATIVE_X_POSITION, value);
                            break;
                        case "relative-y-position":
                            flags.setFlag(PortalFlags.Flag.RELATIVE_Y_POSITION, value);
                            break;
                        case "relative-z-position":
                            flags.setFlag(PortalFlags.Flag.RELATIVE_Z_POSITION, value);
                            break;
                        case "relative-x-velocity":
                            flags.setFlag(PortalFlags.Flag.RELATIVE_X_VELOCITY, value);
                            break;
                        case "relative-y-velocity":
                            flags.setFlag(PortalFlags.Flag.RELATIVE_Y_VELOCITY, value);
                            break;
                        case "relative-z-velocity":
                            flags.setFlag(PortalFlags.Flag.RELATIVE_Z_VELOCITY, value);
                            break;
                        case "relative-yaw":
                            flags.setFlag(PortalFlags.Flag.RELATIVE_YAW, value);
                            break;
                        case "relative-pitch":
                            flags.setFlag(PortalFlags.Flag.RELATIVE_PITCH, value);
                            break;
                        case "retain-passengers":
                            flags.setFlag(PortalFlags.Flag.RETAIN_PASSENGERS, value);
                            break;
                        case "retain-vehicle":
                            flags.setFlag(PortalFlags.Flag.RETAIN_VEHICLE, value);
                            break;
                        case "retain-open-inventory":
                            flags.setFlag(PortalFlags.Flag.RETAIN_OPEN_INVENTORY, value);
                            break;
                        default:
                            System.out.println("Unknown flag: " + key);
                            break;
                    }
                }
            }

            Portal portal = new Portal(active, from, to, teams, sound, flags);

            TGM.registerEvents(portal);
            allPortals.add(portal);
            if (json.has("id")) {
                identifiablePortals.put(json.get("id").getAsString(), portal);
            }

            return portal;
        }
    }
}
