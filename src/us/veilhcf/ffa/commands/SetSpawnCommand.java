package us.veilhcf.ffa.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import us.veilhcf.ffa.Main;

public class SetSpawnCommand implements CommandExecutor {
    private Main main;

    public SetSpawnCommand(Main plugin) {
        this.main = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("setspawn") && !(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "No");
            return true;
        }
        else{
        Player p = (Player) sender;
	        if(!p.hasPermission("ffa.setspawn")) {
	            p.sendMessage(ChatColor.RED + "No");
	            return true;
	        } else {
	                Location l = p.getLocation();
	                int xc = l.getBlockX();
	                int yc = l.getBlockY();
	                int zc = l.getBlockZ();
	                p.getWorld().setSpawnLocation(xc, yc, zc);
	                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYou have set the world spawn!"));
	        }

            return true;
        }
    }
}