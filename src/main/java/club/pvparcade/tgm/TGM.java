package club.pvparcade.tgm;

import cl.bgm.bukkit.util.CommandsManagerRegistration;
import cl.bgm.minecraft.util.commands.CommandsManager;
import cl.bgm.minecraft.util.commands.exceptions.CommandException;
import cl.bgm.minecraft.util.commands.exceptions.CommandPermissionsException;
import cl.bgm.minecraft.util.commands.exceptions.CommandUsageException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import club.pvparcade.tgm.api.ApiManager;
import club.pvparcade.tgm.broadcast.BroadcastManager;
import club.pvparcade.tgm.chat.ChatListener;
import club.pvparcade.tgm.command.BroadcastCommands;
import club.pvparcade.tgm.command.CycleCommands;
import club.pvparcade.tgm.command.MiscCommands;
import club.pvparcade.tgm.command.NickCommands;
import club.pvparcade.tgm.command.PunishCommands;
import club.pvparcade.tgm.command.RankCommands;
import club.pvparcade.tgm.command.RotationCommands;
import club.pvparcade.tgm.command.TGMCommand;
import club.pvparcade.tgm.command.TGMCommandManager;
import club.pvparcade.tgm.command.TagCommands;
import club.pvparcade.tgm.join.JoinManager;
import club.pvparcade.tgm.map.MapInfo;
import club.pvparcade.tgm.map.MapInfoDeserializer;
import club.pvparcade.tgm.map.Rotation;
import club.pvparcade.tgm.map.RotationDeserializer;
import club.pvparcade.tgm.match.MatchManager;
import club.pvparcade.tgm.match.MatchModule;
import club.pvparcade.tgm.modules.GameRuleModule;
import club.pvparcade.tgm.modules.itemremove.ItemRemoveInfo;
import club.pvparcade.tgm.modules.itemremove.ItemRemoveInfoDeserializer;
import club.pvparcade.tgm.modules.killstreak.Killstreak;
import club.pvparcade.tgm.modules.killstreak.KillstreakDeserializer;
import club.pvparcade.tgm.nickname.NickManager;
import club.pvparcade.tgm.parser.effect.EffectDeserializer;
import club.pvparcade.tgm.parser.item.ItemDeserializer;
import club.pvparcade.tgm.player.PlayerManager;
import club.pvparcade.tgm.util.Plugins;
import club.pvparcade.tgm.util.menu.PunishMenu;
import club.pvparcade.api.client.TeamClient;
import club.pvparcade.api.client.http.HttpClient;
import club.pvparcade.api.client.http.HttpClientConfig;
import club.pvparcade.api.client.offline.OfflineClient;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;

@Getter
public class TGM extends JavaPlugin {

    public static TGM instance;

    @Getter
    private Properties gitInfo = new Properties();

    private Gson gson;
    private TeamClient teamClient;

    private MatchManager matchManager;
    private PlayerManager playerManager;
    private ChatListener chatListener;
    private JoinManager joinManager;
    private ApiManager apiManager;
    private NickManager nickManager;

    private BroadcastManager broadcastManager;

    private CommandsManager<CommandSender> commands;
    private CommandsManagerRegistration commandManager;

    @Getter private long startupTime;

    public static TGM get() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        this.startupTime = new Date().getTime();
        saveDefaultConfig();
        FileConfiguration fileConfiguration = getConfig();

        gson = new GsonBuilder()
                // TGM
                .registerTypeAdapter(MapInfo.class, new MapInfoDeserializer())
                .registerTypeAdapter(Killstreak.class, new KillstreakDeserializer())
                .registerTypeAdapter(Rotation.class, new RotationDeserializer())
                .registerTypeAdapter(ItemRemoveInfo.class, new ItemRemoveInfoDeserializer())
                // Bukkit
                .registerTypeAdapter(ItemStack.class, new ItemDeserializer())
                .registerTypeAdapter(PotionEffect.class, new EffectDeserializer())

                .create();

        ConfigurationSection apiConfig = fileConfiguration.getConfigurationSection("api");
        if (apiConfig != null && apiConfig.getBoolean("enabled")) {
            teamClient = new HttpClient(new HttpClientConfig() {
                @Override
                public String getBaseUrl() {
                    return apiConfig.getString("url");
                }

                @Override
                public String getAuthToken() {
                    return apiConfig.getString("auth");
                }
            });
        } else {
            teamClient = new OfflineClient();
        }

        commands = new TGMCommandManager();

        matchManager = new MatchManager(fileConfiguration);
        matchManager.getMapRotation().refresh();

        playerManager = new PlayerManager();
        chatListener = new ChatListener();
        joinManager = new JoinManager();
        apiManager = new ApiManager();
        broadcastManager = new BroadcastManager();

        this.commandManager = new CommandsManagerRegistration(this, this.commands);

        commandManager.register(TGMCommand.TGMCommandNode.class);
        commandManager.register(CycleCommands.class);
        commandManager.register(BroadcastCommands.class);
        commandManager.register(MiscCommands.class);
        commandManager.register(NickCommands.class);
        commandManager.register(RotationCommands.class);
        if (apiConfig.getBoolean("enabled", false)) {
            commandManager.register(PunishCommands.class);
            commandManager.register(TagCommands.class);
            commandManager.register(RankCommands.class);
        }

        PunishMenu.getPresetsMenu().load();

        GameRuleModule.setGameRuleDefaults(Bukkit.getWorlds().get(0)); //Set gamerules in main unused world

        Plugins.checkSoftDependencies();

        matchManager.cycleNextMatch();
        if (matchManager.getMatch() != null) nickManager = new NickManager(); 
    }


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        try {
            this.commands.execute(commandLabel, args, sender, sender);
        } catch (CommandPermissionsException e) {
            if (sender instanceof Player) {
                sender.sendMessage(ChatColor.RED + "Insufficient permissions.");
            } else {
                sender.sendMessage(ChatColor.RED + "You do not have permission.");
            }
        } catch (CommandUsageException e) {
            sender.sendMessage(ChatColor.RED + e.getMessage());
            sender.sendMessage(ChatColor.RED + e.getUsage());
        } catch (CommandException e) {
            sender.sendMessage(e.getMessage());
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        try {
            return this.commands.complete(alias, args, sender, sender);
        } catch (Exception e) {
            return null;
        }
    }

    public static void registerEvents(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, TGM.get());
    }

    public static void unregisterEvents(Listener listener) {
        HandlerList.unregisterAll(listener);
    }

    public <T extends MatchModule> T getModule(Class<T> clazz) {
        return matchManager.getMatch().getModule(clazz);
    }

    public <T extends MatchModule> List<T> getModules(Class<T> clazz) {
        return matchManager.getMatch().getModules(clazz);
    }

    public static void broadcastMessage(String message) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(message);
        }
        Bukkit.getConsoleSender().sendMessage(message);
    }

}
