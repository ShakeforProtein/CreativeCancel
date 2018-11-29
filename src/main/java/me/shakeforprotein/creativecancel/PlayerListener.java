package me.shakeforprotein.creativecancel;

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
    public void onPlayerJoinEvent(PlayerJoinEvent e) {
        if (e.getPlayer().hasPermission(uc.requiredPermission)) {
            uc.getCheckDownloadURL(e.getPlayer());
        }
    }
}
