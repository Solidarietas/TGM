package network.warzone.tgm.gametype;

import java.util.ArrayList;
import java.util.List;
import network.warzone.tgm.match.MatchManifest;
import network.warzone.tgm.match.MatchModule;
import network.warzone.tgm.modules.tdm.TDMModule;

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
