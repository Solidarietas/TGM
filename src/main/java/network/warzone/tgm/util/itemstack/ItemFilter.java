package network.warzone.tgm.util.itemstack;

import java.util.List;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Jorge on 03/03/2021
 *
 * Interface for classes that filter out items.
 * Examples:
 * - {@link network.warzone.tgm.modules.itemremove.ItemRemoveModule}
 * - {@link network.warzone.tgm.modules.ItemKeepModule}
 */
public interface ItemFilter {

    void filter(List<ItemStack> items);

}
