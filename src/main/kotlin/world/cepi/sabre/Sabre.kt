package world.cepi.sabre

import net.minestom.server.MinecraftServer
import net.minestom.server.chat.ColoredText
import net.minestom.server.entity.Player
import net.minestom.server.event.player.PlayerLoginEvent
import net.minestom.server.event.player.PlayerSpawnEvent
import net.minestom.server.extras.MojangAuth
import net.minestom.server.instance.Instance
import net.minestom.server.instance.block.Block
import net.minestom.server.storage.systems.FileStorageSystem
import net.minestom.server.utils.Position
import world.cepi.sabre.Config.Companion.config
import world.cepi.sabre.commands.GamemodeCommand
import world.cepi.sabre.commands.KillCommand
import world.cepi.sabre.commands.StopCommand
import world.cepi.sabre.commands.TpCommand
import world.cepi.sabre.commands.security.*
import world.cepi.sabre.instances.Instances
import world.cepi.sabre.instances.generators.flat.Flat
import world.cepi.sabre.instances.generators.flat.FlatLayer
import world.cepi.sabre.utils.getUUID

fun main() {
    val server = MinecraftServer.init()
    val connectionManager = MinecraftServer.getConnectionManager()
    val storageManager = MinecraftServer.getStorageManager()

    // Basically this sets the default storage manager to be a filesystem
    // As opposed to a database or something, I think
    storageManager.defineDefaultStorageSystem { FileStorageSystem() }

    // This code basically teleports the player to an ethereal instance stored in RAM.
    // I don't know how to keep track of the things so it gets deleted on a restart
    var currentInstance: Instance? = null
    connectionManager.addPlayerInitialization {
        it.sendTitleSubtitleMessage(ColoredText.of(""), ColoredText.of(""))
        it.addEventCallback(PlayerLoginEvent::class.java) { event ->
            event.spawningInstance = currentInstance ?: Instances.createInstanceContainer(Flat(
                    FlatLayer(Block.BEDROCK, 1),
                    FlatLayer(Block.STONE, 25),
                    FlatLayer(Block.DIRT, 7),
                    FlatLayer(Block.GRASS_BLOCK, 1)
            ))
            currentInstance = event.spawningInstance

            // Kicks the player if they are not on the whitelist
            if (config.whitelist && !isWhitelisted(event.player)) event.player.kick("You are not on the whitelist for this server")

            // OPs players when they join if they are on the ops list
            if (isOp(event.player)) event.player.permissionLevel = getPermissionLevel(event.player) ?: 0
        }

        it.addEventCallback(PlayerSpawnEvent::class.java) { event ->
            val player = event.entity as Player
            player.teleport(Position(0F, 64F, 0F))
        }
    }

    if (config.onlineMode) {
        MojangAuth.init();
    }

    // We have to set a different UUID provider because Mojang's API is not used by default
    connectionManager.setUuidProvider { _, username ->
        return@setUuidProvider getUUID(username)
    }

    MinecraftServer.getCommandManager().register(KillCommand())
    MinecraftServer.getCommandManager().register(StopCommand())
    MinecraftServer.getCommandManager().register(TpCommand())
    MinecraftServer.getCommandManager().register(GamemodeCommand())
    MinecraftServer.getCommandManager().register(OpCommand())
    MinecraftServer.getCommandManager().register(WhitelistCommand())

    // The IP and port are currently grabbed from the config file
    server.start(config.ip, config.port)
}

object Sabre {
    const val CONFIG_LOCATION = "./sabre-config.json"
    const val INSTANCE_STORAGE_LOCATION = "./instances"
    const val WHITELIST_LOCATION = "./whitelist.json"
    const val OP_LOCATION = "./ops.json"
}