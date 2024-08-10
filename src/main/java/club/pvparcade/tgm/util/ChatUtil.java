package club.pvparcade.tgm.util;

import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Collection;

public class ChatUtil {
    public static void sendChatComponents(Collection<Player> players, Component[] components) {
        for (Player player : players) sendChatComponents(player, components);
    }

    public static void sendChatComponents(Player player, Component[] components) {
        CraftPlayer obcPlayer = (CraftPlayer) player;
        for (Component component : components) {
            obcPlayer.getHandle().connection.connection.send(new ClientboundSystemChatPacket(
                component,
                false)
            );
        }
    }

}
