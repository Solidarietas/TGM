package club.pvparcade.tgm.parser.item.meta;

//import com.destroystokyo.paper.Namespaced;
import com.google.gson.JsonObject;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Created by Jorge on 03/22/2020
 */
public class ItemCanDestroyOnParser extends ItemCanPlaceDestroyOnParserParent {

    @Override
    public ItemMeta parse(ItemStack itemStack, ItemMeta meta, JsonObject object) {
       return super.parseDestroyPlace(itemStack, meta, object, "canDestroy", "can_break");
    }
}
