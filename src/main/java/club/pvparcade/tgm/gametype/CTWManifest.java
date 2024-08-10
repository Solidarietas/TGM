package club.pvparcade.tgm.gametype;

import club.pvparcade.tgm.match.MatchManifest;
import club.pvparcade.tgm.match.MatchModule;
import club.pvparcade.tgm.modules.ctw.CTWModule;
import club.pvparcade.tgm.modules.wool.WoolChestModule;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luke on 4/27/17.
 */
public class CTWManifest extends MatchManifest {

    @Override
    public List<MatchModule> allocateGameModules() {
        List<MatchModule> matchModules = new ArrayList<>();
        matchModules.add(new WoolChestModule());
        matchModules.add(new CTWModule());
        return matchModules;
    }
}
