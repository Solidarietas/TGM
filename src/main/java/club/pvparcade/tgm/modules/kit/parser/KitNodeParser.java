package club.pvparcade.tgm.modules.kit.parser;

import com.google.gson.JsonObject;
import club.pvparcade.tgm.modules.kit.KitNode;

import java.util.List;

public interface KitNodeParser {
    List<KitNode> parse(JsonObject jsonObject);
}
