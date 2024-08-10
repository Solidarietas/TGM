package club.pvparcade.tgm.modules.kit.parser;

import com.google.gson.JsonObject;
import java.util.Collections;
import java.util.List;
import club.pvparcade.tgm.modules.kit.KitNode;
import club.pvparcade.tgm.modules.kit.types.EffectKitNode;
import club.pvparcade.tgm.parser.effect.EffectDeserializer;

public class EffectKitNodeParser implements KitNodeParser {

    @Override
    public List<KitNode> parse(JsonObject jsonObject) {
        return Collections.singletonList(new EffectKitNode(EffectDeserializer.parse(jsonObject)));
    }

}
