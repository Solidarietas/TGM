package club.pvparcade.tgm.modules.ctf;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import club.pvparcade.tgm.TGM;
import club.pvparcade.tgm.match.Match;
import club.pvparcade.tgm.match.MatchModule;
import club.pvparcade.tgm.modules.base.MatchBase;
import club.pvparcade.tgm.modules.ctf.objective.CTFAmountController;
import club.pvparcade.tgm.modules.ctf.objective.CTFController;
import club.pvparcade.tgm.modules.ctf.objective.CTFControllerSubscriber;
import club.pvparcade.tgm.modules.ctf.objective.CTFObjective;
import club.pvparcade.tgm.modules.ctf.objective.CTFTimeController;
import club.pvparcade.tgm.modules.flag.MatchFlag;
import club.pvparcade.tgm.modules.itemremove.ItemRemoveModule;
import club.pvparcade.tgm.modules.region.Region;
import club.pvparcade.tgm.modules.region.RegionManagerModule;
import club.pvparcade.tgm.modules.team.MatchTeam;
import club.pvparcade.tgm.modules.team.TeamManagerModule;
import club.pvparcade.tgm.modules.time.TimeLimitService;
import club.pvparcade.tgm.modules.time.TimeModule;
import club.pvparcade.tgm.util.Strings;
import club.pvparcade.tgm.util.itemstack.ItemUtils;
import org.bukkit.World;

/**
 * Created by yikes on 12/15/2019
 */
public class CTFModule extends MatchModule implements CTFControllerSubscriber {
    private CTFController controller;
    private List<MatchBase> matchBases = new ArrayList<>();
    private List<MatchFlag> matchFlags = new ArrayList<>();

    public static final String FULL_FLAG = "\u2691"; // ⚑
    public static final String EMPTY_FLAG = "\u2690"; // ⚐
    public static final String RIGHT_ARROW = "\u2794"; // ➔

    @Override
    public void load(Match match) {
        // Grab CTF Json
        JsonObject ctfJson = match.getMapContainer().getMapInfo().getJsonObject().get("ctf").getAsJsonObject();

        // Determine controller from objective
        CTFObjective objective = CTFObjective.valueOf(Strings.getTechnicalName(ctfJson.get("objective").getAsString()));

        // Based on Objective, determine controller and apply other effects
        JsonObject optionObject = ctfJson.get("options").getAsJsonObject();
        TimeModule timeModule = TGM.get().getModule(TimeModule.class);
        if (objective == CTFObjective.TIME) {
            int timeLimit = optionObject.get("time").getAsInt();
            timeModule.setTimeLimited(true);
            timeModule.setTimeLimit(timeLimit);
            this.controller = new CTFTimeController(this, matchFlags, timeLimit);
        } else if (objective == CTFObjective.AMOUNT) {
            int captureAmount = optionObject.get("captures").getAsInt();
            this.controller = new CTFAmountController(this, matchFlags, captureAmount);
        }
        timeModule.setTimeLimitService((TimeLimitService) this.controller);

        // Deserialize flags json into MatchFlag instances
        World world = match.getWorld();
        for (JsonElement flagElem : ctfJson.get("flags").getAsJsonArray()) {
            this.matchFlags.add(MatchFlag.deserialize(flagElem.getAsJsonObject(), controller, world));
        }

        // Don't allow flags to be dropped
        ItemRemoveModule itemRemoveModule = TGM.get().getModule(ItemRemoveModule.class);
        itemRemoveModule.addAll(ItemUtils.BANNER_MATERIALS);


        // If Objective is amount, bases are required for flags to be captured
        if (objective == CTFObjective.AMOUNT) {
            RegionManagerModule regionManagerModule = TGM.get().getModule(RegionManagerModule.class);
            TeamManagerModule teamManagerModule = TGM.get().getModule(TeamManagerModule.class);
            for (JsonElement element : ctfJson.get("bases").getAsJsonArray()) {
                JsonObject baseObject = element.getAsJsonObject();
                Region baseRegion = regionManagerModule.getRegion(match, baseObject.get("region"));
                MatchTeam matchTeam = teamManagerModule.getTeamById(baseObject.get("team").getAsString());
                List<MatchFlag> flags = new ArrayList<>();
                for (MatchFlag flag : matchFlags) {
                    if (flag.getTeam() != null && flag.getTeam().equals(matchTeam)) continue;
                    flags.add(flag);
                }
                matchBases.add(new MatchBase(baseRegion, matchTeam, flags));
            }
        }
    }

    @Override
    public void unload() {
        for (MatchFlag matchFlag : matchFlags) matchFlag.unload();
        for (MatchBase matchBase : matchBases) matchBase.unload();
        controller.unload();
        controller = null;
        matchFlags = null;
        matchBases = null;
    }

    @Override
    public void gameOver(MatchTeam team) {
        TGM.get().getMatchManager().endMatch(team);
    }
}
