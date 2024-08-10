package club.pvparcade.tgm.map;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import club.pvparcade.tgm.TGM;
import club.pvparcade.tgm.gametype.GameType;
import club.pvparcade.tgm.nickname.ProfileCache;
import club.pvparcade.tgm.util.Strings;
import club.pvparcade.api.models.Author;
import club.pvparcade.api.models.MojangProfile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;

/**
 * Created by luke on 4/27/17.
 */
public class MapInfoDeserializer implements JsonDeserializer<MapInfo> {
    @Override
    public MapInfo deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject json = jsonElement.getAsJsonObject();
        String name = json.get("name").getAsString();
        String version = json.get("version").getAsString();
        String objective = json.has("objective") ? json.get("objective").getAsString() : null;
        List<Author> authors = new ArrayList<>();
        for (JsonElement authorJson : json.getAsJsonArray("authors")) {
            if (authorJson.isJsonPrimitive()) {
                authors.add(new Author(authorJson.getAsString()));
            } else {
                Author author = TGM.get().getGson().fromJson(authorJson, Author.class);
                if (TGM.get().getConfig().getBoolean("map.get-names"))
                    Bukkit.getScheduler().runTaskAsynchronously(TGM.get(), () -> {
                        if (author != null && author.getUuid() != null) {
                            try {
                                MojangProfile profile = ProfileCache.getInstance().get(author.getUuid());
                                if (profile == null) {
                                    profile = TGM.get().getTeamClient().getMojangProfile(author.getUuid());
                                    ProfileCache.getInstance().add(profile);
                                }
                                if (profile != null) {
                                    author.setDisplayUsername(profile.getUsername());
                                }
                            } catch (Exception e) {
                                TGM.get().getLogger().warning("Could not retrieve current name for " + author.getUuid().toString() + " on map " + name);
                            }
                        }
                    });
                authors.add(author);
            }
        }
        GameType gameType = GameType.valueOf(json.get("gametype").getAsString());
        List<ParsedTeam> parsedTeams = new ArrayList<>();
        for (JsonElement teamElement : json.getAsJsonArray("teams")) {
            JsonObject teamJson = teamElement.getAsJsonObject();
            String teamId = teamJson.get("id").getAsString();
            String teamName = teamJson.get("name").getAsString();
            ChatColor teamColor = ChatColor.valueOf(teamJson.get("color").getAsString().toUpperCase().replace(" ", "_"));
            GameMode teamGamemode = teamJson.has("gamemode") ? GameMode.valueOf(Strings.getTechnicalName(teamJson.get("gamemode").getAsString())) : GameMode.SURVIVAL;
            int teamMax = teamJson.get("max").getAsInt();
            int teamMin = teamJson.get("min").getAsInt();
            boolean friendlyFire = teamJson.has("friendlyFire") && teamJson.get("friendlyFire").getAsBoolean();
            parsedTeams.add(new ParsedTeam(teamId, teamName, teamColor, teamGamemode, teamMax, teamMin, friendlyFire));
        }

        return new MapInfo(name, version, objective, authors, gameType, parsedTeams, json);
    }

}
