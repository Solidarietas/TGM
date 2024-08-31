package club.pvparcade.tgm.modules;

import club.pvparcade.tgm.TGM;
import club.pvparcade.tgm.config.TGMConfigReloadEvent;
import club.pvparcade.tgm.match.Match;
import club.pvparcade.tgm.match.MatchModule;
import com.google.gson.JsonObject;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class VoidKillHeightModule extends MatchModule implements Listener {

    private static double globalVoidKillHeight = -128;
    private double mapVoidKillHeight;

    public static void loadConfig() {
        ConfigurationSection legacyConfig = TGM.get().getConfig().getConfigurationSection("map");
        if (legacyConfig != null)
            globalVoidKillHeight = legacyConfig.getDouble("void-kill-height");
    }

    @EventHandler
    public void onConfigReload(TGMConfigReloadEvent event) {
        loadConfig();
    }

    @Override
    public void load(Match match) {
        mapVoidKillHeight = globalVoidKillHeight;

        JsonObject mapInfo = match.getMapContainer().getMapInfo().getJsonObject();
        if (mapInfo.has("voidKillHeight")) {
            this.mapVoidKillHeight = mapInfo.get("voidKillHeight").getAsDouble();
        }
    }

    // Temporary fix, will improve performance later
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.getPlayer().getLocation().getY() < mapVoidKillHeight) {
            event.getPlayer().damage(1.0, DamageSource.builder(DamageType.OUT_OF_WORLD).build());
        }
    }
}
