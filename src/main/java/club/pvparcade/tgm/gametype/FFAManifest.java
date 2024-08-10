package club.pvparcade.tgm.gametype;

import club.pvparcade.tgm.match.MatchManifest;
import club.pvparcade.tgm.match.MatchModule;
import club.pvparcade.tgm.modules.ffa.FFAModule;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jorge on 3/2/2018.
 */
public class FFAManifest extends MatchManifest {

    @Override
    public List<MatchModule> allocateGameModules() {
        List<MatchModule> matchModules = new ArrayList<>();
        matchModules.add(new FFAModule());
        return matchModules;
    }
}
