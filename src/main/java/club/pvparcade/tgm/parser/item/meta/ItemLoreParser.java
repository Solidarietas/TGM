package club.pvparcade.tgm.parser.item.meta;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import club.pvparcade.tgm.util.ColorConverter;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jorge on 09/14/2019
 */
public class ItemLoreParser implements ItemMetaParser {

    @Override
    public ItemMeta parse(ItemStack itemStack, ItemMeta meta, JsonObject object) {
        if (object.has("lore")) {
            List<String> lore = new ArrayList<>();
            for (JsonElement element : object.getAsJsonArray("lore")) {
                lore.add(ColorConverter.filterString(element.getAsString()));
            }
            meta.setLore(lore);
        }
        return meta;
    }
}
