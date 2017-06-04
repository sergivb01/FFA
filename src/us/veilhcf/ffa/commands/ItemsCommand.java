package us.veilhcf.ffa.commands;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import us.veilhcf.ffa.Main;
import us.veilhcf.ffa.events.PlayerEvents;

public class ItemsCommand implements CommandExecutor {
    private Main main;
    private PlayerEvents events = new PlayerEvents(main);

    public ItemsCommand(Main plugin) {
        this.main = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("ffaitems") && !(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "No");
            return true;
        }
        else{
            Player p = (Player) sender;
            if(!p.hasPermission("ffa.items")) {
                p.sendMessage(ChatColor.RED + "No");
                return true;
            } else {
                p.getInventory().clear();
                p.getInventory().setHelmet(null);
                p.getInventory().setChestplate(null);
                p.getInventory().setLeggings(null);
                p.getInventory().setBoots(null);

                events.setArmor(p);
                p.teleport(p.getWorld().getSpawnLocation());
                p.setGameMode(GameMode.SURVIVAL);
            }

            return true;
        }
    }
}