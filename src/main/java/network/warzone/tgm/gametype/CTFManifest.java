package network.warzone.tgm.gametype;

import java.util.ArrayList;
import java.util.List;
import network.warzone.tgm.match.MatchManifest;
import network.warzone.tgm.match.MatchModule;
import network.warzone.tgm.modules.ctf.CTFModule;

public class CTFManifest extends MatchManifest {
    @Override
    public List<MatchModule> allocateGameModules() {
        List<MatchModule> matchModules = new ArrayList<>();
        matchModules.add(new CTFModule());
        return matchModules;
    }
}
