package network.warzone.tgm.util;

import lombok.Getter;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

/**
 * Created by Jorge on 09/28/2019
 */
public class Plugins {
    private static final String WORLD_EDIT = "WorldEdit";
    private static final String VAULT = "Vault";

    @Getter private static final boolean protocolSupportPresent = false;
    @Getter private static boolean worldEditPresent = false;
    @Getter private static boolean vaultPresent = false;

    public static void checkSoftDependencies() {
        worldEditPresent        = isPresent(WORLD_EDIT);
        vaultPresent            = isPresent(VAULT);
    }

    public static boolean isPresent(String name) {
        return Bukkit.getPluginManager().isPluginEnabled(name);
    }

    public static class ProtocolSupport {

        // TODO replace with Via Version support
        public static boolean isUsingOldVersion(Player player) {
            return false;
        }

//        public static boolean usingVersionOrNewer(Player player, ProtocolVersion version) {
//            ProtocolVersion playerVersion = ProtocolSupportAPI.getProtocolVersion(player);
//            return version != ProtocolVersion.UNKNOWN && !playerVersion.isBefore(version);
//        }
    }

    public static class Vault {

        private static final RegisteredServiceProvider<Chat> chatProvider = Bukkit.getServer().getServicesManager().getRegistration(Chat.class);

        public static String getPrefix(Player player) {
            if (chatProvider == null) return null;
            return chatProvider.getProvider().getPlayerPrefix(player);
        }

    }

}
