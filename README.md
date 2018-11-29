
# Creative Cancel
Cancel the actions of those too creative.



## About
Allows players with the required permissions to add blocks to, remove blocks from, or ignore, a banned block list. This was written because of an issue with Tridents being able to crash spigot servers that had not at the time of writing been dealt with.

This plugin works on a per world basis and items need to be added or removed on each world. In the future there will be an option to ban on all worlds.

Items are destroyed in four circumstances. When an inventory is closed, when the hotbar slot changes, when the item is dropped and when a player animation event triggers, eg moving or using.

The config is in yaml format, so be aware of this if you choose to manually edit the file.

Also please note, items are blocked by their bukkit material name. As at writing, the list can be found [Here](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Material.html)

There is an option in the config to notify console when a player attempts to do something naughty.

Messages can be modified for each of the triggers



## Commands
```
ccBlock EXAMPLE_ITEM1 ExAmPlE_IteM2 EXAMPLE_ITEM3 etc     - Adds all item arguments to your current worlds block list
ccUnblock EXAMPLE_ITEM1 EXAMPLE_ITEM2 EXAMPLE_ITEM3       - Removes all item arguments from your current worlds block list
ccShowBlocked                                             - Shows all items blocked on your current world
```


## Permissions
```
creativecancel.cancel   -   Players with this permission can add and remove items from the blocked item list.
creativecancel.bypass   -   Players with this permission will not be effected by creative cancel.
creativecancel.show     -   Players with this permission can see what items have been blocked.
creativecancel.updateCheck - Notifies a user if there are updates available for Creative Cancel
```

