package club.pvparcade.tgm.clickevent;

import club.pvparcade.tgm.match.Match;
import club.pvparcade.tgm.modules.screens.ScreenManagerModule;
import org.bukkit.entity.Player;

/**
 * Created by Jorge on 10/11/2019
 */
public class OpenScreenClickEvent extends ClickEvent {

    private String screen;

    public OpenScreenClickEvent(String id) {
        this.screen = id;
    }

    @Override
    public void run(Match match, Player player) {
        match.getModule(ScreenManagerModule.class).getScreen(this.screen).openInventory(player);
    }
}
