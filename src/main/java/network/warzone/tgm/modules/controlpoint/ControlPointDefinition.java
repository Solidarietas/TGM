package network.warzone.tgm.modules.controlpoint;

import java.util.HashMap;
import lombok.AllArgsConstructor;
import lombok.Getter;
import network.warzone.tgm.modules.portal.Portal;
import network.warzone.tgm.modules.team.MatchTeam;
import org.bukkit.ChatColor;

@AllArgsConstructor @Getter
public class ControlPointDefinition {

    private final String name;
    private final MatchTeam initialOwner;
    private final int maxProgress;
    private final int pointsPerTick;
    private final int tickRate;
    private final ChatColor neutralColor;

    private final HashMap<MatchTeam, Portal> portals;
}
