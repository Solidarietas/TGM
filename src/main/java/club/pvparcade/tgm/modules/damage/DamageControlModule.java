package club.pvparcade.tgm.modules.damage;

import static org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.HashMap;
import java.util.Map;
import club.pvparcade.tgm.match.Match;
import club.pvparcade.tgm.match.MatchModule;
import club.pvparcade.tgm.util.Strings;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageControlModule extends MatchModule implements Listener {

    private HashMap<DamageCause, Boolean> enabled = new HashMap<>();

    @Override
    public void load(Match match) {
        JsonObject matchObj = match.getMapContainer().getMapInfo().getJsonObject();
        if (matchObj.has("damageControl")) {
            JsonObject damageControlObj = matchObj.getAsJsonObject("damageControl");
            for (Map.Entry<String, JsonElement> entry : damageControlObj.entrySet()) {
                if (!entry.getValue().isJsonPrimitive()) continue;
                DamageCause cause = DamageCause.valueOf(Strings.getTechnicalName(entry.getKey()));
                if (cause == null) continue;
                enabled.put(cause, entry.getValue().getAsBoolean());
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onDamage(EntityDamageEvent event) {
        if (!this.enabled.getOrDefault(event.getCause(), true)) {
            event.setCancelled(true);
            event.setDamage(0);
        }
    }
}
