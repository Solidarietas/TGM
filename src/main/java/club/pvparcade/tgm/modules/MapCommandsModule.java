package club.pvparcade.tgm.modules;

import com.google.gson.JsonElement;
import java.util.ArrayList;
import java.util.List;
import club.pvparcade.tgm.TGM;
import club.pvparcade.tgm.match.Match;
import club.pvparcade.tgm.match.MatchModule;

// TODO: Make a better event actions module
public class MapCommandsModule extends MatchModule {

    private List<String> startCommands = new ArrayList<>();

    @Override
    public void enable() {
        if (this.startCommands != null)
            startCommands.forEach(c -> TGM.get().getServer().dispatchCommand(TGM.get().getServer().getConsoleSender(), c));
    }

    @Override
    public void load(Match match) {
        if (match.getMapContainer().getMapInfo().getJsonObject().has("startCommands")) {
            for (JsonElement jsonElement : match.getMapContainer().getMapInfo().getJsonObject().getAsJsonArray("startCommands")) {
                if (jsonElement.isJsonPrimitive()) continue;
                this.startCommands.add(jsonElement.getAsString());
            }
        }
    }
}
