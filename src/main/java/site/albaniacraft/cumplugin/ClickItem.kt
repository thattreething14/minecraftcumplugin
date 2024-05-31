package site.albaniacraft.cumplugin

import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.util.Vector
import org.bukkit.scheduler.BukkitRunnable

class ClickItem(private val plugin: JavaPlugin) : Listener {
    private val clickCounts = mutableMapOf<Player, Int>()
    private val activatedPlayers = mutableSetOf<Player>()
    @EventHandler
    fun onPlayerLeftClick(event: PlayerInteractEvent) {
        val player = event.player
        val item = event.item

        if (event.action.name == "LEFT_CLICK_AIR" || event.action.name == "LEFT_CLICK_BLOCK") {
            if (item != null && item.type == Material.WHITE_DYE) {
                activatedPlayers.add(player)
                player.sendMessage("You've applied lotion.")
            }
        }
    }

    @EventHandler
    fun onPlayerRightClick(event: PlayerInteractEvent) {
        val player = event.player
        val item = event.item

        if (event.action == Action.LEFT_CLICK_AIR || event.action == Action.LEFT_CLICK_BLOCK) {
            if (item != null && item.type == Material.WOODEN_SWORD) {
                if (activatedPlayers.contains(player)) {
                    clickCounts.merge(player, 1, Int::plus)

                    val count = clickCounts[player] ?: 0

                    when (count) {
                        4 -> sendTitle(player, "Closer", "")
                        8 -> sendTitle(player, "Even Closer", "")
                        12 -> sendTitle(player, "Almost There", "")
                        16 -> sendTitle(player, "I'm Boutta cum", "")

                        20 -> {
                            sendTitle(player, "IM CUMMING.", "Good luck cleaning this up \uD83D\uDC40")
                            startParticleTrail(player)
                            clickCounts.remove(player)
                            activatedPlayers.remove(player)
                        }
                    }
                } else {
                    sendTitle(player, "You need to apply lotion.", "")
                }
            } else {
            }
        }
    }
    private fun sendTitle(player: Player, title: String, subtitle: String) {
        player.sendTitle(title, subtitle, 10, 70, 20)
    }

    private fun startParticleTrail(player: Player) {
        val duration = 10 * 20
        val particleDensity = 1000
        val cobwebDensity = 1
        val rotationSpeed = 2

        val runnable = object : BukkitRunnable() {
            var ticks = 0
            var angle = 0

            override fun run() {
                if (ticks >= duration) {
                    cancel()
                    return
                }

                for (i in 0 until particleDensity) {
                    angle = (angle + rotationSpeed) % 360
                    val radians = Math.toRadians(angle.toDouble())
                    val xOffset = Math.cos(radians)
                    val zOffset = Math.sin(radians)
                    val circleLocation = player.location.clone().add(xOffset, 1.0, zOffset)

                    // spawns the cum
                    player.world.spawnParticle(
                        Particle.REDSTONE,
                        circleLocation,
                        1, 0.0, 0.0, 0.0, 1.0,
                        Particle.DustOptions(Color.WHITE, 1.0F)
                    )
                }

                if (ticks % cobwebDensity == 0) {
                    for (j in 0 until 4) {
                        angle = (angle + rotationSpeed) % 360
                        val radians = Math.toRadians(angle.toDouble())
                        val xOffset = Math.cos(radians)
                        val zOffset = Math.sin(radians)

                        val cobwebLocation = player.location.clone().add(xOffset, 0.0, zOffset)
                        cobwebLocation.block.type = Material.COBWEB
                    }
                }


                val rotation = Math.toRadians(angle.toDouble())
                player.velocity = Vector(Math.sin(rotation), 1.0, Math.cos(rotation))

                ticks++
            }
        }
        runnable.runTaskTimer(plugin, 0L, 1L)
    }
}