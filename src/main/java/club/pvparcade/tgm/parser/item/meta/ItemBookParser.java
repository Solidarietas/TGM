package club.pvparcade.tgm.parser.item.meta;

import com.google.gson.JsonObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import club.pvparcade.tgm.util.ColorConverter;
import club.pvparcade.tgm.util.Strings;
import org.bukkit.Material;
import org.bukkit.craftbukkit.inventory.CraftMetaBook;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Created by Jorge on 09/14/2019
 */
public class ItemBookParser implements ItemMetaParser {

    //private static Class classIChatBaseComponent;

    private static Class classCraftMetaBook;
    private static Field fieldPages;

    static {
        // What is this reflection here for its never used?
        try {
//            classIChatBaseComponent = Class.forName("net.minecraft.network.chat.IChatBaseComponent");

            // Class.forName("net.minecraft.network.chat.IChatBaseComponent$ChatSerializer");
            // replace with Component.Serializer.fromJson();
            // methodA = classChatSerializer.getDeclaredMethod("a", String.class);

            classCraftMetaBook = CraftMetaBook.class;
            fieldPages = classCraftMetaBook.getDeclaredField("pages");

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public ItemMeta parse(ItemStack itemStack, ItemMeta meta, JsonObject object) {
        if (!itemStack.getType().equals(Material.WRITTEN_BOOK)) return meta;
        BookMeta bookMeta = (BookMeta) meta;

        bookMeta.setTitle(object.has("title") ? ColorConverter.filterString(object.get("title").getAsString()) : "Empty Book");
        bookMeta.setAuthor(object.has("author") ? ColorConverter.filterString(object.get("author").getAsString()) : "Mojang");
        bookMeta.setGeneration(object.has("generation") ?
                BookMeta.Generation.valueOf(Strings.getTechnicalName(object.get("generation").getAsString())) : BookMeta.Generation.ORIGINAL);

        if (object.has("pages")) { // Json pages
            try {
                fieldPages.setAccessible(true);

                List<Object> pages = (List<Object>) fieldPages.get(bookMeta);
                object.getAsJsonArray("pages").forEach(
                        jsonElement -> {
                            try {
                                // TODO Not sure if this will work
                                String page = String.valueOf(
                                    GsonComponentSerializer.gson().deserialize(ColorConverter.filterString(jsonElement.getAsString())));
                                pages.add(page);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                );

                fieldPages.setAccessible(false);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } else {
            bookMeta.addPage("null");
        }

        itemStack.setItemMeta(bookMeta);
        return bookMeta;
    }
}
