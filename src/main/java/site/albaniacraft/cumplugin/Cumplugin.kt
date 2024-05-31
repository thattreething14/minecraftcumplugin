package site.albaniacraft.cumplugin

import org.bukkit.plugin.java.JavaPlugin

class Cumplugin : JavaPlugin() {
    override fun onEnable() {
        server.logger.info("im boutta cum")
        server.pluginManager.registerEvents(ClickItem(this), this)
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}
