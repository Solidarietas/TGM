package club.pvparcade.tgm.parser.item.meta;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import club.pvparcade.tgm.util.Strings;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class ItemCanPlaceDestroyOnParserParent implements ItemMetaParser{

  // Since the code for parsing what blocks aa block can be placed on or what item can destroy what block
  // are so similar, they've been merged into one function
  // jsonArrayName is the name of the json object that has the list of can place/destroy blocks
  // componentName is the name of the component to save the list to in the item
  public ItemMeta parseDestroyPlace(ItemStack itemStack, ItemMeta meta, JsonObject object, String jsonArrayName, String componentName) {
    if (object.has(jsonArrayName)) {
      ArrayList<String> canBreakOrPlaceBlocksList = new ArrayList<>();
      JsonArray jsonElements = object.getAsJsonArray(jsonArrayName);
      for (int i = 0; i < jsonElements.size(); i++) {
        canBreakOrPlaceBlocksList.add(
            jsonElements.get(i).getAsString().toLowerCase().replace(" ", "_"));
      }

      String components = meta.getAsComponentString();
      String newComponent = String.format("%s={blocks:%s}", componentName, canBreakOrPlaceBlocksList);
      components = Strings.setComponent(components, newComponent);
      String itemTypeKey = itemStack.getType().getKey().toString();
      String itemAsString = itemTypeKey + components;
      // Why do we have to create a whole itemStack to get a new itemMeta object?
      ItemStack recreatedItemStack = Bukkit.getItemFactory().createItemStack(itemAsString);
      return recreatedItemStack.getItemMeta();
    }
    return meta;
  }
}
