package me.shakeforprotein.creativecancel;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.*;
import org.bukkit.plugin.java.JavaPlugin;

import static org.bukkit.Material.matchMaterial;

public final class CreativeCancel extends JavaPlugin implements Listener {
    String prefix = ChatColor.GOLD + getConfig().getConfigurationSection("messages").getString("prefix") + "§r";
    String cprefix = getConfig().getConfigurationSection("messages").getString("prefix");

    private UpdateChecker uc;

    @Override
    public void onEnable() {
        // Plugin startup logic
        System.out.println(cprefix + "Creative Cancel is Starting");
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        getServer().getPluginManager().registerEvents(this, this);
        getConfig().options().copyDefaults(true);
        saveConfig();
        this.uc = new UpdateChecker(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        saveConfig();
        System.out.println(cprefix + "Creative Cancel has shutdown");
    }

    @EventHandler
    public void deleteOnChangeItem(PlayerItemHeldEvent e) {
        Player p = e.getPlayer();
        String World = p.getLocation().getWorld().getName();
        if (!p.hasPermission("creativecancel.bypass")) {
            if (getConfig().getConfigurationSection("BlockedItems." + World) != null) {
                ConfigurationSection blockedList = getConfig().getConfigurationSection("BlockedItems." + World);
                for (String item : blockedList.getKeys(false)) {
                    item = item.toUpperCase().trim();
                    if (Material.matchMaterial(item) != null) {
                        if (p.getInventory().contains(Material.getMaterial(item))) {
                            e.setCancelled(true);
                            p.getInventory().remove(matchMaterial(item));
                            p.sendMessage(prefix + ChatColor.RED + getConfig().getConfigurationSection("messages").getString("changedHotbar").replace("XXXPLAYERXXX", p.getName() + "§r").replace("XXXITEMXXX", item + "§r").replace('&', '§'));
                        }


                    }
                }
            }
        }
    }

    @EventHandler
    public void deleteOnDrop(PlayerDropItemEvent e) {
        Player p = e.getPlayer();
        String World = p.getLocation().getWorld().getName();
        if (!p.hasPermission("creativecancel.bypass")) {
            if (getConfig().getConfigurationSection("BlockedItems." + World) != null) {
                ConfigurationSection blockedList = getConfig().getConfigurationSection("BlockedItems." + World);
                for (String itemA : blockedList.getKeys(false)) {
                    String item = blockedList.getString(itemA).toUpperCase().trim();
                    if (Material.matchMaterial(item) != null) {
                        if (e.getItemDrop().getItemStack().getType() == Material.getMaterial(item)) {
                            e.setCancelled(true);
                            p.getInventory().remove(Material.getMaterial(item));
                            p.sendMessage(prefix + ChatColor.RED + getConfig().getConfigurationSection("messages").getString("itemDropped").replace("XXXPLAYERXXX", p.getName()).replace("XXXITEMXXX", item).replace('&', '§'));
                        }
                    }
                }

            }
        }
    }


    @EventHandler
    public void deleteOnCloseInventory(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        String World = p.getLocation().getWorld().getName();
        if (!p.hasPermission("creativecancel.bypass")) {
            if (getConfig().getConfigurationSection("BlockedItems." + World) != null) {
                ConfigurationSection blockedList = getConfig().getConfigurationSection("BlockedItems." + World);
                for (String itemA : blockedList.getKeys(false)) {
                    String item = blockedList.getString(itemA).toUpperCase().trim();
                    if (Material.matchMaterial(item) != null) {
                        if (p.getInventory().contains(matchMaterial(item))) {
                            p.getInventory().remove(matchMaterial(item));
                            p.sendMessage(prefix + ChatColor.RED + getConfig().getConfigurationSection("messages").getString("closeInventory").replace("XXXPLAYERXXX", p.getName()).replace("XXXITEMXXX", item).replace('&', '§'));
                        }
                    }

                }
            }
        }
    }

    @EventHandler
    public void deleteOnAnimate(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        String World = p.getLocation().getWorld().getName();
        if (!p.hasPermission("creativecancel.bypass")) {
            if (getConfig().getConfigurationSection("BlockedItems." + World) != null) {
                ConfigurationSection blockedList = getConfig().getConfigurationSection("BlockedItems." + World);
                for (String itemA : blockedList.getKeys(false)) {
                    String item = blockedList.getString(itemA).toUpperCase();
                    if (Material.matchMaterial(item) != null) {
                        if (p.getInventory().contains(matchMaterial(item))) {
                            e.setCancelled(true);
                            p.getInventory().remove(matchMaterial(item));
                            p.sendMessage(prefix + ChatColor.RED + getConfig().getConfigurationSection("messages").getString("animate").replace("XXXPLAYERXXX", p.getName()).replace("XXXITEMXXX", item).replace('&', '§'));
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;
        String World = p.getLocation().getWorld().getName();
        if (getConfig().getConfigurationSection("BlockedItems") == null) {
            getConfig().createSection("BlockedItems");
            saveConfig();
        }
        if (getConfig().getConfigurationSection("BlockedItems." + World) == null) {
            getConfig().createSection("BlockedItems." + World);
            saveConfig();
        }
        if (getConfig().getConfigurationSection("ConsoleNotify") == null) {
            getConfig().createSection("ConsoleNotify");
            getConfig().set("ConsoleNotify", true);
            saveConfig();
        }

        ConfigurationSection blockedList = getConfig().getConfigurationSection("BlockedItems." + World);
        if (cmd.getName().equalsIgnoreCase("ccBlock") && sender instanceof Player) {
            if (sender.hasPermission("creativecancel.cancel")) {
                for (String argString : args) {
                    boolean addToList = true;
                    for (String item : blockedList.getKeys(false)) {
                        if (blockedList.getString(item).equalsIgnoreCase(argString.toUpperCase())) {
                            addToList = false;
                        }
                    }
                    if (addToList == true) {
                        getConfig().set("BlockedItems." + World + "." + argString.toUpperCase(), argString.toUpperCase());
                        saveConfig();
                        if(getConfig().getString("ConsoleNotify").equalsIgnoreCase("true")) {
                            System.out.println(cprefix + sender.getName() + " has added " + argString + " to the blocked items list on World - " + World);
                        }
                        sender.sendMessage(prefix + getConfig().getConfigurationSection("messages").getString("itemBlocked").replace("XXXPLAYERXXX", p.getName() + "§r").replace("XXXITEMXXX", ChatColor.RED + argString).replace('&', '§'));
                    }
                }
            } else {
                sender.sendMessage(prefix + ChatColor.RED + (prefix + getConfig().getConfigurationSection("messages").getString("noCommandPerm").replace("XXXPLAYERXXX", sender.getName())).replace('&', '§'));
            }
        }
        if (cmd.getName().equalsIgnoreCase("ccUnblock") && sender instanceof Player) {
            if (sender.hasPermission("creativecancel.cancel")) {

                for (String argString : args) {
                    boolean removeFromList = false;
                    for (String item : blockedList.getKeys(false)) {
                        if (blockedList.getString(item).toUpperCase().equalsIgnoreCase(argString.toUpperCase())) {
                            removeFromList = true;
                        }
                    }
                    if (removeFromList == true) {
                        getConfig().set("BlockedItems." + World + "." + argString.toUpperCase(), null);
                        saveConfig();
                        if(getConfig().getString("ConsoleNotify").equalsIgnoreCase("true")) {
                            System.out.println(cprefix + sender.getName() + " has removed " + argString + " from the blocked items list on World - " + World);
                        }
                        sender.sendMessage(prefix + getConfig().getConfigurationSection("messages").getString("itemUnblocked").replace("XXXITEMXXX", ChatColor.GREEN + argString + "§r").replace('&', '§'));
                    } else {
                        sender.sendMessage((prefix + ChatColor.RED + "item " + argString + " not found").replace('&', '§'));
                    }
                }
            } else {
                sender.sendMessage((prefix + ChatColor.RED + (getConfig().getConfigurationSection("messages").getString("noCommandPerm").replace("XXXPLAYERXXX", sender.getName() + "§r")).replace('&', '§')));
            }

        }
        if (cmd.getName().equalsIgnoreCase("ccShowBlocked") && sender instanceof Player) {
            if (sender.hasPermission("creativecancel.show")) {
                sender.sendMessage(prefix + getConfig().getString("messages.showBlocked").replace('&', '§'));
                sender.sendMessage(prefix + "Begin List");
                for (String item : blockedList.getKeys(false)) {
                    sender.sendMessage(prefix + "§3" + item + "  -  " + blockedList.getString(item) + "§f");
                }
                sender.sendMessage(prefix + "End List");


            } else {
                sender.sendMessage(prefix + ChatColor.RED + (getConfig().getConfigurationSection("messages").getString("noCommandPerm").replace("XXXPLAYERXXX", sender.getName() + "§r")).replace('&', '§'));
            }
        }
        return true;
    }
}