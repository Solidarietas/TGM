package club.pvparcade.tgm.clickevent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import club.pvparcade.tgm.match.Match;
import club.pvparcade.tgm.util.Placeholders;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Created by Jorge on 10/11/2019
 */
public class RunAsConsoleClickEvent extends ClickEvent {

    private List<String> commands;

    public RunAsConsoleClickEvent(List<String> commands) {
        this.commands = commands;
    }

    @Override
    public void run(Match match, Player player) {
        Map<String, String> placeholders = new HashMap<>();
        Placeholders.addPlaceholders(placeholders, match);
        Placeholders.addPlaceholders(placeholders, player);
        commands.forEach(command -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), Placeholders.apply(command, placeholders)));
    }

}
