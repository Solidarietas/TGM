package club.pvparcade.tgm.gametype;

import club.pvparcade.tgm.match.MatchManifest;
import club.pvparcade.tgm.match.MatchModule;
import club.pvparcade.tgm.modules.blitz.BlitzModule;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jorge on 10/7/2017.
 */
public class BlitzManifest extends MatchManifest {

    @Override
    public List<MatchModule> allocateGameModules() {
        List<MatchModule> matchModules = new ArrayList<>();
        matchModules.add(new BlitzModule());
        return matchModules;
    }
}
