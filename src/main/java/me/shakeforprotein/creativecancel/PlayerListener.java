package me.shakeforprotein.creativecancel;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
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
            uc.checkUpdates(e.getPlayer());
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

    @EventHandler
    public void liquidFlow(BlockPhysicsEvent e) {
            if (e.getSourceBlock().getType() == Material.WATER || e.getSourceBlock().getType() == Material.LAVA) {
                e.setCancelled(true);
            }
    }

    @EventHandler
    public void liquidFlow2(BlockFromToEvent e) {
        if (e.getBlock().getWorld().getName().equalsIgnoreCase("hub")) {
            if ((e.getBlock().getType() == Material.WATER || e.getBlock().getType() == Material.LAVA) && (e.getToBlock().getType() == Material.LAVA || e.getToBlock().getType() == Material.WATER)) {
                e.setCancelled(true);
            }
        }
    }
}
