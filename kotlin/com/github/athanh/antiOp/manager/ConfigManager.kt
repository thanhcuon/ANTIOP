package com.github.athanh.antiOp.manager

import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.plugin.java.JavaPlugin

object ConfigManager {
    lateinit var config: FileConfiguration
    var opList = mutableListOf<String>()
    var blockedCommands = mutableListOf<String>()
    var banCommands = mutableListOf<String>()
    var banMessage = "You are not in OP list :3"
    var kickMessage = "You have been kicked for unauthorized OP access!"

    fun loadConfig(plugin: JavaPlugin) {
        plugin.reloadConfig()
        config = plugin.config

        opList = config.getStringList("op-list").toMutableList()
        blockedCommands = config.getStringList("blocked-commands").toMutableList()
        banCommands = config.getStringList("ban-commands").toMutableList()
        banMessage = config.getString("ban-message", banMessage)!!
        kickMessage = config.getString("kick-message", kickMessage)!!
    }

    fun saveConfig(plugin: JavaPlugin) {
        config.set("op-list", opList)
        plugin.saveConfig()
    }
}
