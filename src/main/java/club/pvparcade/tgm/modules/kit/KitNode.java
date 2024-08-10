package club.pvparcade.tgm.modules.kit;

import club.pvparcade.tgm.modules.team.MatchTeam;
import org.bukkit.entity.Player;

public interface KitNode {
    void apply(Player player, MatchTeam matchTeam);
}
