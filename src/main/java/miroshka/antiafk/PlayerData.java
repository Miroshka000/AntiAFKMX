package miroshka.antiafk;

import cn.nukkit.Player;

public class PlayerData {
    private final Player player;
    private long lastActivity;
    private boolean isAfk;
    private boolean isWarned;

    public PlayerData(Player player) {
        this.player = player;
        this.lastActivity = System.currentTimeMillis();
        this.isAfk = false;
        this.isWarned = false;
    }

    public Player getPlayer() {
        return player;
    }

    public long getLastActivity() {
        return lastActivity;
    }

    public void updateActivity() {
        this.lastActivity = System.currentTimeMillis();
        this.isAfk = false;
        this.isWarned = false;
    }

    public boolean isAfk() {
        return isAfk;
    }

    public void setAfk(boolean afk) {
        isAfk = afk;
    }

    public boolean isWarned() {
        return isWarned;
    }

    public void setWarned(boolean warned) {
        isWarned = warned;
    }
    
    public long getInactiveTime() {
        return System.currentTimeMillis() - lastActivity;
    }
} 