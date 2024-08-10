package club.pvparcade.tgm.modules.respawn;

import lombok.AllArgsConstructor;
import lombok.Getter;
import club.pvparcade.tgm.modules.team.MatchTeam;

import java.util.List;

@AllArgsConstructor @Getter
public class RespawnRule {

       private List<MatchTeam> teams;
       private int delay;
       private boolean freeze;
       private boolean blindness;
       private boolean confirm;

}
