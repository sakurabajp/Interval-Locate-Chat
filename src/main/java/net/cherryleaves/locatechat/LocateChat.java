package net.cherryleaves.locatechat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public final class LocateChat extends JavaPlugin {

    private BukkitRunnable chatFlowTaskBefore;
    private BukkitRunnable chatFlowTask;

    @Override
    public void onEnable() {
        // Plugin startup logic
        Integer time1 = getConfig().getInt("TIME-INTERVAL");
        getLogger().info(ChatColor.GREEN + "ーーーーーーーーーーーーーーーーーーーーーーーーーーーーーー");
        getLogger().info(ChatColor.AQUA + "Locate the coordinates via chat Plugin!!!!!!!!!!!");
        getLogger().info("");
        getLogger().info(ChatColor.RED + "折角なのでここのコンソールをこのプラグインのログで");
        getLogger().info(ChatColor.RED + "埋め尽くしたいと思いまーす！");
        getLogger().info("");
        getLogger().info(ChatColor.GRAY + "((もちろん、起動時だけだよ！！！！))");
        getLogger().info("");
        getLogger().info(ChatColor.YELLOW + "プラグインが正常に起動しました(建前)");
        getLogger().info("");
        getLogger().info("ちなみに、今の設定は" + time1/20 + "秒だよ！");
        getLogger().info("");
        getLogger().info(ChatColor.GREEN + "ーーーーーーーーーーーーーーーーーーーーーーーーーーーーーー");
        Objects.requireNonNull(getCommand("startflow")).setExecutor(this);
        this.saveDefaultConfig();
        reloadConfig();
        FileConfiguration config = getConfig();
        config.getString("TIME-INTERVAL");
        super.onDisable();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        stopChatFlowTaskBefore();
        stopChatFlowTask();
        super.onEnable();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("startflow")) {
            List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
            String str1 = getConfig().getString("TIME-INTERVAL");
            int time1 = Integer.parseInt(Objects.requireNonNull(str1));
            sender.sendMessage(ChatColor.DARK_AQUA + "これから" + time1/20 + "秒ごとにランダムなプレイヤーの位置情報が公開されます");
            chatFlowTask = new BukkitRunnable() {
                @Override
                public void run() {
                    Random random = new Random();
                    Player randomPlayer = players.get(random.nextInt(players.size()));
                    String playerName = randomPlayer.getName();
                    int x = randomPlayer.getLocation().getBlockX();
                    int y = randomPlayer.getLocation().getBlockY();
                    int z = randomPlayer.getLocation().getBlockZ();
                    for (Player playerA : Bukkit.getOnlinePlayers()) {
                        playerA.playSound(playerA.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 0.1f);
                    }
                    Bukkit.broadcastMessage(ChatColor.YELLOW + "位置情報公開の時間です。");
                    Bukkit.broadcastMessage(ChatColor.RED + playerName + ChatColor.GREEN + " の座標は、" + ChatColor.RED + "X=" + x + ", Y=" + y + ", Z=" + z + ChatColor.GREEN + "です");
                }
            };
            chatFlowTask.runTaskTimer(this, 0, time1); // 6000 ticks = 5 minutes

            return true;
        }
        if (command.getName().equalsIgnoreCase("stopflow")) {
            List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
            Bukkit.broadcastMessage(ChatColor.DARK_AQUA + "コマンドが正常に停止しました");
            stopChatFlowTaskBefore();
            stopChatFlowTask();
        }
        return false;
    }

    private void stopChatFlowTask() {
        if (chatFlowTask != null) {
            chatFlowTask.cancel();
            chatFlowTask = null;
        }
    }
    private void stopChatFlowTaskBefore() {
        if (chatFlowTaskBefore != null) {
            chatFlowTaskBefore.cancel();
            chatFlowTaskBefore = null;
        }
    }
}
