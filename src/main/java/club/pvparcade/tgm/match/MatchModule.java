package club.pvparcade.tgm.match;

import java.util.logging.Logger;
import lombok.Getter;
import org.bukkit.Bukkit;

/**
 * Created by luke on 4/27/17.
 */
@Getter
public abstract class MatchModule {

    private Logger logger = Bukkit.getLogger();

    /**
     * Called immediately after a match is loaded.
     * The map is loaded but no players have been added yet.
     */
    public void load(Match match) {

    }

    /**
     * Called immediately before a match is unloaded.
     * The map is still loaded but all players have
     * already been sent to the next match.
     */
    public void unload() {

    }

    /**
     * Called when the match starts
     */
    public void enable() {

    }

    /**
     * Called when the match ends
     */
    public void disable() {

    }
}
