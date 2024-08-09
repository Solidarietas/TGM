package network.warzone.tgm.parser.item.meta;

//import com.destroystokyo.paper.Namespaced;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import network.warzone.tgm.util.KeyUtil;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Created by Jorge on 03/22/2020
 */
public class ItemCanPlaceOnParser extends ItemCanPlaceDestroyOnParserParent {

    @Override
    public ItemMeta parse(ItemStack itemStack, ItemMeta meta, JsonObject object) {
        return super.parseDestroyPlace(itemStack, meta, object, "canPlaceOn", "can_place_on");
    }
}
