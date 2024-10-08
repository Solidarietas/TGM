package club.pvparcade.tgm.modules.countdown;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import club.pvparcade.tgm.TGM;
import club.pvparcade.tgm.match.Match;
import club.pvparcade.tgm.match.MatchModule;
import club.pvparcade.tgm.match.ModuleData;
import club.pvparcade.tgm.match.ModuleLoadTime;
import club.pvparcade.tgm.modules.tasked.TaskedModuleManager;
import club.pvparcade.tgm.modules.team.MatchTeam;
import club.pvparcade.tgm.modules.team.TeamManagerModule;
import club.pvparcade.tgm.util.Strings;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;

/**
 * Created by Jorge on 10/20/2019
 */
@ModuleData(load = ModuleLoadTime.EARLIEST)
public class CountdownManagerModule extends MatchModule {

    private WeakReference<Match> match;
    private TeamManagerModule teamManagerModule;
    private TaskedModuleManager taskedModuleManager;

    private Map<String, CustomCountdown> customCountdowns = new HashMap<>();

    @Override
    public void load(Match match) {
        this.match = new WeakReference<Match>(match);
        this.teamManagerModule = match.getModule(TeamManagerModule.class);
        this.taskedModuleManager = match.getModule(TaskedModuleManager.class);
        match.getModules().add(new StartCountdown());
        match.getModules().add(new CycleCountdown());
        JsonObject jsonObject = match.getMapContainer().getMapInfo().getJsonObject();
        if (jsonObject.has("countdowns")) {
            for (JsonElement jsonElement : jsonObject.getAsJsonArray("countdowns")) {
                try {
                    getCountdown(jsonElement);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        customCountdowns.values().forEach(countdown -> match.getModules().add(countdown));
    }

    public CustomCountdown getCountdown(JsonElement jsonElement) {
        if (jsonElement.isJsonPrimitive()) {
            return this.customCountdowns.get(jsonElement.getAsString());
        } else {
            JsonObject countdownObj = jsonElement.getAsJsonObject();
            int time = countdownObj.get("time").getAsInt(); // seconds
            String title = countdownObj.get("title").getAsString();
            BarColor color = BarColor.PURPLE;
            BarStyle style = BarStyle.SOLID;
            boolean visible = !countdownObj.has("visible") || countdownObj.get("visible").getAsBoolean();
            boolean invert = countdownObj.has("invert") && countdownObj.get("invert").getAsBoolean();
            List<MatchTeam> teams = new ArrayList<>();
            List<String> onFinish = new ArrayList<>();
            if (countdownObj.has("color")) color = BarColor.valueOf(Strings.getTechnicalName(countdownObj.get("color").getAsString()));
            if (countdownObj.has("style")) style = BarStyle.valueOf(Strings.getTechnicalName(countdownObj.get("style").getAsString()));
            if (countdownObj.has("teams")) teams.addAll(teamManagerModule.getTeams(countdownObj.get("teams").getAsJsonArray()));
            if (countdownObj.has("onFinish")) {
                for (JsonElement cmdElement : countdownObj.getAsJsonArray("onFinish")) {
                    if (!cmdElement.isJsonPrimitive()) continue;
                    onFinish.add(cmdElement.getAsString());
                }
            }
            CustomCountdown customCountdown = new CustomCountdown(time, title, color, style, visible, invert, teams, onFinish);
            if (countdownObj.has("id")) {
                this.customCountdowns.put(countdownObj.get("id").getAsString(), customCountdown);
            }
            return customCountdown;
        }
    }

    public CustomCountdown getCountdown(String id) {
        return this.customCountdowns.get(id);
    }

    public void addCountdown(String id, CustomCountdown countdown) {
        this.customCountdowns.put(id, countdown);
        TGM.registerEvents(countdown);
        this.match.get().getModules().add(countdown);
        this.taskedModuleManager.addTaskedModule(countdown);
    }

    public Map<String, CustomCountdown> getCustomCountdowns() {
        return new HashMap<>(customCountdowns);
    }
}
