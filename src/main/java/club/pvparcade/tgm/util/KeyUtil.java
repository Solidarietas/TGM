package club.pvparcade.tgm.util;

import club.pvparcade.tgm.TGM;
import org.bukkit.NamespacedKey;

/**
 * Created by Jorge on 07/09/2020
 */
public class KeyUtil {

    public static NamespacedKey minecraft(String s) {
        return NamespacedKey.minecraft(s.toLowerCase().replace(" ", "_"));
    }

    public static NamespacedKey tgm(String s) {
        return new NamespacedKey(TGM.get(), s.toLowerCase().replace(" ", "_"));
    }

}
