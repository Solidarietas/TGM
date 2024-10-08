package club.pvparcade.tgm.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import club.pvparcade.tgm.TGM;
import club.pvparcade.tgm.config.TGMConfigReloadEvent;
import club.pvparcade.tgm.util.Plugins;
import club.pvparcade.tgm.util.Ranks;
import club.pvparcade.api.models.Rank;
import club.pvparcade.api.models.UserProfile;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by luke on 4/27/17.
 */
public class PlayerContext {
    @Getter private Player player;
    private UserProfile userProfile;
    private static final List<PlayerLevel> levels = new ArrayList<>();

    static {
        loadConfig();

        TGM.registerEvents(new Listener() {
            @EventHandler
            public void onConfigLoad(TGMConfigReloadEvent event) {
                loadConfig();
            }
        });
    }

    private static void loadConfig() {
        levels.clear();
        ConfigurationSection section = TGM.get().getConfig().getConfigurationSection("chat.levels");
        if (section == null) {
            levels.add(new PlayerLevel(0, ChatColor.GRAY)); // Fallback
            return;
        }
        for (String key : section.getKeys(false)) {
            try {
                int minLevel = Integer.parseInt(key);
                ChatColor color = ChatColor.of(Objects.requireNonNull(section.getString(key)));
                levels.add(new PlayerLevel(minLevel, color));
            } catch (Exception e) {
                TGM.get().getLogger().warning("Failed to register level color for key '" + key + "': " + e.getMessage());
            }
        }
        levels.sort((a, b) -> b.minimumLevel - a.minimumLevel);
    }

    public static ChatColor getColor(int lvl) {
        for (PlayerLevel levelEntry : levels) {
            if (lvl >= levelEntry.minimumLevel) {
                return levelEntry.levelColor;
            }
        }
        return ChatColor.GRAY; // Fallback
    }

    public PlayerContext(Player player, UserProfile userProfile) {
        this.player = player;
        this.userProfile = userProfile;
    }

    public UserProfile getUserProfile() {
        return getUserProfile(false);
    }

    public UserProfile getUserProfile(boolean original) {
        if (isNicked() && !original) {
            return TGM.get().getNickManager().getNick(this).get().getProfile();
        } else {
            return userProfile;
        }
    }

    public String getDisplayName() {
        if (isNicked()) {
            return TGM.get().getNickManager().getNick(this).get().getName();
        }
        return player.getName();
    }

    public String getOriginalName() {
        if (isNicked()) {
            return TGM.get().getNickManager().getNick(this).get().getOriginalName();
        }
        return player.getName();
    }

    public boolean isNicked() {
        return false;
    }

    public String getLevelString() {
        return getLevelString(false);
    }

    public String getLevelString(boolean original) {
        int level = getUserProfile(original).getLevel();
        return "" + getColor(level) + "[" + level + "]";
    }

    public void updateRank(Rank r) {
        updateRank(r, false);
    }

    public void updateRank(Rank r, boolean forceUpdate) {
        List<String> oldPermissions = new ArrayList<>();
        boolean update = false;
        for (Rank rank : getUserProfile().getRanksLoaded()) {
            oldPermissions.addAll(rank.getPermissions());
            if (rank.getId().equals(r.getId())) {
                rank.set(r);
                update = true;
            }
        }
        if (update || forceUpdate) {
            oldPermissions.addAll(r.getPermissions());
            Ranks.removePermissions(player, oldPermissions);
            getUserProfile().getRanksLoaded().forEach(rank -> Ranks.addPermissions(player, rank.getPermissions()));
        }

    }

    public String getPrefix() {
        return getPrefix(false);
    }

    public String getPrefix(boolean original) {
        String prefix = getUserProfile(original).getPrefix();
        if (prefix != null) return prefix;
        if (isNicked() && !original || !Plugins.isVaultPresent()) return null;
        prefix = Plugins.Vault.getPrefix(this.player);
        return prefix == null || prefix.isEmpty() ? null : prefix;
    }

    @AllArgsConstructor @Getter
    private static class PlayerLevel {
        private final int minimumLevel;
        private final ChatColor levelColor;
    }
}
