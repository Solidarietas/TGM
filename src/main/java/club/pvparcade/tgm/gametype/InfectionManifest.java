package club.pvparcade.tgm.gametype;

import java.util.ArrayList;
import java.util.List;
import club.pvparcade.tgm.match.MatchManifest;
import club.pvparcade.tgm.match.MatchModule;
import club.pvparcade.tgm.modules.infection.InfectionModule;

/**
 * Created by Draem on 7/31/2017.
 */
public class InfectionManifest extends MatchManifest {

    @Override
    public List<MatchModule> allocateGameModules() {
        return new ArrayList<MatchModule>() {
            {
                add(new InfectionModule());
            }
        };
    }

}
