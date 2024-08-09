package network.warzone.tgm.parser.effect.tag;

import com.google.gson.JsonObject;
import java.util.Locale;
import java.util.Objects;
import network.warzone.tgm.util.Strings;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.potion.PotionEffectType;

/**
 * Created by Jorge on 09/14/2019
 */
public class EffectTypeParser implements EffectTagParser<PotionEffectType> {

    @Override
    public PotionEffectType parse(JsonObject object) {
        String name = Strings.getTechnicalName(object.get("type").getAsString()).toLowerCase();
        return Registry.EFFECT.get(Objects.requireNonNull(NamespacedKey.fromString(name)));
    }
}
