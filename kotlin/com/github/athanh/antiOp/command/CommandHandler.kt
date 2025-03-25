package com.github.athanh.antiOp.command

import com.github.athanh.antiOp.manager.ConfigManager
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class CommandHandler : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (args.isEmpty()) {
            sender.sendMessage("${ChatColor.RED}Sử dụng: /antiop <reload|add|remove>")
            return true
        }

        when (args[0].lowercase()) {
            "reload" -> {
                val plugin = sender.server.pluginManager.getPlugin("AntiOp") as? JavaPlugin
                if (plugin == null) {
                    sender.sendMessage("${ChatColor.RED}❌ Không tìm thấy plugin AntiOp!")
                    return true
                }
                ConfigManager.saveConfig(plugin)

                sender.sendMessage("${ChatColor.GREEN}✅ AntiOp đã tải lại cấu hình!")
            }

            "add" -> {
                if (sender is Player) {
                    sender.sendMessage("${ChatColor.RED}🚫 Lệnh này chỉ có thể chạy từ Console!")
                    return true
                }
                if (args.size < 2) {
                    sender.sendMessage("${ChatColor.RED}⚠ Sử dụng: /antiop add <tên người chơi>")
                    return true
                }
                val playerName = args[1]
                ConfigManager.opList.add(playerName)
                val plugin = sender.server.pluginManager.getPlugin("AntiOp") as? JavaPlugin
                if (plugin == null) {
                    sender.sendMessage("${ChatColor.RED}❌ Không tìm thấy plugin AntiOp!")
                    return true
                }
                ConfigManager.saveConfig(plugin)

                sender.sendMessage("${ChatColor.GREEN}✅ Đã thêm $playerName vào danh sách OP!")
            }

            "remove" -> {
                if (sender is Player) {
                    sender.sendMessage("${ChatColor.RED}🚫 Lệnh này chỉ có thể chạy từ Console!")
                    return true
                }
                if (args.size < 2) {
                    sender.sendMessage("${ChatColor.RED}⚠ Sử dụng: /antiop remove <tên người chơi>")
                    return true
                }
                val playerName = args[1]
                if (ConfigManager.opList.remove(playerName)) {
                    val plugin = sender.server.pluginManager.getPlugin("AntiOp") as? JavaPlugin
                    if (plugin == null) {
                        sender.sendMessage("${ChatColor.RED}❌ Không tìm thấy plugin AntiOp!")
                        return true
                    }
                    ConfigManager.saveConfig(plugin)

                    sender.sendMessage("${ChatColor.GREEN}✅ Đã xóa $playerName khỏi danh sách OP!")
                } else {
                    sender.sendMessage("${ChatColor.RED}❌ $playerName không có trong danh sách OP!")
                }
            }

            else -> sender.sendMessage("${ChatColor.RED}⚠ Lệnh không hợp lệ!")
        }
        return true
    }
}