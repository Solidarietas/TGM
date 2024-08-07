package network.warzone.tgm.gametype;

import java.util.ArrayList;
import java.util.List;
import network.warzone.tgm.match.MatchManifest;
import network.warzone.tgm.match.MatchModule;
import network.warzone.tgm.modules.koth.KOTHModule;

/**
 * Created by luke on 4/27/17.
 */
public class KOTHManifest extends MatchManifest {

    @Override
    public List<MatchModule> allocateGameModules() {
        List<MatchModule> matchModules = new ArrayList<>();
        matchModules.add(new KOTHModule());
        return matchModules;
    }
}
