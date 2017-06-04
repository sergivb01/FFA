package us.veilhcf.ffa.events;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import me.sergivb01.base.BasePlugin;
import me.sergivb01.base.user.BaseUser;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import org.bukkit.scheduler.BukkitRunnable;
import us.veilhcf.ffa.Main;
import us.veilhcf.ffa.utils.ItemStackBuilder;
import us.veilhcf.ffa.utils.ColorUtils;


public class PlayerEvents implements Listener{
    private Main main;

    public PlayerEvents(Main plugin) {
        this.main = plugin;
    }

    private static ItemStack sword = ItemStackBuilder.get(Material.IRON_SWORD, 1, (short)0, "&7Sword");
    private static ItemStack rod = ItemStackBuilder.get(Material.FISHING_ROD, 1, (short)0, "&7Fishing Rod");
    private static ItemStack bow = ItemStackBuilder.get(Material.BOW, 1, (short)0, "&7Bow");
    private static ItemStack flint = ItemStackBuilder.get(Material.FLINT_AND_STEEL, 1, (short)0, "&7Flint");
    private static ItemStack arrows = ItemStackBuilder.get(Material.ARROW, 8, (short)0, "&7Arrow");


    private static ItemStack head = ItemStackBuilder.get(Material.IRON_HELMET, 1, (short)0, "&7Armor");
    private static ItemStack chestplate = ItemStackBuilder.get(Material.IRON_CHESTPLATE, 1, (short)0, "&7Armor");
    private static ItemStack leggins = ItemStackBuilder.get(Material.IRON_LEGGINGS, 1, (short)0, "&7Armor");
    private static ItemStack boots = ItemStackBuilder.get(Material.IRON_BOOTS, 1, (short)0, "&7Armor");


    public void setArmor(Player p){
        p.setHealth(20);
        p.setFoodLevel(20);

        p.getInventory().setItem(0, sword);
        p.getInventory().setItem(1, rod);
        p.getInventory().setItem(2, bow);
        p.getInventory().setItem(3, flint);
        p.getInventory().setItem(9, arrows);

        p.getInventory().setHeldItemSlot(0);

        p.getInventory().setHelmet(head);
        p.getInventory().setChestplate(chestplate);
        p.getInventory().setLeggings(leggins);
        p.getInventory().setBoots(boots);
        p.sendMessage(ChatColor.GRAY + "You now have your items.");
    }

    private void toSpawn(Player p){
        double x = p.getLocation().getX() + 0.5;
        double y = p.getLocation().getY() + 1;
        double z = p.getLocation().getZ() + 0.5;
        Location loc = new Location(Bukkit.getWorld("world"), x, y, z);
        p.teleport(loc);
    }

    @EventHandler
    public void onBlockSpread(BlockSpreadEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e){
            e.setQuitMessage(null);
    }


  @SuppressWarnings("deprecation")
@EventHandler(priority=EventPriority.HIGHEST)
  public void onPlayerJoin(PlayerJoinEvent e){
    Player p = e.getPlayer();

      p.sendMessage(new ColorUtils().translateFromString("&8&m------------------------------------"));
      p.sendMessage("");
      p.sendMessage(new ColorUtils().translateFromString("&3Welcome &b" + p.getName() + " &3to the VeilMC FFA."));
      p.sendMessage("");
      p.sendMessage(new ColorUtils().translateFromString("&4&lREMEMBER: &7Team is not allowed and may result in a ban."));
      p.sendMessage(new ColorUtils().translateFromString("&8&m------------------------------------"));


      e.setJoinMessage(null);

      BaseUser baseUser = BasePlugin.getPlugin().getUserManager().getUser(p.getUniqueId());
      if(!baseUser.isStaffUtil()){
          p.getInventory().clear();
          p.getInventory().setHelmet(null);
          p.getInventory().setChestplate(null);
          p.getInventory().setLeggings(null);
          p.getInventory().setBoots(null);

          p.setHealth(20);
          p.setFoodLevel(20);

          setArmor(p);
          /*p.getInventory().setItem(0, iSword);
          p.getInventory().setItem(1, iRod);
          p.getInventory().setItem(2, iBow);
          p.getInventory().setItem(3, iFlint);
          p.getInventory().setItem(8, iArrow);

          p.getInventory().setHeldItemSlot(0);

          p.getInventory().setHelmet(new ItemStack(Material.IRON_HELMET));
          p.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
          p.getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
          p.getInventory().setBoots(new ItemStack(Material.IRON_BOOTS));
          */

      }else{
          p.sendMessage(ChatColor.RED + "Not giving items as you are in mod mode.");
      }

      p.setPlayerListName(ChatColor.GRAY + p.getName());
      toSpawn(p);
      p.teleport(p.getWorld().getSpawnLocation());

}

    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        BaseUser baseUser = BasePlugin.getPlugin().getUserManager().getUser(e.getPlayer().getUniqueId());
        if(baseUser.isStaffUtil() || baseUser.isVanished()){
            e.setCancelled(true);
            return;
        }


        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (e.getPlayer().getItemInHand() != null){
                ItemStack is = e.getPlayer().getItemInHand();
                if (is.getType() == Material.FLINT_AND_STEEL) {
                    is.setDurability((short)(is.getDurability() + 30));
                }
            }
        }
        if(e.getAction().equals(Action.PHYSICAL) && e.getClickedBlock().getType().equals(Material.STONE_PLATE)){
            e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 20, 48));
        }

    }

    private void death(Player p){

        main.getServer().getScheduler().scheduleSyncDelayedTask(main, () -> {
            setArmor(p);
            toSpawn(p);
        }, 20 * 3);


    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e){
        toSpawn(e.getPlayer());
    }


    @EventHandler
    public void onDeath(PlayerDeathEvent e){
        Player p = e.getEntity();
        Player killer = e.getEntity().getKiller();

        e.getDrops().clear();
        e.setDeathMessage(null);

        death(p);


        //p.getKiller().setStatistic(Statistic.PLAYER_KILLS, p.getKiller().getStatistic(Statistic.PLAYER_KILLS) + 1);
        //p.setStatistic(Statistic.DEATHS, p.getStatistic(Statistic.DEATHS) + 1);


        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&b" + p.getName() + " &3was killed by &b" + killer.getName() + " &7(" + (Math.floor(killer.getHealth() * 10) / 10) + "HP)"));
    }

  @EventHandler
  public void onWeatherChange(WeatherChangeEvent event){
    event.setCancelled(true);
  }
 
  @EventHandler
  public void time(ThunderChangeEvent event){
    event.setCancelled(true);
  }

  @EventHandler
  public void onBlockBreak(BlockBreakEvent e){
      Block block = e.getBlock();

      if (block.getType().equals(Material.FIRE)) {
          return;
      }

      if (!e.getPlayer().isOp()) {
          e.setCancelled(true);
          e.getPlayer().sendMessage(ChatColor.RED + "You may not break blocks.");
      }

  }

  @EventHandler
  public void onBlockPlace(BlockPlaceEvent event){
      if (event.getBlock().getType().equals(Material.FIRE) || event.getBlock().getType().equals(Material.COBBLESTONE) || event.getBlock().getType().equals(Material.WOOD)) {
            new BukkitRunnable(){public void run(){
                      event.getBlock().setType(Material.AIR);
              }
          }.runTaskLater(main, 5 * 20);

          return;
      }

    if (!event.getPlayer().isOp()) {
      event.setCancelled(true);
        event.getPlayer().sendMessage(ChatColor.RED + "You may not place blocks.");
    }

  }
  
  @EventHandler
  public void bucketFill(PlayerBucketEmptyEvent event){

      if (event.getBucket().equals(Material.WATER_BUCKET) || event.getBucket().equals(Material.LAVA_BUCKET)) {
          new BukkitRunnable() {
              public void run() {
                  //event.getBlockClicked().setType(Material.AIR);
                  event.getBlockClicked().getRelative(event.getBlockFace()).setType(Material.AIR);
              }
          }.runTaskLater(main, 5 * 20);
      }

    /*if (!event.getPlayer().isOp()) {
      event.setCancelled(true);
      event.getPlayer().sendMessage(ChatColor.RED + "You may not empty a bucket.");
    }*/
  }
  
  @EventHandler
  public void bucketEmpty(PlayerBucketFillEvent event){
    if (!event.getPlayer().isOp()) {
      event.setCancelled(true);
        event.getPlayer().sendMessage(ChatColor.RED + "You may not fill a bucket.");
    }
  }

  @EventHandler
  public void onShoot(EntityDamageByEntityEvent e){
      if ((e.getDamager() instanceof Arrow)){
          Arrow a = (Arrow)e.getDamager();
          if ((a.getShooter() instanceof Player)) {
              a.getShooter();
              Player p = (Player) a.getShooter();
              Damageable dp = (Damageable) e.getEntity();
              if ((dp instanceof Player)) {
                  Player v = (Player) dp;
                  double ptviev = dp.getHealth();
                  Integer damage = Integer.valueOf((int) e.getFinalDamage());
                  Integer realHealth = Integer.valueOf((int) (ptviev - damage.intValue()));
                  if (realHealth.intValue() > 0) {
                      if (p.getPlayer().getName() != v.getPlayer().getName()) {
                          p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&bShoot&8] &b" + ((Player) dp).getName() + " &3is now on &b" + (realHealth.intValue() / 2.0D) + "&3 hearts!"));
                      }
                  }
              }
          }
      }
  }

  @EventHandler
  public void onPlayerDropItem(PlayerDropItemEvent e){
    e.setCancelled(true);
  }
  
  @EventHandler
  public void onPlayerPickItem(PlayerPickupItemEvent e){

    if(e.getItem().equals(Material.GOLDEN_APPLE)){
        e.getPlayer().removePotionEffect(PotionEffectType.REGENERATION);
        e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 2));
    }

    e.setCancelled(true);
    e.getItem().remove();
  }

  @EventHandler
  public void chat(final AsyncPlayerChatEvent e) {
  	  	Player p = e.getPlayer();
  	/*if(Main.getRanks(p).equalsIgnoreCase("Owner")){//e.getMessage()
  		e.setFormat(ChatColor.translateAlternateColorCodes('&', "&8[&4Owner&8] &4") + e.getPlayer().getDisplayName() + ChatColor.translateAlternateColorCodes('&', "&7:&f ") + (new ColorUtils().translateFromString(e.getMessage() + "")));
  	}
  	
  	else if(Main.getRanks(p).equalsIgnoreCase("staff")){
  		e.setFormat(ChatColor.translateAlternateColorCodes('&', "&8[&6Staff&8] &6") + e.getPlayer().getDisplayName() + ChatColor.translateAlternateColorCodes('&', "&7:&f ") + (new ColorUtils().translateFromString(e.getMessage() + "")));
  	}
  	
  	else if(Main.getRanks(p).equalsIgnoreCase("vip")){
  		e.setFormat(ChatColor.translateAlternateColorCodes('&', "&8[&eVIP&8] &e") + e.getPlayer().getDisplayName() + ChatColor.translateAlternateColorCodes('&', "&7:&f ") + (new ColorUtils().translateFromString(e.getMessage() + "")));
  	}
  	
  	else if(Main.getRanks(p).equalsIgnoreCase("media")){
  		e.setFormat(ChatColor.translateAlternateColorCodes('&', "&8[&dMedia&8] &d") + e.getPlayer().getDisplayName() + ChatColor.translateAlternateColorCodes('&', "&7:&f ") + (new ColorUtils().translateFromString(e.getMessage() + "")));
  	}
  	else{
        e.setFormat(ChatColor.translateAlternateColorCodes('&', "&a") + e.getPlayer().getDisplayName() + ChatColor.translateAlternateColorCodes('&', "&7:&f ") + e.getMessage() + "");
  		//p.sendMessage(ChatColor.GOLD + "In order to chat you must purchase a rank at " + ChatColor.RESET + ChatColor.YELLOW + "store.veilhcf.us");
        //e.setCancelled(true);
  	//}*/
      //String prefix =  Rank.getPrefix(p.getUniqueId());

      e.setFormat(ChatColor.translateAlternateColorCodes('&', "") + e.getPlayer().getDisplayName() + ChatColor.translateAlternateColorCodes('&', "&7:&f ") + e.getMessage() + "");
  	
  }


  @EventHandler
  public void onFoodLevelChange(FoodLevelChangeEvent e){
    e.setCancelled(true);
  }

  @EventHandler
  public void onEntityDamage(EntityDamageEvent e){
      if(e.getCause().equals(EntityDamageEvent.DamageCause.FALL)){
        e.setCancelled(true);
      }
  }


}
