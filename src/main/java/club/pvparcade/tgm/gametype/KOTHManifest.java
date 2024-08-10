package club.pvparcade.tgm.gametype;

import java.util.ArrayList;
import java.util.List;
import club.pvparcade.tgm.match.MatchManifest;
import club.pvparcade.tgm.match.MatchModule;
import club.pvparcade.tgm.modules.koth.KOTHModule;

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
