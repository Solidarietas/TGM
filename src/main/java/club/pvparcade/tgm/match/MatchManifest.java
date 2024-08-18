package club.pvparcade.tgm.match;

import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import club.pvparcade.tgm.modules.BuildHeightLimitModule;
import club.pvparcade.tgm.modules.CraftingModule;
import club.pvparcade.tgm.modules.DisabledCommandsModule;
import club.pvparcade.tgm.modules.ExploitPreventionModule;
import club.pvparcade.tgm.modules.GameRuleModule;
import club.pvparcade.tgm.modules.InventoryPreviewModule;
import club.pvparcade.tgm.modules.ItemKeepModule;
import club.pvparcade.tgm.modules.MapCommandsModule;
import club.pvparcade.tgm.modules.MatchProgressNotifications;
import club.pvparcade.tgm.modules.MatchResultModule;
import club.pvparcade.tgm.modules.RegenModule;
import club.pvparcade.tgm.modules.SpawnPointHandlerModule;
import club.pvparcade.tgm.modules.SpawnPointLoaderModule;
import club.pvparcade.tgm.modules.SpectatorModule;
import club.pvparcade.tgm.modules.StatsModule;
import club.pvparcade.tgm.modules.TabListModule;
import club.pvparcade.tgm.modules.TeamJoinNotificationsModule;
import club.pvparcade.tgm.modules.border.WorldBorderModule;
import club.pvparcade.tgm.modules.countdown.CountdownManagerModule;
import club.pvparcade.tgm.modules.damage.DamageControlModule;
import club.pvparcade.tgm.modules.damage.EntityDamageModule;
import club.pvparcade.tgm.modules.damage.FireworkDamageModule;
import club.pvparcade.tgm.modules.death.DeathMessageModule;
import club.pvparcade.tgm.modules.death.DeathModule;
import club.pvparcade.tgm.modules.filter.FilterManagerModule;
import club.pvparcade.tgm.modules.generator.GeneratorModule;
import club.pvparcade.tgm.modules.itemremove.ItemRemoveModule;
import club.pvparcade.tgm.modules.killstreak.KillstreakModule;
import club.pvparcade.tgm.modules.kit.KitEditorModule;
import club.pvparcade.tgm.modules.kit.KitLoaderModule;
import club.pvparcade.tgm.modules.kit.classes.GameClassModule;
import club.pvparcade.tgm.modules.launchpad.LaunchPadLoaderModule;
import club.pvparcade.tgm.modules.legacy.LegacyArmorModule;
import club.pvparcade.tgm.modules.legacy.LegacyAttackSpeedModule;
import club.pvparcade.tgm.modules.legacy.LegacyDamageModule;
import club.pvparcade.tgm.modules.legacy.LegacyKnockbackModule;
import club.pvparcade.tgm.modules.legacy.LegacyShieldModule;
import club.pvparcade.tgm.modules.points.PointsModule;
import club.pvparcade.tgm.modules.portal.PortalManagerModule;
import club.pvparcade.tgm.modules.region.RegionManagerModule;
import club.pvparcade.tgm.modules.reports.ReportsModule;
import club.pvparcade.tgm.modules.respawn.RespawnModule;
import club.pvparcade.tgm.modules.scoreboard.ScoreboardManagerModule;
import club.pvparcade.tgm.modules.screens.ScreenManagerModule;
import club.pvparcade.tgm.modules.tasked.TaskedModuleManager;
import club.pvparcade.tgm.modules.team.TeamManagerModule;
import club.pvparcade.tgm.modules.time.TimeModule;
import club.pvparcade.tgm.modules.visibility.VisibilityModule;

/**
 * Created by luke on 4/27/17.
 */
public abstract class MatchManifest {

    /**
     * Determines which modules to load based on the
     * given gametype.
     * @return
     */
    public abstract List<MatchModule> allocateGameModules();

    /**
     * Core set of modules that nearly all games will use.
     * Match Manifests still have the option to override these
     * if needed.
     * @return
     */
    public List<MatchModule> allocateCoreModules(JsonObject mapJson) {
        List<MatchModule> modules = new ArrayList<>();

        modules.add(new ExploitPreventionModule());
        modules.add(new TeamJoinNotificationsModule());
        modules.add(new TeamManagerModule());
        modules.add(new SpectatorModule());
        modules.add(new InventoryPreviewModule());
        modules.add(new SpawnPointHandlerModule());
        modules.add(new SpawnPointLoaderModule());
        modules.add(new VisibilityModule());
        modules.add(new TimeModule());
        modules.add(new TabListModule());
        modules.add(new MatchProgressNotifications());
        modules.add(new MatchResultModule());
        modules.add(new ScoreboardManagerModule());
        modules.add(new RegionManagerModule());
        modules.add(new TaskedModuleManager());
        modules.add(new CountdownManagerModule());
        modules.add(new KitEditorModule());
        modules.add(new KitLoaderModule());
        modules.add(new DeathModule());
        modules.add(new DeathMessageModule());
        modules.add(new BuildHeightLimitModule());
        modules.add(new FilterManagerModule());
        modules.add(new DisabledCommandsModule());
        modules.add(new ScreenManagerModule());
        modules.add(new PointsModule());
        modules.add(new LegacyDamageModule());
        modules.add(new LegacyArmorModule());
        modules.add(new LegacyShieldModule());
        modules.add(new LegacyAttackSpeedModule());
        modules.add(new EntityDamageModule());
        modules.add(new FireworkDamageModule());
        modules.add(new GameRuleModule());
        modules.add(new ItemRemoveModule());
        modules.add(new ItemKeepModule());
        modules.add(new RegenModule());
        modules.add(new KillstreakModule());
        modules.add(new ReportsModule());
        modules.add(new StatsModule());
        modules.add(new PortalManagerModule());
        modules.add(new LaunchPadLoaderModule());
        modules.add(new WorldBorderModule());
        modules.add(new LegacyKnockbackModule());
        modules.add(new MapCommandsModule());
        modules.add(new DamageControlModule());
        modules.add(new RespawnModule());
        modules.add(new CraftingModule());

        if (GameClassModule.isUsingClasses(mapJson)) modules.add(new GameClassModule());
        if (GeneratorModule.hasGenerators(mapJson)) modules.add(new GeneratorModule());

        return modules;
    }
}
