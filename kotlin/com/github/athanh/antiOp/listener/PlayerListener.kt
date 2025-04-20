package com.github.athanh.antiOp.listener

import com.github.athanh.antiOp.manager.ConfigManager
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.java.JavaPlugin

class PlayerListener(private val plugin: JavaPlugin) : Listener {

    init {
        startOpCheckTask()
    }
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        checkOp(event.player)
    }

    @EventHandler
    fun onCommand(event: PlayerCommandPreprocessEvent) {
        val player = event.player
        val command = event.message.lowercase()

        if (ConfigManager.whitelistPlugins.any { pluginName ->
                player.name.equals(pluginName, ignoreCase = true)
            }) {
            return  // Bỏ qua việc kiểm tra nếu người gửi trong whitelist
        }


        for (blocked in ConfigManager.blockedCommands) {
            if (command.startsWith("/$blocked")) {
                event.isCancelled = true
                player.sendMessage("${ChatColor.RED}🚫 Lệnh này đã bị chặn bởi AntiOp!")
                return
            }
        }
        if (command.startsWith("/op ")) {
            val args = command.split(" ")
            if (args.size >= 2) {
                val targetName = args[1]
                val targetPlayer = Bukkit.getPlayer(targetName)

                if (targetPlayer != null && !ConfigManager.opList.contains(targetPlayer.name)) {
                    event.isCancelled = true
                    Bukkit.getScheduler().runTask(plugin, Runnable {
                        targetPlayer.isOp = false
                        banPlayer(targetPlayer)
                    })
                    player.sendMessage("${ChatColor.RED}🚨 Bạn không thể cấp OP cho $targetName!")
                }
            }
        }
    }

    private fun checkOp(player: Player) {
        if (player.isOp && !ConfigManager.opList.contains(player.name)) {
            Bukkit.getScheduler().runTask(plugin, Runnable {
                player.isOp = false
                banPlayer(player)
            })
        }
    }


    private fun startOpCheckTask() {
        Bukkit.getScheduler().runTaskTimer(plugin, Runnable {
            Bukkit.getOnlinePlayers().forEach { checkOp(it) }
        }, 100L, 100L)
    }

    private fun banPlayer(player: Player) {
        val console = Bukkit.getConsoleSender()
        val playerName = player.name
        val ip = player.address?.address?.hostAddress ?: "UNKNOWN_IP"

        ConfigManager.banCommands.forEach { cmd ->
            val formattedCmd = cmd.replace("%player%", playerName).replace("%ip%", ip)
            Bukkit.dispatchCommand(console, formattedCmd)
        }

        Bukkit.getLogger().warning("❌ Người chơi ${playerName} đã bị xử lý vì có quyền OP trái phép!")
    }
}
