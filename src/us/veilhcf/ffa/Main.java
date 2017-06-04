package us.veilhcf.ffa;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.sergivb01.base.BasePlugin;
import me.sergivb01.base.user.BaseUser;
import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.util.org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import us.veilhcf.ffa.commands.ItemsCommand;
import us.veilhcf.ffa.commands.SetSpawnCommand;
import us.veilhcf.ffa.commands.SpawnCommand;
import us.veilhcf.ffa.events.PlayerEvents;
import us.veilhcf.ffa.utils.ColorUtils;
import us.veilhcf.ffa.utils.ScoreboardHelper;

import java.util.HashMap;
import java.util.Map;

//import ru.tehkode.permissions.PermissionUser;
//import ru.tehkode.permissions.bukkit.PermissionsEx;

public class Main extends JavaPlugin implements Listener, PluginMessageListener {
    private static Main instance;
    private PlayerEvents playerEvents;
    private int abc = 0;
    private String abcd = "ALL";
    private PluginManager manager = Bukkit.getServer().getPluginManager();

    private final Map<Player, ScoreboardHelper> scoreboardHelperMap = new HashMap<>();

    public static Main getInstance() {
        if (instance == null) {
            instance = new Main();
        }
        return instance;
    }


    @SuppressWarnings({"deprecation"})
    @Override
    public void onEnable() {
        long timeMillis = System.currentTimeMillis();



        instance = this;

        //Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(this, "RedisBungee");
        //Bukkit.getServer().getMessenger().registerIncomingPluginChannel(this, "RedisBungee", this);

        Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        Bukkit.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);


        Bukkit.getServer().getPluginManager().registerEvents(this, this);

        setupScoreboard();

        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            onPlayerJoin(player);
        }


        getConsoleCommandSender().sendMessage("");
        getConsoleCommandSender().sendMessage(new ColorUtils().translateFromString(ChatColor.YELLOW + "[Lobby] Plugin loaded in " + (System.currentTimeMillis() - timeMillis) + "ms."));
        getConsoleCommandSender().sendMessage("");
        getConsoleCommandSender().sendMessage(new ColorUtils().translateFromString(ChatColor.YELLOW + "&lPlugin has been loaded and connected to the database"));
        getConsoleCommandSender().sendMessage("");


        getCommand("setspawn").setExecutor(new SetSpawnCommand(this));
        getCommand("spawn").setExecutor(new SpawnCommand(this));
        getCommand("ffaitems").setExecutor(new ItemsCommand(this));
        manager.registerEvents(new PlayerEvents(this), this);

    }

    @Override
    public void onDisable() {
        instance = null;
    }

    private ConsoleCommandSender getConsoleCommandSender() {
        return Bukkit.getServer().getConsoleSender();
    }

    private void onPlayerJoin(Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (player.isOnline()) {
                    Scoreboard scoreboard = Bukkit.getServer().getScoreboardManager().getNewScoreboard();
                    ScoreboardHelper scoreboardHelper = new ScoreboardHelper(scoreboard, new ColorUtils().translateFromString("&3&lVeilMC &b&lFFA"));

                    scoreboardHelperMap.put(player, scoreboardHelper);
                }
            }
        }.runTaskLater(this, 20L);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        onPlayerJoin(player);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        scoreboardHelperMap.remove(player);
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        Player player = event.getPlayer();
        scoreboardHelperMap.remove(player);
    }

   /* @SuppressWarnings("deprecation")
    public static String getRanks(Player player) {

        String message = Rank.getRank(player.getName()).getName();

        /*PermissionUser permissionUser = PermissionsEx.getUser(player);
        for (String ranks : permissionUser.getGroupNames()) {
            message += ranks + ", ";
        }

        if (message.length() > 2) {
            message = message.substring(0, message.length() - 2);
        }

        if (message.length() == 0) {
            message = "User";
        }

        if (message.equalsIgnoreCase("default")) {
            message = "User";
        }

        return message;
    }*/

    private void setCount(int a) {
        abc = a;
    }

    private void setServer(String z) {
        abcd = z;
    }

    private int getPl() {
        return abc;
    }

    private String getSrv() {
        return abcd;
    }


    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("BungeeCord")) {
            return;
        }

        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();

        if (subchannel.equals("PlayerCount")) {
            String server = in.readUTF();
            int playerCount = in.readInt();

            //player.sendMessage("Player count on server " + server + " is equal to " + playerCount);
            //player.sendMessage(playerCount + "");
            setCount(playerCount);
            setServer(server);

        }

    }

    public String getCount(Player player, String server) {

        if (server == null) {
            server = "ALL";
        }

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("PlayerCount");
        out.writeUTF(server);

        player.sendPluginMessage(this, "BungeeCord", out.toByteArray());
        return server;
    }

    public int getPing(Player p) {
        CraftPlayer cp = (CraftPlayer) p;
        EntityPlayer ep = cp.getHandle();
        return ep.ping;
    }


    private static String capitalizeFirstLetter(String sentence) {
        String words[] = sentence.replaceAll("\\s+", " ").trim().split(" ");
        String newSentence = "";
        for (String word : words) {
            for (int i = 0; i < word.length(); i++)
                newSentence = newSentence + ((i == 0) ? word.substring(i, i + 1).toUpperCase():
                        (i != word.length() - 1) ? word.substring(i, i + 1).toLowerCase() : word.substring(i, i + 1).toLowerCase().toLowerCase() + " ");
        }

        return newSentence.trim();
    }

    private void setupScoreboard() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Map.Entry<Player, ScoreboardHelper> entry : scoreboardHelperMap.entrySet()) {
                    Player player = entry.getKey();
                    BaseUser baseUser = BasePlugin.getPlugin().getUserManager().getUser(player.getUniqueId());

                    ScoreboardHelper scoreboardHelper = entry.getValue();
                    scoreboardHelper.clear();

                    int kills = player.getStatistic(Statistic.PLAYER_KILLS);
                    int deaths = player.getStatistic(Statistic.DEATHS);


                    //double kd = kills/deaths;

                    //kd = Math.round(kd * 100);
                    //kd = kd / 100;

                    scoreboardHelper.add(new ColorUtils().translateFromString("&8&l&m---------------------"));
                    scoreboardHelper.add(new ColorUtils().translateFromString("&3Online » &b" + Bukkit.getOnlinePlayers().size() + "&3/&b" + Bukkit.getServer().getMaxPlayers()));
                    scoreboardHelper.add(new ColorUtils().translateFromString("&3Kills » &b" + kills));
                    scoreboardHelper.add(new ColorUtils().translateFromString("&3Deaths » &b" + deaths));
                    //scoreboardHelper.add(new ColorUtils().translateFromString("&3Killstreak » &b" + playerEvents.getKillMap().get(player.getName())));

                if (player.hasPermission("rank.staff")) {
                        if(baseUser.isGlintEnabled()) {
                            scoreboardHelper.add(new ColorUtils().translateFromString("&8&l&m---------------------"));

                            if (baseUser.isStaffUtil()) {
                                scoreboardHelper.add(new ColorUtils().translateFromString("&3Staff Mode: &bEnabled"));

                                scoreboardHelper.add(new ColorUtils().translateFromString(" &b» &3StaffChat: &b" + capitalizeFirstLetter(String.valueOf(baseUser.isInStaffChat()))));

                                scoreboardHelper.add(new ColorUtils().translateFromString(" &b» &3Vanished: &b" + capitalizeFirstLetter(String.valueOf(baseUser.isVanished()))));

                                scoreboardHelper.add(new ColorUtils().translateFromString(" &b» &3Gamemode: &b" + capitalizeFirstLetter(player.getGameMode().toString())));

                                if (BasePlugin.getPlugin().getServerHandler().isChatDisabled()) {
                                    scoreboardHelper.add(new ColorUtils().translateFromString(" &b» &3Chat: &bLocked"));
                                }

                                if (BasePlugin.getPlugin().getServerHandler().isChatSlowed()) {
                                    scoreboardHelper.add(new ColorUtils().translateFromString(" &b» &3Slow Chat: &b" + DurationFormatUtils.formatDurationWords(BasePlugin.getPlugin().getServerHandler().getRemainingChatSlowedMillis(), true, true)));
                                }

                            } else {
                                scoreboardHelper.add(new ColorUtils().translateFromString("&3Staff Mode: &bDisabled"));
                            }

                        }
                }

                    scoreboardHelper.add(new ColorUtils().translateFromString("&8&l&m---------------------"));

                    scoreboardHelper.update(player);

                }
            }
        }.runTaskTimer(this, 0L, 20L);
    }

}