package club.pvparcade.tgm.gametype;

import club.pvparcade.tgm.match.MatchManifest;
import club.pvparcade.tgm.match.MatchModule;
import club.pvparcade.tgm.modules.ctf.CTFModule;

import java.util.ArrayList;
import java.util.List;

public class KOTFManifest extends MatchManifest {
    @Override
    public List<MatchModule> allocateGameModules() {
        List<MatchModule> matchModules = new ArrayList<>();
        matchModules.add(new CTFModule());
        return matchModules;
    }
}
