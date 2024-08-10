package club.pvparcade.tgm.modules.kit.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import club.pvparcade.tgm.modules.kit.KitNode;
import club.pvparcade.tgm.modules.team.MatchTeam;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

@AllArgsConstructor @Getter
public class EffectKitNode implements KitNode {

    private final PotionEffect potionEffect;

    @Override
    public void apply(Player player, MatchTeam matchTeam) {
        player.addPotionEffect(potionEffect);
    }
}
