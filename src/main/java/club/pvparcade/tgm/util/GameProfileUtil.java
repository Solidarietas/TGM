package club.pvparcade.tgm.util;

import com.mojang.authlib.GameProfile;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

public class GameProfileUtil {
    public static void setGameProfileField(GameProfile obj, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field field = GameProfile.class.getDeclaredField(fieldName);
        field.setAccessible(true);

        field.set(obj, value);
    }

    public static GameProfile getGameProfile(Player player) {
        return ((CraftPlayer) player).getHandle().getGameProfile();
    }
}
