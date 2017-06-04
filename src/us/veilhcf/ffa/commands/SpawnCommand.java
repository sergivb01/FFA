package us.veilhcf.ffa.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import us.veilhcf.ffa.Main;

public class SpawnCommand implements CommandExecutor {
    private Main main;

    public SpawnCommand(Main plugin) {
        this.main = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("spawn")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "No");
                return true;

            } else {
                Player p = (Player) sender;

                if (!p.hasPermission("ffa.spawn")) {
                    p.sendMessage(ChatColor.RED + "No");
                    return true;
                }


                p.teleport(p.getWorld().getSpawnLocation());
                p.sendMessage(ChatColor.GREEN + "You have been teleported to the spawn!");

            }
        }
        return true;
    }
}