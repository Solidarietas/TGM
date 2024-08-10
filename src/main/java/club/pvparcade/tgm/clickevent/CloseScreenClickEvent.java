package club.pvparcade.tgm.clickevent;

import club.pvparcade.tgm.match.Match;
import org.bukkit.entity.Player;

/**
 * Created by Jorge on 10/11/2019
 */
public class CloseScreenClickEvent extends ClickEvent {

    @Override
    public void run(Match match, Player player) {
        player.closeInventory();
    }
}
