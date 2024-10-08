package club.pvparcade.tgm.parser.item.meta;

import com.google.gson.JsonObject;
import club.pvparcade.tgm.util.itemstack.ItemUtils;
import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;

/**
 * Created by Jorge on 09/14/2019
 */
public class ItemColorParser implements ItemMetaParser {

    @Override
    public ItemMeta parse(ItemStack itemStack, ItemMeta meta, JsonObject object) {
        if (!object.has("color")) return meta;
        String[] rgb = object.get("color").getAsString().replace(" ", "").split(",");
        Color color = Color.fromRGB(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2]));
        if (itemStack.getType().name().contains("LEATHER_")) {
            LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) meta;
            leatherArmorMeta.setColor(color);
            itemStack.setItemMeta(leatherArmorMeta);
            return leatherArmorMeta;
        } else if (ItemUtils.isPotion(itemStack.getType())) {
            PotionMeta potionMeta = (PotionMeta) meta;
            potionMeta.setColor(color);
            itemStack.setItemMeta(potionMeta);
            return potionMeta;
        }
        return meta;
    }
}
