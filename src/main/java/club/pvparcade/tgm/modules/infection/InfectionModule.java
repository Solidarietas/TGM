package club.pvparcade.tgm.modules.infection;

import com.google.gson.JsonObject;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;
import java.util.Random;
import lombok.Getter;
import club.pvparcade.tgm.TGM;
import club.pvparcade.tgm.match.Match;
import club.pvparcade.tgm.match.MatchModule;
import club.pvparcade.tgm.match.MatchResultEvent;
import club.pvparcade.tgm.match.MatchStatus;
import club.pvparcade.tgm.modules.death.DeathInfo;
import club.pvparcade.tgm.modules.death.DeathMessageModule;
import club.pvparcade.tgm.modules.death.DeathModule;
import club.pvparcade.tgm.modules.respawn.RespawnModule;
import club.pvparcade.tgm.modules.respawn.RespawnRule;
import club.pvparcade.tgm.modules.scoreboard.ScoreboardInitEvent;
import club.pvparcade.tgm.modules.scoreboard.ScoreboardManagerModule;
import club.pvparcade.tgm.modules.scoreboard.SimpleScoreboard;
import club.pvparcade.tgm.modules.team.MatchTeam;
import club.pvparcade.tgm.modules.team.TeamManagerModule;
import club.pvparcade.tgm.modules.team.event.TeamChangeEvent;
import club.pvparcade.tgm.modules.team.event.TeamUpdateAliasEvent;
import club.pvparcade.tgm.modules.time.TimeModule;
import club.pvparcade.tgm.modules.time.TimeSubscriber;
import club.pvparcade.tgm.player.event.PlayerJoinTeamAttemptEvent;
import club.pvparcade.tgm.player.event.TGMPlayerDeathEvent;
import club.pvparcade.tgm.player.event.TGMPlayerRespawnEvent;
import club.pvparcade.tgm.user.PlayerContext;
import club.pvparcade.tgm.util.Players;
import club.pvparcade.tgm.util.Strings;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

/**
 * Created by Draem on 7/31/2017.
 */
@Getter
public class InfectionModule extends MatchModule implements Listener, TimeSubscriber {

    private WeakReference<Match> match;
    private TeamManagerModule teamManager;
    private HashMap<Integer, String> teamScoreboardLines = new HashMap<>();
    private HashMap<String, Integer> teamAliveScoreboardLines = new HashMap<>();
    private ScoreboardManagerModule scoreboardManagerController;
    private int timeScoreboardLine;
    private String timeScoreboardValue;
    private boolean defaultScoreboardLoaded = false;

    private DeathModule deathModule;

    private int length;

    private MatchTeam humans;
    private MatchTeam infected;

    private final RespawnRule defaultRespawnRule = new RespawnRule(null, 3000, true, true, 3000, false);

    private BukkitTask task;

    @Override
    public void load(Match match) {
        JsonObject json = match.getMapContainer().getMapInfo().getJsonObject().get("infection").getAsJsonObject();
        length = json.get("length").getAsInt();
        teamManager = match.getModule(TeamManagerModule.class);
        deathModule = match.getModule(DeathModule.class);
        this.match = new WeakReference<>(match);
        this.humans = teamManager.getTeamById("humans");
        this.infected = teamManager.getTeamById("infected");
        TimeModule time = TGM.get().getModule(TimeModule.class);
        time.setTimeLimitService(this::getWinningTeam);
        time.getTimeSubscribers().add(this);
        time.setTimeLimit(length * 60);
        time.setTimeLimited(true);
        this.timeScoreboardValue = length + ":00";
        this.scoreboardManagerController = TGM.get().getModule(ScoreboardManagerModule.class);
        TGM.get().getModule(RespawnModule.class).setDefaultRule(defaultRespawnRule);
        TGM.get().getModule(DeathMessageModule.class).getDeathMessages().clear();
        TGM.get().getModule(DeathMessageModule.class).setDefaultDeathMessage(
                (d) -> {
                    if (d.killer != null) {
                        if (d.killerTeam != humans)
                            DeathMessageModule.broadcastDeathMessage(d.player, d.killer, "%s%s &7has been infected by %s%s",
                                d.playerTeam.getColor(),
                                    d.player.getName(),
                                    d.killerTeam.getColor(),
                                    d.killer.getName()
                            );
                        else
                            DeathMessageModule.broadcastDeathMessage(d.player, d.killer, "%s%s &7has been slain by %s%s",
                                    d.playerTeam.getColor(),
                                    d.player.getName(),
                                    d.killerTeam.getColor(),
                                    d.killer.getName());
                    } else {
                        if (d.playerTeam != humans)
                            DeathMessageModule.broadcastDeathMessage(d.player, null, "%s%s &7wasted away to the environment",
                                    d.playerTeam.getColor(),
                                    d.player.getName());
                        else
                            DeathMessageModule.broadcastDeathMessage(d.player, null, "%s%s &7has been taken by the environment",
                                    d.playerTeam.getColor(),
                                    d.player.getName());
                    }
                    return true;
                }
        );
    }

    public PlayerContext infectRandom() {
        PlayerContext player = this.humans.getMembers().get(new Random().nextInt(this.humans.getMembers().size()));
        broadcastMessage(String.format("&2&l%s &7has been infected!", player.getPlayer().getName()));
        teamManager.joinTeam(player, infected, true, true);
        return player;
    }

    @Override
    public void enable() {
        int players = this.humans.getMembers().size();
        int zombies = ((int) (players * (5 / 100.0F)) == 0 ? 1 : (int) (players * (5 / 100.0F))) - this.infected.getMembers().size();
        if (zombies > 0 && players != 1) {
            for (int i = 0; i < zombies; i++) {
                infectRandom();
            }
        }

        for (MatchTeam team : teamManager.getTeams()) {
            team.getMembers().forEach(playerContext -> playerContext.getPlayer().setGameMode(GameMode.ADVENTURE));
        }
        this.task = Bukkit.getScheduler().runTaskTimer(TGM.get(), () -> {
            for (PlayerContext i : infected.getMembers()) {
                Player infectedPlayer = i.getPlayer();
                Optional<PlayerContext> playerOptional = Players.getNearestPlayer(i, humans.getMembers());
                playerOptional.ifPresent(context -> infectedPlayer.setCompassTarget(Players.location(context)));
            }
        }, 5L, 5L);
    }

    public void processSecond(int elapsed) {
        int diff = (length * 60) - elapsed;
        if (diff < 0) diff = 0;
        timeScoreboardValue = ChatColor.WHITE + "Time Left: " + ChatColor.AQUA + Strings.formatTime(diff) + ChatColor.WHITE;
        for (SimpleScoreboard simpleScoreboard : scoreboardManagerController.getScoreboards().values())
            refreshOnlyDynamicScoreboard(simpleScoreboard);
    }

    @Override
    public void unload() {
        teamScoreboardLines.clear();
        teamAliveScoreboardLines.clear();
        if (this.task != null) this.task.cancel();
    }

    private MatchTeam getWinningTeam() {
        return this.humans;
    }

    private void refreshScoreboardAlias(MatchTeam matchTeam) {
        for (SimpleScoreboard simpleScoreboard : TGM.get().getModule(ScoreboardManagerModule.class).getScoreboards().values()) {
            int line = teamAliveScoreboardLines.get(matchTeam.getId());
            simpleScoreboard.remove(line + 1);
            simpleScoreboard.add(matchTeam.getColor() + matchTeam.getAlias(), line + 1);
            simpleScoreboard.update();
        }
    }

    private void refreshScoreboard(SimpleScoreboard board) {
        if (board == null) return;
        teamScoreboardLines.forEach((i, s) -> board.add(s, i));
        refreshOnlyDynamicScoreboard(board);
    }

    private void refreshOnlyDynamicScoreboard(SimpleScoreboard board) {
        if (board == null) return;
        teamAliveScoreboardLines.forEach((id, i) -> board.add(
                ChatColor.WHITE + "  " + teamManager.getTeamById(id).getMembers().size() + ChatColor.GRAY + " Alive", i));
        board.add(timeScoreboardValue, timeScoreboardLine);
        board.update();
    }

    @EventHandler
    public void onScoreboardInit(ScoreboardInitEvent event) {
        if(!defaultScoreboardLoaded) defaultScoreboard();
        SimpleScoreboard simpleScoreboard = event.getSimpleScoreboard();
        simpleScoreboard.setTitle(ChatColor.AQUA + "Infection");
        refreshScoreboard(simpleScoreboard);
    }


    @EventHandler(ignoreCancelled = true)
    public void onJoinAttempt(PlayerJoinTeamAttemptEvent event) {
        if (this.match.get().getMatchStatus().equals(MatchStatus.PRE)) {
            if (event.isAutoJoin()) {
                event.setTeam(this.humans);
            }
        } else {
            if (event.isAutoJoin()) {
                event.setTeam(this.infected);
            } else if (!event.getTeam().isSpectator() && event.getTeam() != this.infected) {
                event.getPlayerContext().getPlayer().sendMessage(ChatColor.RED + "You can't pick a team after the match starts in this gamemode.");
                event.setCancelled(true);
            }
        }
    }

    private void defaultScoreboard() {
        teamScoreboardLines.clear();
        teamAliveScoreboardLines.clear();
        int positionOnScoreboard = 1;
        int spaceCount = 1;
        for (int j = teamManager.getTeams().size() - 1; j >= 0; j--) {
            MatchTeam team = teamManager.getTeams().get(j);
            if (team.isSpectator()) continue;
            teamScoreboardLines.put(positionOnScoreboard, StringUtils.repeat(" ", spaceCount++));
            positionOnScoreboard++;
            teamAliveScoreboardLines.put(team.getId(), positionOnScoreboard);
            positionOnScoreboard++;
            teamScoreboardLines.put(positionOnScoreboard, team.getColor() + team.getAlias());
            positionOnScoreboard++;
        }
        teamScoreboardLines.put(positionOnScoreboard++, StringUtils.repeat(" ", spaceCount++));
        timeScoreboardLine = positionOnScoreboard++;
        timeScoreboardValue = ChatColor.WHITE + "Time: " + ChatColor.AQUA + "0:00";
        teamScoreboardLines.put(positionOnScoreboard, StringUtils.repeat(" ", spaceCount));
        defaultScoreboardLoaded = true;
    }

    @EventHandler
    public void onTeamUpdate(TeamUpdateAliasEvent event) {
        MatchTeam team = event.getMatchTeam();
        if (!team.isSpectator()) refreshScoreboardAlias(team);
    }

    @EventHandler
    public void onDeath(TGMPlayerDeathEvent event) {
        DeathInfo deathInfo = deathModule.getPlayer(event.getVictim());

        MatchTeam playerTeam = deathInfo.playerTeam;

        // Check if the player who died is a human.
        if (playerTeam.equals(humans)) {
            // Infect the player
            teamManager.joinTeam(TGM.get().getPlayerManager().getPlayerContext(event.getVictim()), infected, true, true);
        }
    }

    @EventHandler
    public void onRespawn(TGMPlayerRespawnEvent event) {
        if (teamManager.getTeam(event.getPlayer()).equals(this.infected)) {
            event.getPlayer().addPotionEffects(Collections.singleton(new PotionEffect(PotionEffectType.JUMP_BOOST, 10000, 1, true, false)));
        }
        event.getPlayer().setGameMode(GameMode.ADVENTURE);
    }

    public void broadcastMessage(String msg) {
        Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg)));
    }

    @EventHandler
    public void onBukkitDeath(PlayerDeathEvent event) {
        event.setDeathMessage("");
    }

    @EventHandler
    public void onTeamChange(TeamChangeEvent event) {
        if (event.isCancelled()) return;
        if (defaultScoreboardLoaded) {
            for (SimpleScoreboard simpleScoreboard : scoreboardManagerController.getScoreboards().values())
                refreshOnlyDynamicScoreboard(simpleScoreboard);
        }
        Player player = event.getPlayerContext().getPlayer();
        if (infected.equals(event.getTeam())) {
            infect(player);
            addTag(player);
            if (humans.getMembers().size() == 0 && match.get().getMatchStatus().equals(MatchStatus.MID)) {
                TGM.get().getMatchManager().endMatch(infected);
                return;
            }
            long timePassed = new Date().getTime() - this.match.get().getStartedTime();
            if (timePassed < 10000) {
                freeze(player, (int) (10000 - timePassed) / 50);
            }
        } else {
            removeTag(player);
        }
    }

    @EventHandler
    public void onMatchEnd(MatchResultEvent event) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            removeTag(player);
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        removeTag(event.getPlayer());
        handleQuit();
    }

    private void addTag(Player player) {
        player.addScoreboardTag("infected");
    }

    private void removeTag(Player player) {
        player.removeScoreboardTag("infected");
    }

    private void handleQuit() {
        if (infected.getMembers().size() == 0 && match.get().getMatchStatus().equals(MatchStatus.MID)) {
            // Infect the last player to join
            PlayerContext player = humans.getMembers().get(humans.getMembers().size() - 1);
            broadcastMessage(String.format("&2&l%s &7has been infected!", player.getPlayer().getName()));

            infect(player.getPlayer());
        }
    }

    //TODO Remove effects and replace with new kit module
    private void infect(Player player) {
        player.getWorld().strikeLightningEffect(player.getLocation());
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lYou have been infected!"));
        player.addPotionEffects(Collections.singleton(new PotionEffect(PotionEffectType.JUMP_BOOST, 50000, 1, true, false)));
        player.setGameMode(GameMode.ADVENTURE);
        addTag(player);
    }

    private void freeze(Player player, int ticks) {
        player.addPotionEffects(Arrays.asList(
                new PotionEffect(PotionEffectType.SLOWNESS, ticks, 255, true, false),
                new PotionEffect(PotionEffectType.JUMP_BOOST, ticks, 128, true, false),
                new PotionEffect(PotionEffectType.BLINDNESS, ticks, 255, true, false)
        ));

        Bukkit.getScheduler().runTaskLater(TGM.get(), () -> unfreeze(player), ticks);
    }

    private void unfreeze(Player player) {
        player.removePotionEffect(PotionEffectType.JUMP_BOOST);
        player.removePotionEffect(PotionEffectType.SLOWNESS);
        player.removePotionEffect(PotionEffectType.BLINDNESS);

        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, 50000, 1, true, false));
    }

}
