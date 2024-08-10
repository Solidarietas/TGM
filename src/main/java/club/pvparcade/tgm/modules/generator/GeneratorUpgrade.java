package club.pvparcade.tgm.modules.generator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor @Getter
public class GeneratorUpgrade {
    private int interval;
    private ItemStack item;
    private String broadcast;
    private String holoContent;
}
