package miroshka.antiafk;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerChatEvent;
import cn.nukkit.event.player.PlayerCommandPreprocessEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerMoveEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.scheduler.NukkitRunnable;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;

public class AntiAFK extends PluginBase implements Listener {
    private Map<String, PlayerData> playerDataMap;
    private Config config;
    private MessageUtils messageUtils;
    
    private int afkTimeMinutes;
    private int kickTimeMinutes;
    private int warningTimeMinutes;
    private boolean broadcastAfk;
    private List<String> exemptPlayers;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        this.config = getConfig();
        
        this.playerDataMap = new HashMap<>();
        this.messageUtils = new MessageUtils(config);
        
        loadConfig();
        
        getServer().getPluginManager().registerEvents(this, this);
        
        startAfkCheckTask();
        
        this.getLogger().info(TextFormat.GREEN + "AntiAFK " + TextFormat.YELLOW + "v" + this.getDescription().getVersion() + TextFormat.GREEN + " successfully enabled!");
        this.getLogger().info(TextFormat.AQUA + "-----------------------------------");
        this.getLogger().info(TextFormat.YELLOW + "Follow us:");
        this.getLogger().info(TextFormat.BLUE + "• Telegram: " + TextFormat.WHITE + "https://t.me/ForgePlugins");
        this.getLogger().info(TextFormat.BLUE + "• VKontakte: " + TextFormat.WHITE + "https://vk.com/forgeplugin");
        this.getLogger().info(TextFormat.BLUE + "• GitHub: " + TextFormat.WHITE + "https://github.com/Miroshka000");
        this.getLogger().info(TextFormat.AQUA + "-----------------------------------");
    }

    private void loadConfig() {
        this.afkTimeMinutes = config.getInt("afk-time", 5);
        this.kickTimeMinutes = config.getInt("kick-time", 10);
        this.warningTimeMinutes = config.getInt("warning-time", 2);
        this.broadcastAfk = config.getBoolean("broadcast-afk", true);
        this.exemptPlayers = config.getStringList("exempt-players");
    }

    private void startAfkCheckTask() {
        new NukkitRunnable() {
            @Override
            public void run() {
                checkPlayersAfkStatus();
            }
        }.runTaskTimer(this, 20 * 60, 20 * 60);
    }

    private void checkPlayersAfkStatus() {
        long currentTime = System.currentTimeMillis();
        
        for (Player player : getServer().getOnlinePlayers().values()) {
            if (isPlayerExempt(player)) {
                continue;
            }
            
            String playerName = player.getName();
            
            if (!playerDataMap.containsKey(playerName)) {
                playerDataMap.put(playerName, new PlayerData(player));
                continue;
            }
            
            PlayerData playerData = playerDataMap.get(playerName);
            long inactiveTimeMinutes = playerData.getInactiveTime() / (1000 * 60);
            
            if (inactiveTimeMinutes >= kickTimeMinutes) {
                player.kick(messageUtils.getKickReason());
                playerDataMap.remove(playerName);
                continue;
            }
            
            if (inactiveTimeMinutes >= (kickTimeMinutes - warningTimeMinutes) && !playerData.isWarned()) {
                playerData.setWarned(true);
                player.sendMessage(messageUtils.formatMessage("warning", player, "time", warningTimeMinutes));
                continue;
            }
            
            if (inactiveTimeMinutes >= afkTimeMinutes && !playerData.isAfk()) {
                playerData.setAfk(true);
                
                if (broadcastAfk) {
                    getServer().broadcastMessage(messageUtils.formatMessage("afk", player));
                }
            }
        }
    }
    
    private boolean isPlayerExempt(Player player) {
        if (player.hasPermission("antiafk.bypass")) {
            return true;
        }
        
        return exemptPlayers.contains(player.getName().toLowerCase());
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        playerDataMap.put(player.getName(), new PlayerData(player));
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        playerDataMap.remove(event.getPlayer().getName());
    }
    
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        updatePlayerActivity(event.getPlayer());
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        updatePlayerActivity(event.getPlayer());
    }
    
    @EventHandler
    public void onPlayerChat(PlayerChatEvent event) {
        updatePlayerActivity(event.getPlayer());
    }
    
    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        updatePlayerActivity(event.getPlayer());
    }
    
    private void updatePlayerActivity(Player player) {
        String playerName = player.getName();
        
        if (playerDataMap.containsKey(playerName)) {
            PlayerData playerData = playerDataMap.get(playerName);
            
            if (playerData.isAfk()) {
                playerData.setAfk(false);
                
                if (broadcastAfk) {
                    getServer().broadcastMessage(messageUtils.formatMessage("no-longer-afk", player));
                }
            }
            
            playerData.updateActivity();
        } else {
            playerDataMap.put(playerName, new PlayerData(player));
        }
    }
} 