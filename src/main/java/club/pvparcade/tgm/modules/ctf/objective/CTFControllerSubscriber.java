package club.pvparcade.tgm.modules.ctf.objective;

import club.pvparcade.tgm.modules.team.MatchTeam;

/**
 * Represents a subscriber to a CTFController
 * Created by yikes on 12/15/2019
 */
public interface CTFControllerSubscriber {
    /**
     * Called when controller says game is over
     * @param team Team who won
     */
    void gameOver(MatchTeam team);
}
