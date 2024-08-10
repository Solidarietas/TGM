package club.pvparcade.tgm.modules.kit.classes;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import club.pvparcade.tgm.TGM;
import club.pvparcade.tgm.modules.itemremove.ItemRemoveModule;
import club.pvparcade.tgm.modules.kit.classes.abilities.Ability;
import club.pvparcade.tgm.util.ArmorType;
import club.pvparcade.tgm.util.ColorConverter;
import club.pvparcade.tgm.util.itemstack.ItemUtils;
import club.pvparcade.tgm.util.itemstack.Unbreakable;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public abstract class GameClass {
    private Set<Ability> abilities;

    protected HashMap<Integer, ItemStack> items = new HashMap<>();

    GameClass(Ability... abilities) {
        this.abilities = new HashSet<>(Arrays.asList(abilities));
    }

    public void setItem(int slot, ItemStack itemStack) {
        TGM.get().getModule(ItemRemoveModule.class).add(itemStack.getType());
        this.items.put(slot, itemStack);
    }

    /**
     * Applies items and effects
     */
    public void apply(Player player, ChatColor color) {
        for (Ability ability : abilities) ability.apply(player);
        for (int slot : this.items.keySet()) {
            ItemStack item = this.items.get(slot);
            if (ItemUtils.UNBREAKABLE_MATERIALS.contains(item.getType())) Unbreakable.setUnbreakable(item);


            if (ArmorType.getArmorType(item) == ArmorType.HELMET) {

                if (item.getType() == Material.LEATHER_HELMET) {
                    LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
                    meta.setColor(ColorConverter.getColor(color));
                    item.setItemMeta(meta);
                }

                player.getInventory().setHelmet(item);
            } else if (ArmorType.getArmorType(item) == ArmorType.CHESTPLATE) {

                if (item.getType() == Material.LEATHER_CHESTPLATE) {
                    LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
                    meta.setColor(ColorConverter.getColor(color));
                    item.setItemMeta(meta);
                }

                player.getInventory().setChestplate(item);
            } else if (ArmorType.getArmorType(item) == ArmorType.LEGGINGS) {

                if (item.getType() == Material.LEATHER_LEGGINGS) {
                    LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
                    meta.setColor(ColorConverter.getColor(color));
                    item.setItemMeta(meta);
                }

                player.getInventory().setLeggings(item);
            } else if (ArmorType.getArmorType(item) == ArmorType.BOOTS) {

                if (item.getType() == Material.LEATHER_BOOTS) {
                    LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
                    meta.setColor(ColorConverter.getColor(color));
                    item.setItemMeta(meta);
                }

                player.getInventory().setBoots(item);
            } else {
                player.getInventory().setItem(slot, item);
            }
        }
        this.extraApply(player);
    }

    protected void extraApply(Player player) {}

    /**
     * Adds player's UUID to each of the classes' abilities registeredPlayers
     * @param p
     */
    public void addToAbilityCaches(Player p) {
        UUID playerUUID = p.getUniqueId();
        for (Ability ability : abilities) {
            ability.getRegisteredPlayers().add(playerUUID);
        }
    }

    /**
     * Removes player's UUID to each of the classes' abilities registeredPlayers
     * @param p
     */
    public void removeFromAbilityCaches(Player p) {
        UUID playerUUID = p.getUniqueId();
        for (Ability ability : abilities) {
            ability.getRegisteredPlayers().remove(playerUUID);
            ability.getCooldowns().remove(playerUUID);
        }
    }
}
