package com.github.athanh.antiOp

import com.github.athanh.antiOp.command.CommandHandler
import com.github.athanh.antiOp.listener.PlayerListener
import com.github.athanh.antiOp.manager.ConfigManager
import org.bukkit.plugin.java.JavaPlugin


class Main : JavaPlugin() {

    override fun onEnable() {
        saveDefaultConfig()
        ConfigManager.loadConfig(this)

        server.pluginManager.registerEvents(PlayerListener(this), this)

        getCommand("antiop")?.setExecutor(CommandHandler())

        logger.info("✅ AntiOp đã được kích hoạt!")
    }

    override fun onDisable() {
        logger.info("❌ AntiOp đã bị tắt!")
    }
}
