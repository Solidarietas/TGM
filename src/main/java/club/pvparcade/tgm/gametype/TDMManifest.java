package club.pvparcade.tgm.gametype;

import java.util.ArrayList;
import java.util.List;
import club.pvparcade.tgm.match.MatchManifest;
import club.pvparcade.tgm.match.MatchModule;
import club.pvparcade.tgm.modules.tdm.TDMModule;

/**
 * Created by luke on 4/27/17.
 */
public class TDMManifest extends MatchManifest {

    @Override
    public List<MatchModule> allocateGameModules() {
        List<MatchModule> matchModules = new ArrayList<>();
        matchModules.add(new TDMModule());
        return matchModules;
    }
}
