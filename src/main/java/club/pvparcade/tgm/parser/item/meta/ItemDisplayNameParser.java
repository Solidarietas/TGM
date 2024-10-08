package club.pvparcade.tgm.parser.item.meta;

import com.google.gson.JsonObject;
import club.pvparcade.tgm.util.ColorConverter;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Created by Jorge on 09/14/2019
 */
public class ItemDisplayNameParser implements ItemMetaParser {

    @Override
    public ItemMeta parse(ItemStack itemStack, ItemMeta meta, JsonObject object) {
        if (object.has("displayName"))
            meta.setDisplayName(ColorConverter.filterString(object.get("displayName").getAsString()));
        return meta;
    }
}
