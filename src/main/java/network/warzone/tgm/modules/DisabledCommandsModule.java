package network.warzone.tgm.modules;

import network.warzone.tgm.match.MatchModule;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.HashSet;
import java.util.Set;

public class DisabledCommandsModule extends MatchModule implements Listener {

    private final Set<String> disabledCommands = new HashSet<>();

    public DisabledCommandsModule() {
        disabledCommands.add("/me");
        disabledCommands.add("/minecraft:me");
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if (disabledCommands.contains(event.getMessage().split(" ")[0].toLowerCase())) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.RED + event.getMessage().split(" ")[0] + " is a disabled command.");
        }
    }

    @Override
    public void unload() {
        disabledCommands.clear();
    }
}
