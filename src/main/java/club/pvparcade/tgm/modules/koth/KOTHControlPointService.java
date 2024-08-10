package club.pvparcade.tgm.modules.koth;

import static org.bukkit.SoundCategory.AMBIENT;

import lombok.AllArgsConstructor;
import lombok.Getter;
import club.pvparcade.tgm.TGM;
import club.pvparcade.tgm.match.Match;
import club.pvparcade.tgm.modules.controlpoint.ControlPoint;
import club.pvparcade.tgm.modules.controlpoint.ControlPointDefinition;
import club.pvparcade.tgm.modules.controlpoint.ControlPointService;
import club.pvparcade.tgm.modules.portal.Portal;
import club.pvparcade.tgm.modules.team.MatchTeam;
import club.pvparcade.tgm.modules.team.TeamManagerModule;
import club.pvparcade.tgm.user.PlayerContext;
import org.bukkit.ChatColor;
import org.bukkit.Sound;

@AllArgsConstructor @Getter
public class KOTHControlPointService implements ControlPointService {

    private KOTHModule kothModule;
    private Match match;
    private ControlPointDefinition definition;

    @Override
    public void holding(MatchTeam matchTeam) {
        if (kothModule.getKothObjective() == KOTHObjective.CAPTURES) return;
        kothModule.incrementPoints(matchTeam, definition.getPointsPerTick());
    }

    @Override
    public void capturing(MatchTeam matchTeam, int progress, int maxProgress, boolean upward) {
        kothModule.updateScoreboardControlPointLine(definition);
    }

    @Override
    public void captured(MatchTeam matchTeam) {
        TGM.broadcastMessage(matchTeam.getColor() + ChatColor.BOLD.toString() + matchTeam.getAlias() + ChatColor.WHITE +
                " took control of " + ChatColor.AQUA + ChatColor.BOLD + definition.getName());

        for (MatchTeam team : match.getModule(TeamManagerModule.class).getTeams()) {
            for (PlayerContext playerContext : team.getMembers()) {
                if (team.equals(matchTeam) || team.isSpectator()) {
                    playerContext.getPlayer().playSound(playerContext.getPlayer().getLocation(), Sound.BLOCK_PORTAL_TRAVEL, AMBIENT, 0.7f, 2f);
                } else {
                    playerContext.getPlayer().playSound(playerContext.getPlayer().getLocation(), Sound.ENTITY_BLAZE_DEATH, AMBIENT, 0.8f, 0.8f);
                }
            }
        }

        kothModule.updateScoreboardControlPointLine(definition);

        if (kothModule.getKothObjective() == KOTHObjective.POINTS) {
            kothModule.incrementPoints(matchTeam, definition.getPointsPerTick());
        } else {
            if (definition.getPortals() != null) {
                for (MatchTeam portalOwner : definition.getPortals().keySet()) {
                    Portal portal = definition.getPortals().get(portalOwner);
                    portal.setActive(portalOwner == matchTeam);
                }
            }

            for (ControlPoint controlPoint : kothModule.getControlPoints()) {
                MatchTeam controller = controlPoint.getController();
                if (matchTeam != controller) return;
            }
            TGM.get().getMatchManager().endMatch(matchTeam);
        }
    }

    @Override
    public void lost(MatchTeam matchTeam) {
        TGM.broadcastMessage(matchTeam.getColor() + ChatColor.BOLD.toString() + matchTeam.getAlias() + ChatColor.WHITE +
                " lost control of " + ChatColor.AQUA + ChatColor.BOLD + definition.getName());

        if (kothModule.getKothObjective() == KOTHObjective.CAPTURES) {
            if (definition.getPortals() != null && definition.getPortals().containsKey(matchTeam)) {
                definition.getPortals().get(matchTeam).setActive(false);
            }
        }
    }
}
