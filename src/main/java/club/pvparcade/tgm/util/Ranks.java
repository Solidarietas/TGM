package club.pvparcade.tgm.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.Getter;
import club.pvparcade.tgm.TGM;
import club.pvparcade.api.models.Rank;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

/**
 * Created by Jorge on 2/23/2018.
 */
public class Ranks {

    @Getter private static Map<UUID, PermissionAttachment> attachments = new HashMap<>();

    public static void update(Rank rank) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            TGM.get().getPlayerManager().getPlayerContext(player).updateRank(rank);
        }
    }

    public static void createAttachment(Player player) {
        attachments.put(player.getUniqueId(), player.addAttachment(TGM.get()));
    }

    public static void addPermissions(Player player, List<String> permissions) {
        permissions.forEach(permission -> addPermission(player, permission));
    }

    public static void addPermission(Player player, String permission) {
        if (attachments.containsKey(player.getUniqueId())) {
            attachments.get(player.getUniqueId()).setPermission(permission, true);
        }
    }

    public static void removePermissions(Player player, List<String> permissions) {
        permissions.forEach(permission -> removePermission(player, permission));
    }

    public static void removePermission(Player player, String permission) {
        if (attachments.containsKey(player.getUniqueId())) {
            attachments.get(player.getUniqueId()).unsetPermission(permission);
        }
    }

    public static void removeAttachment(Player player) {
        attachments.remove(player.getUniqueId());
    }

}
