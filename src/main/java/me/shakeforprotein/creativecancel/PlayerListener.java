package me.shakeforprotein.creativecancel;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener implements Listener {

    private CreativeCancel pl;
    private UpdateChecker uc;

    public PlayerListener(CreativeCancel main){
        this.pl = main;
        this.uc = new UpdateChecker(pl);
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent e) {  if (e.getPlayer().hasPermission(uc.requiredPermission)) {
        if ((pl.getConfig().getString(e.getPlayer().getName()) == null) || ((pl.getConfig().getString(e.getPlayer().getName()) != null) && (pl.getConfig().getString(e.getPlayer().getName()).equalsIgnoreCase("false")))) {
            uc.getCheckDownloadURL(e.getPlayer());
            pl.getConfig().set(e.getPlayer().getName(), "true");
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(pl, new Runnable() {
                public void run() {
                    pl.getConfig().set(e.getPlayer().getName(), "false");
                }
            }, 60L);
        } else {
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(pl, new Runnable() {
                public void run() {
                    try {
                        pl.getConfig().set(e.getPlayer().getName(), null);
                    } catch (NullPointerException e) {
                    }
                }
            }, 80L);
        }
    }
    }
}
